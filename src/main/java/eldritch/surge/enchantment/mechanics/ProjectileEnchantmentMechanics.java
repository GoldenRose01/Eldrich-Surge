package eldritch.surge.enchantment.mechanics;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.ArrayList;
import java.util.List;

public final class ProjectileEnchantmentMechanics {
    private ProjectileEnchantmentMechanics() {
    }

    public static void initialize() {
        LootTableEvents.MODIFY_DROPS.register((table, context, drops) -> {
            Entity directAttacker = context.getOptional(LootContextParams.DIRECT_ATTACKING_ENTITY);
            Entity attackingEntity = context.getOptional(LootContextParams.ATTACKING_ENTITY);
            AbstractArrow arrow = directAttacker instanceof AbstractArrow directArrow
                    ? directArrow
                    : attackingEntity instanceof AbstractArrow attackingArrow ? attackingArrow : null;
            if (arrow == null) {
                return;
            }

            int level = EnchantmentLevels.onItem(context.getLevel(), arrow.getWeaponItem(), "theft");
            if (level <= 0 || drops.isEmpty()) {
                return;
            }

            List<ItemStack> bonusDrops = new ArrayList<>();
            for (ItemStack drop : List.copyOf(drops)) {
                int extra = context.getRandom().nextInt(level + 1);
                while (extra > 0) {
                    int count = Math.min(extra, drop.getMaxStackSize());
                    bonusDrops.add(drop.copyWithCount(count));
                    extra -= count;
                }
            }
            drops.addAll(bonusDrops);
        });
    }

    public static float scaleArrowVelocity(ServerLevel level, AbstractArrow arrow, float velocity) {
        int elasticity = EnchantmentLevels.onItem(level, arrow.getWeaponItem(), "elasticity");
        return elasticity > 0 ? velocity * (1.0F + 0.25F * elasticity) : velocity;
    }

    public static double sniperBonus(ServerLevel level, AbstractArrow arrow) {
        int sniper = EnchantmentLevels.onItem(level, arrow.getWeaponItem(), "sniper");
        Entity owner = arrow.getOwner();
        if (sniper <= 0 || owner == null) {
            return 0.0D;
        }

        return owner.distanceTo(arrow) * 0.1D * sniper;
    }

    public static boolean isNinelevenTarget(ServerLevel level, AbstractArrow arrow, Entity target) {
        int nineleven = EnchantmentLevels.onItem(level, arrow.getWeaponItem(), "nineleven");
        String targetId = net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getKey(target.getType()).getPath();
        return nineleven > 0 && (targetId.equals("phantom") || targetId.equals("bat"));
    }

    public static void applyCurseOfTarget(ServerLevel level, AbstractArrow arrow, Entity target) {
        int curseLevel = EnchantmentLevels.onItem(level, arrow.getWeaponItem(), "curse_of_target");
        if (curseLevel > 0 && target instanceof net.minecraft.world.entity.LivingEntity living) {
            living.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20 * curseLevel, 0));
        }
    }

    public static void detonatePop(ServerLevel level, AbstractArrow arrow, double x, double y, double z) {
        int pop = EnchantmentLevels.onItem(level, arrow.getWeaponItem(), "pop");
        if (pop <= 0) {
            return;
        }

        float radius = 1.5F + 0.5F * pop;
        level.explode(arrow, x, y, z, radius, false, Level.ExplosionInteraction.NONE);
    }

    public static int popLevel(ServerLevel level, AbstractArrow arrow) {
        return EnchantmentLevels.onItem(level, arrow.getWeaponItem(), "pop");
    }
}
