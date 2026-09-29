package eldritch.surge.enchantment.mechanics;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.level.levelgen.Heightmap;

/** Defensive enchantment effects that trigger when their wearer is hit. */
public final class DefensiveEnchantmentMechanics {
    private static final int VOID_ESCAPE_RADIUS = 32;

    private DefensiveEnchantmentMechanics() {
    }

    /** Returns true only when End Adaptability found a safe landing and prevented void damage. */
    public static boolean tryEscapeVoid(ServerLevel level, LivingEntity victim, DamageSource source) {
        if (!(victim instanceof ServerPlayer player)
                || !source.is(DamageTypes.FELL_OUT_OF_WORLD)
                || EnchantmentLevels.onArmor(level, player, "end_adaptability") <= 0) {
            return false;
        }

        BlockPos landing = nearestSafeLanding(level, player.blockPosition());
        if (landing == null) {
            return false;
        }

        if (!player.teleportTo(level, landing.getX() + 0.5D, landing.getY(), landing.getZ() + 0.5D,
                java.util.Set.of(), player.getYRot(), player.getXRot(), false)) {
            return false;
        }
        player.fallDistance = 0.0F;
        player.setDeltaMovement(0.0D, 0.0D, 0.0D);
        return true;
    }

    public static void retaliate(ServerLevel level, LivingEntity wearer, Entity attacker, DamageSource source) {
        if (!(attacker instanceof LivingEntity target) || target == wearer || !target.isAlive()) {
            return;
        }

        int depth = EnchantmentLevels.onArmor(level, wearer, "heart_of_depth");
        if (depth > 0) {
            target.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                    net.minecraft.world.effect.MobEffects.DARKNESS, 60 * depth, 0));
        }

        int nether = EnchantmentLevels.onArmor(level, wearer, "heart_of_nether");
        if (nether > 0 && !source.is(DamageTypeTags.IS_PROJECTILE)) {
            target.igniteForSeconds(2.0F * nether);
        }

        int sky = EnchantmentLevels.onArmor(level, wearer, "heart_of_the_sky");
        if (sky > 0) {
            target.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                    net.minecraft.world.effect.MobEffects.SLOWNESS, 60 * sky, Math.min(3, sky - 1)));
            target.setTicksFrozen(Math.min(300, target.getTicksFrozen() + 60 * sky));
        }
    }

    private static BlockPos nearestSafeLanding(ServerLevel level, BlockPos origin) {
        for (int radius = 0; radius <= VOID_ESCAPE_RADIUS; radius++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (Math.max(Math.abs(dx), Math.abs(dz)) != radius) continue;
                    int x = origin.getX() + dx;
                    int z = origin.getZ() + dz;
                    int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
                    if (y <= level.getMinY() || y + 1 >= level.getMaxY()) continue;

                    BlockPos feet = new BlockPos(x, y, z);
                    if (level.getBlockState(feet.below()).isSolid()
                            && level.getBlockState(feet).getCollisionShape(level, feet).isEmpty()
                            && level.getBlockState(feet.above()).getCollisionShape(level, feet.above()).isEmpty()) {
                        return feet;
                    }
                }
            }
        }
        return null;
    }
}
