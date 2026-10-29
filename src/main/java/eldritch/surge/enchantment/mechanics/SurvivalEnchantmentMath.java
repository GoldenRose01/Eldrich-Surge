package eldritch.surge.enchantment.mechanics;

/** Shared, bounded probabilities and multiplicative penalties. */
public final class SurvivalEnchantmentMath {
    private SurvivalEnchantmentMath() {}

    public static double dodgeChance(int level) {
        return switch (Math.clamp(level, 0, 3)) {
            case 1 -> 0.125D;
            case 2 -> 0.25D;
            case 3 -> 0.33D;
            default -> 0.0D;
        };
    }

    public static float actionSpeed(int level) {
        return (float) Math.pow(0.8D, Math.clamp(level, 0, 3));
    }

    public static double armorSpeed(int totalLevel) {
        return Math.pow(0.95D, Math.clamp(totalLevel, 0, 12));
    }

    public static int nextFishingScore(int score) {
        return Math.clamp(score, 0, 99) + 1;
    }

    public static double flightSpeed(int level) {
        int rank = Math.clamp(level, 0, 3);
        return rank == 0 ? 1.0D : 1.5D + 0.3D * (rank - 1);
    }

    public static double stepHeightBonus(int level) {
        return level <= 0 ? 0.0D : 0.4D;
    }

    public static double stepJumpBonus(int level) {
        // Vanilla jumps already clear 1.25 blocks; III clears a block plus a slab.
        return level >= 3 ? 0.05D : 0.0D;
    }

    public static float kineticImpactMultiplier(int level) {
        return 1.0F - Math.min(0.8F, 0.24F * Math.clamp(level, 0, 5));
    }

    /** Convert a bonus in percentage points to a reroll chance after vanilla selection. */
    public static double treasureRerollChance(int score, double baseChance) {
        if (baseChance >= 1.0D) return 1.0D;
        return Math.clamp(Math.clamp(score, 0, 100) / 100.0D / (1.0D - baseChance), 0.0D, 1.0D);
    }
}
