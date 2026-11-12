package eldritch.surge.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class EnchantingReagentStorage {
    private static final Map<Key, ItemStack> FALLBACK_REAGENTS = new ConcurrentHashMap<>();

    private EnchantingReagentStorage() {
    }

    public static ItemStack take(Level level, BlockPos pos) {
        if (level != null) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ReagentHolder holder) {
                ItemStack stack = holder.eldritchSurge$getReagent();
                if (!stack.isEmpty()) {
                    holder.eldritchSurge$setReagent(ItemStack.EMPTY);
                    return stack;
                }
            }
        }
        ItemStack stack = FALLBACK_REAGENTS.remove(Key.of(level, pos));
        return stack == null ? ItemStack.EMPTY : stack;
    }

    public static void store(Level level, BlockPos pos, ItemStack stack) {
        if (level != null) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ReagentHolder holder) {
                holder.eldritchSurge$setReagent(stack);
                return;
            }
        }
        Key key = Key.of(level, pos);
        if (stack == null || stack.isEmpty()) {
            FALLBACK_REAGENTS.remove(key);
        } else {
            FALLBACK_REAGENTS.put(key, stack.copy());
        }
    }

    public static void clear(Level level, BlockPos pos) {
        if (level != null) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ReagentHolder holder) {
                holder.eldritchSurge$setReagent(ItemStack.EMPTY);
            }
        }
        FALLBACK_REAGENTS.remove(Key.of(level, pos));
    }

    public static void dropStored(Level level, BlockPos pos) {
        ItemStack stored = take(level, pos);
        if (!stored.isEmpty() && level != null && !level.isClientSide()) {
            Containers.dropItemStack(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stored);
        }
    }

    private record Key(Identifier dimension, BlockPos pos) {
        private static Key of(Level level, BlockPos pos) {
            Identifier dim = level == null ? Identifier.withDefaultNamespace("overworld") : level.dimension().identifier();
            return new Key(dim, pos.immutable());
        }
    }
}
