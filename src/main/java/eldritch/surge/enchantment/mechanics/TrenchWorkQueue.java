package eldritch.surge.enchantment.mechanics;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayDeque;

/** Bounded main-thread digging, without loading/generating chunks for a spell. */
final class TrenchWorkQueue {
    static final int POSITIONS_PER_TICK = 2048;
    static final int MAX_PENDING = 16;
    private static final ArrayDeque<Job> JOBS = new ArrayDeque<>();

    private TrenchWorkQueue() {}

    static void initialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (int remaining = POSITIONS_PER_TICK; remaining > 0 && !JOBS.isEmpty(); remaining--) {
                Job job = JOBS.removeFirst();
                if (job.step()) JOBS.addLast(job);
            }
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> JOBS.clear());
    }

    static boolean enqueue(ServerLevel level, BlockPos origin, int effectiveLevel) {
        if (JOBS.size() >= MAX_PENDING) return false;
        JOBS.addLast(new Job(level, origin, Math.clamp(2 + effectiveLevel, 2, 32)));
        return true;
    }

    private static final class Job {
        private final ServerLevel level;
        private final BlockPos origin;
        private final int radius, minY, topY;
        private int dx, dz, y;

        Job(ServerLevel level, BlockPos origin, int radius) {
            this.level = level;
            this.origin = origin.immutable();
            this.radius = radius;
            this.minY = Math.max(level.getMinY(), -55);
            this.topY = Math.min(origin.getY(), level.getMaxY() - 1);
            dx = dz = -radius;
            y = topY;
        }

        boolean step() {
            if (topY < minY) return false;
            BlockPos pos = new BlockPos(origin.getX() + dx, y, origin.getZ() + dz);
            if (level.hasChunkAt(pos) && level.getWorldBorder().isWithinBounds(pos)) {
                var state = level.getBlockState(pos);
                if (!state.isAir() && !state.is(Blocks.BEDROCK)) {
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                }
            }
            if (--y < minY) {
                y = topY;
                if (++dz > radius) {
                    dz = -radius;
                    dx++;
                }
            }
            return dx <= radius;
        }
    }
}
