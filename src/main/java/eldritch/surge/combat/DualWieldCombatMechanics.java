package eldritch.surge.combat;

import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import eldritch.surge.mixin.LivingEntityAttackAccessor;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** Handles dual-wielding two swords or two axes with independent cooldowns and simultaneous Dash strikes. */
public final class DualWieldCombatMechanics {
    private static final long SIMULTANEOUS_WINDOW_TICKS = 4L;
    private static final Map<UUID, DualWieldState> STATES = new ConcurrentHashMap<>();

    private DualWieldCombatMechanics() {
    }

    public static void initialize() {
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> STATES.clear());
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> STATES.remove(handler.player.getUUID()));

        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!world.isClientSide() && hand == InteractionHand.MAIN_HAND && isDualWieldingSameWeaponType(player)) {
                DualWieldState state = state(player);
                if (!state.offhandAttackInProgress) {
                    state.lastMainAttackTick = world.getGameTime();
                }
            }
            return InteractionResult.PASS;
        });

        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!isDualWieldingSameWeaponType(player)) {
                return InteractionResult.PASS;
            }
            if (hand == InteractionHand.MAIN_HAND) {
                return InteractionResult.PASS;
            }
            if (world.isClientSide()) {
                return InteractionResult.SUCCESS;
            }
            if (player instanceof ServerPlayer serverPlayer) {
                performOffhandAttack(serverPlayer, entity);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });

        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (!isDualWieldingSameWeaponType(player) || hand != InteractionHand.OFF_HAND) {
                return InteractionResult.PASS;
            }
            if (!world.isClientSide()) {
                DualWieldState state = state(player);
                state.offhandCooldownStartTick = world.getGameTime();
                player.swing(InteractionHand.OFF_HAND, net.minecraft.world.item.component.SwingAnimation.DEFAULT, true);
            }
            return InteractionResult.SUCCESS;
        });
    }

    public static boolean isDualWieldingSameWeaponType(Player player) {
        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();
        return (main.is(ItemTags.SWORDS) && off.is(ItemTags.SWORDS))
                || (main.is(ItemTags.AXES) && off.is(ItemTags.AXES));
    }

    public static boolean isSimultaneousDualDash(ServerLevel world, Player player) {
        if (!isDualWieldingSameWeaponType(player)) {
            return false;
        }
        if (EnchantmentLevels.onItem(world, player.getMainHandItem(), "dash") <= 0
                || EnchantmentLevels.onItem(world, player.getOffhandItem(), "dash") <= 0) {
            return false;
        }
        DualWieldState state = STATES.get(player.getUUID());
        if (state == null || state.lastMainAttackTick < 0L || state.lastOffAttackTick < 0L) {
            return false;
        }
        return Math.abs(state.lastMainAttackTick - state.lastOffAttackTick) <= SIMULTANEOUS_WINDOW_TICKS;
    }

    private static void performOffhandAttack(ServerPlayer player, Entity target) {
        DualWieldState state = state(player);
        long now = player.level().getGameTime();
        ItemStack originalMain = player.getMainHandItem();
        ItemStack originalOff = player.getOffhandItem();

        LivingEntityAttackAccessor accessor = (LivingEntityAttackAccessor) player;
        int savedMainTicker = accessor.eldritchSurge$getAttackStrengthTicker();

        state.offhandAttackInProgress = true;
        state.lastOffAttackTick = now;

        swapMainHandModifiers(player, originalMain, originalOff);
        player.setItemInHand(InteractionHand.MAIN_HAND, originalOff);
        player.setItemInHand(InteractionHand.OFF_HAND, originalMain);
        try {
            int delay = Math.max(1, (int) Math.ceil(player.getCurrentItemAttackStrengthDelay()));
            int elapsed = state.offhandCooldownStartTick < 0L
                    ? delay
                    : (int) Math.min(delay, Math.max(0L, now - state.offhandCooldownStartTick));
            accessor.eldritchSurge$setAttackStrengthTicker(elapsed);
            state.offhandCooldownStartTick = now;

            player.attack(target);
            player.swing(InteractionHand.OFF_HAND, net.minecraft.world.item.component.SwingAnimation.DEFAULT, true);
        } finally {
            ItemStack updatedOff = player.getMainHandItem();
            ItemStack updatedMain = player.getOffhandItem();
            swapMainHandModifiers(player, updatedOff, updatedMain);
            player.setItemInHand(InteractionHand.MAIN_HAND, updatedMain);
            player.setItemInHand(InteractionHand.OFF_HAND, updatedOff);
            accessor.eldritchSurge$setAttackStrengthTicker(savedMainTicker);
            state.offhandAttackInProgress = false;
        }
    }

    private static void swapMainHandModifiers(ServerPlayer player, ItemStack removing, ItemStack adding) {
        removing.forEachModifier(EquipmentSlot.MAINHAND, (attribute, modifier) -> {
            AttributeInstance instance = player.getAttribute(attribute);
            if (instance != null) {
                instance.removeModifier(modifier.id());
            }
        });
        adding.forEachModifier(EquipmentSlot.MAINHAND, (attribute, modifier) -> {
            AttributeInstance instance = player.getAttribute(attribute);
            if (instance != null && !instance.hasModifier(modifier.id())) {
                instance.addTransientModifier(modifier);
            }
        });
    }

    private static DualWieldState state(Player player) {
        return STATES.computeIfAbsent(player.getUUID(), ignored -> new DualWieldState());
    }

    private static final class DualWieldState {
        long lastMainAttackTick = -100L;
        long lastOffAttackTick = -100L;
        long offhandCooldownStartTick = -100L;
        boolean offhandAttackInProgress;
    }
}
