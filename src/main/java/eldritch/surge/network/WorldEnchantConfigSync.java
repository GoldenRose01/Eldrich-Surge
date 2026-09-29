package eldritch.surge.network;

import eldritch.surge.EldritchSurge;
import eldritch.surge.config.EnchantmentCapsConfig;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public final class WorldEnchantConfigSync {
    private WorldEnchantConfigSync() {}

    public static void initialize() {
        PayloadTypeRegistry.clientboundPlay().register(WorldEnchantConfigPayload.TYPE, WorldEnchantConfigPayload.CODEC);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> send(handler.player));
    }

    public static void send(ServerPlayer player) {
        ServerPlayNetworking.send(player, new WorldEnchantConfigPayload(EnchantmentCapsConfig.serializeForSync()));
    }

    public static void broadcast(MinecraftServer server) {
        if (server == null) return;
        WorldEnchantConfigPayload payload = new WorldEnchantConfigPayload(EnchantmentCapsConfig.serializeForSync());
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (ServerPlayNetworking.canSend(player, WorldEnchantConfigPayload.TYPE)) {
                ServerPlayNetworking.send(player, payload);
            }
        }
    }
}
