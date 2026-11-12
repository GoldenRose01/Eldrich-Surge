package eldritch.surge.game;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class LoyaltyTridentStorage {
    private static final Map<UUID, List<ItemStack>> PENDING = new ConcurrentHashMap<>();

    private LoyaltyTridentStorage() {
    }

    public static void store(UUID playerUuid, ItemStack stack) {
        if (playerUuid == null || stack == null || stack.isEmpty()) return;
        PENDING.computeIfAbsent(playerUuid, ignored -> new ArrayList<>()).add(stack.copy());
    }

    public static void restoreTridents(ServerPlayer player) {
        if (player == null) return;
        List<ItemStack> tridents = PENDING.remove(player.getUUID());
        if (tridents == null || tridents.isEmpty()) return;
        for (ItemStack trident : tridents) {
            if (!player.getInventory().add(trident)) {
                player.spawnAtLocation((ServerLevel) player.level(), trident, 0.1F);
            }
        }
    }
}
