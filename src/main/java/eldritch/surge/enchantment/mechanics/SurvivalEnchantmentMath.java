package eldritch.surge.enchantment.mechanics;

/** Shared, bounded probabilities and multiplicative penalties. */
public final class SurvivalEnchantmentMath {
    private SurvivalEnchantmentMath() {}

    public static double dodgeChance(double level) {
        if (level > 3) return Math.min(0.95D, 0.33D + 0.08D * (level - 3));
        return switch ((int) Math.clamp(level, 0, 3)) {
            case 1 -> 0.125D;
            case 2 -> 0.25D;
            case 3 -> 0.33D;
            default -> 0.0D;
        };
    }

    public static float actionSpeed(double level) {
        return (float) Math.max(0.01D, Math.pow(0.8D, Math.max(0, level)));
    }

    public static double armorSpeed(double totalLevel) {
        return Math.max(0.01D, Math.pow(0.95D, Math.max(0, totalLevel)));
    }

    public static int nextFishingScore(int score) {
        return Math.clamp(score, 0, 99) + 1;
    }

    public static double flightSpeed(double level) {
        double rank = Math.max(0, level);
        return rank == 0 ? 1.0D : 1.5D + 0.3D * (rank - 1);
    }

    public static double stepHeightBonus(double level) {
        return level <= 0 ? 0.0D : 0.4D + Math.max(0, level - 3) * 0.1D;
    }

    public static double stepJumpBonus(double level) {
        // Vanilla jumps already clear 1.25 blocks; III clears a block plus a slab.
        return level >= 3 ? 0.05D + Math.max(0, level - 3) * 0.01D : 0.0D;
    }

    public static float kineticImpactMultiplier(double level) {
        if (level > 5) return (float) Math.max(0.05D, 0.2D / (1.0D + 0.24D * (level - 5)));
        return 1.0F - Math.min(0.8F, 0.24F * (float) Math.max(0, level));
    }

    /** Convert a bonus in percentage points to a reroll chance after vanilla selection. */
    public static double treasureRerollChance(int score, double baseChance) {
        if (baseChance >= 1.0D) return 1.0D;
        return Math.clamp(Math.clamp(score, 0, 100) / 100.0D / (1.0D - baseChance), 0.0D, 1.0D);
    }
}
