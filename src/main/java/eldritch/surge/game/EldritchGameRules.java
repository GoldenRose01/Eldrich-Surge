package eldritch.surge.game;

import eldritch.surge.EldritchSurge;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;

public final class EldritchGameRules {
    public static final GameRule<Boolean> KEEP_LOYALTY_TRIDENTS = GameRuleBuilder
            .forBoolean(false)
            .category(GameRuleCategory.PLAYER)
            .buildAndRegister(EldritchSurge.id("keep_loyalty_tridents"));

    public static final GameRule<Boolean> SWEEPING_REQUIRES_ENCHANTMENT = GameRuleBuilder
            .forBoolean(true)
            .category(GameRuleCategory.PLAYER)
            .buildAndRegister(EldritchSurge.id("sweeping_requires_enchantment"));

    public static final GameRule<Double> PVP_ENCHANTMENT_MODIFIER = GameRuleBuilder
            .forDouble(1.0D)
            .category(GameRuleCategory.PLAYER)
            .buildAndRegister(EldritchSurge.id("pvp_enchantment_modifier"));

    public static final GameRule<Boolean> LETHAL_POISON = GameRuleBuilder
            .forBoolean(false)
            .category(GameRuleCategory.MOBS)
            .buildAndRegister(EldritchSurge.id("lethal_poison"));

    private EldritchGameRules() {
    }

    public static void initialize() {
        EldritchSurge.LOGGER.debug("Registered Eldritch Surge gamerules.");
    }

    public static boolean shouldKeepLoyaltyTridents(net.minecraft.world.level.Level level) {
        if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            return serverLevel.getGameRules().get(KEEP_LOYALTY_TRIDENTS)
                    || eldritch.surge.config.EnchantmentCapsConfig.isKeepLoyaltyTridents();
        }
        return eldritch.surge.config.EnchantmentCapsConfig.isKeepLoyaltyTridents();
    }

    public static boolean requiresSweepingEnchantment(net.minecraft.world.level.Level level) {
        if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            return serverLevel.getGameRules().get(SWEEPING_REQUIRES_ENCHANTMENT)
                    && eldritch.surge.config.EnchantmentCapsConfig.isSweepingRequiresEnchantment();
        }
        return eldritch.surge.config.EnchantmentCapsConfig.isSweepingRequiresEnchantment();
    }

    public static boolean isLethalPoison(net.minecraft.world.level.Level level) {
        if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            return serverLevel.getGameRules().get(LETHAL_POISON)
                    || eldritch.surge.config.EnchantmentCapsConfig.isLethalPoison();
        }
        return eldritch.surge.config.EnchantmentCapsConfig.isLethalPoison();
    }

    public static boolean isLethalPoisonFor(net.minecraft.world.level.Level level, net.minecraft.world.entity.LivingEntity entity) {
        String mode = eldritch.surge.config.EnchantmentCapsConfig.getLethalPoisonMode();
        if (level instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            boolean ruleEnabled = serverLevel.getGameRules().get(LETHAL_POISON);
            if (!ruleEnabled && "off".equalsIgnoreCase(mode)) {
                return false;
            }
            if (ruleEnabled && "off".equalsIgnoreCase(mode)) {
                mode = "all";
            }
        }
        return switch (mode.toLowerCase(java.util.Locale.ROOT)) {
            case "player", "players" -> entity instanceof net.minecraft.world.entity.player.Player;
            case "mob", "mobs" -> !(entity instanceof net.minecraft.world.entity.player.Player);
            case "all", "true", "on" -> true;
            default -> false;
        };
    }
}
