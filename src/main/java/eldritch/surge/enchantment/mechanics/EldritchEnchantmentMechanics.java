package eldritch.surge.enchantment.mechanics;

public final class EldritchEnchantmentMechanics {
    private EldritchEnchantmentMechanics() {
    }

    public static void initialize() {
        BloodyMaryMechanics.initialize();
        MidasTouchMechanics.initialize();
        SoftFallingMechanics.initialize();
        DiggerMiningMechanic.initialize();
        ToolEnchantmentMechanics.initialize();
        PassiveEnchantmentMechanics.initialize();
        ProjectileEnchantmentMechanics.initialize();
        ArmorRepairMechanics.initialize();
        VanguardChargeMechanics.initialize();
    }
}
