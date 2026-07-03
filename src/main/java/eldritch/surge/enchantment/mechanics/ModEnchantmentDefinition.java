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
        return acquisition.equalsIgnoreCase("Normal");
    }

    public boolean advancedTableDefault() {
        return acquisition.equalsIgnoreCase("Advanced");
    }

    public boolean lootDefault() {
        return classification.equalsIgnoreCase("Ritual") || id.equals("gream_reaper") || id.equals("soft_falling");
    }
}

