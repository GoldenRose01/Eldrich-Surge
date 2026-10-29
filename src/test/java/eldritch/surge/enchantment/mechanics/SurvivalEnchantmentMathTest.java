package eldritch.surge.enchantment.mechanics;

/** Checks requested gameplay probabilities and limits, including cumulative armor penalties. */
public final class SurvivalEnchantmentMathTest {
    public static void main(String[] args) {
        double[] dodge = {0, 0.125, 0.25, 0.33};
        double[] actions = {1, 0.8, 0.64, 0.512};
        for (int level = 0; level <= 3; level++) {
            close(dodge[level], SurvivalEnchantmentMath.dodgeChance(level));
            close(actions[level], SurvivalEnchantmentMath.actionSpeed(level));
        }
        double[] armorPenalties = {0, 5, 9.75, 14.2625, 18.549375, 22.62190625,
                26.4908109375, 30.166270390625, 33.65795687109375, 36.97505902753906,
                40.12630607616211, 43.119990772354, 45.9639912337363};
        for (int total = 0; total <= 12; total++) {
            close(armorPenalties[total], (1 - SurvivalEnchantmentMath.armorSpeed(total)) * 100);
        }
        for (double base : new double[]{0, 0.05, 0.08, 0.2, 1}) {
            for (int catches = 0; catches <= 100; catches++) {
                double reroll = SurvivalEnchantmentMath.treasureRerollChance(catches, base);
                close(Math.min(1, base + catches / 100.0), reroll + (1 - reroll) * base);
            }
        }
        if (SurvivalEnchantmentMath.nextFishingScore(0) != 1
                || SurvivalEnchantmentMath.nextFishingScore(99) != 100
                || SurvivalEnchantmentMath.nextFishingScore(Integer.MAX_VALUE) != 100
                || SurvivalEnchantmentMath.nextFishingScore(-5) != 1) throw new AssertionError("Fishing counter bounds");
        close(0.33, SurvivalEnchantmentMath.dodgeChance(100));
        close(1, SurvivalEnchantmentMath.actionSpeed(-1));
        close(SurvivalEnchantmentMath.armorSpeed(12), SurvivalEnchantmentMath.armorSpeed(100));
        double[] flight = {1, 1.5, 1.8, 2.1};
        double[] steps = {0.6, 1, 1, 1};
        for (int rank = 0; rank <= 3; rank++) {
            close(flight[rank], SurvivalEnchantmentMath.flightSpeed(rank));
            close(steps[rank], 0.6 + SurvivalEnchantmentMath.stepHeightBonus(rank));
        }
        close(0, SurvivalEnchantmentMath.stepJumpBonus(1));
        close(0, SurvivalEnchantmentMath.stepJumpBonus(2));
        close(0.05, SurvivalEnchantmentMath.stepJumpBonus(3));
        double[] impact = {1, 0.76, 0.52, 0.28, 0.2, 0.2};
        for (int rank = 0; rank <= 5; rank++) close(impact[rank], SurvivalEnchantmentMath.kineticImpactMultiplier(rank));
        close(2.1, SurvivalEnchantmentMath.flightSpeed(Integer.MAX_VALUE));
        close(0, SurvivalEnchantmentMath.stepHeightBonus(-1));
        System.out.println("Validated Dodge, Fatigue, Fanatic, Happy Boost, Step Assist and Kinetic Protection scaling.");
    }

    private static void close(double expected, double actual) {
        if (Math.abs(expected - actual) > 0.000001) throw new AssertionError(expected + " != " + actual);
    }
}
