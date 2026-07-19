package eldritch.surge.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class EnchantingReagentStorage {
    private static final Map<Key, ItemStack> REAGENTS = new ConcurrentHashMap<>();

    private EnchantingReagentStorage() {
    }

    public static ItemStack take(Level level, BlockPos pos) {
        ItemStack stack = REAGENTS.remove(Key.of(level, pos));
        return stack == null ? ItemStack.EMPTY : stack;
    }

    public static void store(Level level, BlockPos pos, ItemStack stack) {
        Key key = Key.of(level, pos);
        if (stack.isEmpty()) {
            REAGENTS.remove(key);
        } else {
            REAGENTS.put(key, stack.copy());
        }
    }

    public static void clear(Level level, BlockPos pos) {
        REAGENTS.remove(Key.of(level, pos));
    }

    private record Key(Identifier dimension, BlockPos pos) {
        private static Key of(Level level, BlockPos pos) {
            return new Key(level.dimension().identifier(), pos.immutable());
        }
    }
}
