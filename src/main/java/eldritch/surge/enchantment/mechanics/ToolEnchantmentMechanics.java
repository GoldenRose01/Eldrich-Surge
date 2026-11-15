package eldritch.surge.enchantment.mechanics;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class ToolEnchantmentMechanics {
    private ToolEnchantmentMechanics() {
    }

    public static void initialize() {
        PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, blockEntity) -> {
            if (level.isClientSide() || !(level instanceof ServerLevel serverLevel) || !(player instanceof ServerPlayer serverPlayer)) {
                return true;
            }
            if (serverPlayer.isCreative()) {
                return true;
            }

            ItemStack tool = serverPlayer.getMainHandItem();
            int smeltingLevel = EnchantmentLevels.onItem(serverLevel, tool, "smelting");
            if (smeltingLevel <= 0) {
                return true;
            }
            if (state.requiresCorrectToolForDrops() && !serverPlayer.hasCorrectToolForDrops(state)) {
                return true;
            }

            List<ItemStack> drops = Block.getDrops(state, serverLevel, pos, blockEntity, serverPlayer, tool);
            if (drops.isEmpty()) {
                return true;
            }

            int fortune = serverLevel.registryAccess().lookup(Registries.ENCHANTMENT)
                    .flatMap(reg -> reg.get(Enchantments.FORTUNE))
                    .map(holder -> EnchantmentHelper.getItemEnchantmentLevel(holder, tool))
                    .orElse(0);
            int effectiveFortune = Math.min(5, Math.max(fortune, smeltingLevel > 1 ? smeltingLevel : 0));

            boolean anySmelted = false;
            List<ItemStack> finalDrops = new ArrayList<>(drops.size());
            for (ItemStack drop : drops) {
                if (drop.isEmpty()) {
                    continue;
                }
                SingleRecipeInput input = new SingleRecipeInput(drop);
                Optional<RecipeHolder<SmeltingRecipe>> recipe = serverLevel.recipeAccess().getRecipeFor(RecipeType.SMELTING, input, serverLevel);
                if (recipe.isPresent()) {
                    ItemStack smelted = recipe.get().value().assemble(input);
                    if (!smelted.isEmpty()) {
                        int count = drop.getCount();
                        if (effectiveFortune > 0) {
                            int bonus = serverLevel.getRandom().nextInt(effectiveFortune + 2) - 1;
                            if (bonus > 0) {
                                count *= (bonus + 1);
                            }
                        }
                        smelted.setCount(smelted.getCount() * count);
                        finalDrops.add(smelted);
                        anySmelted = true;
                        continue;
                    }
                }
                finalDrops.add(drop);
            }

            if (!anySmelted) {
                return true;
            }

            serverLevel.destroyBlock(pos, false, serverPlayer);
            tool.hurtAndBreak(1, serverPlayer, EquipmentSlot.MAINHAND);
            for (ItemStack out : finalDrops) {
                spawnSplitDrops(serverLevel, pos, out);
            }
            return false;
        });
    }

    private static void spawnSplitDrops(ServerLevel level, BlockPos pos, ItemStack stack) {
        int remaining = stack.getCount();
        int maxStack = stack.getMaxStackSize();
        while (remaining > 0) {
            int take = Math.min(remaining, maxStack);
            ItemStack part = stack.copyWithCount(take);
            level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, part));
            remaining -= take;
        }
    }
}
