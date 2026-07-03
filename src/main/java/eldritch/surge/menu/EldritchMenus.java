package eldritch.surge.menu;

import eldritch.surge.EldritchSurge;
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;

public final class EldritchMenus {
    public static final MenuType<AdvancedEnchantmentMenu> ADVANCED_ENCHANTING_MENU = Registry.register(
            BuiltInRegistries.MENU,
            EldritchSurge.id("advanced_enchanting"),
            new ExtendedMenuType<>(AdvancedEnchantmentMenu::new, BlockPos.STREAM_CODEC.cast())
    );

    private EldritchMenus() {
    }

    public static void initialize() {
        EldritchSurge.LOGGER.debug("Registered Eldritch Surge menus.");
    }
}
