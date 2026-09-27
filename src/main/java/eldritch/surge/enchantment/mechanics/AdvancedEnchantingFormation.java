package eldritch.surge.enchantment.mechanics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public final class AdvancedEnchantingFormation {
    private AdvancedEnchantingFormation() {
    }

    public static boolean isValid(Level level, BlockPos tablePos) {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (x == 0 && z == 0) {
                    continue;
                }

                var state = level.getBlockState(tablePos.offset(x, 0, z));
                boolean quartzPosition = (Math.abs(x) == 2 && z == 0) || (Math.abs(z) == 2 && x == 0);
                boolean goldPosition = Math.abs(x) == 2 && Math.abs(z) == 2;
                boolean emptyPosition = (Math.abs(x) <= 1 && Math.abs(z) == 1)
                        || (Math.abs(x) == 1 && z == 0);
                if (quartzPosition && !state.is(Blocks.CHISELED_QUARTZ_BLOCK)) {
                    return false;
                }
                if (goldPosition && !state.is(Blocks.GOLD_BLOCK)) {
                    return false;
                }
                if (emptyPosition && !state.is(Blocks.AIR) && !state.is(Blocks.CAVE_AIR)) {
                    return false;
                }
            }
        }

        return true;
    }
}
