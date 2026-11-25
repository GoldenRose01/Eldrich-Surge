//! Phase 4C — Spell Books (`eldrich-surge:spell_book` registration, `use_item` cast with cooldown,
//! `on_entity_item_update` ground-drop parity, and the 5 active spells + `TrenchWorkQueue`).

use crate::enchants;
use mod_api::{
    Compound, EquipmentSlot, HostSide, InteractionResult, ItemPart, ItemStack, LivingRef,
    Registrar, ServerHost, SpawnReason, Tag, UseItemEvent,
};
use std::collections::{HashSet, VecDeque};
use std::sync::{Mutex, OnceLock};

pub const SPELL_BOOK_ID: &str = "eldrich-surge:spell_book";
pub const SPELL_COOLDOWN_TICKS: i32 = 100;
pub const MAX_PENDING_TRENCH_JOBS: usize = 16;
pub const TRENCH_TICKS_PER_LAYER: u32 = 100;

pub const CAST_SPELLS: &[&str] = &[
    "eldrich-surge:cocktail_spell",
    "eldrich-surge:ragnarok",
    "eldrich-surge:red_moon",
    "eldrich-surge:storm_spell",
    "eldrich-surge:trench_spell",
];

pub fn is_cast_spell(id: &str) -> bool {
    let lower = id.trim().to_ascii_lowercase();
    let full = if lower.contains(':') {
        lower
    } else {
        format!("eldrich-surge:{lower}")
    };
    CAST_SPELLS.contains(&full.as_str())
}

pub fn active_spell_on_stack(stack: &ItemStack) -> Option<(String, i32)> {
    for &spell in CAST_SPELLS {
        let lvl = enchants::level_on_item(stack, spell);
        if lvl > 0 {
            return Some((spell.to_owned(), lvl));
        }
    }
    for (id, lvl) in enchants::stored_enchantments(stack) {
        if lvl > 0 && is_cast_spell(&id) {
            return Some((id, lvl));
        }
    }
    None
}

pub fn mark_spell_book_model(stack: &mut ItemStack) {
    let mut patch = stack
        .components()
        .map(Compound::from)
        .unwrap_or_else(Compound::new);
    let mut cmd = Compound::new();
    cmd.insert(
        "strings".into(),
        Tag::List(vec![Tag::String("spell".into())]),
    );
    patch.insert("minecraft:custom_model_data".into(), Tag::Compound(cmd));
    let _ = stack.set_components(patch);
}

pub fn trench_radius(effective_level: i32) -> i32 {
    (2 + effective_level.max(1)).clamp(2, 16)
}

#[derive(Debug, Clone)]
pub struct TrenchJob {
    pub dimension: String,
    pub origin: [i32; 3],
    pub radius: i32,
    pub current_y: i32,
    pub min_y: i32,
    pub ticks_until_next: u32,
}

#[derive(Default)]
struct SpellRuntimeState {
    trench_jobs: VecDeque<TrenchJob>,
    airborne_items: HashSet<u32>,
}

static SPELL_RUNTIME: OnceLock<Mutex<SpellRuntimeState>> = OnceLock::new();

fn runtime() -> &'static Mutex<SpellRuntimeState> {
    SPELL_RUNTIME.get_or_init(|| Mutex::new(SpellRuntimeState::default()))
}

pub fn enqueue_trench_job(dimension: String, origin: [i32; 3], effective_level: i32) -> bool {
    let Ok(mut guard) = runtime().lock() else {
        return false;
    };
    if guard.trench_jobs.len() >= MAX_PENDING_TRENCH_JOBS {
        return false;
    }
    guard.trench_jobs.push_back(TrenchJob {
        dimension,
        origin,
        radius: trench_radius(effective_level),
        current_y: origin[1],
        min_y: -55,
        ticks_until_next: 0,
    });
    true
}

pub fn on_server_tick(host: &mut dyn ServerHost) {
    let mut ready_jobs = Vec::new();
    if let Ok(mut guard) = runtime().lock() {
        let count = guard.trench_jobs.len();
        for _ in 0..count {
            if let Some(mut job) = guard.trench_jobs.pop_front() {
                if job.ticks_until_next == 0 {
                    ready_jobs.push(job);
                } else {
                    job.ticks_until_next -= 1;
                    guard.trench_jobs.push_back(job);
                }
            }
        }
    }

    let mut requeue = Vec::new();
    for mut job in ready_jobs {
        if job.current_y < job.min_y {
            continue;
        }
        if let Some(level) = host.level(&job.dimension) {
            let r = job.radius.min(8);
            let y = job.current_y;
            for dx in -r..r {
                for dz in -r..r {
                    let pos = [job.origin[0] + dx, y, job.origin[2] + dz];
                    if let Some(state) = level.block_state(host, pos) {
                        let bid = state.block().id(host);
                        if bid != "minecraft:air"
                            && bid != "minecraft:cave_air"
                            && bid != "minecraft:void_air"
                            && bid != "minecraft:bedrock"
                            && bid != "minecraft:water"
                            && bid != "minecraft:lava"
                        {
                            let _ = level.destroy_block(host, pos, true, None, false);
                        }
                    }
                }
            }
        }
        job.current_y -= 1;
        job.ticks_until_next = TRENCH_TICKS_PER_LAYER;
        if job.current_y >= job.min_y {
            requeue.push(job);
        }
    }

    if !requeue.is_empty()
        && let Ok(mut guard) = runtime().lock()
    {
        for job in requeue {
            guard.trench_jobs.push_back(job);
        }
    }
}

pub fn on_server_stopping() {
    if let Ok(mut guard) = runtime().lock() {
        guard.trench_jobs.clear();
        guard.airborne_items.clear();
    }
}

fn equip_mob(host: &mut dyn ServerHost, mob: LivingRef, prefix: &str) {
    let slots = [
        (EquipmentSlot::MainHand, format!("{prefix}_sword")),
        (EquipmentSlot::Head, format!("{prefix}_helmet")),
        (EquipmentSlot::Chest, format!("{prefix}_chestplate")),
        (EquipmentSlot::Legs, format!("{prefix}_leggings")),
        (EquipmentSlot::Feet, format!("{prefix}_boots")),
    ];
    for (slot, item_id) in slots {
        if let Ok(ident) = mod_api::Identifier::parse(&item_id)
            && let Some(key) = host.item_key(&ident)
            && let Some(stack) = ItemStack::new(key, 1)
        {
            let _ = mob.set_equipment(host, slot, Some(stack));
        }
    }
}

fn execute_spell(
    host: &mut dyn ServerHost,
    spell_id: &str,
    level_val: i32,
    dimension: &str,
    origin: [f64; 3],
    caster_uuid: Option<u128>,
) -> bool {
    let Some(level) = host.level(dimension) else {
        return false;
    };
    let eff = level_val.max(1);
    match spell_id {
        "eldrich-surge:cocktail_spell" => {
            let Some(uuid) = caster_uuid else {
                return false;
            };
            let Some(caster) = host.player(uuid) else {
                return false;
            };
            let Some(caster_living) = host.living(caster.entity()) else {
                return false;
            };
            let radius = 8.0 + f64::from(eff) * 8.0;
            let r_sq = radius * radius;
            let effects = caster_living.effects(host).unwrap_or_default();
            for p in host.players().server_players() {
                if let Some(pos) = p.entity().position(host) {
                    let dx = pos[0] - origin[0];
                    let dy = pos[1] - origin[1];
                    let dz = pos[2] - origin[2];
                    if dx * dx + dy * dy + dz * dz <= r_sq
                        && let Some(target_living) = host.living(p.entity())
                    {
                        for ef in &effects {
                            let _ = target_living.add_effect(host, ef);
                        }
                    }
                }
            }
            true
        }
        "eldrich-surge:storm_spell" => {
            if caster_uuid.is_none() {
                return false;
            }
            let _ = level.set_weather(host, 0, 20 * 60 * 5, true, true);
            let _ = level.set_weather_levels(host, 1.0, 1.0);
            for i in 0..5 {
                let angle = f64::from(i) * std::f64::consts::TAU / 5.0;
                let lx = origin[0] + angle.cos() * 12.0;
                let lz = origin[2] + angle.sin() * 12.0;
                let _ = level.strike_lightning(host, [lx, origin[1], lz], false);
            }
            true
        }
        "eldrich-surge:ragnarok" => {
            for i in 0..4 {
                let angle = f64::from(i) * std::f64::consts::FRAC_PI_2;
                let sx = origin[0] + angle.cos() * 6.0;
                let sz = origin[2] + angle.sin() * 6.0;
                let spawn_pos = [sx, origin[1], sz];
                let horse = level.spawn_mob(
                    host,
                    "minecraft:skeleton_horse",
                    spawn_pos,
                    SpawnReason::Triggered,
                );
                let rider = level.spawn_mob(
                    host,
                    "minecraft:wither_skeleton",
                    spawn_pos,
                    SpawnReason::Triggered,
                );
                if let (Some(h), Some(r)) = (horse, rider) {
                    if let Some(living) = host.living(r) {
                        equip_mob(host, living, "minecraft:netherite");
                    }
                    let _ = r.start_riding(host, h, true);
                }
            }
            true
        }
        "eldrich-surge:red_moon" => {
            for i in 0..30 {
                let angle = f64::from(i) * std::f64::consts::TAU / 30.0;
                let dist = 5.0 + f64::from(i % 11);
                let sx = origin[0] + angle.cos() * dist;
                let sz = origin[2] + angle.sin() * dist;
                let spawn_pos = [sx, origin[1], sz];
                let zombie = level.spawn_mob(
                    host,
                    "minecraft:zombie",
                    spawn_pos,
                    SpawnReason::Triggered,
                );
                if let Some(z) = zombie
                    && let Some(living) = host.living(z)
                {
                    equip_mob(host, living, "minecraft:diamond");
                }
                if i % 5 == 0 {
                    let horse = level.spawn_mob(
                        host,
                        "minecraft:zombie_horse",
                        spawn_pos,
                        SpawnReason::Triggered,
                    );
                    if let (Some(h), Some(z)) = (horse, zombie) {
                        let _ = z.start_riding(host, h, true);
                    }
                }
            }
            true
        }
        "eldrich-surge:trench_spell" => enqueue_trench_job(
            dimension.to_owned(),
            [
                origin[0].floor() as i32,
                origin[1].floor() as i32,
                origin[2].floor() as i32,
            ],
            eff,
        ),
        _ => false,
    }
}

struct SpellBookPart;

impl ItemPart for SpellBookPart {
    fn item_subscribed(&self) -> &'static [Option<mod_api::HookId>] {
        &[
            Some(mod_api::HookId::UseItem),
            Some(mod_api::HookId::OnEntityItemUpdate),
        ]
    }

    fn on_entity_item_update(
        &self,
        host: &mut dyn ServerHost,
        event: &mut mod_api::OnEntityItemUpdateEvent<'_>,
    ) -> mod_api::Flow {
        let Some((spell_id, lvl)) = active_spell_on_stack(&event.stack) else {
            return mod_api::Flow::Continue;
        };
        let ent = event.item.entity();
        let on_ground = ent.on_ground(host) == Some(true);
        let eid = ent.id();
        if !on_ground {
            if let Ok(mut guard) = runtime().lock() {
                guard.airborne_items.insert(eid);
            }
            return mod_api::Flow::Continue;
        }
        let was_airborne = runtime()
            .lock()
            .ok()
            .is_some_and(|mut g| g.airborne_items.remove(&eid));
        if !was_airborne {
            return mod_api::Flow::Continue;
        }
        let origin = ent.position(host).unwrap_or([0.0, 64.0, 0.0]);
        if execute_spell(host, &spell_id, lvl, "minecraft:overworld", origin, None) {
            let _ = ent.discard(host);
        }
        mod_api::Flow::Continue
    }

    fn use_item(&self, host: HostSide<'_>, event: &mut UseItemEvent<'_>) -> Option<InteractionResult> {
        let Some((spell_id, lvl)) = active_spell_on_stack(&event.stack) else {
            return None;
        };
        let HostSide::Server(server) = host else {
            return Some(InteractionResult::Success);
        };
        let Some(player) = server.player(event.player) else {
            return None;
        };
        let group = server.item_cooldown_group(&event.stack);
        if !group.is_empty() && player.has_item_cooldown(server, &group) == Some(true) {
            return Some(InteractionResult::Pass);
        }
        let origin = player.entity().position(server).unwrap_or([0.0, 64.0, 0.0]);
        let dim = player
            .dimension(server)
            .map(|d| d.to_string())
            .unwrap_or_else(|| "minecraft:overworld".to_owned());

        if execute_spell(server, &spell_id, lvl, &dim, origin, Some(event.player)) {
            if !group.is_empty() {
                let _ = player.set_item_cooldown(server, &group, SPELL_COOLDOWN_TICKS);
            }
            return Some(InteractionResult::Consume);
        }
        Some(InteractionResult::Pass)
    }
}

pub fn register(r: &mut dyn Registrar) {
    let item_key = r.item_key(SPELL_BOOK_ID);
    r.item_part(item_key, SpellBookPart);
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn recognizes_all_five_cast_spells() {
        for s in CAST_SPELLS {
            assert!(is_cast_spell(s));
        }
        assert!(is_cast_spell("storm_spell"));
        assert!(!is_cast_spell("eldrich-surge:katana"));
    }

    #[test]
    fn trench_radius_clamps_correctly() {
        assert_eq!(trench_radius(1), 3);
        assert_eq!(trench_radius(10), 12);
        assert_eq!(trench_radius(50), 16);
    }

    #[test]
    fn trench_queue_enforces_max_pending_capacity() {
        on_server_stopping();
        for i in 0..MAX_PENDING_TRENCH_JOBS {
            assert!(enqueue_trench_job(
                "minecraft:overworld".to_owned(),
                [i as i32, 64, 0],
                1
            ));
        }
        assert!(!enqueue_trench_job(
            "minecraft:overworld".to_owned(),
            [99, 64, 0],
            1
        ));
        on_server_stopping();
    }
}
