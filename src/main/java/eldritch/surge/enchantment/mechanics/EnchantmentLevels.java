package eldritch.surge.enchantment.mechanics;

import eldritch.surge.EldritchSurge;
import net.minecraft.core.Holder;
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

import java.util.Optional;

public final class EnchantmentLevels {
    private EnchantmentLevels() {
    }

    public static int onMainHand(ServerLevel level, LivingEntity entity, String id) {
        return onItem(level, entity.getMainHandItem(), id);
    }

    public static int onEquipment(ServerLevel level, LivingEntity entity, String id) {
        int total = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            total += onItem(level, entity.getItemBySlot(slot), id);
        }
        return total;
    }

    public static int onArmor(ServerLevel level, LivingEntity entity, String id) {
        int total = 0;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.isArmor()) {
                total += onItem(level, entity.getItemBySlot(slot), id);
            }
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

        Optional<Holder.Reference<Enchantment>> enchantment = level.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .get(ResourceKey.create(Registries.ENCHANTMENT, EldritchSurge.id(id)));

        return enchantment.map(entry -> EnchantmentHelper.getItemEnchantmentLevel(entry, stack)).orElse(0);
    }

    public static int onMinecraftItem(Level level, ItemStack stack, String id) {
        if (stack.isEmpty()) {
            return 0;
        }

        Optional<Holder.Reference<Enchantment>> enchantment = level.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .get(ResourceKey.create(Registries.ENCHANTMENT, Identifier.withDefaultNamespace(id)));

        return enchantment.map(entry -> EnchantmentHelper.getItemEnchantmentLevel(entry, stack)).orElse(0);
    }
}
