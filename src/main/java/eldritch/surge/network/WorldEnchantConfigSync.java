package eldritch.surge.network;

import eldritch.surge.EldritchSurge;
import eldritch.surge.config.EnchantmentCapsConfig;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;

public final class WorldEnchantConfigSync {
    private WorldEnchantConfigSync() {}

    public static void initialize() {
        PayloadTypeRegistry.clientboundPlay().register(WorldEnchantConfigPayload.TYPE, WorldEnchantConfigPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(WorldEnchantConfigPayload.TYPE, WorldEnchantConfigPayload.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(SoftFallingJumpPayload.TYPE, SoftFallingJumpPayload.CODEC);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> send(handler.player));
        ServerPlayNetworking.registerGlobalReceiver(WorldEnchantConfigPayload.TYPE, (payload, context) ->
                context.server().execute(() -> {
                    if (!context.player().permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)) {
                        EldritchSurge.LOGGER.warn("Rejected world enchantment config update from non-operator {}.", context.player().getGameProfile().name());
                        send(context.player());
                        return;
                    }
                    EnchantmentCapsConfig.applySynchronizedData(payload.json());
                    EnchantmentCapsConfig.save();
                }));
    }

    public static void send(ServerPlayer player) {
        if (ServerPlayNetworking.canSend(player, WorldEnchantConfigPayload.TYPE)) {
            ServerPlayNetworking.send(player, new WorldEnchantConfigPayload(EnchantmentCapsConfig.serializeForSync(),
                    player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)));
        }
    }

    public static void broadcast(MinecraftServer server) {
        if (server == null) return;
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (ServerPlayNetworking.canSend(player, WorldEnchantConfigPayload.TYPE)) {
                send(player);
            }
        }
    }

    public static void submit(String json) {
        if (net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.canSend(WorldEnchantConfigPayload.TYPE)) {
            net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(new WorldEnchantConfigPayload(json, false));
        }
    }
}
