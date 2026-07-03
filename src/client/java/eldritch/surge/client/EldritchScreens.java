package eldritch.surge.client;

import eldritch.surge.EldritchSurge;
import eldritch.surge.menu.EldritchMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;

import java.lang.reflect.Field;
import java.util.Map;

public final class EldritchScreens {
    private EldritchScreens() {
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void initialize() {
        try {
            Field screensField = MenuScreens.class.getDeclaredField("SCREENS");
            screensField.setAccessible(true);
            Map screens = (Map) screensField.get(null);
            screens.put(EldritchMenus.ADVANCED_ENCHANTING_MENU, screens.get(MenuType.ENCHANTMENT));
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Unable to register advanced enchanting screen.", exception);
        }
        EldritchSurge.LOGGER.debug("Registered Eldritch Surge client screens.");
    }
}
