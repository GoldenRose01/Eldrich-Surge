package eldritch.surge.enchantment.mechanics;

import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** Missing items must return zero before accessing the world or its registries. */
public final class MissingEnchantmentItemsTest {
    public static void main(String[] args) {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        for (ItemStack stack : new ItemStack[]{null, ItemStack.EMPTY}) {
            assertZero(EnchantmentLevels.onItem((ServerLevel) null, stack, "elasticity"));
            assertZero(EnchantmentLevels.onItem((Level) null, stack, "pop"));
            assertZero(EnchantmentLevels.onMinecraftItem(null, stack, "power"));
        }
        System.out.println("Validated null and empty items across all enchantment lookup entry points.");
    }

    private static void assertZero(int level) {
        if (level != 0) throw new AssertionError("Missing item enchantment level: " + level);
    }
}
