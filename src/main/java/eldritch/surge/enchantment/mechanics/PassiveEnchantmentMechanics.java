package eldritch.surge.enchantment.mechanics;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShulkerBoxBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class PassiveEnchantmentMechanics {
    private static final Identifier ARMORED_ARMOR = Identifier.fromNamespaceAndPath("eldritch-surge", "armored_armor");
    private static final Identifier EXCAVATOR_BLOCK_RANGE = Identifier.fromNamespaceAndPath("eldritch-surge", "excavator_block_range");
    private static final Identifier EXCAVATOR_ENTITY_RANGE = Identifier.fromNamespaceAndPath("eldritch-surge", "excavator_entity_range");
    private static final Identifier SMITHCRAFTS_ARMOR = Identifier.fromNamespaceAndPath("eldritch-surge", "smithcrafts_armor");
    private static final Identifier RABBIT_FOOT_LUCK = Identifier.fromNamespaceAndPath("eldritch-surge", "rabbit_foot_luck");
    private static final Map<UUID, Set<UUID>> SEEKER_TARGETS = new HashMap<>();
    private static final Map<UUID, ItemEntity> SEEKER_ITEMS = new HashMap<>();

    private PassiveEnchantmentMechanics() {
    }

    public static void initialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                applyPlayerPassives((ServerLevel) player.level(), player);
                applyArmored(player);
                applyExcavator(player);
                applyOffhandAndLuckModifiers(player);
                updateSeekerGlow((ServerLevel) player.level(), player);
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

        int health = EnchantmentLevels.onArmor(level, player, "health_upgrade");
        if (health > 0) {
            player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 80, Math.min(4, health - 1), true, false, true));
        }

        int sea = EnchantmentLevels.onArmor(level, player, "heart_of_the_sea");
        if (sea > 0) {
            player.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 80, Math.min(1, sea - 1), true, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 80, Math.min(1, sea - 1), true, false, true));
        }

        int softFalling = EnchantmentLevels.onArmor(level, player, "soft_falling");
        if (softFalling > 0) {
            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 40, 0, true, false, true));
        }

        int night = EnchantmentLevels.onArmor(level, player, "blessing_of_the_night");
        long dayTime = level.getOverworldClockTime() % 24000L;
        if (night > 0 && dayTime >= 13000L && dayTime <= 23000L && player.tickCount % 1200 == 0) {
            ExperienceOrb.award(level, player.position(), night * 5);
        }

        int sun = EnchantmentLevels.onArmor(level, player, "sun_blessing");
        if (sun > 0 && level.dimension() == Level.OVERWORLD && dayTime < 12000L
                && level.canSeeSkyFromBelowWater(player.blockPosition()) && player.tickCount % 1200 == 0) {
            ExperienceOrb.award(level, player.position(), sun * 10);
        }

        int hell = EnchantmentLevels.onArmor(level, player, "hell_blessing");
        if (hell > 0 && (player.isInLava() || player.isOnFire()) && player.tickCount % 20 == 0) {
            ExperienceOrb.award(level, player.position(), hell * 10);
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

        int swiftness = EnchantmentLevels.onEquipment(level, player, "swiftness");
        if (swiftness > 0 && player.onGround()) {
            var look = player.getLookAngle();
            var direction = new net.minecraft.world.phys.Vec3(look.x, 0.0D, look.z);
            if (direction.lengthSqr() > 1.0E-6D) {
                player.setDeltaMovement(player.getDeltaMovement().add(direction.normalize().scale(0.03D * swiftness)));
            }
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

    private static void applyArmored(ServerPlayer player) {
        int level = player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)
                ? EnchantmentLevels.onArmor(player.level(), player, "armored")
                : 0;
        AttributeInstance armor = player.getAttribute(Attributes.ARMOR);
        if (armor == null) return;
        AttributeModifier current = armor.getModifier(ARMORED_ARMOR);
        if (level <= 0) {
            if (current != null) armor.removeModifier(ARMORED_ARMOR);
        } else {
            double points = level * 4.0D;
            if (current == null || current.amount() != points) {
                armor.addOrUpdateTransientModifier(new AttributeModifier(
                        ARMORED_ARMOR, points, AttributeModifier.Operation.ADD_VALUE));
            }
        }
    }

    private static void applyExcavator(ServerPlayer player) {
        int level = EnchantmentLevels.onItem(player.level(), player.getMainHandItem(), "excavator")
                + EnchantmentLevels.onItem(player.level(), player.getOffhandItem(), "excavator");
        updateModifier(player, Attributes.BLOCK_INTERACTION_RANGE, EXCAVATOR_BLOCK_RANGE, level);
        updateModifier(player, Attributes.ENTITY_INTERACTION_RANGE, EXCAVATOR_ENTITY_RANGE, level);
    }

    private static void applyOffhandAndLuckModifiers(ServerPlayer player) {
        int smithcrafts = EnchantmentLevels.onItem(player.level(), player.getOffhandItem(), "smithcrafts");
        updateModifier(player, Attributes.ARMOR, SMITHCRAFTS_ARMOR, smithcrafts * 2);
        int rabbitFoot = EnchantmentLevels.onArmor(player.level(), player, "rabbit_foot");
        updateModifier(player, Attributes.LUCK, RABBIT_FOOT_LUCK, rabbitFoot);
    }

    private static void updateSeekerGlow(ServerLevel level, ServerPlayer player) {
        int seeker = EnchantmentLevels.onArmor(level, player, "seeker_blessing");
        Set<UUID> current = new HashSet<>();
        if (seeker > 0) {
            for (ItemEntity item : level.getEntitiesOfClass(ItemEntity.class,
                    player.getBoundingBox().inflate(2.0D * seeker))) {
                item.setGlowingTag(true);
                current.add(item.getUUID());
                SEEKER_ITEMS.put(item.getUUID(), item);
            }
        }
        Set<UUID> previous = SEEKER_TARGETS.put(player.getUUID(), current);
        if (previous != null) {
            for (UUID id : previous) {
                if (!current.contains(id) && SEEKER_TARGETS.values().stream().noneMatch(items -> items.contains(id))) {
                    ItemEntity item = SEEKER_ITEMS.remove(id);
                    if (item != null && item.isAlive()) item.setGlowingTag(false);
                }
            }
        }
    }

    private static void updateModifier(ServerPlayer player, net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> attributeKey, Identifier id, int level) {
        var attribute = player.getAttribute(attributeKey);
        if (attribute == null) return;
        AttributeModifier current = attribute.getModifier(id);
        if (level <= 0) {
            if (current != null) attribute.removeModifier(id);
            return;
        }
        double amount = level;
        if (current == null || current.amount() != amount) {
            attribute.addOrUpdateTransientModifier(new AttributeModifier(id, amount, AttributeModifier.Operation.ADD_VALUE));
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
