package eldritch.surge.enchantment.mechanics;

/** Converts stored ranks to effect strength; never changes the item or acquisition caps. */
public final class EnchantmentScaling {
    private EnchantmentScaling() {}

    public static double effective(int level, int normalMax) {
        if (level <= 0) return 0.0D;
        if (normalMax <= 1) return 1.0D;
        if (level <= normalMax) return level;
        return normalMax + Math.log1p((double) level - normalMax);
    }

    public static double effective(String id, int level) {
        var definition = ModEnchantmentDefinitions.BY_ID.get(id);
        return effective(level, definition == null ? 1 : definition.maxLevel());
    }

    /** Counts, durations and potion tiers require whole numbers. */
    public static int discrete(String id, int level) {
        return (int) Math.round(effective(id, level));
    }

    public static int repairInterval(int effectiveLevel) {
        return Math.max(1, 4800 / Math.max(1, effectiveLevel));
    }
}
