use mod_api::{Compound, EquipmentSlot, Host, ItemStack, LivingRef, Tag};

pub fn level_on_item(stack: &ItemStack, id: &str) -> i32 {
    let Some(components) = stack.components() else {
        return 0;
    };
    let patch = Compound::from(components);
    let Some(Tag::Compound(ench_map)) = patch.get(&"minecraft:enchantments".into()) else {
        return 0;
    };
    let full = if id.contains(':') {
        id.to_owned()
    } else {
        format!("eldrich-surge:{id}")
    };
    ench_map
        .get(&full.as_str().into())
        .map(tag_to_i32)
        .unwrap_or(0)
}

pub fn max_on_armor(host: &dyn Host, living: LivingRef, id: &str) -> i32 {
    let mut best = 0;
    for slot in [
        EquipmentSlot::Head,
        EquipmentSlot::Chest,
        EquipmentSlot::Legs,
        EquipmentSlot::Feet,
    ] {
        if let Some(stack) = living.equipment(host, slot) {
            best = best.max(level_on_item(&stack, id));
        }
    }
    best
}

pub fn sum_on_armor(host: &dyn Host, living: LivingRef, id: &str) -> i32 {
    let mut total = 0;
    for slot in [
        EquipmentSlot::Head,
        EquipmentSlot::Chest,
        EquipmentSlot::Legs,
        EquipmentSlot::Feet,
    ] {
        if let Some(stack) = living.equipment(host, slot) {
            total += level_on_item(&stack, id);
        }
    }
    total
}

pub fn max_on_equipment(host: &dyn Host, living: LivingRef, id: &str) -> i32 {
    let mut best = max_on_armor(host, living, id);
    for slot in [EquipmentSlot::MainHand, EquipmentSlot::OffHand] {
        if let Some(stack) = living.equipment(host, slot) {
            best = best.max(level_on_item(&stack, id));
        }
    }
    best
}

pub fn stored_enchantments(stack: &ItemStack) -> Vec<(String, i32)> {
    let Some(components) = stack.components() else {
        return Vec::new();
    };
    let patch = Compound::from(components);
    let Some(Tag::Compound(ench_map)) = patch.get(&"minecraft:stored_enchantments".into()) else {
        return Vec::new();
    };
    ench_map
        .iter()
        .map(|(k, v)| (k.to_string_lossy(), tag_to_i32(v)))
        .collect()
}

pub fn make_enchanted_book(host: &dyn Host, id: &str, level: i32) -> Option<ItemStack> {
    let key = host.item_key(&mod_api::Identifier::parse("minecraft:enchanted_book").ok()?)?;
    let mut stack = ItemStack::new(key, 1)?;
    let full = if id.contains(':') {
        id.to_owned()
    } else {
        format!("eldrich-surge:{id}")
    };
    let mut stored = Compound::new();
    stored.insert(full.as_str().into(), Tag::Int(level));
    let mut patch = Compound::new();
    patch.insert("minecraft:stored_enchantments".into(), Tag::Compound(stored));
    stack.set_components(patch).ok()?;
    Some(stack)
}

fn tag_to_i32(tag: &Tag) -> i32 {
    match tag {
        Tag::Byte(n) => i32::from(*n),
        Tag::Short(n) => i32::from(*n),
        Tag::Int(n) => *n,
        Tag::Long(n) => *n as i32,
        _ => 0,
    }
}
