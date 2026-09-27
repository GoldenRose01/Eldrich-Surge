package eldritch.surge.enchantment.mechanics;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class VanguardChargeMechanics {
    private static final Map<UUID, Charge> CHARGES = new HashMap<>();

    private VanguardChargeMechanics() {
    }

    public static void initialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            CHARGES.entrySet().removeIf(entry -> {
                ServerPlayer player = server.getPlayerList().getPlayer(entry.getKey());
                if (player == null || !(player.level() instanceof ServerLevel level)) return true;
                Charge charge = entry.getValue();
                Entity entity = level.getEntity(charge.target);
                if (!(entity instanceof LivingEntity target) || !target.isAlive() || charge.ticksLeft <= 0) return true;
                entry.setValue(new Charge(charge.target, charge.level, charge.direction, charge.ticksLeft - 1));

                Vec3 step = charge.direction.scale(0.9D);
                if (level.noCollision(target, target.getBoundingBox().move(step))) {
                    target.setPos(target.getX() + step.x, target.getY() + step.y, target.getZ() + step.z);
                    target.setDeltaMovement(step);
                    return false;
                }

                target.hurtServer(level, player.damageSources().playerAttack(player), 4.0F + charge.level * 2.0F);
                target.push(charge.direction.x * 0.7D, charge.direction.y * 0.5D, charge.direction.z * 0.7D);
                return true;
            });
        });
    }

    public static void activate(ServerPlayer player, LivingEntity target, int level) {
        Vec3 direction = player.getLookAngle().normalize();
        player.setDeltaMovement(direction.scale(1.25D));
        target.setDeltaMovement(direction.scale(0.9D));
        CHARGES.put(player.getUUID(), new Charge(target.getUUID(), level, direction, 12));
    }

    private record Charge(UUID target, int level, Vec3 direction, int ticksLeft) {
    }
}
