package eldritch.surge.enchantment.mechanics;

import java.util.List;

public record ModEnchantmentDefinition(
        String id,
        String displayName,
        String itemCategory,
        int maxLevel,
        String classification,
        String acquisition,
        List<String> supportedItems,
        List<String> slots,
        String exclusiveSet,
        String effectDescription
) {
    public boolean normalTableDefault() {
        if (id.equals("gream_reaper") || id.equals("deathbreak") || id.equals("blade_of_apocalypse") || id.equals("star_fate")) {
            return false;
        }
        return acquisition.toLowerCase().contains("normal");
    }

    public boolean advancedTableDefault() {
        if (id.equals("gream_reaper") || id.equals("deathbreak") || id.equals("blade_of_apocalypse") || id.equals("star_fate")) {
            return false;
        }
        return acquisition.toLowerCase().contains("advanced");
    }

    public boolean lootDefault() {
        return (classification.equalsIgnoreCase("Ritual") && !isCastSpell())
                || classification.equalsIgnoreCase("Loot")
                || acquisition.equalsIgnoreCase("Loot")
                || id.equals("gream_reaper")
                || id.equals("soft_falling");
    }

    public boolean isCastSpell() {
        return itemCategory.toLowerCase().contains("cast spells");
    }
}

