package eldritch.surge.creative;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;

import java.util.Locale;

/** Pure metadata classifier, kept separate from tab registration for reliable validation. */
final class CreativeEnchantmentClassifier {
    private CreativeEnchantmentClassifier() {}

    static CreativeCategory categoryOf(ModEnchantmentDefinition definition) {
        String itemCategory = normalized(definition.itemCategory());
        String supportedItems = normalized(String.join(" ", definition.supportedItems()));

        if (containsAny(itemCategory, "helmet", "chestplate", "leggings", "boots", "armor", "elytra", "shield")
                || containsAny(supportedItems, "armor", "head_armor", "chest_armor", "leg_armor", "foot_armor", "elytra", "shield")) {
            return CreativeCategory.ARMOR;
        }
        if (containsAny(itemCategory, "bows", "crossbows") || containsAny(supportedItems, "bow", "crossbow")) {
            return CreativeCategory.BOWS;
        }
        if (containsAny(itemCategory, "mace", "spear")
                || containsAny(supportedItems, "mace", "spear", "trident")
                || containsAny(definition.id(), "launch", "inking", "neptunes_will", "sea_breeze")) {
            return CreativeCategory.RANGED_WEAPONS;
        }
        if (containsAny(itemCategory, "weapons")
                || containsAny(supportedItems, "sword", "swords", "weapon", "melee")) {
            return CreativeCategory.MELEE_WEAPONS;
        }
        if (containsAny(itemCategory, "tools")
                || containsAny(supportedItems, "pickaxe", "pickaxes", "shovel", "shovels", "hoe", "hoes", "mining", "shears")) {
            return CreativeCategory.TOOLS;
        }
        return CreativeCategory.EXTRA;
    }

    private static boolean containsAny(String value, String... tokens) {
        for (String token : tokens) if (value.contains(token)) return true;
        return false;
    }

    private static String normalized(String value) {
        return value.toLowerCase(Locale.ROOT);
    }
}
