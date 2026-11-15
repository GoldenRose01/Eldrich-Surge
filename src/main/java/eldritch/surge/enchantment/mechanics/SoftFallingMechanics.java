package eldritch.surge.enchantment.mechanics;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageTypes;
import eldritch.surge.network.SoftFallingJumpPayload;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SoftFallingMechanics {
    private static final Map<UUID, Integer> LAST_TRIGGER_TICK = new HashMap<>();

    private SoftFallingMechanics() {}

    public static void initialize() {
        ServerPlayNetworking.registerGlobalReceiver(SoftFallingJumpPayload.TYPE, (payload, context) ->
                context.server().execute(() -> tryActivate(context.player(), payload.pressed())));
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (!(entity instanceof Player player) || !source.is(DamageTypes.FALL)) return true;
            return EnchantmentLevels.onItem(player.level(), player.getItemBySlot(EquipmentSlot.FEET), "soft_falling") <= 0;
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
                LAST_TRIGGER_TICK.remove(handler.player.getUUID()));
    }

    private static void tryActivate(Player player, boolean pressed) {
        // Soft Falling grants fall-damage immunity without applying Slow Falling.
    }
}
