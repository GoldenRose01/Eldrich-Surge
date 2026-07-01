package eldritch.surge.enchantment;

import net.minecraft.core.Registry;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class EnchantmentIndex {
    private static final Map<Identifier, ResourceKey<Enchantment>> ENCHANTMENTS = new LinkedHashMap<>();

    private EnchantmentIndex() {
    }

    public static void refreshFromRegistry() {
        ENCHANTMENTS.clear();
    }

    public static void refreshFromRegistry(Registry<Enchantment> enchantments) {
        ENCHANTMENTS.clear();

        for (ResourceKey<Enchantment> key : enchantments.registryKeySet()) {
            ENCHANTMENTS.put(key.identifier(), key);
        }
    }

    public static Map<Identifier, ResourceKey<Enchantment>> snapshot() {
        return Collections.unmodifiableMap(ENCHANTMENTS);
    }

    public static ResourceKey<Enchantment> keyOf(Identifier id) {
        return ResourceKey.create(Registries.ENCHANTMENT, id);
    }
}
