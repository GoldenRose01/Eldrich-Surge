package eldritch.surge.enchantment.mechanics;

import eldritch.surge.mixin.WindChargeInvoker;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public final class SeaBreezeMechanics {
    private SeaBreezeMechanics() {
    }

    public static boolean releaseWindCharge(ServerLevel level, ThrownTrident trident, ItemStack weapon, Vec3 impact) {
        double strength = EnchantmentLevels.onItemStrength(level, weapon, "sea_breeze");
        if (strength <= 0) {
            return false;
        }

        WindCharge charge = new WindCharge(level, impact.x, impact.y, impact.z, Vec3.ZERO);
        charge.setOwner(trident.getOwner());
        eldritch.surge.combat.SecondaryEnchantmentDamage.run(() ->
                ((WindChargeInvoker) (Object) charge).eldritchSurge$explode(impact));
        double extra = Math.max(0, strength - 3);
        if (extra > 0) {
            // I-III keep the original wind charge. Higher ranks add logarithmic gust force.
            for (var target : level.getEntitiesOfClass(net.minecraft.world.entity.LivingEntity.class,
                    new net.minecraft.world.phys.AABB(impact, impact).inflate(Math.min(16, 3 + extra)))) {
                if (!target.isAlive() || target.isSpectator()) continue;
                Vec3 direction = target.position().subtract(impact).normalize();
                target.push(direction.x * 0.15D * extra, 0.1D * extra, direction.z * 0.15D * extra);
            }
        }
        return true;
    }
}
