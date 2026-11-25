//! Phase 5 — EnchLib Bridge (`EnchantmentCapsConfig`, `EnchLibMobCategories`, `MobCategoryDamageMechanics`).

use mod_api::Host;
use serde::Deserialize;
use std::collections::{BTreeMap, BTreeSet, HashMap};
use std::path::Path;
use std::sync::{OnceLock, RwLock};

pub const NORMAL_TABLE_ID: &str = "minecraft:enchanting_table";
pub const ADVANCED_TABLE_ID: &str = "eldrich-surge:advanced_enchanting_table";

pub const CATEGORY_COMPONENTS: &[(&str, &str)] = &[
    ("eldrich-surge:bane_of_end", "void"),
    ("eldrich-surge:undead_slayer", "undead"),
    ("eldrich-surge:exorcist", "hell"),
    ("eldrich-surge:butcher", "animals"),
    ("eldrich-surge:herbicide", "fungi"),
    ("eldrich-surge:witch_hunter", "magik"),
    ("eldrich-surge:wrath_of_the_abyss", "water"),
    ("eldrich-surge:creeping_threat", "arthropods"),
    ("eldrich-surge:smoother", "cubic"),
    ("eldrich-surge:flogging", "rebel"),
];

pub const STAR_FATE_CATEGORIES: &[&str] = &["magik", "arthropods", "rebel", "fungi", "cubic"];
pub const BLADE_CATEGORIES: &[&str] = &["animals", "undead", "void", "hell", "water"];

pub const GEOGRAPHICAL_CATEGORIES: &[&str] = &[
    "surface",
    "underground",
    "aquatic",
    "aerial",
    "nether",
    "end",
    "arid",
    "cold",
];

const BUNDLED_CAPS_JSON: &str = include_str!("defaults/enchantment_caps.json");
const BUNDLED_MOB_CONFIG: &str = include_str!("defaults/MobCategory.config");

#[derive(Debug, Clone, Copy, PartialEq, Eq, Hash)]
pub enum RarityClass {
    Common,
    Uncommon,
    Rare,
    VeryRare,
}

impl RarityClass {
    pub fn weight(self) -> i32 {
        match self {
            Self::Common => 100,
            Self::Uncommon => 60,
            Self::Rare => 30,
            Self::VeryRare => 5,
        }
    }

    pub fn advanced_xp_base(self) -> i32 {
        match self {
            Self::Common => 30,
            Self::Uncommon => 40,
            Self::Rare => 50,
            Self::VeryRare => 65,
        }
    }

    pub fn default_table_chance(self) -> i32 {
        match self {
            Self::Common => 100,
            Self::Uncommon => 80,
            Self::Rare => 50,
            Self::VeryRare => 25,
        }
    }

    pub fn from_str_lossy(raw: &str) -> Self {
        match raw.trim().to_ascii_lowercase().as_str() {
            "uncommon" => Self::Uncommon,
            "rare" => Self::Rare,
            "very_rare" | "veryrare" | "very rare" => Self::VeryRare,
            _ => Self::Common,
        }
    }

    pub fn from_classification(raw: &str) -> Self {
        let lower = raw.trim().to_ascii_lowercase();
        if lower.contains("ultra")
            || lower.contains("legendary")
            || lower.contains("unique")
            || lower.contains("ritual")
            || lower.contains("very")
        {
            Self::VeryRare
        } else if lower.contains("rare") || lower.contains("curse") {
            Self::Rare
        } else if lower.contains("uncommon") {
            Self::Uncommon
        } else {
            Self::Common
        }
    }
}

#[derive(Debug, Clone)]
pub struct EnchantmentRule {
    pub enabled: bool,
    pub anvil_max_level: i32,
    pub enchanting_table_max_level: i32,
    pub normal_table: bool,
    pub advanced_table: bool,
    pub loot: bool,
    pub rarity_class: RarityClass,
    pub normal_table_chance: i32,
    pub advanced_table_chance: i32,
    pub supported_items: Vec<String>,
    pub incompatible_enchantments: BTreeSet<String>,
}

impl Default for EnchantmentRule {
    fn default() -> Self {
        Self {
            enabled: true,
            anvil_max_level: 0,
            enchanting_table_max_level: 0,
            normal_table: true,
            advanced_table: true,
            loot: true,
            rarity_class: RarityClass::Common,
            normal_table_chance: 100,
            advanced_table_chance: 100,
            supported_items: Vec::new(),
            incompatible_enchantments: BTreeSet::new(),
        }
    }
}

#[derive(Debug, Clone)]
pub struct EnchLibBridge {
    pub enchlib_loaded: bool,
    pub rules: HashMap<String, EnchantmentRule>,
    pub mob_categories: HashMap<String, BTreeSet<String>>,
    pub mob_categories_overridden: bool,
}

impl Default for EnchLibBridge {
    fn default() -> Self {
        Self::with_embedded_defaults()
    }
}

static GLOBAL_STATE: OnceLock<RwLock<EnchLibBridge>> = OnceLock::new();

fn state_lock() -> &'static RwLock<EnchLibBridge> {
    GLOBAL_STATE.get_or_init(|| RwLock::new(EnchLibBridge::with_embedded_defaults()))
}

pub struct EnchLibState;

impl EnchLibState {
    pub fn load_for_world(
        world_dir: Option<&Path>,
        instance_dir: Option<&Path>,
        enchlib_loaded: bool,
    ) {
        let mut bridge = EnchLibBridge::with_embedded_defaults();
        bridge.enchlib_loaded = enchlib_loaded;

        if let Some(inst) = instance_dir {
            let global_caps = inst.join("config/eldrich-surge/enchantment_caps.json");
            if let Ok(text) = std::fs::read_to_string(&global_caps) {
                bridge.overlay_caps_json(&text);
            }
        }
        if let Some(world) = world_dir {
            let world_caps = world.join("eldrich-surge/enchantment_caps.json");
            if let Ok(text) = std::fs::read_to_string(&world_caps) {
                bridge.overlay_caps_json(&text);
            }
        }

        let enchlib_dir = world_dir
            .map(|w| w.join("config/enchlib"))
            .filter(|p| p.exists())
            .or_else(|| {
                instance_dir
                    .map(|i| i.join("config/enchlib"))
                    .filter(|p| p.exists())
            });

        if let Some(dir) = enchlib_dir {
            bridge.enchlib_loaded = true;
            if let Ok(text) = std::fs::read_to_string(dir.join("AviableEnch.json5")) {
                bridge.overlay_aviable_ench_json5(&text);
            }
            if let Ok(text) = std::fs::read_to_string(dir.join("EnchantmentsDetails.json5")) {
                bridge.overlay_details_json5(&text);
            }
            let mob_json = dir.join("MobCategory.json");
            let mob_cfg = dir.join("MobCategory.config");
            if let Ok(text) = std::fs::read_to_string(&mob_json) {
                bridge.overlay_mob_category_json(&text);
            } else if let Ok(text) = std::fs::read_to_string(&mob_cfg) {
                bridge.overlay_mob_category_config(&text);
            }
        }

        if let Ok(mut guard) = state_lock().write() {
            *guard = bridge;
        }
    }

    pub fn reset_to_defaults() {
        if let Ok(mut guard) = state_lock().write() {
            *guard = EnchLibBridge::with_embedded_defaults();
        }
    }

    pub fn is_enabled(id: &str) -> bool {
        state_lock().read().map_or(true, |g| g.is_enabled(id))
    }

    pub fn is_allowed_in_normal_table(id: &str) -> bool {
        state_lock()
            .read()
            .map_or(true, |g| g.is_allowed_in_normal_table(id))
    }

    pub fn is_allowed_in_advanced_table(id: &str) -> bool {
        state_lock()
            .read()
            .map_or(true, |g| g.is_allowed_in_advanced_table(id))
    }

    pub fn is_allowed_as_loot(id: &str) -> bool {
        state_lock()
            .read()
            .map_or(true, |g| g.is_allowed_as_loot(id))
    }

    pub fn resolve_anvil_cap(id: &str, vanilla_cap: i32) -> i32 {
        state_lock()
            .read()
            .map_or(vanilla_cap.max(1), |g| g.resolve_anvil_cap(id, vanilla_cap))
    }

    pub fn resolve_enchanting_table_cap(id: &str, vanilla_cap: i32) -> i32 {
        state_lock()
            .read()
            .map_or(vanilla_cap.max(1), |g| {
                g.resolve_enchanting_table_cap(id, vanilla_cap)
            })
    }

    pub fn resolve_rarity_class(id: &str) -> RarityClass {
        state_lock()
            .read()
            .map_or(RarityClass::Common, |g| g.resolve_rarity_class(id))
    }

    pub fn passes_rarity(
        id: &str,
        table_id: &str,
        item_id: &str,
        slot: i32,
        level: i32,
        seed: i32,
    ) -> bool {
        state_lock().read().map_or(true, |g| {
            g.passes_rarity(id, table_id, item_id, slot, level, seed)
        })
    }

    pub fn is_allowed_for_item(
        host: Option<&dyn Host>,
        id: &str,
        item_id: &str,
        item_tags: &[&str],
    ) -> bool {
        state_lock()
            .read()
            .map_or(true, |g| g.is_allowed_for_item(host, id, item_id, item_tags))
    }

    pub fn are_compatible(id_a: &str, id_b: &str) -> bool {
        state_lock()
            .read()
            .map_or(true, |g| g.are_compatible(id_a, id_b))
    }

    pub fn is_compatible_with_existing(id: &str, existing_ids: &[&str]) -> bool {
        state_lock()
            .read()
            .map_or(true, |g| g.is_compatible_with_existing(id, existing_ids))
    }

    pub fn is_category_component(id: &str) -> bool {
        let norm = normalize_ench_id(id);
        CATEGORY_COMPONENTS.iter().any(|(c, _)| *c == norm)
    }

    pub fn category_component_count(existing_ids: &[&str]) -> usize {
        let mut seen = BTreeSet::new();
        for id in existing_ids {
            let norm = normalize_ench_id(id);
            if CATEGORY_COMPONENTS.iter().any(|(c, _)| *c == norm) {
                seen.insert(norm);
            }
        }
        seen.len()
    }

    pub fn has_mob_category(host: Option<&dyn Host>, entity_id: &str, category: &str) -> bool {
        state_lock()
            .read()
            .map_or(false, |g| g.has_mob_category(host, entity_id, category))
    }

    pub fn count_mob_categories(
        host: Option<&dyn Host>,
        entity_id: &str,
        categories: &[&str],
    ) -> i32 {
        state_lock()
            .read()
            .map_or(0, |g| g.count_mob_categories(host, entity_id, categories))
    }

    pub fn mob_category_damage_bonus(
        host: Option<&dyn Host>,
        victim_id: &str,
        is_creeper: bool,
        is_vanilla_smite_target: bool,
        weapon_enchants: &[(String, i32)],
    ) -> f32 {
        state_lock().read().map_or(0.0, |g| {
            g.mob_category_damage_bonus(
                host,
                victim_id,
                is_creeper,
                is_vanilla_smite_target,
                weapon_enchants,
            )
        })
    }
}

impl EnchLibBridge {
    pub fn with_embedded_defaults() -> Self {
        let mut bridge = Self {
            enchlib_loaded: false,
            rules: HashMap::new(),
            mob_categories: HashMap::new(),
            mob_categories_overridden: false,
        };
        bridge.overlay_caps_json(BUNDLED_CAPS_JSON);
        bridge.apply_default_incompatibilities();
        bridge.apply_vanilla_table_defaults();
        bridge.parse_mob_category_config_into(BUNDLED_MOB_CONFIG, false);
        bridge
    }

    fn apply_vanilla_table_defaults(&mut self) {
        for vanilla in [
            "minecraft:sharpness",
            "minecraft:smite",
            "minecraft:bane_of_arthropods",
        ] {
            let entry = self.rules.entry(vanilla.to_owned()).or_default();
            entry.normal_table = false;
            entry.advanced_table = false;
            entry.loot = false;
        }
        for ritual in [
            "eldrich-surge:deathbreak",
            "eldrich-surge:blade_of_apocalypse",
            "eldrich-surge:star_fate",
            "eldrich-surge:gream_reaper",
        ] {
            let entry = self.rules.entry(ritual.to_owned()).or_default();
            entry.normal_table = false;
            entry.advanced_table = false;
        }
        for spell in [
            "eldrich-surge:cocktail_spell",
            "eldrich-surge:ragnarok",
            "eldrich-surge:red_moon",
            "eldrich-surge:storm_spell",
            "eldrich-surge:trench_spell",
        ] {
            let entry = self.rules.entry(spell.to_owned()).or_default();
            entry.normal_table = false;
            entry.advanced_table = true;
            entry.rarity_class = RarityClass::VeryRare;
        }
    }

    fn apply_default_incompatibilities(&mut self) {
        let pairs: &[(&str, &str)] = &[
            ("minecraft:sharpness", "eldrich-surge:katana"),
            ("minecraft:bane_of_arthropods", "eldrich-surge:creeping_threat"),
            ("minecraft:impaling", "eldrich-surge:neptunes_will"),
            ("eldrich-surge:star_fate", "eldrich-surge:witch_hunter"),
            ("eldrich-surge:star_fate", "eldrich-surge:creeping_threat"),
            ("eldrich-surge:star_fate", "eldrich-surge:flogging"),
            ("eldrich-surge:star_fate", "eldrich-surge:herbicide"),
            ("eldrich-surge:star_fate", "eldrich-surge:smoother"),
            ("eldrich-surge:blade_of_apocalypse", "eldrich-surge:butcher"),
            ("eldrich-surge:blade_of_apocalypse", "eldrich-surge:undead_slayer"),
            ("eldrich-surge:blade_of_apocalypse", "eldrich-surge:bane_of_end"),
            ("eldrich-surge:blade_of_apocalypse", "eldrich-surge:exorcist"),
            ("eldrich-surge:blade_of_apocalypse", "eldrich-surge:wrath_of_the_abyss"),
            ("eldrich-surge:gream_reaper", "eldrich-surge:star_fate"),
            ("eldrich-surge:gream_reaper", "eldrich-surge:blade_of_apocalypse"),
            ("eldrich-surge:gream_reaper", "eldrich-surge:katana"),
            ("eldrich-surge:gream_reaper", "eldrich-surge:neptunes_will"),
        ];
        for (a, b) in pairs {
            self.add_symmetric_incompatibility(a, b);
        }
    }

    pub fn add_symmetric_incompatibility(&mut self, a: &str, b: &str) {
        let na = normalize_ench_id(a);
        let nb = normalize_ench_id(b);
        if na == nb {
            return;
        }
        self.rules
            .entry(na.clone())
            .or_default()
            .incompatible_enchantments
            .insert(nb.clone());
        self.rules
            .entry(nb)
            .or_default()
            .incompatible_enchantments
            .insert(na);
    }

    pub fn overlay_caps_json(&mut self, raw: &str) {
        let cleaned = strip_bom(raw);
        let Ok(root) = serde_json::from_str::<serde_json::Value>(cleaned) else {
            return;
        };
        let Some(map) = root
            .get("enchantments")
            .and_then(|v| v.as_object())
            .or_else(|| root.as_object())
        else {
            return;
        };
        for (key, val) in map {
            let Some(obj) = val.as_object() else {
                continue;
            };
            let id = normalize_ench_id(key);
            let mut rule = self.rules.get(&id).cloned().unwrap_or_default();
            if let Some(b) = obj.get("enabled").and_then(|v| v.as_bool()) {
                rule.enabled = b;
            }
            if let Some(n) = obj.get("anvilMaxLevel").and_then(|v| v.as_i64()) {
                rule.anvil_max_level = (n as i32).clamp(0, 255);
            }
            if let Some(n) = obj.get("enchantingTableMaxLevel").and_then(|v| v.as_i64()) {
                rule.enchanting_table_max_level = (n as i32).clamp(0, 255);
            }
            if let Some(b) = obj.get("normalTable").and_then(|v| v.as_bool()) {
                rule.normal_table = b;
            }
            if let Some(b) = obj.get("advancedTable").and_then(|v| v.as_bool()) {
                rule.advanced_table = b;
            }
            if let Some(b) = obj.get("loot").and_then(|v| v.as_bool()) {
                rule.loot = b;
            }
            if let Some(s) = obj
                .get("rarityClass")
                .or_else(|| obj.get("rarity"))
                .and_then(|v| v.as_str())
            {
                rule.rarity_class = RarityClass::from_str_lossy(s);
            } else if let Some(s) = obj.get("classification").and_then(|v| v.as_str()) {
                rule.rarity_class = RarityClass::from_classification(s);
            }
            if let Some(n) = obj.get("normalTableChance").and_then(|v| v.as_i64()) {
                rule.normal_table_chance = (n as i32).clamp(0, 100);
            } else {
                rule.normal_table_chance = rule.rarity_class.default_table_chance();
            }
            if let Some(n) = obj.get("advancedTableChance").and_then(|v| v.as_i64()) {
                rule.advanced_table_chance = (n as i32).clamp(0, 100);
            }
            if let Some(arr) = obj.get("supportedItems").and_then(|v| v.as_array()) {
                rule.supported_items = arr
                    .iter()
                    .filter_map(|v| v.as_str().map(|s| s.trim().to_owned()))
                    .filter(|s| !s.is_empty())
                    .collect();
            }
            let mut inc_list = Vec::new();
            if let Some(arr) = obj
                .get("incompatibleEnchantments")
                .and_then(|v| v.as_array())
            {
                for item in arr {
                    if let Some(s) = item.as_str() {
                        inc_list.push(normalize_ench_id(s));
                    }
                }
            }
            self.rules.insert(id.clone(), rule);
            for other in inc_list {
                self.add_symmetric_incompatibility(&id, &other);
            }
        }
    }

    pub fn overlay_aviable_ench_json5(&mut self, raw: &str) {
        #[derive(Deserialize)]
        struct Entry {
            id: String,
            enabled: bool,
        }
        let cleaned = strip_json5(raw);
        let Ok(entries) = serde_json::from_str::<Vec<Entry>>(&cleaned) else {
            return;
        };
        for e in entries {
            let id = normalize_ench_id(&e.id);
            self.rules.entry(id).or_default().enabled = e.enabled;
        }
    }

    pub fn overlay_details_json5(&mut self, raw: &str) {
        #[derive(Deserialize)]
        struct TableRule {
            enabled: Option<bool>,
            chance: Option<i32>,
        }
        #[derive(Deserialize)]
        struct Detail {
            id: String,
            max_level: Option<i32>,
            enc_category: Option<Vec<String>>,
            rarity: Option<String>,
            incompatible_enchantments: Option<Vec<String>>,
            table_rules: Option<BTreeMap<String, TableRule>>,
        }
        #[derive(Deserialize)]
        struct Root {
            enchantments: Vec<Detail>,
        }

        let cleaned = strip_json5(raw);
        let Ok(root) = serde_json::from_str::<Root>(&cleaned) else {
            return;
        };

        for d in root.enchantments {
            let id = normalize_ench_id(&d.id);
            let mut rule = self.rules.get(&id).cloned().unwrap_or_default();
            if let Some(ml) = d.max_level
                && ml > 0
            {
                let clamped = ml.clamp(1, 255);
                let existing_cap = rule
                    .anvil_max_level
                    .max(rule.enchanting_table_max_level);
                if !(clamped == 1 && existing_cap > 1) {
                    rule.anvil_max_level = clamped;
                    rule.enchanting_table_max_level = clamped;
                }
            }
            if let Some(cats) = d.enc_category {
                rule.supported_items = cats;
            }
            if let Some(r) = d.rarity {
                rule.rarity_class = RarityClass::from_str_lossy(&r);
            }
            if let Some(tables) = d.table_rules {
                if let Some(tr) = tables.get(NORMAL_TABLE_ID) {
                    if let Some(en) = tr.enabled {
                        rule.normal_table = en;
                    }
                    if let Some(ch) = tr.chance {
                        rule.normal_table_chance = ch.clamp(0, 100);
                    }
                }
                if let Some(tr) = tables.get(ADVANCED_TABLE_ID) {
                    if let Some(en) = tr.enabled {
                        rule.advanced_table = en;
                    }
                    if let Some(ch) = tr.chance {
                        rule.advanced_table_chance = ch.clamp(0, 100);
                    }
                }
            }
            let incs = d.incompatible_enchantments.unwrap_or_default();
            self.rules.insert(id.clone(), rule);
            for other in incs {
                self.add_symmetric_incompatibility(&id, &other);
            }
        }
    }

    pub fn overlay_mob_category_json(&mut self, raw: &str) {
        let cleaned = strip_json5(raw);
        let Ok(val) = serde_json::from_str::<serde_json::Value>(&cleaned) else {
            return;
        };
        let Some(obj) = val.as_object() else {
            return;
        };
        let mut parsed: HashMap<String, BTreeSet<String>> = HashMap::new();
        for (mob, cats_val) in obj {
            let norm_mob = normalize_mob_id(mob);
            let mut set = BTreeSet::new();
            if let Some(arr) = cats_val.as_array() {
                for c in arr {
                    if let Some(s) = c.as_str() {
                        let cat = s.trim().to_ascii_lowercase();
                        if !cat.is_empty() {
                            set.insert(cat);
                        }
                    }
                }
            }
            parsed.insert(norm_mob, set);
        }
        if !parsed.is_empty() {
            self.mob_categories = parsed;
            self.mob_categories_overridden = true;
        }
    }

    pub fn overlay_mob_category_config(&mut self, raw: &str) {
        self.parse_mob_category_config_into(raw, true);
    }

    fn parse_mob_category_config_into(&mut self, raw: &str, mark_overridden: bool) {
        let cleaned = strip_bom(raw);
        let mut parsed: HashMap<String, BTreeSet<String>> = HashMap::new();
        for line in cleaned.lines() {
            let trimmed = line.trim();
            if trimmed.is_empty() || trimmed.starts_with('#') || trimmed.starts_with("//") {
                continue;
            }
            let Some((left, right)) = trimmed.split_once('=') else {
                continue;
            };
            let mob = normalize_mob_id(left);
            if mob.is_empty() {
                continue;
            }
            let mut set = BTreeSet::new();
            for part in right.split(',') {
                let cat = part.trim().to_ascii_lowercase();
                if !cat.is_empty() {
                    set.insert(cat);
                }
            }
            parsed.insert(mob, set);
        }
        if !parsed.is_empty() {
            self.mob_categories = parsed;
            if mark_overridden {
                self.mob_categories_overridden = true;
            }
        }
    }

    pub fn is_enabled(&self, id: &str) -> bool {
        let norm = normalize_ench_id(id);
        self.rules.get(&norm).map_or(true, |r| r.enabled)
    }

    pub fn is_allowed_in_normal_table(&self, id: &str) -> bool {
        let norm = normalize_ench_id(id);
        self.rules
            .get(&norm)
            .map_or(true, |r| r.enabled && r.normal_table)
    }

    pub fn is_allowed_in_advanced_table(&self, id: &str) -> bool {
        let norm = normalize_ench_id(id);
        self.rules
            .get(&norm)
            .map_or(true, |r| r.enabled && r.advanced_table)
    }

    pub fn is_allowed_as_loot(&self, id: &str) -> bool {
        let norm = normalize_ench_id(id);
        self.rules
            .get(&norm)
            .map_or(true, |r| r.enabled && r.loot)
    }

    pub fn resolve_anvil_cap(&self, id: &str, vanilla_cap: i32) -> i32 {
        let norm = normalize_ench_id(id);
        let configured = self.rules.get(&norm).map_or(0, |r| r.anvil_max_level);
        if configured > 0 {
            if configured == 1 && vanilla_cap > 1 {
                vanilla_cap.clamp(1, 255)
            } else {
                configured.clamp(1, 255)
            }
        } else {
            vanilla_cap.clamp(1, 255)
        }
    }

    pub fn resolve_enchanting_table_cap(&self, id: &str, vanilla_cap: i32) -> i32 {
        let norm = normalize_ench_id(id);
        let configured = self
            .rules
            .get(&norm)
            .map_or(0, |r| r.enchanting_table_max_level);
        if configured > 0 {
            if configured == 1 && vanilla_cap > 1 {
                vanilla_cap.clamp(1, 255)
            } else {
                configured.clamp(1, 255)
            }
        } else {
            vanilla_cap.clamp(1, 255)
        }
    }

    pub fn resolve_rarity_class(&self, id: &str) -> RarityClass {
        let norm = normalize_ench_id(id);
        self.rules
            .get(&norm)
            .map_or(RarityClass::Common, |r| r.rarity_class)
    }

    pub fn passes_rarity(
        &self,
        id: &str,
        table_id: &str,
        item_id: &str,
        slot: i32,
        level: i32,
        seed: i32,
    ) -> bool {
        let norm = normalize_ench_id(id);
        let rule = self.rules.get(&norm);
        let rarity = rule.map_or(RarityClass::Common, |r| r.rarity_class);
        let table_chance = match table_id {
            ADVANCED_TABLE_ID => rule.map_or(100, |r| r.advanced_table_chance),
            _ => rule.map_or(rarity.default_table_chance(), |r| r.normal_table_chance),
        };
        let effective = (table_chance.clamp(0, 100) * rarity.weight()) / 100;
        if effective >= 100 {
            return true;
        }
        if effective <= 0 {
            return false;
        }
        let roll = deterministic_roll_100(&norm, item_id, slot, level, seed);
        roll < effective
    }

    pub fn is_allowed_for_item(
        &self,
        _host: Option<&dyn Host>,
        id: &str,
        item_id: &str,
        item_tags: &[&str],
    ) -> bool {
        let norm = normalize_ench_id(id);
        let Some(rule) = self.rules.get(&norm) else {
            return true;
        };
        if rule.supported_items.is_empty() {
            return true;
        }
        let mut has_positive = false;
        let mut positive_matched = false;
        let mut excluded = false;

        for raw in &rule.supported_items {
            let trimmed = raw.trim();
            if trimmed.is_empty() {
                continue;
            }
            if let Some(neg) = trimmed.strip_prefix('!') {
                if neg.eq_ignore_ascii_case("all") {
                    if item_id != "minecraft:book" && item_id != "minecraft:enchanted_book" {
                        excluded = true;
                    }
                } else if matches_item_selector(neg, item_id, item_tags) {
                    excluded = true;
                }
            } else {
                has_positive = true;
                if matches_item_selector(trimmed, item_id, item_tags) {
                    positive_matched = true;
                }
            }
        }

        (!has_positive || positive_matched) && !excluded
    }

    pub fn are_compatible(&self, id_a: &str, id_b: &str) -> bool {
        let a = normalize_ench_id(id_a);
        let b = normalize_ench_id(id_b);
        if a == b {
            return true;
        }
        // CurseCompatibilityMixin rules
        if a == "eldrich-surge:curse_of_incompatibility"
            || b == "eldrich-surge:curse_of_incompatibility"
        {
            return false;
        }
        if (a == "eldrich-surge:curse_of_fatigue" && b == "minecraft:efficiency")
            || (b == "eldrich-surge:curse_of_fatigue" && a == "minecraft:efficiency")
        {
            return false;
        }
        if self
            .rules
            .get(&a)
            .is_some_and(|r| r.incompatible_enchantments.contains(&b))
        {
            return false;
        }
        if self
            .rules
            .get(&b)
            .is_some_and(|r| r.incompatible_enchantments.contains(&a))
        {
            return false;
        }
        true
    }

    pub fn is_compatible_with_existing(&self, id: &str, existing_ids: &[&str]) -> bool {
        existing_ids.iter().all(|other| self.are_compatible(id, other))
    }

    pub fn has_mob_category(
        &self,
        host: Option<&dyn Host>,
        entity_id: &str,
        category: &str,
    ) -> bool {
        let cat = category.trim().to_ascii_lowercase();
        if cat == "all" {
            return true;
        }
        let norm_entity = normalize_mob_id(entity_id);
        if self.mob_categories_overridden
            && let Some(cats) = self.mob_categories.get(&norm_entity)
        {
            return cats.contains(&cat);
        }
        if let Some(h) = host {
            let tag = fallback_entity_tag(&cat);
            if let Some(entry) = h.registry_entry("minecraft:entity_type", &norm_entity)
                && h.registry_entry_in_tag(&entry, &tag) == Some(true)
            {
                return true;
            }
        }
        self.mob_categories
            .get(&norm_entity)
            .is_some_and(|cats| cats.contains(&cat))
    }

    pub fn count_mob_categories(
        &self,
        host: Option<&dyn Host>,
        entity_id: &str,
        categories: &[&str],
    ) -> i32 {
        categories
            .iter()
            .filter(|cat| self.has_mob_category(host, entity_id, cat))
            .count() as i32
    }

    pub fn mob_category_damage_bonus(
        &self,
        host: Option<&dyn Host>,
        victim_id: &str,
        is_creeper: bool,
        is_vanilla_smite_target: bool,
        weapon_enchants: &[(String, i32)],
    ) -> f32 {
        let mut level_map: HashMap<String, i32> = HashMap::new();
        for (id, lvl) in weapon_enchants {
            if *lvl > 0 {
                let norm = normalize_ench_id(id);
                let e = level_map.entry(norm).or_insert(0);
                *e = (*e).max(*lvl);
            }
        }

        // Blade of Apocalypse replaces bonusForHit (does not stack with star_fate or 10 components)
        if let Some(&blade_lvl) = level_map.get("eldrich-surge:blade_of_apocalypse")
            && blade_lvl > 0
        {
            let matched = self.count_mob_categories(host, victim_id, BLADE_CATEGORIES);
            return matched as f32 * blade_lvl as f32 * 2.5;
        }

        let mut bonus = 0.0_f32;
        for (ench_id, cat) in CATEGORY_COMPONENTS {
            if let Some(&lvl) = level_map.get(*ench_id)
                && lvl > 0
            {
                let applies = if *ench_id == "eldrich-surge:creeping_threat" {
                    is_creeper || self.has_mob_category(host, victim_id, cat)
                } else {
                    self.has_mob_category(host, victim_id, cat)
                };
                if applies {
                    bonus += lvl as f32 * 2.5;
                }
            }
        }

        if let Some(&star_lvl) = level_map.get("eldrich-surge:star_fate")
            && star_lvl > 0
        {
            let matched = self.count_mob_categories(host, victim_id, STAR_FATE_CATEGORIES);
            bonus += matched as f32 * star_lvl as f32 * 2.5;
        }

        if let Some(&smite_lvl) = level_map.get("minecraft:smite")
            && smite_lvl > 0
            && !is_vanilla_smite_target
            && self.has_mob_category(host, victim_id, "undead")
        {
            bonus += smite_lvl as f32 * 2.5;
        }

        bonus
    }
}

pub fn normalize_ench_id(raw: &str) -> String {
    let trimmed = raw.trim().to_ascii_lowercase();
    if trimmed.contains(':') {
        trimmed
    } else {
        format!("eldrich-surge:{trimmed}")
    }
}

pub fn normalize_mob_id(raw: &str) -> String {
    let lower = raw.trim().to_ascii_lowercase();
    match lower.as_str() {
        "dog" => return "minecraft:wolf".to_owned(),
        "ghastling" => return "minecraft:happy_ghast".to_owned(),
        "ravanger" => return "minecraft:ravager".to_owned(),
        "wither boss" => return "minecraft:wither".to_owned(),
        "zombified villager" => return "minecraft:zombie_villager".to_owned(),
        _ => {}
    }
    let normalized = lower.replace(' ', "_");
    if normalized.contains(':') {
        normalized
    } else {
        format!("minecraft:{normalized}")
    }
}

fn fallback_entity_tag(cat: &str) -> String {
    if cat == "void" {
        "eldrich-surge:is_end_mob".to_owned()
    } else if GEOGRAPHICAL_CATEGORIES.contains(&cat) {
        format!("eldrich-surge:geographical/{cat}")
    } else {
        format!("eldrich-surge:{cat}")
    }
}

pub fn strip_bom(raw: &str) -> &str {
    raw.strip_prefix('\u{FEFF}').unwrap_or(raw)
}

/// Strips UTF-8 BOM, `//` line comments, `/* ... */` block comments, and trailing commas
/// outside of JSON string literals so JSON5 configs parse cleanly with `serde_json`.
pub fn strip_json5(raw: &str) -> String {
    let src = strip_bom(raw).as_bytes();
    let mut no_comments = Vec::with_capacity(src.len());
    let mut i = 0;
    let mut in_str = false;
    let mut escaped = false;

    while i < src.len() {
        let b = src[i];
        if in_str {
            no_comments.push(b);
            if escaped {
                escaped = false;
            } else if b == b'\\' {
                escaped = true;
            } else if b == b'"' {
                in_str = false;
            }
            i += 1;
            continue;
        }
        if b == b'"' {
            in_str = true;
            no_comments.push(b);
            i += 1;
            continue;
        }
        if b == b'/' && i + 1 < src.len() && src[i + 1] == b'/' {
            i += 2;
            while i < src.len() && src[i] != b'\n' {
                i += 1;
            }
            continue;
        }
        if b == b'/' && i + 1 < src.len() && src[i + 1] == b'*' {
            i += 2;
            while i + 1 < src.len() && !(src[i] == b'*' && src[i + 1] == b'/') {
                i += 1;
            }
            i = (i + 2).min(src.len());
            continue;
        }
        no_comments.push(b);
        i += 1;
    }

    // Remove trailing commas before `}` or `]`
    let mut out = Vec::with_capacity(no_comments.len());
    in_str = false;
    escaped = false;
    i = 0;
    while i < no_comments.len() {
        let b = no_comments[i];
        if in_str {
            out.push(b);
            if escaped {
                escaped = false;
            } else if b == b'\\' {
                escaped = true;
            } else if b == b'"' {
                in_str = false;
            }
            i += 1;
            continue;
        }
        if b == b'"' {
            in_str = true;
            out.push(b);
            i += 1;
            continue;
        }
        if b == b',' {
            let mut j = i + 1;
            while j < no_comments.len() && no_comments[j].is_ascii_whitespace() {
                j += 1;
            }
            if j < no_comments.len() && (no_comments[j] == b'}' || no_comments[j] == b']') {
                i += 1;
                continue;
            }
        }
        out.push(b);
        i += 1;
    }

    String::from_utf8_lossy(&out).into_owned()
}

fn deterministic_roll_100(ench_id: &str, item_id: &str, slot: i32, level: i32, seed: i32) -> i32 {
    let mut h: u64 = ((seed as u64) << 32) ^ 0x9E37_79B9_7F4A_7C15;
    for b in ench_id.bytes().chain(item_id.bytes()) {
        h ^= u64::from(b);
        h = h.wrapping_mul(0x1000_0000_01B3);
    }
    h ^= (slot as u64).wrapping_mul(0x517C_C1B7_2722_0A95);
    h ^= (level as u64).wrapping_mul(0x94D0_49BB_1331_11EB);
    h ^= h >> 33;
    (h % 100) as i32
}

fn matches_item_selector(selector: &str, item_id: &str, item_tags: &[&str]) -> bool {
    let s = selector.trim().to_ascii_lowercase();
    if s == "all" || s == "all_items" {
        return true;
    }
    if let Some(tag) = s.strip_prefix('#') {
        return item_tags
            .iter()
            .any(|t| t.trim_start_matches('#').eq_ignore_ascii_case(tag));
    }
    if let Some(exact) = s.strip_prefix("item:") {
        return item_id.eq_ignore_ascii_case(exact);
    }
    if s.contains(':') {
        return item_id.eq_ignore_ascii_case(&s);
    }
    let path = item_id.split_once(':').map_or(item_id, |(_, p)| p);
    match s.as_str() {
        "wooden" | "wooded" | "wood" => path.starts_with("wooden_"),
        "stone" => path.starts_with("stone_"),
        "iron" => path.starts_with("iron_"),
        "gold" | "golden" => path.starts_with("golden_"),
        "diamond" => path.starts_with("diamond_"),
        "netherite" => path.starts_with("netherite_"),
        "sword" | "swords" => path.ends_with("_sword"),
        "axe" | "axes" => path.ends_with("_axe"),
        "pickaxe" | "pickaxes" => path.ends_with("_pickaxe"),
        "shovel" | "shovels" => path.ends_with("_shovel"),
        "hoe" | "hoes" => path.ends_with("_hoe"),
        "weapon" | "weapons" => path.ends_with("_sword") || path.ends_with("_axe"),
        "tools" => {
            path.ends_with("_pickaxe")
                || path.ends_with("_axe")
                || path.ends_with("_shovel")
                || path.ends_with("_hoe")
        }
        "bow" | "bows" => path == "bow",
        "crossbow" | "crossbows" => path == "crossbow",
        "trident" | "tridents" => path == "trident",
        "mace" | "maces" => path == "mace",
        "fishing" | "fishing_rod" | "fishing_rods" => path == "fishing_rod",
        "head_armor" => path.ends_with("_helmet") || path == "turtle_helmet",
        "turtle_shell" => path == "turtle_helmet",
        "chest_armor" => path.ends_with("_chestplate"),
        "leg_armor" => path.ends_with("_leggings"),
        "foot_armor" => path.ends_with("_boots"),
        "armor" => {
            path.ends_with("_helmet")
                || path.ends_with("_chestplate")
                || path.ends_with("_leggings")
                || path.ends_with("_boots")
                || path == "turtle_helmet"
        }
        "books" => path == "book" || path == "enchanted_book" || path == "spell_book",
        _ => path == s,
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn strips_bom_and_json5_comments_and_trailing_commas() {
        let raw = "\u{FEFF}// header\n[ /* block */ { \"id\": \"eldrich-surge:katana\", \"enabled\": false, }, ]\n";
        let cleaned = strip_json5(raw);
        let parsed: serde_json::Value = serde_json::from_str(&cleaned).expect("valid json");
        assert_eq!(parsed[0]["id"], "eldrich-surge:katana");
        assert_eq!(parsed[0]["enabled"], false);
    }

    #[test]
    fn rarity_class_weights_and_xp_bases_match_spec() {
        assert_eq!(RarityClass::Common.weight(), 100);
        assert_eq!(RarityClass::Uncommon.weight(), 60);
        assert_eq!(RarityClass::Rare.weight(), 30);
        assert_eq!(RarityClass::VeryRare.weight(), 5);
        assert_eq!(RarityClass::Common.advanced_xp_base(), 30);
        assert_eq!(RarityClass::Uncommon.advanced_xp_base(), 40);
        assert_eq!(RarityClass::Rare.advanced_xp_base(), 50);
        assert_eq!(RarityClass::VeryRare.advanced_xp_base(), 65);
    }

    #[test]
    fn mob_id_normalization_and_all_wildcard() {
        assert_eq!(normalize_mob_id("dog"), "minecraft:wolf");
        assert_eq!(normalize_mob_id("ghastling"), "minecraft:happy_ghast");
        assert_eq!(normalize_mob_id("ravanger"), "minecraft:ravager");
        assert_eq!(normalize_mob_id("wither boss"), "minecraft:wither");
        assert_eq!(
            normalize_mob_id("zombified villager"),
            "minecraft:zombie_villager"
        );

        let bridge = EnchLibBridge::with_embedded_defaults();
        assert!(bridge.has_mob_category(None, "minecraft:zombie", "all"));
        assert!(bridge.has_mob_category(None, "minecraft:zombie", "undead"));
        assert!(bridge.has_mob_category(None, "minecraft:cow", "animals"));
    }

    #[test]
    fn curse_and_symmetric_incompatibilities_work() {
        let bridge = EnchLibBridge::with_embedded_defaults();
        assert!(!bridge.are_compatible(
            "eldrich-surge:curse_of_incompatibility",
            "minecraft:unbreaking"
        ));
        assert!(!bridge.are_compatible("eldrich-surge:curse_of_fatigue", "minecraft:efficiency"));
        assert!(!bridge.are_compatible("minecraft:sharpness", "eldrich-surge:katana"));
        assert!(!bridge.are_compatible("eldrich-surge:katana", "minecraft:sharpness"));
        assert!(!bridge.are_compatible(
            "eldrich-surge:gream_reaper",
            "eldrich-surge:blade_of_apocalypse"
        ));
        assert!(bridge.are_compatible("eldrich-surge:katana", "minecraft:unbreaking"));
    }

    #[test]
    fn mob_category_damage_bonus_and_blade_exclusivity() {
        let bridge = EnchLibBridge::with_embedded_defaults();
        let bonus = bridge.mob_category_damage_bonus(
            None,
            "minecraft:creeper",
            true,
            false,
            &[("eldrich-surge:creeping_threat".to_owned(), 2)],
        );
        assert!((bonus - 5.0).abs() < f32::EPSILON);

        let blade_bonus = bridge.mob_category_damage_bonus(
            None,
            "minecraft:zombie",
            false,
            true,
            &[
                ("eldrich-surge:blade_of_apocalypse".to_owned(), 2),
                ("eldrich-surge:undead_slayer".to_owned(), 5),
            ],
        );
        assert!((blade_bonus - 5.0).abs() < f32::EPSILON);
    }

    #[test]
    fn item_selectors_handle_negation_and_groups() {
        let mut bridge = EnchLibBridge::with_embedded_defaults();
        bridge.rules.insert(
            "eldrich-surge:test_ench".to_owned(),
            EnchantmentRule {
                supported_items: vec!["swords".to_owned(), "!wooden".to_owned()],
                ..Default::default()
            },
        );
        assert!(bridge.is_allowed_for_item(
            None,
            "eldrich-surge:test_ench",
            "minecraft:diamond_sword",
            &[]
        ));
        assert!(!bridge.is_allowed_for_item(
            None,
            "eldrich-surge:test_ench",
            "minecraft:wooden_sword",
            &[]
        ));
    }

    #[test]
    fn configured_cap_one_guard_preserves_multilevel_cap() {
        let mut bridge = EnchLibBridge::with_embedded_defaults();
        bridge.overlay_details_json5(
            r#"{ "enchantments": [ { "id": "eldrich-surge:katana", "max_level": 1 } ] }"#,
        );
        assert_eq!(bridge.resolve_anvil_cap("eldrich-surge:katana", 5), 10);
    }
}
