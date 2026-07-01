package eldritch.surge.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resources.Identifier;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class EnchantmentIndex {
    private static final Map<Identifier, RegistryKey<Enchantment>> ENCHANTMENTS = new LinkedHashMap<>();

    private EnchantmentIndex() {
    }

    public static void refreshFromRegistry() {
        ENCHANTMENTS.clear();

        for (RegistryEntry.Reference<Enchantment> entry : Registries.ENCHANTMENT.streamEntries().toList()) {
            Optional<RegistryKey<Enchantment>> key = entry.getKey();
            key.ifPresent(enchantmentKey -> ENCHANTMENTS.put(enchantmentKey.getValue(), enchantmentKey));
        }
    }

    public static Map<Identifier, RegistryKey<Enchantment>> snapshot() {
        return Collections.unmodifiableMap(ENCHANTMENTS);
    }

    public static RegistryKey<Enchantment> keyOf(Identifier id) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, id);
    }
}
