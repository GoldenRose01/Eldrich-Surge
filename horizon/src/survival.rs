use crate::{enchants, enchlib_bridge::EnchLibState};
use mod_api::{
    EquipmentSlot, Flow, ItemStack, LivingDeathEvent, LivingDropsEvent, PlayerBreakSpeedEvent,
    PlayerRespawnedEvent, ServerHost,
};
use std::collections::HashMap;
use std::sync::Mutex;

pub fn is_raw_meat(item_id: &str) -> bool {
    matches!(
        item_id,
        "minecraft:beef"
            | "minecraft:porkchop"
            | "minecraft:chicken"
            | "minecraft:mutton"
            | "minecraft:rabbit"
    )
}

#[derive(Default)]
pub struct SurvivalState {
    soulbound_saved: Mutex<HashMap<u128, Vec<(u32, ItemStack)>>>,
}

impl SurvivalState {
    pub fn on_living_death(&self, host: &mut dyn ServerHost, event: &LivingDeathEvent<'_>) {
        // 1. Triumph heal/feed for killer
        if event.source.causing > 0
            && let Some(killer_entity) = host.entity(event.source.causing as u32)
            && let Some(killer_living) = host.living(killer_entity)
            && let Some(weapon) = killer_living.equipment(host, EquipmentSlot::MainHand)
        {
            let triumph = enchants::level_on_item(&weapon, "triumph");
            if triumph > 0 {
                let _ = killer_living.heal(host, 2.0 * triumph as f32);
                if let Some(player) = host
                    .players()
                    .server_players()
                    .into_iter()
                    .find(|p| p.entity().id() == killer_entity.id())
                {
                    let _ = player.feed(host, 2 * triumph, 0.5 * triumph as f32);
                }
            }
        }

        // 2. Soulbond retention on player death
        if event.kind != "minecraft:player" {
            return;
        }
        let Some(player) = host
            .players()
            .server_players()
            .into_iter()
            .find(|p| p.entity().id() == event.entity)
        else {
            return;
        };
        let Some(uuid) = player.entity().uuid(host) else {
            return;
        };
        let Some(slots) = player.inventory_slots(host) else {
            return;
        };
        let kept: Vec<(u32, ItemStack)> = slots
            .into_iter()
            .filter(|(_, stack)| enchants::level_on_item(stack, "soulbond") > 0)
            .collect();
        if !kept.is_empty() {
            self.soulbound_saved
                .lock()
                .expect("soulbond lock")
                .insert(uuid, kept);
        }
    }

    pub fn on_living_drops(&self, host: &mut dyn ServerHost, event: &mut LivingDropsEvent<'_>) {
        // Butcher: double raw meat drops on "animals" mob category
        if event.source.causing > 0
            && EnchLibState::has_mob_category(Some(host), event.kind, "animals")
            && let Some(killer_entity) = host.entity(event.source.causing as u32)
            && let Some(killer_living) = host.living(killer_entity)
            && let Some(weapon) = killer_living.equipment(host, EquipmentSlot::MainHand)
            && enchants::level_on_item(&weapon, "butcher") > 0
        {
            for drop in event.drops.iter_mut() {
                let item_id = drop.stack.item().id(host);
                if is_raw_meat(&item_id) {
                    let doubled = (drop.stack.count() * 2).min(64);
                    if let Some(new_stack) = ItemStack::new(drop.stack.item(), doubled) {
                        drop.stack = new_stack;
                    }
                }
            }
        }

        if event.kind != "minecraft:player" {
            return;
        }
        let Some(uuid) = event.player else {
            return;
        };
        if self
            .soulbound_saved
            .lock()
            .expect("soulbond lock")
            .contains_key(&uuid)
        {
            event
                .drops
                .retain(|drop| enchants::level_on_item(&drop.stack, "soulbond") == 0);
        }
    }

    pub fn on_player_respawned(&self, host: &mut dyn ServerHost, event: &PlayerRespawnedEvent<'_>) {
        let Some(kept) = self
            .soulbound_saved
            .lock()
            .expect("soulbond lock")
            .remove(&event.player)
        else {
            return;
        };
        let Some(player) = host.player(event.player) else {
            return;
        };
        for (slot, stack) in kept {
            let _ = player.set_inventory_slot(host, slot, Some(stack));
        }
    }

    pub fn on_break_speed(
        &self,
        host: &mut dyn ServerHost,
        event: &mut PlayerBreakSpeedEvent,
    ) -> Flow {
        let Some(player) = host.players().acting_player() else {
            return Flow::Continue;
        };
        let Some(living) = host.living(player.entity()) else {
            return Flow::Continue;
        };
        let Some(tool) = living.equipment(host, EquipmentSlot::MainHand) else {
            return Flow::Continue;
        };
        let digger = enchants::level_on_item(&tool, "digger");
        if digger > 0 {
            event.new_speed = event.original_speed + 2.0 * digger as f32;
        }
        Flow::Continue
    }
}
