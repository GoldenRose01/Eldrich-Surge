package eldritch.surge.enchantment.ench;

import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public final class HoleInOneEnchantment {
    public static final ModEnchantmentDefinition DEFINITION = new ModEnchantmentDefinition(
            "hole_in_one",
            "Hole in One",
            "SPEAR (NATIVA VANILLA)",
            3,
            "Epic",
            "Advanced",
            List.of("#eldrich-surge:enchantable/spear"),
            List.of("hand"),
            "",
            "Gli attacchi corpo a corpo con la lancia hanno una probabilità di eliminare istantaneamente i mob semplici (1% al I, 5% al III, poi logaritmica verso il 15%)."
    );

    private HoleInOneEnchantment() {
    }

    public static double oneshotChance(int level) {
        if (level <= 0) {
            return 0.0D;
        }
        if (level == 1) {
            return 0.01D;
        }
        if (level == 2) {
            return 0.03D;
        }
        if (level == 3) {
            return 0.05D;
        }
        return 0.05D + 0.10D * (1.0D - 1.0D / (1.0D + 0.35D * Math.log(level - 2.0D)));
    }

    public static boolean isSimpleMob(LivingEntity target) {
        if (target == null || !target.isAlive() || target instanceof Player) {
            return false;
        }
        if (target instanceof EnderDragon || target instanceof WitherBoss
                || target instanceof Warden || target instanceof ElderGuardian) {
            return false;
        }
        return target.getMaxHealth() <= 100.0F;
    }
}
