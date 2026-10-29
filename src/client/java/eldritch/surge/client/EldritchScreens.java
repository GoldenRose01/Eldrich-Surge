package eldritch.surge.client;

import eldritch.surge.menu.EldritchMenus;
import net.minecraft.client.gui.screens.MenuScreens;

public final class EldritchScreens {
    private EldritchScreens() { }
    public static void initialize() {
        MenuScreens.register(EldritchMenus.ADVANCED_ENCHANTING_MENU, AdvancedEnchantmentScreen::new);
    }
}
