package eldritch.surge.combat;

import eldritch.surge.enchantment.EldritchEnchantments;
import eldritch.surge.entity.EldritchEntityCategories;
import eldritch.surge.game.EldritchGameRules;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;

import java.util.Optional;

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
        if (!(attacker instanceof LivingEntity livingAttacker)) {
            return originalDamage;
        }

        int bladeLevel = getLevel(world, EldritchEnchantments.BLADE_OF_APOCALYPSE, livingAttacker.getMainHandItem());
        if (bladeLevel <= 0) {
            return originalDamage;
        }

        float bonus = calculateBladeBonus(victim, bladeLevel);
        if (bonus <= 0.0F) {
            return originalDamage;
        }

        return originalDamage + applyPvpModifier(world, victim, bonus);
    }

    public static float calculateBladeBonus(LivingEntity victim, int level) {
        int matchedCategories = 0;

        if (victim.getType().builtInRegistryHolder().is(EldritchEntityCategories.ANIMALS)) {
            matchedCategories++;
        }
        if (victim.getType().builtInRegistryHolder().is(EldritchEntityCategories.UNDEAD)) {
            matchedCategories++;
        }
        if (victim.getType().builtInRegistryHolder().is(EldritchEntityCategories.IS_END_MOB)) {
            matchedCategories++;
        }
        if (victim.getType().builtInRegistryHolder().is(EldritchEntityCategories.HELL)) {
            matchedCategories++;
        }
        if (victim.getType().builtInRegistryHolder().is(EldritchEntityCategories.WATER)) {
            matchedCategories++;
        }

        return matchedCategories * level * BLADE_DAMAGE_PER_LEVEL_PER_CATEGORY;
    }

    public static float applyPvpModifier(ServerLevel world, LivingEntity victim, float bonusDamage) {
        if (!(victim instanceof Player)) {
            return bonusDamage;
        }

        return (float) (bonusDamage * world.getGameRules().get(EldritchGameRules.PVP_ENCHANTMENT_MODIFIER));
    }

    private static int getLevel(ServerLevel world, ResourceKey<Enchantment> enchantment, ItemInstance stack) {
        Optional<Holder.Reference<Enchantment>> entry = world.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .get(enchantment);

        return entry.map(enchantmentEntry -> EnchantmentHelper.getItemEnchantmentLevel(enchantmentEntry, stack)).orElse(0);
    }
}
