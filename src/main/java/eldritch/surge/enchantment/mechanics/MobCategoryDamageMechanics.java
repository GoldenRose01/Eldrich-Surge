package eldritch.surge.enchantment.mechanics;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Damage rules driven by EnchLib's editable world mob categories. */
public final class MobCategoryDamageMechanics {
    private static final float BLADE_DAMAGE_PER_LEVEL_PER_CATEGORY = 2.0F;
    private static final List<CategoryBonus> INDIVIDUAL_ENCHANTMENTS = List.of(
            new CategoryBonus("bane_of_end", "void", 2.5F),
            new CategoryBonus("undead_slayer", "undead", 2.5F),
            new CategoryBonus("exorcist", "hell", 2.5F),
            new CategoryBonus("butcher", "animals", 2.5F),
            new CategoryBonus("herbicide", "fungi", 3.0F),
            new CategoryBonus("witch_hunter", "magik", 2.5F),
            new CategoryBonus("wrath_of_the_abyss", "water", 3.0F),
            new CategoryBonus("creeping_threat", "arthropods", 2.5F),
            new CategoryBonus("smoother", "cubic", 3.0F),
            new CategoryBonus("flogging", "rebel", 2.5F)
    );
    private static final List<CategoryBonus> STAR_FATE_CATEGORIES = List.of(
            new CategoryBonus("star_fate", "magik", 2.5F),
            new CategoryBonus("star_fate", "arthropods", 2.75F),
            new CategoryBonus("star_fate", "rebel", 2.5F),
            new CategoryBonus("star_fate", "fungi", 2.5F),
            new CategoryBonus("star_fate", "cubic", 2.5F)
    );
    private static final List<String> BLADE_CATEGORIES = List.of("animals", "undead", "void", "hell", "water");
    private static final java.util.Set<String> CATEGORY_COMPONENTS = java.util.Set.of(
            "eldritch-surge:butcher", "eldritch-surge:undead_slayer", "eldritch-surge:bane_of_end",
            "eldritch-surge:exorcist", "eldritch-surge:wrath_of_the_abyss", "eldritch-surge:witch_hunter",
            "eldritch-surge:creeping_threat", "eldritch-surge:flogging", "eldritch-surge:herbicide",
            "eldritch-surge:smoother");

    private MobCategoryDamageMechanics() {
    }

    public static float bonusForHit(ServerLevel world, ItemStack weapon, LivingEntity victim) {
        float bonus = sumBonuses(world, weapon, victim, INDIVIDUAL_ENCHANTMENTS);
        return bonus + sumBonuses(world, weapon, victim, STAR_FATE_CATEGORIES);
    }

    public static float bladeOfApocalypseBonus(ServerLevel world, LivingEntity victim, int level) {
        return EnchLibMobCategories.count(world, victim, BLADE_CATEGORIES)
                * level * BLADE_DAMAGE_PER_LEVEL_PER_CATEGORY;
    }

    public static boolean isCategoryComponent(String enchantmentId) {
        return CATEGORY_COMPONENTS.contains(enchantmentId);
    }

    public static int categoryComponentCount(ItemStack stack) {
        return (int) EnchantmentHelper.getEnchantmentsForCrafting(stack).entrySet().stream()
                .filter(entry -> entry.getIntValue() > 0)
                .filter(entry -> entry.getKey().unwrapKey()
                        .map(key -> isCategoryComponent(key.identifier().toString())).orElse(false))
                .count();
    }

    /** Defaults shared by damage evaluation and EnchLib's enchantment editor. */
    public static Map<String, List<String>> enchantmentCategoryDefaults() {
        Map<String, List<String>> categories = new LinkedHashMap<>();
        for (CategoryBonus bonus : INDIVIDUAL_ENCHANTMENTS) {
            categories.put("eldritch-surge:" + bonus.enchantmentId(), List.of(bonus.category()));
        }
        categories.put("eldritch-surge:star_fate", STAR_FATE_CATEGORIES.stream()
                .map(CategoryBonus::category).distinct().toList());
        categories.put("eldritch-surge:blade_of_apocalypse", BLADE_CATEGORIES);
        return Map.copyOf(categories);
    }

    private static float sumBonuses(ServerLevel world, ItemStack weapon, LivingEntity victim, List<CategoryBonus> rules) {
        float bonus = 0.0F;
        for (CategoryBonus rule : rules) {
            int level = EnchantmentLevels.onItem(world, weapon, rule.enchantmentId());
            if (level > 0 && EnchLibMobCategories.has(world, victim, rule.category())) {
                bonus += level * rule.damagePerLevel();
            }
        }
        return bonus;
    }

    private record CategoryBonus(String enchantmentId, String category, float damagePerLevel) {
    }
}
