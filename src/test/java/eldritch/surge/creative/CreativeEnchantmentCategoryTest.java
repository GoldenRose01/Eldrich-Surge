package eldritch.surge.creative;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinitions;

public final class CreativeEnchantmentCategoryTest {
    public static void main(String[] args) {
        expect("bloody_mary", CreativeCategory.MELEE_WEAPONS);
        expect("midas_touch", CreativeCategory.MELEE_WEAPONS);
        expect("void_sweep", CreativeCategory.MELEE_WEAPONS);
        expect("digger", CreativeCategory.TOOLS);
        expect("excavator", CreativeCategory.TOOLS);
        expect("smelting", CreativeCategory.TOOLS);
        expect("neptunes_will", CreativeCategory.RANGED_WEAPONS);
        expect("swiftness", CreativeCategory.BOWS);
        System.out.println("Validated creative tab classification for representative enchantments.");
    }

    private static void expect(String id, CreativeCategory expected) {
        var definition = ModEnchantmentDefinitions.BY_ID.get(id);
        if (definition == null || CreativeEnchantmentClassifier.categoryOf(definition) != expected) {
            throw new AssertionError("Unexpected creative tab for " + id);
        }
    }
}
