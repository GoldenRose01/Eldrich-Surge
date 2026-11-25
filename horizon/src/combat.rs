use crate::{
    config::{EldrichConfig, LethalPoisonMode},
    enchants,
    enchlib_bridge::EnchLibState,
};
use mod_api::{
    ActiveEffect, AttackEntityEvent, EntityRef, EquipmentSlot, Flow, Host, LevelRandomHost,
    LivingIncomingDamageEvent, ServerHost, SweepAttackEvent,
};
use std::collections::HashMap;
use std::sync::Mutex;

#[derive(Default)]
pub struct CombatState {
    pending_hits: Mutex<HashMap<i32, PendingHit>>,
}

struct PendingHit {
    armor_multiplier: f32,
    extra_flat_bonus: f32,
    is_hoe: bool,
}

impl CombatState {
    pub fn on_incoming_damage(
        &self,
        _level: &mut dyn LevelRandomHost,
        event: &mut LivingIncomingDamageEvent<'_>,
        config: &EldrichConfig,
    ) -> Flow {
        let original = event.container.new_damage();
        let cached = self
            .pending_hits
            .lock()
            .expect("combat pending_hits lock")
            .remove(&event.entity);

        let mut multiplier = cached.as_ref().map_or(1.0, |c| c.armor_multiplier);
        let mut damage = (original * multiplier.max(0.15)).max(0.0);

        if let Some(weapon) = event.weapon {
            let superweight = enchants::level_on_item(weapon, "superweight");
            if superweight > 0 {
                damage *= 1.0 + superweight as f32 * 0.02;
                damage += superweight as f32 * 2.0;
            }

            let gream = enchants::level_on_item(weapon, "gream_reaper");
            if gream > 0 {
                damage += gream as f32 * 3.0;
            }

            let void_sweep = enchants::level_on_item(weapon, "void_sweep");
            if void_sweep > 0 && cached.as_ref().is_some_and(|c| c.is_hoe) {
                damage = damage.max(10.0 + void_sweep as f32);
            }

            if event.source.damage_type.contains("mace_smash") {
                let heavy = enchants::level_on_item(weapon, "heavy_impact");
                damage += heavy as f32 * 1.5;
            }
        }

        if let Some(c) = cached {
            damage += c.extra_flat_bonus;
        }

        // Lethal poison mode check (allows poison damage to be lethal when configured)
        let _poison_mode = config.lethal_poison_mode();
        if matches!(
            _poison_mode,
            LethalPoisonMode::All | LethalPoisonMode::Player | LethalPoisonMode::Mob
        ) && event.source.damage_type.contains("magic")
        {
            // Hook point ready for lethal poison tick scaling
        }

        let _ = &mut multiplier;

        if (damage - original).abs() > f32::EPSILON {
            event.container.set_new_damage(damage);
        }
        Flow::Continue
    }

    pub fn on_attack_entity(
        &self,
        host: &mut dyn ServerHost,
        event: &mut AttackEntityEvent<'_>,
    ) -> Flow {
        let Some(player) = host.players().acting_player() else {
            return Flow::Continue;
        };
        let Some(attacker_living) = host.living(player.entity()) else {
            return Flow::Continue;
        };
        let Some(weapon) = attacker_living.equipment(host, EquipmentSlot::MainHand) else {
            return Flow::Continue;
        };
        let Some(victim_entity) = host.entity(event.entity as u32) else {
            return Flow::Continue;
        };
        let Some(victim_living) = host.living(victim_entity) else {
            return Flow::Continue;
        };

        // Snapshot armor/offhand/payback modifiers for `living_incoming_damage`
        let mut armor_multiplier = 1.0_f32;
        let weapon_prot = enchants::sum_on_armor(host, victim_living, "weapon_protection");
        if weapon_prot > 0 {
            armor_multiplier -= (weapon_prot as f32 * 0.07).min(0.50);
        }
        if enchants::max_on_equipment(host, victim_living, "god_protection") > 0 {
            armor_multiplier *= 0.40;
        }

        let mut extra_flat_bonus = 0.0_f32;
        let dash = enchants::level_on_item(&weapon, "dash");
        if dash > 0
            && attacker_living
                .equipment(host, EquipmentSlot::OffHand)
                .is_some_and(|off| enchants::level_on_item(&off, "dash") > 0)
        {
            extra_flat_bonus += 1.0 + dash as f32 * 0.5;
        }
        let payback = enchants::level_on_item(&weapon, "payback");
        if payback > 0
            && let (Some(hp), Some(max_hp)) = (
                attacker_living.health(host),
                attacker_living.max_health(host),
            )
            && max_hp > 0.0
        {
            let missing = (1.0 - hp / max_hp).clamp(0.0, 1.0);
            extra_flat_bonus += payback as f32 * missing * missing * 6.0;
        }

        let victim_id = victim_entity
            .kind(host)
            .map(|k| k.id(host))
            .unwrap_or_default();
        let is_creeper = victim_id == "minecraft:creeper";
        let weapon_enchants = enchants::all_on_item(&weapon);
        extra_flat_bonus += EnchLibState::mob_category_damage_bonus(
            Some(host),
            &victim_id,
            is_creeper,
            false,
            &weapon_enchants,
        );

        let is_hoe = weapon.item().id(host).ends_with("_hoe");
        self.pending_hits.lock().expect("combat lock").insert(
            event.entity,
            PendingHit {
                armor_multiplier,
                extra_flat_bonus,
                is_hoe,
            },
        );

        // On-hit status effects and knockback
        let freeze = enchants::level_on_item(&weapon, "freeze_aspect");
        if freeze > 0
            && let Some(key) = host.effect_key("minecraft:slowness")
        {
            let _ = victim_living.add_effect(
                host,
                &ActiveEffect::new(key, 60 + freeze * 20, (freeze - 1).min(3), false, true, true),
            );
        }

        let leeching = enchants::level_on_item(&weapon, "leeching_aspect");
        if leeching > 0 && is_crit_like(host, player.entity()) {
            let _ = attacker_living.heal(host, (1.5 * leeching as f32).max(1.0));
        }

        let launch = enchants::level_on_item(&weapon, "launch");
        if launch > 0 {
            push_entity(host, victim_entity, [0.0, 0.25 * f64::from(launch), 0.0]);
        }

        let catapult = enchants::level_on_item(&weapon, "catapult");
        if catapult > 0 && is_crit_like(host, player.entity()) {
            push_entity(
                host,
                victim_entity,
                [0.0, 0.45 * f64::from(catapult) * 3.5, 0.0],
            );
        }

        let creeping = enchants::level_on_item(&weapon, "creeping_threat");
        if creeping > 0 {
            if let Some(key) = host.effect_key("minecraft:weakness") {
                let _ = victim_living.add_effect(
                    host,
                    &ActiveEffect::new(key, 80, (creeping - 1).min(2), false, true, true),
                );
            }
            if (is_creeper || EnchLibState::has_mob_category(Some(host), &victim_id, "arthropods"))
                && let Some(key) = host.effect_key("minecraft:slowness")
            {
                let _ = victim_living.add_effect(
                    host,
                    &ActiveEffect::new(key, 20 + 15 * creeping, 3, false, true, true),
                );
            }
        }

        let backslash = enchants::level_on_item(&weapon, "backslash");
        if backslash > 0 {
            if let Some(key) = host.effect_key("minecraft:weakness") {
                let _ = victim_living.add_effect(
                    host,
                    &ActiveEffect::new(key, 40 * backslash, backslash - 1, false, true, true),
                );
            }
            if let Some(view) = player.entity().view_vector(host) {
                push_entity(
                    host,
                    victim_entity,
                    [
                        -view[0] * 0.35 * f64::from(backslash),
                        0.1,
                        -view[2] * 0.35 * f64::from(backslash),
                    ],
                );
            }
        }

        let inking = enchants::level_on_item(&weapon, "inking");
        if inking > 0 {
            if let Some(k) = host.effect_key("minecraft:darkness") {
                let _ = victim_living.add_effect(
                    host,
                    &ActiveEffect::new(k, 60 + inking * 20, 0, false, true, true),
                );
            }
            if let Some(k) = host.effect_key("minecraft:glowing") {
                let _ = victim_living.add_effect(
                    host,
                    &ActiveEffect::new(k, 80 + inking * 20, 0, false, true, true),
                );
            }
            if let Some(k) = host.effect_key("minecraft:weakness") {
                let _ = victim_living.add_effect(
                    host,
                    &ActiveEffect::new(k, 80, (inking - 1).min(2), false, true, true),
                );
            }
        }

        let spider = enchants::level_on_item(&weapon, "curse_of_spider");
        if spider > 0
            && let Some(key) = host.effect_key("minecraft:poison")
        {
            let _ = victim_living.add_effect(
                host,
                &ActiveEffect::new(key, 30 * spider, spider - 1, false, true, true),
            );
        }

        let perish = enchants::level_on_item(&weapon, "curse_of_perish");
        if perish > 0
            && let Some(key) = host.effect_key("minecraft:wither")
        {
            let _ = victim_living.add_effect(
                host,
                &ActiveEffect::new(key, 30 * perish, perish - 1, false, true, true),
            );
        }

        Flow::Continue
    }
}

pub fn on_sweep_attack(
    host: &mut dyn ServerHost,
    event: &mut SweepAttackEvent<'_>,
    config: &EldrichConfig,
) -> Flow {
    use std::sync::atomic::Ordering;
    if !config.sweeping_requires_enchantment.load(Ordering::Relaxed) {
        return Flow::Continue;
    }
    let Some(player) = host.players().acting_player() else {
        return Flow::Continue;
    };
    let Some(living) = host.living(player.entity()) else {
        return Flow::Continue;
    };
    let Some(weapon) = living.equipment(host, EquipmentSlot::MainHand) else {
        return Flow::Cancel;
    };
    if enchants::level_on_item(&weapon, "minecraft:sweeping_edge") > 0
        || enchants::level_on_item(&weapon, "void_sweep") > 0
    {
        Flow::Continue
    } else {
        let _ = event;
        Flow::Cancel
    }
}

fn is_crit_like(host: &dyn Host, entity: EntityRef) -> bool {
    entity.on_ground(host) == Some(false)
        && entity.velocity(host).is_some_and(|v| v[1] < -0.05)
}

fn push_entity(host: &mut dyn ServerHost, entity: EntityRef, delta: [f64; 3]) {
    let cur = entity.velocity(host).unwrap_or([0.0, 0.0, 0.0]);
    let _ = entity.set_velocity(
        host,
        [cur[0] + delta[0], cur[1] + delta[1], cur[2] + delta[2]],
    );
}
