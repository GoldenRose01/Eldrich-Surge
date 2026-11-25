use crate::{enchants, enchlib_bridge::EnchLibState};
use mod_api::{AnvilUpdateEvent, Flow, Host, ServerHost};
use std::collections::BTreeSet;

const RITUAL_XP_COST: i32 = 30;

const BLADE_COMPONENTS: &[&str] = &[
    "eldrich-surge:bane_of_end",
    "eldrich-surge:butcher",
    "eldrich-surge:exorcist",
    "eldrich-surge:undead_slayer",
    "eldrich-surge:wrath_of_the_abyss",
];

const STAR_FATE_COMPONENTS: &[&str] = &[
    "eldrich-surge:creeping_threat",
    "eldrich-surge:flogging",
    "eldrich-surge:herbicide",
    "eldrich-surge:smoother",
    "eldrich-surge:witch_hunter",
];

pub fn on_anvil_update(host: &dyn ServerHost, event: &mut AnvilUpdateEvent<'_>) -> Flow {
    let left = &event.update.left;
    let Some(right) = event.update.right.as_ref() else {
        return Flow::Continue;
    };

    let left_id = left.item().id(host);
    let right_id = right.item().id(host);

    // Enforce EnchLib CurseCompatibility + symmetric incompatibilities + max-3 category components
    let left_all = if left_id == "minecraft:enchanted_book" {
        enchants::stored_enchantments(left)
    } else {
        enchants::all_on_item(left)
    };
    let right_all = if right_id == "minecraft:enchanted_book" {
        enchants::stored_enchantments(right)
    } else {
        enchants::all_on_item(right)
    };
    let left_id_refs: Vec<&str> = left_all.iter().map(|(id, _)| id.as_str()).collect();
    let right_id_refs: Vec<&str> = right_all.iter().map(|(id, _)| id.as_str()).collect();
    let is_book_output = left_id == "minecraft:enchanted_book";
    if !right_id_refs.is_empty()
        && !check_anvil_combination_allowed(is_book_output, &left_id_refs, &right_id_refs)
    {
        event.update.output = None;
        event.update.xp_cost = 0;
        return Flow::Cancel;
    }

    if left_id != "minecraft:enchanted_book" {
        return Flow::Continue;
    }

    let left_ench = enchants::stored_enchantments(left);
    let left_ids: BTreeSet<&str> = left_ench.iter().map(|(id, _)| id.as_str()).collect();

    if right_id == "minecraft:enchanted_book" {
        let right_ench = enchants::stored_enchantments(right);
        let right_ids: BTreeSet<&str> = right_ench.iter().map(|(id, _)| id.as_str()).collect();

        // Blade of Apocalypse + Star Fate -> Gream Reaper
        if is_single(&left_ids, "eldrich-surge:blade_of_apocalypse")
            && is_single(&right_ids, "eldrich-surge:star_fate")
        {
            let lvl = (left_ench[0].1 + right_ench[0].1).min(10);
            return set_book_output(host, event, "gream_reaper", lvl, RITUAL_XP_COST);
        }
        if is_single(&left_ids, "eldrich-surge:star_fate")
            && is_single(&right_ids, "eldrich-surge:blade_of_apocalypse")
        {
            let lvl = (left_ench[0].1 + right_ench[0].1).min(10);
            return set_book_output(host, event, "gream_reaper", lvl, RITUAL_XP_COST);
        }

        // Unbreaking III + Unbreaking III -> Deathbreak I
        if is_single(&left_ids, "minecraft:unbreaking")
            && is_single(&right_ids, "minecraft:unbreaking")
            && left_ench[0].1 >= 3
            && right_ench[0].1 >= 3
        {
            return set_book_output(host, event, "deathbreak", 1, 10);
        }

        return Flow::Continue;
    }

    if right_id == "minecraft:nether_star" {
        if matches_set(&left_ids, BLADE_COMPONENTS) {
            return set_book_output(host, event, "blade_of_apocalypse", 1, RITUAL_XP_COST);
        }
        if matches_set(&left_ids, STAR_FATE_COMPONENTS) {
            return set_book_output(host, event, "star_fate", 1, RITUAL_XP_COST);
        }
        if is_single(&left_ids, "eldrich-surge:katana") {
            let lvl = left_ench[0].1.min(10).max(1);
            return set_book_output(host, event, "gream_reaper", lvl, RITUAL_XP_COST);
        }
    }

    Flow::Continue
}

fn set_book_output(
    host: &dyn Host,
    event: &mut AnvilUpdateEvent<'_>,
    id: &str,
    level: i32,
    xp_cost: i32,
) -> Flow {
    if let Some(book) = enchants::make_enchanted_book(host, id, level) {
        event.update.output = Some(book);
        event.update.xp_cost = xp_cost;
        event.update.material_cost = 1;
    }
    Flow::Continue
}

fn is_single(set: &BTreeSet<&str>, target: &str) -> bool {
    set.len() == 1 && set.contains(target)
}

fn matches_set(set: &BTreeSet<&str>, expected: &[&str]) -> bool {
    set.len() == expected.len() && expected.iter().all(|id| set.contains(id))
}

pub fn check_anvil_combination_allowed(
    is_book_output: bool,
    left_ids: &[&str],
    right_ids: &[&str],
) -> bool {
    for &r in right_ids {
        if !EnchLibState::is_compatible_with_existing(r, left_ids) {
            return false;
        }
    }
    if !is_book_output {
        let right_cats = EnchLibState::category_component_count(right_ids);
        if right_cats > 0 {
            let left_cats = EnchLibState::category_component_count(left_ids);
            let mut merged: Vec<&str> = left_ids.to_vec();
            merged.extend_from_slice(right_ids);
            let merged_cats = EnchLibState::category_component_count(&merged);
            if merged_cats > 3 && merged_cats > left_cats {
                return false;
            }
        }
    }
    true
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn curse_compatibility_and_max_three_category_components() {
        assert!(!check_anvil_combination_allowed(
            false,
            &["eldrich-surge:curse_of_incompatibility"],
            &["minecraft:unbreaking"]
        ));
        assert!(!check_anvil_combination_allowed(
            false,
            &["minecraft:efficiency"],
            &["eldrich-surge:curse_of_fatigue"]
        ));
        // 3 category components allowed on sword; 4th blocked
        assert!(check_anvil_combination_allowed(
            false,
            &["eldrich-surge:butcher", "eldrich-surge:undead_slayer"],
            &["eldrich-surge:exorcist"]
        ));
        assert!(!check_anvil_combination_allowed(
            false,
            &[
                "eldrich-surge:butcher",
                "eldrich-surge:undead_slayer",
                "eldrich-surge:exorcist"
            ],
            &["eldrich-surge:bane_of_end"]
        ));
        // Books are exempt from the 3-component limit (for 5-component ritual fusion)
        assert!(check_anvil_combination_allowed(
            true,
            &[
                "eldrich-surge:butcher",
                "eldrich-surge:undead_slayer",
                "eldrich-surge:exorcist"
            ],
            &["eldrich-surge:bane_of_end", "eldrich-surge:wrath_of_the_abyss"]
        ));
    }
}
