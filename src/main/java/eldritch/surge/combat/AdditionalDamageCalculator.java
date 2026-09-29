package eldritch.surge.combat;

import eldritch.surge.enchantment.EldritchEnchantments;
import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import eldritch.surge.enchantment.mechanics.EnchLibMobCategories;
import eldritch.surge.enchantment.mechanics.MobCategoryDamageMechanics;
import eldritch.surge.enchantment.mechanics.SpiritCastMechanics;
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
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public final class AdditionalDamageCalculator {
    private static final TagKey<EntityType<?>> VANILLA_SMITE_TARGETS = TagKey.create(
            Registries.ENTITY_TYPE, Identifier.withDefaultNamespace("sensitive_to_smite"));

    private AdditionalDamageCalculator() {
    }

    public static float addBladeOfApocalypseDamage(float originalDamage, LivingEntity victim, Entity attacker) {
        if (!(victim.level() instanceof ServerLevel world)) {
            return originalDamage;
        }

        return addBladeOfApocalypseDamage(world, originalDamage, victim, attacker);
    }

    public static float addBladeOfApocalypseDamage(ServerLevel world, float originalDamage, LivingEntity victim, Entity attacker) {
        return addBladeOfApocalypseDamage(world, originalDamage, victim, attacker, null);
    }

    public static float addBladeOfApocalypseDamage(ServerLevel world, float originalDamage, LivingEntity victim, Entity attacker, net.minecraft.world.damagesource.DamageSource source) {
        float damage = reduceIncomingDamage(world, originalDamage, victim, attacker, source);
        if (!(attacker instanceof LivingEntity livingAttacker)) {
            return damage;
        }

        float finalDamage = damage;
        ItemStack weapon = livingAttacker.getMainHandItem();
        int dash = EnchantmentLevels.onItem(world, weapon, "dash");
        ItemStack offhand = livingAttacker.getOffhandItem();
        if (dash > 0 && ItemStack.isSameItemSameComponents(weapon, offhand)
                && EnchantmentLevels.onItem(world, offhand, "dash") > 0) {
            finalDamage += 1.0F + dash / 2.0F;
        }

        int bladeLevel = EnchantmentLevels.onItem(world, weapon, EldritchEnchantments.BLADE_OF_APOCALYPSE.identifier().getPath());
        if (bladeLevel <= 0) {
            finalDamage += categoryDamage(world, victim, livingAttacker);
        } else {
            float bonus = calculateBladeBonus(victim, bladeLevel);
            if (bonus > 0.0F) {
                finalDamage += applyPvpModifier(world, victim, bonus);
            }
        }

        if (source != null && source.is(DamageTypeTags.IS_MACE_SMASH)) {
            finalDamage += EnchantmentLevels.onItem(world, livingAttacker.getMainHandItem(), "heavy_impact") * 1.5F;
        }

        return finalDamage;
    }

    public static float calculateBladeBonus(LivingEntity victim, int level) {
        if (!(victim.level() instanceof ServerLevel world)) return 0.0F;
        return MobCategoryDamageMechanics.bladeOfApocalypseBonus(world, victim, level);
    }

    public static float applyPvpModifier(ServerLevel world, LivingEntity victim, float bonusDamage) {
        if (!(victim instanceof Player)) {
            return bonusDamage;
        }

        return (float) (bonusDamage * world.getGameRules().get(EldritchGameRules.PVP_ENCHANTMENT_MODIFIER));
    }

    public static void afterSuccessfulHit(ServerLevel world, LivingEntity victim, Entity attacker, float damage, net.minecraft.world.damagesource.DamageSource source) {
        if (!(attacker instanceof LivingEntity livingAttacker)) {
            return;
        }

        ItemStack weapon = livingAttacker.getMainHandItem();
        ItemStack offhand = livingAttacker.getOffhandItem();
        if (EnchantmentLevels.onItem(world, weapon, "dash") > 0
                && ItemStack.isSameItemSameComponents(weapon, offhand)
                && EnchantmentLevels.onItem(world, offhand, "dash") > 0) {
            world.sendParticles(net.minecraft.core.particles.ParticleTypes.CRIT,
                    livingAttacker.getX(), livingAttacker.getY() + 1.0D, livingAttacker.getZ(),
                    10, 0.35D, 0.45D, 0.35D, 0.08D);
        }
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
        }

        int catapult = EnchantmentLevels.onItem(world, weapon, "catapult");
        if (catapult > 0 && livingAttacker instanceof Player player && isCriticalLike(player)) {
            var velocity = victim.getDeltaMovement();
            victim.setDeltaMovement(velocity.x, 0.45D * catapult * 3.5D, velocity.z);
        }

        int lunge = EnchantmentLevels.onItem(world, weapon, "lunge");
        if (lunge > 0) {
            double dx = victim.getX() - livingAttacker.getX();
            double dz = victim.getZ() - livingAttacker.getZ();
            victim.push(dx * 0.9D, 0.2D, dz * 0.9D);
        }

        int creeping = EnchantmentLevels.onItem(world, weapon, "creeping_threat");
        if (creeping > 0) {
            victim.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, Math.min(2, creeping - 1)));
            if (EnchLibMobCategories.has(world, victim, "arthropods")) {
                victim.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 20 + 15 * creeping, 3));
            }
        }

        int backslash = EnchantmentLevels.onItem(world, weapon, "backslash");
        if (backslash > 0) {
            victim.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40 * backslash, backslash - 1));
            var look = livingAttacker.getLookAngle();
            victim.push(-look.x * 0.35D * backslash, 0.1D, -look.z * 0.35D * backslash);
        }

        int inking = EnchantmentLevels.onItem(world, weapon, "inking");
        if (inking > 0) {
            victim.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60 + inking * 20, 0));
            victim.addEffect(new MobEffectInstance(MobEffects.GLOWING, 80 + inking * 20, 0));
            victim.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 80, Math.min(2, inking - 1)));
        }

        int spider = EnchantmentLevels.onItem(world, weapon, "curse_of_spider");
        if (spider > 0) {
            victim.addEffect(new MobEffectInstance(MobEffects.POISON, 30 * spider, spider - 1));
        }

        int perish = EnchantmentLevels.onItem(world, weapon, "curse_of_perish");
        if (perish > 0) {
            victim.addEffect(new MobEffectInstance(MobEffects.WITHER, 30 * perish, perish - 1));
        }

        int gravityWell = EnchantmentLevels.onItem(world, weapon, "gravity_well");
        if (gravityWell > 0 && livingAttacker.fallDistance > 0.0F) {
            for (LivingEntity nearby : world.getEntitiesOfClass(LivingEntity.class, victim.getBoundingBox().inflate(5.0D))) {
                if (nearby == livingAttacker || nearby == victim || !nearby.isAlive()) continue;
                double dx = victim.getX() - nearby.getX();
                double dy = victim.getY() - nearby.getY();
                double dz = victim.getZ() - nearby.getZ();
                double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (distance > 0.001D) nearby.push(dx / distance * 0.12D * gravityWell, dy / distance * 0.12D * gravityWell, dz / distance * 0.12D * gravityWell);
            }
        }

        if (source != null && source.is(DamageTypeTags.IS_MACE_SMASH)) {
            applyMaceSlamEffects(world, victim, livingAttacker, weapon, damage);
        }

        if (victim.isDeadOrDying()) {
            applyKillRewards(world, victim, livingAttacker, weapon);
        }
    }

    private static void applyMaceSlamEffects(ServerLevel world, LivingEntity impact, LivingEntity attacker, ItemStack weapon, float damage) {
        int seismic = EnchantmentLevels.onItem(world, weapon, "seismic_wave");
        if (seismic > 0) {
            float waveDamage = damage * 0.20F * seismic;
            for (LivingEntity nearby : world.getEntitiesOfClass(LivingEntity.class, impact.getBoundingBox().inflate(4.0D))) {
                if (nearby != attacker && nearby != impact && nearby.isAlive()) {
                    net.minecraft.world.damagesource.DamageSource waveSource = attacker instanceof Player player
                            ? attacker.damageSources().playerAttack(player)
                            : attacker.damageSources().mobAttack(attacker);
                    nearby.hurtServer(world, waveSource, waveDamage);
                }
            }
        }

        int comet = EnchantmentLevels.onItem(world, weapon, "comet_slam");
        if (comet > 0 && attacker.fallDistance > 12.0F) {
            world.explode(attacker, impact.getX(), impact.getY(), impact.getZ(), 3.0F, false, net.minecraft.world.level.Level.ExplosionInteraction.NONE);
            for (LivingEntity nearby : world.getEntitiesOfClass(LivingEntity.class, impact.getBoundingBox().inflate(5.0D))) {
                if (nearby == attacker || !nearby.isAlive()) continue;
                nearby.igniteForSeconds(5.0F);
                nearby.push((nearby.getX() - impact.getX()) * 1.2D, 1.0D, (nearby.getZ() - impact.getZ()) * 1.2D);
            }
        }
    }

    private static float categoryDamage(ServerLevel world, LivingEntity victim, LivingEntity attacker) {
        ItemStack weapon = attacker.getMainHandItem();
        float bonus = 0.0F;
        bonus += EnchantmentLevels.onItem(world, weapon, "katana") * 1.25F;
        bonus += EnchantmentLevels.onItem(world, weapon, "gream_reaper") * 1.5F;
        bonus += EnchantmentLevels.onItem(world, weapon, "neptunes_will") * 2.0F;
        bonus += EnchantmentLevels.onItem(world, weapon, "superweight") * 2.0F;
        bonus += paybackBonus(world, weapon, attacker);
        if (attacker instanceof Player player && isCriticalLike(player)) {
            bonus += EnchantmentLevels.onItem(world, weapon, "experienced") * 4.0F;
        }
        bonus += MobCategoryDamageMechanics.bonusForHit(world, weapon, victim);
        bonus += customSmiteBonus(world, weapon, victim);

        return applyPvpModifier(world, victim, bonus);
    }

    private static float reduceIncomingDamage(ServerLevel world, float originalDamage, LivingEntity victim, Entity attacker,
                                              net.minecraft.world.damagesource.DamageSource source) {
        float multiplier = 1.0F;
        int combustion = EnchantmentLevels.onArmor(world, victim, "combustion_protection");
        if (combustion > 0 && source != null
                && (source.is(DamageTypeTags.IS_FIRE) || source.is(DamageTypeTags.IS_EXPLOSION))) {
            multiplier -= Math.min(0.40F, combustion * 0.08F);
        }
        int weaponProtection = EnchantmentLevels.onArmor(world, victim, "weapon_protection");
        if (weaponProtection > 0 && attacker instanceof LivingEntity livingAttacker && !livingAttacker.getMainHandItem().isEmpty()) {
            multiplier -= Math.min(0.5F, weaponProtection * 0.07F);
        }

        if (EnchantmentLevels.onEquipment(world, victim, "god_protection") > 0) {
            multiplier *= 0.40F;
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
        int spiritCast = EnchantmentLevels.onItem(world, weapon, "spirti_cast");
        if (spiritCast > 0 && attacker instanceof net.minecraft.server.level.ServerPlayer player) {
            SpiritCastMechanics.recordKill(world, player, spiritCast);
        }

        int triumph = EnchantmentLevels.onItem(world, weapon, "triumph");
        if (triumph > 0) {
            attacker.heal(2.0F * triumph);
        }

        int midas = EnchantmentLevels.onItem(world, weapon, "midas_touch");
        if (midas > 0 && world.getRandom().nextFloat() < 0.08F * midas) {
            ItemStack gold = world.getRandom().nextFloat() < 0.15F
                    ? new ItemStack(Items.GOLD_INGOT)
                    : new ItemStack(Items.GOLD_NUGGET, midas);
            world.addFreshEntity(new ItemEntity(world, victim.getX(), victim.getY(), victim.getZ(), gold));
        }
    }

    private static float customSmiteBonus(ServerLevel world, ItemStack weapon, LivingEntity victim) {
        int level = EnchantmentLevels.onMinecraftItem(world, weapon, "smite");
        if (level <= 0 || victim.getType().builtInRegistryHolder().is(VANILLA_SMITE_TARGETS)
                || !EnchLibMobCategories.has(world, victim, "undead")) {
            return 0.0F;
        }

        // Vanilla already handles its own tag; this extends Smite to modded mobs categorized by EnchLib.
        return level * 2.5F;
    }

    private static boolean isCriticalLike(Player player) {
        return player.fallDistance > 0.0F && !player.onGround() && !player.isInWater() && !player.isPassenger();
    }
}
