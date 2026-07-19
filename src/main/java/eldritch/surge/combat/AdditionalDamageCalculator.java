package eldritch.surge.combat;

import eldritch.surge.enchantment.EldritchEnchantments;
import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import eldritch.surge.entity.EldritchEntityTaxonomy;
import eldritch.surge.game.EldritchGameRules;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.server.level.ServerLevel;

public final class AdditionalDamageCalculator {
    private static final float BLADE_DAMAGE_PER_LEVEL_PER_CATEGORY = 2.0F;

    private AdditionalDamageCalculator() {
    }

    public static float addBladeOfApocalypseDamage(float originalDamage, LivingEntity victim, Entity attacker) {
        if (!(victim.level() instanceof ServerLevel world)) {
            return originalDamage;
        }

        return addBladeOfApocalypseDamage(world, originalDamage, victim, attacker);
    }

    public static float addBladeOfApocalypseDamage(ServerLevel world, float originalDamage, LivingEntity victim, Entity attacker) {
        float damage = reduceIncomingDamage(world, originalDamage, victim, attacker);
        if (!(attacker instanceof LivingEntity livingAttacker)) {
            return damage;
        }

        int bladeLevel = EnchantmentLevels.onItem(world, livingAttacker.getMainHandItem(), EldritchEnchantments.BLADE_OF_APOCALYPSE.identifier().getPath());
        float finalDamage = damage;
        if (bladeLevel <= 0) {
            finalDamage += categoryDamage(world, victim, livingAttacker);
        } else {
            float bonus = calculateBladeBonus(victim, bladeLevel);
            if (bonus > 0.0F) {
                finalDamage += applyPvpModifier(world, victim, bonus);
            }
        }

        return finalDamage;
    }

    public static float calculateBladeBonus(LivingEntity victim, int level) {
        int matchedCategories = EldritchEntityTaxonomy.countMatches(
                victim,
                EldritchEntityTaxonomy.BLADE_OF_APOCALYPSE_DAMAGE_TAGS
        );
        return matchedCategories * level * BLADE_DAMAGE_PER_LEVEL_PER_CATEGORY;
    }

    public static float applyPvpModifier(ServerLevel world, LivingEntity victim, float bonusDamage) {
        if (!(victim instanceof Player)) {
            return bonusDamage;
        }

        return (float) (bonusDamage * world.getGameRules().get(EldritchGameRules.PVP_ENCHANTMENT_MODIFIER));
    }

    public static void afterSuccessfulHit(ServerLevel world, LivingEntity victim, Entity attacker, float damage) {
        if (!(attacker instanceof LivingEntity livingAttacker)) {
            return;
        }

        ItemStack weapon = livingAttacker.getMainHandItem();
        int freeze = EnchantmentLevels.onItem(world, weapon, "freeze_aspect");
        if (freeze > 0) {
            victim.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 60 + freeze * 20, Math.min(3, freeze - 1)));
            victim.setTicksFrozen(Math.min(victim.getTicksFrozen() + 80 * freeze, 200));
        }

        int leeching = EnchantmentLevels.onItem(world, weapon, "leeching_aspect");
        if (leeching > 0 && livingAttacker instanceof Player player && isCriticalLike(player)) {
            player.heal(Math.max(1.0F, damage * 0.15F * leeching));
        }

        int launch = EnchantmentLevels.onItem(world, weapon, "launch");
        if (launch > 0) {
            victim.push(0.0D, 0.25D * launch, 0.0D);
            victim.hurtMarked = true;
        }

        int catapult = EnchantmentLevels.onItem(world, weapon, "catapult");
        if (catapult > 0 && livingAttacker instanceof Player player && isCriticalLike(player)) {
            victim.push(0.0D, 0.45D + 0.25D * catapult, 0.0D);
            victim.hurtMarked = true;
        }

        int lunge = EnchantmentLevels.onItem(world, weapon, "lunge");
        if (lunge > 0) {
            double dx = victim.getX() - livingAttacker.getX();
            double dz = victim.getZ() - livingAttacker.getZ();
            victim.push(dx * 0.9D, 0.2D, dz * 0.9D);
            victim.hurtMarked = true;
        }

        int creeping = EnchantmentLevels.onItem(world, weapon, "creeping_threat");
        if (creeping > 0) {
            victim.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, Math.min(2, creeping - 1)));
            if (EldritchEntityTaxonomy.matches(victim, EldritchEntityTaxonomy.ARTHROPODS)) {
                victim.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 80, Math.min(3, creeping - 1)));
            }
        }

        int backslash = EnchantmentLevels.onItem(world, weapon, "backslash");
        if (backslash > 0) {
            victim.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, Math.min(3, backslash - 1)));
        }

        int inking = EnchantmentLevels.onItem(world, weapon, "inking");
        if (inking > 0) {
            victim.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60 + inking * 20, 0));
            victim.addEffect(new MobEffectInstance(MobEffects.GLOWING, 80 + inking * 20, 0));
            victim.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, Math.min(2, inking - 1)));
        }

        int spider = EnchantmentLevels.onItem(world, weapon, "curse_of_spider");
        if (spider > 0) {
            victim.addEffect(new MobEffectInstance(MobEffects.POISON, 80 + spider * 20, Math.min(2, spider - 1)));
        }

        if (victim.isDeadOrDying()) {
            applyKillRewards(world, victim, livingAttacker, weapon);
        }
    }

    private static float categoryDamage(ServerLevel world, LivingEntity victim, LivingEntity attacker) {
        ItemStack weapon = attacker.getMainHandItem();
        float bonus = 0.0F;
        bonus += EnchantmentLevels.onItem(world, weapon, "katana") * 1.25F;
        bonus += EnchantmentLevels.onItem(world, weapon, "gream_reaper") * 1.5F;
        bonus += EnchantmentLevels.onItem(world, weapon, "superweight") * 2.0F;
        bonus += paybackBonus(world, weapon, attacker);
        if (attacker instanceof Player player && isCriticalLike(player)) {
            bonus += EnchantmentLevels.onItem(world, weapon, "experienced") * 4.0F;
        }
        bonus += tagBonus(world, weapon, "bane_of_end", victim, EldritchEntityTaxonomy.IS_END_MOB, 2.5F);
        bonus += tagBonus(world, weapon, "undead_slayer", victim, EldritchEntityTaxonomy.UNDEAD, 3.0F);
        bonus += tagBonus(world, weapon, "exorcist", victim, EldritchEntityTaxonomy.HELL, 2.5F);
        bonus += tagBonus(world, weapon, "butcher", victim, EldritchEntityTaxonomy.ANIMALS, 2.0F);
        bonus += tagBonus(world, weapon, "herbicide", victim, EldritchEntityTaxonomy.FUNGI, 2.5F);
        bonus += tagBonus(world, weapon, "witch_hunter", victim, EldritchEntityTaxonomy.MAGIK, 2.5F);
        bonus += tagBonus(world, weapon, "wrath_of_the_abyss", victim, EldritchEntityTaxonomy.WATER, 2.5F);
        bonus += tagBonus(world, weapon, "creeping_threat", victim, EldritchEntityTaxonomy.ARTHROPODS, 2.75F);
        bonus += tagBonus(world, weapon, "smoother", victim, EldritchEntityTaxonomy.CUBIC, 2.5F);
        bonus += tagBonus(world, weapon, "flogging", victim, EldritchEntityTaxonomy.REBEL, 2.5F);

        return applyPvpModifier(world, victim, bonus);
    }

    private static float reduceIncomingDamage(ServerLevel world, float originalDamage, LivingEntity victim, Entity attacker) {
        float multiplier = 1.0F;
        int weaponProtection = EnchantmentLevels.onArmor(world, victim, "weapon_protection");
        if (weaponProtection > 0 && attacker instanceof LivingEntity livingAttacker && !livingAttacker.getMainHandItem().isEmpty()) {
            multiplier -= Math.min(0.5F, weaponProtection * 0.07F);
        }

        int armored = EnchantmentLevels.onArmor(world, victim, "armored");
        if (armored > 0) {
            multiplier -= Math.min(0.35F, armored * 0.04F);
        }

        int superweight = attacker instanceof LivingEntity livingAttacker
                ? EnchantmentLevels.onItem(world, livingAttacker.getMainHandItem(), "superweight")
                : 0;
        if (superweight > 0) {
            multiplier += superweight * 0.02F;
        }

        return Math.max(0.0F, originalDamage * Math.max(0.15F, multiplier));
    }

    private static float paybackBonus(ServerLevel world, ItemStack weapon, LivingEntity attacker) {
        int payback = EnchantmentLevels.onItem(world, weapon, "payback");
        if (payback <= 0) {
            return 0.0F;
        }

        float missingHealthRatio = 1.0F - (attacker.getHealth() / attacker.getMaxHealth());
        return payback * missingHealthRatio * missingHealthRatio * 6.0F;
    }

    private static void applyKillRewards(ServerLevel world, LivingEntity victim, LivingEntity attacker, ItemStack weapon) {
        int triumph = EnchantmentLevels.onItem(world, weapon, "triumph");
        if (triumph > 0) {
            attacker.heal(2.0F * triumph);
        }

        int midas = EnchantmentLevels.onItem(world, weapon, "midas_touch");
        if (midas > 0 && world.getRandom().nextFloat() < 0.15F * midas) {
            ItemStack gold = world.getRandom().nextFloat() < 0.15F
                    ? new ItemStack(Items.GOLD_INGOT)
                    : new ItemStack(Items.GOLD_NUGGET, 1 + world.getRandom().nextInt(Math.max(1, midas)));
            world.addFreshEntity(new ItemEntity(world, victim.getX(), victim.getY(), victim.getZ(), gold));
        }
    }

    private static float tagBonus(ServerLevel world, ItemStack weapon, String enchantment, LivingEntity victim, net.minecraft.tags.TagKey<net.minecraft.world.entity.EntityType<?>> tag, float perLevel) {
        int level = EnchantmentLevels.onItem(world, weapon, enchantment);
        if (level <= 0 || !EldritchEntityTaxonomy.matches(victim, tag)) {
            return 0.0F;
        }

        return level * perLevel;
    }

    private static boolean isCriticalLike(Player player) {
        return player.fallDistance > 0.0F && !player.onGround() && !player.isInWater() && !player.isPassenger();
    }
}
