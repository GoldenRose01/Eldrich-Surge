package eldritch.surge.enchantment.mechanics;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gamerules.GameRules;

public final class MidasTouchMechanics {
    private MidasTouchMechanics() {}

    public static void initialize() {
        ServerLivingEntityEvents.AFTER_DEATH.register(MidasTouchMechanics::onDeath);
    }

    private static void onDeath(LivingEntity victim, net.minecraft.world.damagesource.DamageSource source) {
        if (!(victim.level() instanceof ServerLevel level)
                || !level.getGameRules().get(GameRules.MOB_DROPS)) return;

        LivingEntity attacker = source.getEntity() instanceof LivingEntity living ? living : null;
        ItemStack weapon = source.getWeaponItem();
        if ((weapon == null || weapon.isEmpty()) && attacker != null) weapon = attacker.getMainHandItem();
        if (weapon == null || weapon.isEmpty()) return;

        int enchantmentLevel = EnchantmentLevels.onItem(level, weapon, "midas_touch");
        if (enchantmentLevel <= 0 || level.getRandom().nextFloat() >= dropChance(enchantmentLevel)) return;

        ItemStack gold = level.getRandom().nextFloat() < 0.15F
                ? new ItemStack(Items.GOLD_INGOT)
                : new ItemStack(Items.GOLD_NUGGET, enchantmentLevel);
        level.addFreshEntity(new ItemEntity(level, victim.getX(), victim.getY(), victim.getZ(), gold));
    }

    static float dropChance(int level) {
        return Math.min(1.0F, Math.max(0, level) * 0.20F);
    }
}
