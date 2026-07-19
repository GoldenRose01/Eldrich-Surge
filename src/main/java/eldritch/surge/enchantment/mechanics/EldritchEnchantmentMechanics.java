package eldritch.surge.enchantment.mechanics;

public final class EldritchEnchantmentMechanics {
    private EldritchEnchantmentMechanics() {
    }

    public static void initialize() {
        DiggerMiningMechanic.initialize();
        ToolEnchantmentMechanics.initialize();
        PassiveEnchantmentMechanics.initialize();
    }
}
