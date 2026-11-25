//! Phase 4A — Advanced Enchanting Table (`AdvancedEnchantingFormation`, `EnchantingReagentStorage`,
//! `AdvancedInfusingCosts`, offer eligibility, and vanilla/mod enchanting table hooks).

use crate::enchlib_bridge::{EnchLibState, RarityClass};
use crate::spell_books;
use mod_api::{
    BlockDropsEvent, BlockEntityPart, BlockPart, EnchantmentLevelSetEvent, Flow, Host, LevelHost,
    LevelRef, ModContainer, PlayerContainerCloseEvent, PlayerContainerOpenEvent,
    PreRemoveSideEffectsEvent, PrepareItemEnchantEvent, Registrar, ServerHost,
};
use serde::{Deserialize, Serialize};
use std::collections::HashMap;
use std::path::Path;
use std::sync::{Mutex, OnceLock};

pub const ADVANCED_TABLE_BLOCK_ID: &str = "eldrich-surge:advanced_enchanting_table";
pub const VANILLA_TABLE_BLOCK_ID: &str = "minecraft:enchanting_table";

pub const HARDCODED_ADVANCED_EXCLUSIONS: &[&str] = &[
    "eldrich-surge:deathbreak",
    "eldrich-surge:blade_of_apocalypse",
    "eldrich-surge:star_fate",
    "eldrich-surge:gream_reaper",
    "minecraft:sharpness",
    "minecraft:smite",
    "minecraft:bane_of_arthropods",
];

// ── AdvancedInfusingCosts ──────────────────────────────────────────────────

pub struct AdvancedInfusingCosts;

impl AdvancedInfusingCosts {
    pub fn base_cost_for_rarity(rarity: RarityClass) -> i32 {
        rarity.advanced_xp_base()
    }

    pub fn experience(added_levels: i32, full_cap: i32, rarity: RarityClass) -> i32 {
        Self::scaled_cost(Self::base_cost_for_rarity(rarity), added_levels, full_cap)
    }

    pub fn reagents(added_levels: i32, full_cap: i32) -> i32 {
        Self::scaled_cost(3, added_levels, full_cap)
    }

    fn scaled_cost(full_price: i32, added_levels: i32, full_cap: i32) -> i32 {
        if added_levels < 1 || full_cap < 1 || added_levels > full_cap {
            return 0;
        }
        ((full_price * added_levels + full_cap - 1) / full_cap).max(1)
    }
}

// ── AdvancedEnchantingFormation ────────────────────────────────────────────

pub struct AdvancedEnchantingFormation;

impl AdvancedEnchantingFormation {
    /// Pure 5x5 horizontal formation check at table Y-level:
    /// - Cardinal `(±2, 0)` and `(0, ±2)` -> `minecraft:chiseled_quartz_block`
    /// - Corners `(±2, ±2)` -> `minecraft:gold_block`
    /// - Inner 8 (`|x|<=1, |z|<=1` excl `(0,0)`) -> `minecraft:air` or `minecraft:cave_air`
    pub fn is_valid_blocks<F>(table_pos: [i32; 3], mut block_at: F) -> bool
    where
        F: FnMut([i32; 3]) -> Option<String>,
    {
        for dx in -2_i32..=2 {
            for dz in -2_i32..=2 {
                if dx == 0 && dz == 0 {
                    continue;
                }
                let ax = dx.abs();
                let az = dz.abs();
                let pos = [table_pos[0] + dx, table_pos[1], table_pos[2] + dz];
                if (ax == 2 && dz == 0) || (az == 2 && dx == 0) {
                    if block_at(pos).as_deref() != Some("minecraft:chiseled_quartz_block") {
                        return false;
                    }
                } else if ax == 2 && az == 2 {
                    if block_at(pos).as_deref() != Some("minecraft:gold_block") {
                        return false;
                    }
                } else if ax <= 1 && az <= 1 {
                    let b = block_at(pos);
                    if !matches!(b.as_deref(), Some("minecraft:air") | Some("minecraft:cave_air")) {
                        return false;
                    }
                }
            }
        }
        true
    }

    pub fn is_valid(host: &dyn Host, level: LevelRef, table_pos: [i32; 3]) -> bool {
        Self::is_valid_blocks(table_pos, |pos| {
            level
                .block_state(host, pos)
                .map(|st| st.block().id(host))
        })
    }
}

// ── EnchantingReagentStorage ───────────────────────────────────────────────

#[derive(Debug, Clone, PartialEq, Eq, Hash, Serialize, Deserialize)]
pub struct StoredReagent {
    pub item_id: String,
    pub count: u32,
}

#[derive(Debug, Clone, PartialEq, Eq, Hash)]
struct TableKey {
    dimension: String,
    pos: [i32; 3],
}

#[derive(Default)]
struct ReagentStoreInner {
    entries: HashMap<TableKey, StoredReagent>,
}

static REAGENT_STORE: OnceLock<Mutex<ReagentStoreInner>> = OnceLock::new();

fn reagent_store() -> &'static Mutex<ReagentStoreInner> {
    REAGENT_STORE.get_or_init(|| Mutex::new(ReagentStoreInner::default()))
}

pub struct EnchantingReagentStorage;

impl EnchantingReagentStorage {
    pub fn is_valid_reagent(item_id: &str) -> bool {
        matches!(
            item_id,
            "minecraft:amethyst_shard" | "minecraft:echo_shard" | "minecraft:lapis_lazuli"
        )
    }

    pub fn store(dimension: &str, pos: [i32; 3], item_id: &str, count: u32) {
        if count == 0 || !Self::is_valid_reagent(item_id) {
            Self::clear(dimension, pos);
            return;
        }
        let key = TableKey {
            dimension: dimension.to_owned(),
            pos,
        };
        if let Ok(mut guard) = reagent_store().lock() {
            guard.entries.insert(
                key,
                StoredReagent {
                    item_id: item_id.to_owned(),
                    count,
                },
            );
        }
    }

    pub fn take(dimension: &str, pos: [i32; 3]) -> Option<StoredReagent> {
        let key = TableKey {
            dimension: dimension.to_owned(),
            pos,
        };
        reagent_store().lock().ok()?.entries.remove(&key)
    }

    pub fn peek(dimension: &str, pos: [i32; 3]) -> Option<StoredReagent> {
        let key = TableKey {
            dimension: dimension.to_owned(),
            pos,
        };
        reagent_store().lock().ok()?.entries.get(&key).cloned()
    }

    pub fn clear(dimension: &str, pos: [i32; 3]) {
        let key = TableKey {
            dimension: dimension.to_owned(),
            pos,
        };
        if let Ok(mut guard) = reagent_store().lock() {
            guard.entries.remove(&key);
        }
    }

    pub fn clear_all() {
        if let Ok(mut guard) = reagent_store().lock() {
            guard.entries.clear();
        }
    }

    pub fn save_to_world(world_dir: &Path) {
        #[derive(Serialize)]
        struct EntryRow {
            dimension: String,
            pos: [i32; 3],
            item_id: String,
            count: u32,
        }
        let Ok(guard) = reagent_store().lock() else {
            return;
        };
        let rows: Vec<EntryRow> = guard
            .entries
            .iter()
            .map(|(k, v)| EntryRow {
                dimension: k.dimension.clone(),
                pos: k.pos,
                item_id: v.item_id.clone(),
                count: v.count,
            })
            .collect();
        let path = world_dir.join("eldrich-surge/reagents.json");
        if let Some(parent) = path.parent() {
            let _ = std::fs::create_dir_all(parent);
        }
        if let Ok(json) = serde_json::to_string_pretty(&rows) {
            let _ = std::fs::write(path, json);
        }
    }

    pub fn load_from_world(world_dir: &Path) {
        #[derive(Deserialize)]
        struct EntryRow {
            dimension: String,
            pos: [i32; 3],
            item_id: String,
            count: u32,
        }
        let path = world_dir.join("eldrich-surge/reagents.json");
        let Ok(text) = std::fs::read_to_string(path) else {
            return;
        };
        let Ok(rows) = serde_json::from_str::<Vec<EntryRow>>(&text) else {
            return;
        };
        if let Ok(mut guard) = reagent_store().lock() {
            guard.entries.clear();
            for r in rows {
                if r.count > 0 && Self::is_valid_reagent(&r.item_id) {
                    guard.entries.insert(
                        TableKey {
                            dimension: r.dimension,
                            pos: r.pos,
                        },
                        StoredReagent {
                            item_id: r.item_id,
                            count: r.count,
                        },
                    );
                }
            }
        }
    }
}

// ── Offer Eligibility ──────────────────────────────────────────────────────

pub fn is_offer_eligible(
    ench_id: &str,
    item_id: &str,
    reagent_id: Option<&str>,
    existing_enchants: &[(String, i32)],
    vanilla_max_level: i32,
    has_ritual: bool,
) -> bool {
    if item_id.is_empty() {
        return false;
    }
    if HARDCODED_ADVANCED_EXCLUSIONS.contains(&ench_id) {
        return false;
    }

    let is_spell = spell_books::is_cast_spell(ench_id);
    let is_echo = reagent_id == Some("minecraft:echo_shard");
    if is_spell != is_echo {
        return false;
    }
    if is_spell && item_id != "minecraft:book" {
        return false;
    }

    if !EnchLibState::is_enabled(ench_id) || !EnchLibState::is_allowed_in_advanced_table(ench_id) {
        return false;
    }

    if !is_spell && item_id != "minecraft:book" && item_id != "minecraft:enchanted_book" {
        if !EnchLibState::is_allowed_for_item(None, ench_id, item_id, &[]) {
            return false;
        }
    }

    let existing_ids: Vec<&str> = existing_enchants
        .iter()
        .filter(|(id, lvl)| *lvl > 0 && id != ench_id)
        .map(|(id, _)| id.as_str())
        .collect();
    if !EnchLibState::is_compatible_with_existing(ench_id, &existing_ids) {
        return false;
    }

    let current_level = existing_enchants
        .iter()
        .find(|(id, _)| id == ench_id)
        .map_or(0, |(_, lvl)| *lvl);

    if EnchLibState::is_category_component(ench_id)
        && current_level == 0
        && EnchLibState::category_component_count(&existing_ids) >= 3
    {
        return false;
    }

    let configured_cap = EnchLibState::resolve_enchanting_table_cap(ench_id, vanilla_max_level);
    if configured_cap < 1 {
        return false;
    }
    let effective_cap = if has_ritual {
        configured_cap.min(255)
    } else {
        1
    };
    current_level < effective_cap
}

// ── Horizon Block / BlockEntity Registration & Hooks ───────────────────────

struct AdvancedTableBlockPart;

impl BlockPart for AdvancedTableBlockPart {
    fn block_subscribed(&self) -> &'static [Option<mod_api::HookId>] {
        &[]
    }
}

struct AdvancedTableBlockEntityPart;

impl BlockPart for AdvancedTableBlockEntityPart {
    fn block_subscribed(&self) -> &'static [Option<mod_api::HookId>] {
        &[]
    }
}

impl BlockEntityPart for AdvancedTableBlockEntityPart {
    fn block_entity_subscribed(&self) -> &'static [Option<mod_api::HookId>] {
        &[Some(mod_api::HookId::PreRemoveSideEffects)]
    }

    fn pre_remove_side_effects(
        &self,
        _level: &mut dyn mod_api::LevelRandomHost,
        event: &mut PreRemoveSideEffectsEvent<'_>,
    ) {
        let pos = [event.pos.x, event.pos.y, event.pos.z];
        let _ = EnchantingReagentStorage::take("minecraft:overworld", pos);
    }
}

pub fn register(r: &mut dyn Registrar) {
    let block_key = r.block_key(ADVANCED_TABLE_BLOCK_ID);
    r.block_container(
        &block_key,
        ModContainer {
            slots: 2,
            faces: Default::default(),
            inputs: vec![1],
            outputs: vec![],
            menu: None,
        },
    );
    r.block_part(block_key, AdvancedTableBlockPart);
    let be_kind = r
        .block_entity_type("advanced_enchanting_table")
        .blocks(&[ADVANCED_TABLE_BLOCK_ID])
        .build();
    let _ = r.block_entity_part(&be_kind, AdvancedTableBlockEntityPart);
}

pub fn on_block_drops(_host: &dyn LevelHost, event: &mut BlockDropsEvent<'_>) -> Flow {
    let dim = format!("{:?}", event.dimension);
    let pos = [event.pos.x, event.pos.y, event.pos.z];
    let _ = EnchantingReagentStorage::take(&dim, pos);
    Flow::Continue
}

pub fn on_prepare_item_enchant(
    _host: &mut dyn ServerHost,
    _event: &mut PrepareItemEnchantEvent<'_>,
) {
}

pub fn on_enchantment_level_set(
    _host: &mut dyn ServerHost,
    _event: &mut EnchantmentLevelSetEvent<'_>,
) {
}

pub fn on_player_container_open(
    _host: &mut dyn ServerHost,
    _event: &mut PlayerContainerOpenEvent<'_>,
) {
}

pub fn on_player_container_close(
    _host: &mut dyn ServerHost,
    _event: &mut PlayerContainerCloseEvent<'_>,
) {
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn infusing_costs_match_java_test_suite() {
        // Common (30 XP, 3 reagents)
        assert_eq!(AdvancedInfusingCosts::experience(5, 5, RarityClass::Common), 30);
        assert_eq!(AdvancedInfusingCosts::reagents(5, 5), 3);
        assert_eq!(AdvancedInfusingCosts::experience(1, 5, RarityClass::Common), 6);
        assert_eq!(AdvancedInfusingCosts::reagents(1, 5), 1);

        // Uncommon (40), Rare (50), VeryRare (65)
        assert_eq!(
            AdvancedInfusingCosts::experience(4, 4, RarityClass::Uncommon),
            40
        );
        assert_eq!(AdvancedInfusingCosts::experience(3, 3, RarityClass::Rare), 50);
        assert_eq!(
            AdvancedInfusingCosts::experience(1, 1, RarityClass::VeryRare),
            65
        );

        // Invalid inputs return 0
        assert_eq!(AdvancedInfusingCosts::experience(0, 5, RarityClass::Common), 0);
        assert_eq!(AdvancedInfusingCosts::experience(6, 5, RarityClass::Common), 0);
        assert_eq!(AdvancedInfusingCosts::reagents(0, 5), 0);
        assert_eq!(AdvancedInfusingCosts::reagents(2, 0), 0);
    }

    #[test]
    fn formation_validates_exact_5x5_ring() {
        let valid_world = |pos: [i32; 3]| -> Option<String> {
            let [x, _, z] = pos;
            let ax = x.abs();
            let az = z.abs();
            if (ax == 2 && z == 0) || (az == 2 && x == 0) {
                Some("minecraft:chiseled_quartz_block".to_owned())
            } else if ax == 2 && az == 2 {
                Some("minecraft:gold_block".to_owned())
            } else if ax <= 1 && az <= 1 {
                Some("minecraft:air".to_owned())
            } else {
                Some("minecraft:stone".to_owned())
            }
        };
        assert!(AdvancedEnchantingFormation::is_valid_blocks([0, 64, 0], valid_world));

        // Torch in inner ring fails
        let torch_inner = |pos: [i32; 3]| -> Option<String> {
            if pos == [1, 64, 0] {
                Some("minecraft:torch".to_owned())
            } else {
                valid_world(pos)
            }
        };
        assert!(!AdvancedEnchantingFormation::is_valid_blocks([0, 64, 0], torch_inner));

        // Missing gold corner fails
        let missing_gold = |pos: [i32; 3]| -> Option<String> {
            if pos == [2, 64, 2] {
                Some("minecraft:iron_block".to_owned())
            } else {
                valid_world(pos)
            }
        };
        assert!(!AdvancedEnchantingFormation::is_valid_blocks([0, 64, 0], missing_gold));
    }

    #[test]
    fn reagent_storage_round_trips_and_validates_reagent_types() {
        EnchantingReagentStorage::clear_all();
        EnchantingReagentStorage::store("minecraft:overworld", [10, 64, -5], "minecraft:amethyst_shard", 12);
        assert_eq!(
            EnchantingReagentStorage::peek("minecraft:overworld", [10, 64, -5]),
            Some(StoredReagent {
                item_id: "minecraft:amethyst_shard".to_owned(),
                count: 12,
            })
        );
        let taken = EnchantingReagentStorage::take("minecraft:overworld", [10, 64, -5]);
        assert!(taken.is_some());
        assert!(EnchantingReagentStorage::peek("minecraft:overworld", [10, 64, -5]).is_none());

        // Non-reagent is rejected
        EnchantingReagentStorage::store("minecraft:overworld", [1, 64, 1], "minecraft:dirt", 5);
        assert!(EnchantingReagentStorage::peek("minecraft:overworld", [1, 64, 1]).is_none());
    }

    #[test]
    fn offer_eligibility_enforces_spells_echo_and_exclusions() {
        // Hardcoded exclusion
        assert!(!is_offer_eligible(
            "eldrich-surge:blade_of_apocalypse",
            "minecraft:diamond_sword",
            Some("minecraft:amethyst_shard"),
            &[],
            5,
            true
        ));
        // Cast spell requires echo_shard AND minecraft:book
        assert!(is_offer_eligible(
            "eldrich-surge:storm_spell",
            "minecraft:book",
            Some("minecraft:echo_shard"),
            &[],
            1,
            true
        ));
        assert!(!is_offer_eligible(
            "eldrich-surge:storm_spell",
            "minecraft:enchanted_book",
            Some("minecraft:echo_shard"),
            &[],
            1,
            true
        ));
        assert!(!is_offer_eligible(
            "eldrich-surge:storm_spell",
            "minecraft:book",
            Some("minecraft:amethyst_shard"),
            &[],
            1,
            true
        ));
    }
}
