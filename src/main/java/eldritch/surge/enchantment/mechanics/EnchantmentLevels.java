package eldritch.surge.enchantment.mechanics;

import eldritch.surge.EldritchSurge;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Arrays;

public final class EnchantmentLevels {
    /* Registry lookups are repeated heavily by passive effects. Keep only the
       current thread's registry so integrated client/server worlds do not
       evict each other's entries or retain unloaded worlds. */
    private static final ThreadLocal<LookupCache> LOOKUP_CACHE = new ThreadLocal<>();
    private static final EquipmentSlot[] EQUIPMENT_SLOTS = EquipmentSlot.values();
    private static final EquipmentSlot[] ARMOR_SLOTS = Arrays.stream(EQUIPMENT_SLOTS)
            .filter(EquipmentSlot::isArmor)
            .toArray(EquipmentSlot[]::new);

    private EnchantmentLevels() {
    }

    public static int onMainHand(ServerLevel level, LivingEntity entity, String id) {
        return onItem(level, entity.getMainHandItem(), id);
    }

    public static int onEquipment(ServerLevel level, LivingEntity entity, String id) {
        int total = 0;
        for (EquipmentSlot slot : EQUIPMENT_SLOTS) {
            total += onItem(level, entity.getItemBySlot(slot), id);
        }
        return total;
    }

    public static int onArmor(ServerLevel level, LivingEntity entity, String id) {
        int total = 0;
        for (EquipmentSlot slot : ARMOR_SLOTS) {
            total += onItem(level, entity.getItemBySlot(slot), id);
        }
        return total;
    }

    public static int onItem(ServerLevel level, ItemStack stack, String id) {
        return onItem((Level) level, stack, id);
    }

    public static int onItem(Level level, ItemStack stack, String id) {
        if (stack.isEmpty()) {
            return 0;
        }
        return getLevel(level, stack, EldritchSurge.id(id));
    }

    public static int onMinecraftItem(Level level, ItemStack stack, String id) {
        if (stack.isEmpty()) {
            return 0;
        }

        return getLevel(level, stack, Identifier.withDefaultNamespace(id));
    }

    private static int getLevel(Level level, ItemStack stack, Identifier id) {
        RegistryAccess registryAccess = level.registryAccess();
        LookupCache cache = LOOKUP_CACHE.get();
        if (cache == null || cache.registryAccess != registryAccess) {
            cache = new LookupCache(registryAccess);
            LOOKUP_CACHE.set(cache);
        }

        Optional<Holder.Reference<Enchantment>> enchantment = cache.enchantments.computeIfAbsent(id, key ->
                registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                        .get(ResourceKey.create(Registries.ENCHANTMENT, key)));
        return enchantment.map(entry -> EnchantmentHelper.getItemEnchantmentLevel(entry, stack)).orElse(0);
    }

    private static final class LookupCache {
        private final RegistryAccess registryAccess;
        private final Map<Identifier, Optional<Holder.Reference<Enchantment>>> enchantments = new HashMap<>();

        private LookupCache(RegistryAccess registryAccess) {
            this.registryAccess = registryAccess;
        }
    }
}
