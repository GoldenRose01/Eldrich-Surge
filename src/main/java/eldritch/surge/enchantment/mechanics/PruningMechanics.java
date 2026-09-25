package eldritch.surge.enchantment.mechanics;

import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class PruningMechanics {
    private static final TagKey<Block> PRUNABLE_BLOCKS = TagKey.create(
            Registries.BLOCK,
            eldritch.surge.EldritchSurge.id("prunable")
    );

    private PruningMechanics() {
    }

    public static boolean isPrunable(BlockState state) {
        return state.is(PRUNABLE_BLOCKS);
    }
}
