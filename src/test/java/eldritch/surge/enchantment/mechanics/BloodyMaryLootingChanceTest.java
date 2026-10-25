package eldritch.surge.enchantment.mechanics;

public final class BloodyMaryLootingChanceTest {
    public static void main(String[] args) {
        expectChance(0, 0.10F);
        expectChance(1, 0.20F);
        expectChance(2, 0.30F);
        expectChance(3, 0.40F);
        expectChance(-1, 0.10F);
        expectChance(10, 1.0F);
        System.out.println("Validated Bloody Mary drop chance scaling with Looting.");
    }

    private static void expectChance(int lootingLevel, float expected) {
        float actual = BloodyMaryMechanics.dropChance(lootingLevel);
        if (Math.abs(actual - expected) > 0.0001F) {
            throw new AssertionError("Looting " + lootingLevel + ": expected " + expected + ", got " + actual);
        }
    }
}
