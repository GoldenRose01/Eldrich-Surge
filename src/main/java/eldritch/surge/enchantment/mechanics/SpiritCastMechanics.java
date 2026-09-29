package eldritch.surge.enchantment.mechanics;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/** Kill counter and randomized temporary reward for Spirit Cast. */
public final class SpiritCastMechanics {
    private static final Map<UUID, Integer> KILL_COUNTS = new HashMap<>();

    private SpiritCastMechanics() {}

    public static void recordKill(ServerLevel level, ServerPlayer player, int enchantmentLevel) {
        int threshold = Math.max(1, 15 - enchantmentLevel);
        int kills = KILL_COUNTS.merge(player.getUUID(), 1, Integer::sum);
        if (kills < threshold) return;
        KILL_COUNTS.put(player.getUUID(), 0);
        var effect = switch (level.getRandom().nextInt(3)) {
            case 0 -> MobEffects.STRENGTH;
            case 1 -> MobEffects.SPEED;
            default -> MobEffects.RESISTANCE;
        };
        player.addEffect(new MobEffectInstance(effect, 200, 0));
    }
}
