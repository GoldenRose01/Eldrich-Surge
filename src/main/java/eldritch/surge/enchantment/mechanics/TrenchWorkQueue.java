package eldritch.surge.enchantment.mechanics;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayDeque;
import java.util.Iterator;

/** Progressive layer-by-layer digging (1 layer every 5 seconds) with Silk Touch drops. */
final class TrenchWorkQueue {
    static final int TICKS_PER_LAYER = 100;
    static final int MAX_PENDING = 16;
    private static final ArrayDeque<Job> JOBS = new ArrayDeque<>();

    private TrenchWorkQueue() {}

    static void initialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            Iterator<Job> iterator = JOBS.iterator();
            while (iterator.hasNext()) {
                Job job = iterator.next();
                if (!job.tick()) {
                    iterator.remove();
                }
            }
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> JOBS.clear());
    }

    static boolean enqueue(ServerLevel level, BlockPos origin, int effectiveLevel) {
        if (JOBS.size() >= MAX_PENDING) return false;
        JOBS.addLast(new Job(level, origin, Math.clamp(2 + effectiveLevel, 2, 16)));
        return true;
    }

    private static final class Job {
        private final ServerLevel level;
        private final BlockPos origin;
        private final int radius;
        private final int minY;
        private final ItemStack silkTouchTool;
        private int currentY;
        private int cooldownTicks;

        Job(ServerLevel level, BlockPos origin, int radius) {
            this.level = level;
            this.origin = origin.immutable();
            this.radius = radius;
            this.minY = Math.max(level.getMinY(), -55);
            this.currentY = Math.min(origin.getY(), level.getMaxY() - 1);
            this.cooldownTicks = 0;

            ItemStack tool = new ItemStack(Items.NETHERITE_PICKAXE);
            level.registryAccess().lookup(Registries.ENCHANTMENT)
                    .flatMap(reg -> reg.get(Enchantments.SILK_TOUCH))
                    .ifPresent(silk -> tool.enchant(silk, 1));
            this.silkTouchTool = tool;
        }

        boolean tick() {
            if (currentY < minY) {
                return false;
            }
            if (cooldownTicks > 0) {
                cooldownTicks--;
                return true;
            }

            digLayer(currentY);
            currentY--;
            cooldownTicks = TICKS_PER_LAYER - 1;
            return currentY >= minY;
        }

        private void digLayer(int y) {
            boolean brokeAny = false;
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
            for (int dx = -8; dx < 8; dx++) {
                for (int dz = -8; dz < 8; dz++) {
                    pos.set(origin.getX() + dx, y, origin.getZ() + dz);
                    if (!level.hasChunkAt(pos) || !level.getWorldBorder().isWithinBounds(pos)) {
                        continue;
                    }
                    BlockState state = level.getBlockState(pos);
                    if (state.isAir() || state.is(Blocks.BEDROCK) || state.getDestroySpeed(level, pos) < 0.0F) {
                        continue;
                    }
                    if (state.getFluidState().isEmpty()) {
                        for (ItemStack drop : Block.getDrops(state, level, pos, level.getBlockEntity(pos), null, silkTouchTool)) {
                            if (!drop.isEmpty()) {
                                level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, drop));
                            }
                        }
                    }
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                    brokeAny = true;
                }
            }
            if (brokeAny) {
                level.levelEvent(2001, new BlockPos(origin.getX(), y, origin.getZ()), Block.getId(Blocks.STONE.defaultBlockState()));
            }
        }
    }
}
