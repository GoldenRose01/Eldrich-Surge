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
        return acquisition.toLowerCase().contains("normal");
    }

    public boolean advancedTableDefault() {
        return acquisition.toLowerCase().contains("advanced");
    }

    public boolean lootDefault() {
        return (classification.equalsIgnoreCase("Ritual") && !isCastSpell()) || id.equals("gream_reaper") || id.equals("soft_falling");
    }

    public boolean isCastSpell() {
        return itemCategory.toLowerCase().contains("cast spells");
    }
}

