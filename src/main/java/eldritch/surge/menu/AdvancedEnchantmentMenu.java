package eldritch.surge.menu;

import eldritch.surge.block.EldritchBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class AdvancedEnchantmentMenu extends EnchantmentMenu implements AdvancedEnchantingMenuMarker {
    private static final Identifier EMPTY_SLOT_AMETHYST_SHARD =
            Identifier.withDefaultNamespace("container/slot/amethyst_shard");
    private final ContainerLevelAccess access;

    public AdvancedEnchantmentMenu(int syncId, Inventory inventory, BlockPos ignoredPos) {
        this(syncId, inventory, ContainerLevelAccess.NULL);
    }

    public AdvancedEnchantmentMenu(int syncId, Inventory inventory, ContainerLevelAccess access) {
        super(syncId, inventory, access);
        this.access = access;
        replaceLapisSlot();
    }

    @Override
    public MenuType<?> getType() {
        return EldritchMenus.ADVANCED_ENCHANTING_MENU;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, EldritchBlocks.ADVANCED_ENCHANTING_TABLE);
    }

    private void replaceLapisSlot() {
        Slot vanillaLapisSlot = slots.get(1);
        Slot amethystSlot = new Slot(vanillaLapisSlot.container, vanillaLapisSlot.index, vanillaLapisSlot.x, vanillaLapisSlot.y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(Items.AMETHYST_SHARD);
            }

            @Override
            public Identifier getNoItemIcon() {
                return EMPTY_SLOT_AMETHYST_SHARD;
            }
        };
        slots.set(1, amethystSlot);
    }
}
