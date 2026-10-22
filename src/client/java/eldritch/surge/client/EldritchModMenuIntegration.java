package eldritch.surge.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;

public final class EldritchModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        boolean cloth = FabricLoader.getInstance().isModLoaded("cloth-config");
        boolean yacl = FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3");
        if (cloth && yacl) return EldritchConfigSelectorScreen::new;
        if (cloth) return EldritchConfigScreen::create;
        if (yacl) return EldritchYaclConfigScreen::create;
        return parent -> null;
    }
}
