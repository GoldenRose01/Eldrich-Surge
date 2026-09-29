package eldritch.surge.enchantment.mechanics;

import eldritch.surge.EldritchSurge;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/** Book-only anvil transformations. Kept separate from combat and repair behavior. */
public final class SpecialAnvilRecipes {
    private static final Set<String> BLADE_COMPONENTS = Set.of(
            "butcher", "undead_slayer", "bane_of_end", "exorcist", "wrath_of_the_abyss");
    private static final Set<String> STAR_FATE_COMPONENTS = Set.of(
            "witch_hunter", "creeping_threat", "flogging", "herbicide", "smoother");
    private static final int RITUAL_XP_COST = 30;

    private SpecialAnvilRecipes() {
    }

    public static boolean createOutput(ContainerLevelAccess access, Container inputs, ResultContainer result, DataSlot cost) {
        ItemStack left = inputs.getItem(0);
        ItemStack right = inputs.getItem(1);
        return access.evaluate((level, pos) -> {
            if (!(level instanceof ServerLevel serverLevel)) return false;
            ItemStack output = fusionOutput(serverLevel, left, right);
            if (output.isEmpty()) return false;
            result.setItem(0, output);
            cost.set(RITUAL_XP_COST);
            return true;
        }, false);
    }

    private static ItemStack fusionOutput(ServerLevel level, ItemStack left, ItemStack right) {
        if (isExactBook(left, Set.of("blade_of_apocalypse"))
                && isExactBook(right, Set.of("star_fate"))) {
            return createBook(level, "gream_reaper", Math.min(10,
                    enchantmentLevel(left, "blade_of_apocalypse") + enchantmentLevel(right, "star_fate")));
        }
        if (isExactBook(left, Set.of("star_fate"))
                && isExactBook(right, Set.of("blade_of_apocalypse"))) {
            return createBook(level, "gream_reaper", Math.min(10,
                    enchantmentLevel(left, "star_fate") + enchantmentLevel(right, "blade_of_apocalypse")));
        }

        if (!right.is(Items.NETHER_STAR) || !left.is(Items.ENCHANTED_BOOK)) return ItemStack.EMPTY;
        if (isFusionBook(left, BLADE_COMPONENTS)) return createBook(level, "blade_of_apocalypse", 1);
        if (isFusionBook(left, STAR_FATE_COMPONENTS)) return createBook(level, "star_fate", 1);
        int katanaLevel = exactSingleLevel(left, "katana");
        if (katanaLevel > 0) return createBook(level, "gream_reaper", Math.min(10, katanaLevel));
        return ItemStack.EMPTY;
    }

    private static boolean isFusionBook(ItemStack stack, Set<String> required) {
        return isExactBook(stack, required);
    }

    private static boolean isExactBook(ItemStack stack, Set<String> expected) {
        if (!stack.is(Items.ENCHANTED_BOOK)) return false;
        return enchantmentIds(stack).equals(expected.stream()
                .map(id -> "eldritch-surge:" + id).collect(Collectors.toSet()));
    }

    private static Set<String> enchantmentIds(ItemStack stack) {
        return EnchantmentHelper.getEnchantmentsForCrafting(stack).keySet().stream()
                .map(holder -> holder.unwrapKey().map(key -> key.identifier().toString()).orElse(""))
                .collect(Collectors.toSet());
    }

    private static int exactSingleLevel(ItemStack stack, String id) {
        if (!isExactBook(stack, Set.of(id))) return 0;
        return enchantmentLevel(stack, id);
    }

    private static int enchantmentLevel(ItemStack stack, String id) {
        return EnchantmentHelper.getEnchantmentsForCrafting(stack).entrySet().stream()
                .filter(entry -> entry.getKey().unwrapKey()
                        .map(key -> key.identifier().equals(EldritchSurge.id(id))).orElse(false))
                .mapToInt(Map.Entry::getValue)
                .findFirst().orElse(0);
    }

    private static ItemStack createBook(ServerLevel level, String enchantmentId, int enchantmentLevel) {
        var enchantment = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
                .get(ResourceKey.create(Registries.ENCHANTMENT, EldritchSurge.id(enchantmentId)));
        return enchantment.<ItemStack>map(holder -> EnchantmentHelper.createBook(
                new EnchantmentInstance(holder, enchantmentLevel))).orElse(ItemStack.EMPTY);
    }
}
