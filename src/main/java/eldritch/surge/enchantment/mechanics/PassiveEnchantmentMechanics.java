package eldritch.surge.enchantment.mechanics;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShulkerBoxBlock;

import java.util.ArrayList;
import java.util.List;

public final class PassiveEnchantmentMechanics {
    private PassiveEnchantmentMechanics() {
    }

    public static void initialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                applyPlayerPassives((ServerLevel) player.level(), player);
            }
        });
    }

    private static void applyPlayerPassives(ServerLevel level, ServerPlayer player) {
        int combustion = EnchantmentLevels.onArmor(level, player, "combustion_protection");
        if (combustion > 0) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 40, Math.min(1, combustion - 1), true, false, true));
            player.clearFire();
        }

        int vision = EnchantmentLevels.onArmor(level, player, "vision_blessing");
        if (vision > 0) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 260, 0, true, false, true));
        }

        int armored = EnchantmentLevels.onArmor(level, player, "armored");
        if (armored > 0) {
            player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 40, Math.min(2, armored - 1), true, false, true));
        }

        int health = EnchantmentLevels.onArmor(level, player, "health_upgrade");
        if (health > 0) {
            player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 80, Math.min(4, health - 1), true, false, true));
        }

        int sea = Math.max(
                EnchantmentLevels.onArmor(level, player, "heart_of_the_sea"),
                EnchantmentLevels.onArmor(level, player, "heart_of_depth")
        );
        if (sea > 0) {
            player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 80, 0, true, false, true));
            if (player.isInWater()) {
                player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 60, Math.min(1, sea - 1), true, false, true));
            }
        }

        int sky = EnchantmentLevels.onArmor(level, player, "heart_of_the_sky");
        if (sky > 0 || EnchantmentLevels.onArmor(level, player, "soft_falling") > 0) {
            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 40, 0, true, false, true));
        }

        int night = EnchantmentLevels.onArmor(level, player, "blessing_of_the_night");
        long dayTime = level.getOverworldClockTime() % 24000L;
        if (night > 0 && (dayTime >= 13000L && dayTime <= 23000L)) {
            player.addEffect(new MobEffectInstance(MobEffects.SPEED, 60, Math.min(1, night - 1), true, false, true));
        }

        int sun = EnchantmentLevels.onArmor(level, player, "sun_blessing");
        if (sun > 0 && dayTime < 12000L && level.canSeeSkyFromBelowWater(player.blockPosition())) {
            player.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 60, Math.min(1, sun - 1), true, false, true));
        }

        int hell = Math.max(
                EnchantmentLevels.onArmor(level, player, "hell_blessing"),
                EnchantmentLevels.onArmor(level, player, "heart_of_nether")
        );
        if (hell > 0 && level.dimension() == Level.NETHER) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 80, 0, true, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 80, Math.min(1, hell - 1), true, false, true));
        }

        int end = Math.max(
                EnchantmentLevels.onArmor(level, player, "end_blessing"),
                EnchantmentLevels.onArmor(level, player, "end_adaptability")
        );
        if (end > 0 && level.dimension() == Level.END) {
            player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 80, Math.min(1, end - 1), true, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 80, 0, true, false, true));
        }

        int hicker = EnchantmentLevels.onArmor(level, player, "hicker");
        if (hicker > 0) {
            player.addEffect(new MobEffectInstance(MobEffects.JUMP_BOOST, 40, Math.min(2, hicker - 1), true, false, true));
        }

        int dash = EnchantmentLevels.onArmor(level, player, "dash");
        if (dash > 0 && player.isSprinting()) {
            player.addEffect(new MobEffectInstance(MobEffects.SPEED, 40, Math.min(2, dash - 1), true, false, true));
        }

        int iceSpeed = EnchantmentLevels.onArmor(level, player, "ice_speed");
        if (iceSpeed > 0 && player.onGround()) {
            player.addEffect(new MobEffectInstance(MobEffects.SPEED, 40, Math.min(1, iceSpeed - 1), true, false, true));
        }

        int clearmind = EnchantmentLevels.onArmor(level, player, "clearmind");
        if (clearmind > 0) {
            player.removeEffect(MobEffects.BLINDNESS);
            player.removeEffect(MobEffects.NAUSEA);
            player.removeEffect(MobEffects.DARKNESS);
        }

        int healingAura = EnchantmentLevels.onArmor(level, player, "healing_aura");
        if (healingAura > 0 && player.isShiftKeyDown() && player.tickCount % 40 == 0) {
            for (ServerPlayer nearby : level.players()) {
                if (nearby.distanceToSqr(player) <= 36.0D && nearby.getHealth() < nearby.getMaxHealth()) {
                    nearby.heal(1.0F + healingAura);
                }
            }
        }

        if (player.tickCount % 10 == 0) {
            applyInventoryAutomation(level, player);
        }
    }

    private static void applyInventoryAutomation(ServerLevel level, ServerPlayer player) {
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            ItemStack shulker = player.getInventory().getItem(slot);
            if (!isShulker(shulker)) {
                continue;
            }

            if (EnchantmentLevels.onItem(level, shulker, "refill") > 0) {
                refillMainHand(player, shulker);
            }

            if (EnchantmentLevels.onItem(level, shulker, "vacuum") > 0) {
                vacuumNearbyItems(level, player, shulker);
            }
        }
    }

    public static boolean trySiphonPickup(ServerLevel level, ServerPlayer player, ItemEntity itemEntity) {
        ItemStack remaining = itemEntity.getItem().copy();
        boolean changed = false;

        for (int slot = 0; slot < player.getInventory().getContainerSize() && !remaining.isEmpty(); slot++) {
            ItemStack shulker = player.getInventory().getItem(slot);
            if (shulker != player.getOffhandItem()
                    && isShulker(shulker)
                    && EnchantmentLevels.onItem(level, shulker, "siphon") > 0) {
                ItemStack next = siphonIntoPartialStacks(shulker, remaining);
                if (next.getCount() != remaining.getCount()) {
                    remaining = next;
                    changed = true;
                }
            }
        }

        ItemStack offhand = player.getOffhandItem();
        if (!remaining.isEmpty() && isShulker(offhand) && EnchantmentLevels.onItem(level, offhand, "siphon") > 0) {
            ItemStack next = siphonIntoPartialStacks(offhand, remaining);
            if (next.getCount() != remaining.getCount()) {
                remaining = next;
                changed = true;
            }
        }

        if (!changed) {
            return false;
        }

        if (remaining.isEmpty()) {
            itemEntity.discard();
        } else {
            itemEntity.setItem(remaining);
        }
        return true;
    }

    private static ItemStack siphonIntoPartialStacks(ItemStack shulker, ItemStack incoming) {
        NonNullList<ItemStack> contents = shulkerContents(shulker);
        ItemStack remaining = incoming.copy();
        boolean changed = false;

        for (ItemStack stored : contents) {
            if (remaining.isEmpty()) {
                break;
            }

            if (!stored.isEmpty()
                    && ItemStack.isSameItemSameComponents(stored, remaining)
                    && stored.getCount() < stored.getMaxStackSize()) {
                int moved = Math.min(remaining.getCount(), stored.getMaxStackSize() - stored.getCount());
                stored.grow(moved);
                remaining.shrink(moved);
                changed = true;
            }
        }

        if (changed) {
            saveShulkerContents(shulker, contents);
        }
        return remaining;
    }

    private static void refillMainHand(ServerPlayer player, ItemStack shulker) {
        ItemStack held = player.getMainHandItem();
        if (held.isEmpty() || held.getCount() >= held.getMaxStackSize()) {
            return;
        }

        NonNullList<ItemStack> contents = shulkerContents(shulker);
        for (int index = 0; index < contents.size(); index++) {
            ItemStack stored = contents.get(index);
            if (!stored.isEmpty() && ItemStack.isSameItemSameComponents(held, stored)) {
                stored.shrink(1);
                held.grow(1);
                saveShulkerContents(shulker, contents);
                return;
            }
        }
    }

    private static void vacuumNearbyItems(ServerLevel level, ServerPlayer player, ItemStack shulker) {
        List<ItemEntity> nearby = level.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(4.0D));
        if (nearby.isEmpty()) {
            return;
        }

        NonNullList<ItemStack> contents = shulkerContents(shulker);
        boolean changed = false;
        for (ItemEntity entity : nearby) {
            if (!entity.isAlive()) {
                continue;
            }

            ItemStack remaining = insert(contents, entity.getItem());
            if (remaining.isEmpty()) {
                entity.discard();
                changed = true;
            } else if (remaining.getCount() != entity.getItem().getCount()) {
                entity.setItem(remaining);
                changed = true;
            }
        }

        if (changed) {
            saveShulkerContents(shulker, contents);
        }
    }

    private static ItemStack insert(NonNullList<ItemStack> contents, ItemStack stack) {
        ItemStack remaining = stack.copy();
        for (ItemStack stored : contents) {
            if (remaining.isEmpty()) {
                return ItemStack.EMPTY;
            }

            if (!stored.isEmpty() && ItemStack.isSameItemSameComponents(stored, remaining) && stored.getCount() < stored.getMaxStackSize()) {
                int moved = Math.min(remaining.getCount(), stored.getMaxStackSize() - stored.getCount());
                stored.grow(moved);
                remaining.shrink(moved);
            }
        }

        for (int index = 0; index < contents.size(); index++) {
            if (remaining.isEmpty()) {
                return ItemStack.EMPTY;
            }

            if (contents.get(index).isEmpty()) {
                int moved = Math.min(remaining.getCount(), remaining.getMaxStackSize());
                contents.set(index, remaining.copyWithCount(moved));
                remaining.shrink(moved);
            }
        }

        return remaining;
    }

    private static NonNullList<ItemStack> shulkerContents(ItemStack shulker) {
        NonNullList<ItemStack> contents = NonNullList.withSize(27, ItemStack.EMPTY);
        ItemContainerContents component = shulker.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        component.copyInto(contents);
        return contents;
    }

    private static void saveShulkerContents(ItemStack shulker, NonNullList<ItemStack> contents) {
        shulker.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(new ArrayList<>(contents)));
    }

    private static boolean isShulker(ItemStack stack) {
        return stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock;
    }
}
