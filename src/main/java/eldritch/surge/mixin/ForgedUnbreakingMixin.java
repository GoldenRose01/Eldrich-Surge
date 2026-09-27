package eldritch.surge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ForgedUnbreakingMixin {
    @WrapOperation(
            method = "hurtAndBreak(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;Ljava/util/function/Consumer;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;processDurabilityChange(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/server/level/ServerPlayer;)I")
    )
    private int eldritchSurge$adjustDurability(ItemStack stack, int damage, ServerLevel level, ServerPlayer player,
                                                Operation<Integer> original) {
        if (EnchantmentLevels.onItem(level, stack, "deathbreak") > 0) return 0;

        int fragility = EnchantmentLevels.onItem(level, stack, "curse_of_fragility");
        damage += fragility;

        if (player != null) {
            boolean conditionalForged = level.dimension() == Level.END
                    && EnchantmentLevels.onItem(level, stack, "end_forged") > 0
                    || level.dimension() == Level.NETHER
                    && EnchantmentLevels.onItem(level, stack, "nether_forged") > 0
                    || level.dimension() == Level.OVERWORLD
                    && EnchantmentLevels.onItem(level, stack, "villager_forged") > 0;

            ResourceKey<Biome> biomeKey = level.getBiome(player.blockPosition()).unwrapKey().orElse(null);
            boolean dwarfForged = biomeKey != null && isUndergroundVanillaBiome(biomeKey)
                    && EnchantmentLevels.onItem(level, stack, "dwarf_forged") > 0;
            if ((conditionalForged || dwarfForged) && level.getRandom().nextInt(3) == 0) return 0;
        }

        return original.call(stack, damage, level, player);
    }

    private static boolean isUndergroundVanillaBiome(ResourceKey<Biome> biome) {
        String path = biome.identifier().getPath();
        return path.equals("lush_caves") || path.equals("dripstone_caves") || path.equals("deep_dark");
    }
}
