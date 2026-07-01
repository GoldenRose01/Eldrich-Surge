package eldritch.surge.combat;

import eldritch.surge.enchantment.EldritchEnchantments;
import eldritch.surge.entity.EldritchEntityCategories;
import eldritch.surge.game.EldritchGameRules;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.Optional;

public final class AdditionalDamageCalculator {
    private static final float BLADE_DAMAGE_PER_LEVEL_PER_CATEGORY = 2.0F;

    private AdditionalDamageCalculator() {
    }

    public static float addBladeOfApocalypseDamage(float originalDamage, LivingEntity victim, Entity attacker) {
        if (!(victim.getWorld() instanceof ServerWorld world)) {
            return originalDamage;
        }

        return addBladeOfApocalypseDamage(world, originalDamage, victim, attacker);
    }

    public static float addBladeOfApocalypseDamage(ServerWorld world, float originalDamage, LivingEntity victim, Entity attacker) {
        if (!(attacker instanceof LivingEntity livingAttacker)) {
            return originalDamage;
        }

        int bladeLevel = getLevel(world, EldritchEnchantments.BLADE_OF_APOCALYPSE, livingAttacker.getMainHandStack());
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

        if (victim.getType().isIn(EldritchEntityCategories.ANIMALS)) {
            matchedCategories++;
        }
        if (victim.getType().isIn(EldritchEntityCategories.UNDEAD)) {
            matchedCategories++;
        }
        if (victim.getType().isIn(EldritchEntityCategories.IS_END_MOB)) {
            matchedCategories++;
        }
        if (victim.getType().isIn(EldritchEntityCategories.HELL)) {
            matchedCategories++;
        }
        if (victim.getType().isIn(EldritchEntityCategories.WATER)) {
            matchedCategories++;
        }

        return matchedCategories * level * BLADE_DAMAGE_PER_LEVEL_PER_CATEGORY;
    }

    public static float applyPvpModifier(World world, LivingEntity victim, float bonusDamage) {
        if (!(victim instanceof PlayerEntity)) {
            return bonusDamage;
        }

        return (float) (bonusDamage * world.getGameRules().get(EldritchGameRules.PVP_ENCHANTMENT_MODIFIER));
    }

    private static int getLevel(ServerWorld world, RegistryKey<Enchantment> enchantment, ItemStack stack) {
        Optional<RegistryEntry.Reference<Enchantment>> entry = world.getRegistryManager()
                .getOrThrow(RegistryKeys.ENCHANTMENT)
                .getEntry(enchantment);

        return entry.map(enchantmentEntry -> EnchantmentHelper.getLevel(enchantmentEntry, stack)).orElse(0);
    }
}
