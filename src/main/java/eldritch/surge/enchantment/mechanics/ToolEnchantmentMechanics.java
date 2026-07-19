package eldritch.surge.enchantment.mechanics;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.ItemLike;

import java.util.Map;

public final class ToolEnchantmentMechanics {
    private static final Map<Block, ItemLike> SMELTED_DROPS = Map.ofEntries(
            Map.entry(Blocks.IRON_ORE, Items.IRON_INGOT),
            Map.entry(Blocks.DEEPSLATE_IRON_ORE, Items.IRON_INGOT),
            Map.entry(Blocks.GOLD_ORE, Items.GOLD_INGOT),
            Map.entry(Blocks.DEEPSLATE_GOLD_ORE, Items.GOLD_INGOT),
            Map.entry(Blocks.COPPER_ORE, Items.COPPER_INGOT),
            Map.entry(Blocks.DEEPSLATE_COPPER_ORE, Items.COPPER_INGOT),
            Map.entry(Blocks.ANCIENT_DEBRIS, Items.NETHERITE_SCRAP),
            Map.entry(Blocks.SAND, Items.GLASS),
            Map.entry(Blocks.RED_SAND, Items.GLASS),
            Map.entry(Blocks.COBBLESTONE, Items.STONE),
            Map.entry(Blocks.COBBLED_DEEPSLATE, Items.DEEPSLATE)
    );

    private static boolean breakingExtraBlocks;

    private ToolEnchantmentMechanics() {
    }

    public static void initialize() {
        PlayerBlockBreakEvents.BEFORE.register((level, player, pos, state, blockEntity) -> {
            if (breakingExtraBlocks || level.isClientSide() || !(level instanceof ServerLevel serverLevel) || !(player instanceof ServerPlayer serverPlayer)) {
                return true;
            }

            if (EnchantmentLevels.onItem(serverLevel, serverPlayer.getMainHandItem(), "smelting") > 0 && hasSmeltedDrop(state)) {
                serverLevel.destroyBlock(pos, false, serverPlayer);
                dropSmelted(serverLevel, pos, state);
                return false;
            }

            int excavator = EnchantmentLevels.onItem(serverLevel, serverPlayer.getMainHandItem(), "excavator");
            if (excavator > 0) {
                breakArea(serverPlayer, pos, miningPlane(serverPlayer), excavator >= 3 ? 2 : 1);
            }

            return true;
        });
    }

    private static Direction.Axis miningPlane(ServerPlayer player) {
        if (Math.abs(player.getXRot()) > 60.0F) {
            return Direction.Axis.Y;
        }

        return player.getDirection().getAxis();
    }

    private static void breakArea(ServerPlayer player, BlockPos center, Direction.Axis normalAxis, int radius) {
        breakingExtraBlocks = true;
        try {
            for (int first = -radius; first <= radius; first++) {
                for (int second = -radius; second <= radius; second++) {
                    if (first == 0 && second == 0) {
                        continue;
                    }

                    BlockPos target = offset(center, normalAxis, first, second);
                    if (canBreak((ServerLevel) player.level(), target)) {
                        player.gameMode.destroyBlock(target);
                    }
                }
            }
        } finally {
            breakingExtraBlocks = false;
        }
    }

    private static BlockPos offset(BlockPos center, Direction.Axis normalAxis, int first, int second) {
        return switch (normalAxis) {
            case X -> center.offset(0, first, second);
            case Y -> center.offset(first, 0, second);
            case Z -> center.offset(first, second, 0);
        };
    }

    private static boolean canBreak(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return !state.isAir() && state.getDestroySpeed(level, pos) >= 0.0F;
    }

    private static boolean hasSmeltedDrop(BlockState state) {
        return SMELTED_DROPS.containsKey(state.getBlock());
    }

    private static void dropSmelted(ServerLevel level, BlockPos pos, BlockState state) {
        ItemLike smelted = SMELTED_DROPS.get(state.getBlock());
        if (smelted != null) {
            level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, new ItemStack(smelted)));
        }
    }
}
