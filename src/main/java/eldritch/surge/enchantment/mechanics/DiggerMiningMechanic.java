package eldritch.surge.enchantment.mechanics;

import eldritch.surge.enchantment.EldritchEnchantments;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public final class DiggerMiningMechanic {
    private static boolean breakingExtraBlocks;

    private DiggerMiningMechanic() {
    }

    public static void initialize() {
        PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
            if (breakingExtraBlocks || level.isClientSide() || !(level instanceof ServerLevel serverLevel) || !(player instanceof ServerPlayer serverPlayer)) {
                return;
            }

            ItemStack tool = serverPlayer.getMainHandItem();
            if (getDiggerLevel(serverLevel, tool) <= 0) {
                return;
            }

            breakArea(serverPlayer, pos, miningPlane(serverPlayer));
        });
    }

    private static int getDiggerLevel(ServerLevel level, ItemStack stack) {
        Optional<Holder.Reference<Enchantment>> enchantment = level.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .get(EldritchEnchantments.DIGGER);

        return enchantment.map(entry -> EnchantmentHelper.getItemEnchantmentLevel(entry, stack)).orElse(0);
    }

    private static Direction.Axis miningPlane(ServerPlayer player) {
        if (Math.abs(player.getXRot()) > 60.0F) {
            return Direction.Axis.Y;
        }

        return player.getDirection().getAxis();
    }

    private static void breakArea(ServerPlayer player, BlockPos center, Direction.Axis normalAxis) {
        breakingExtraBlocks = true;
        try {
            for (int first = -1; first <= 1; first++) {
                for (int second = -1; second <= 1; second++) {
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
}
