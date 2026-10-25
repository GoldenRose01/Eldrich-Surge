package eldritch.surge.client;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/** Offers both installed providers without loading either provider before selection. */
final class EldritchConfigSelectorScreen extends Screen {
    private final Screen parent;

    EldritchConfigSelectorScreen(Screen parent) {
        super(Component.translatable("config.eldrich-surge.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        addRenderableWidget(Button.builder(Component.literal("Cloth Config"),
                        button -> minecraft.setScreenAndShow(EldritchConfigScreen.create(parent)))
                .bounds(width / 2 - 100, height / 2 - 36, 200, 20).build());
        addRenderableWidget(Button.builder(Component.literal("YACL"),
                        button -> minecraft.setScreenAndShow(EldritchYaclConfigScreen.create(parent)))
                .bounds(width / 2 - 100, height / 2 - 10, 200, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.back"), button -> onClose())
                .bounds(width / 2 - 100, height / 2 + 22, 200, 20).build());
    }

    @Override
    public void onClose() {
        minecraft.setScreenAndShow(parent);
    }
}
