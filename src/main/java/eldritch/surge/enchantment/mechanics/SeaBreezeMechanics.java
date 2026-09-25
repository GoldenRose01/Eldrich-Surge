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
        if (EnchantmentLevels.onItem(level, weapon, "sea_breeze") <= 0) {
            return false;
        }

        WindCharge charge = new WindCharge(level, impact.x, impact.y, impact.z, Vec3.ZERO);
        charge.setOwner(trident.getOwner());
        ((WindChargeInvoker) (Object) charge).eldritchSurge$explode(impact);
        return true;
    }
}
