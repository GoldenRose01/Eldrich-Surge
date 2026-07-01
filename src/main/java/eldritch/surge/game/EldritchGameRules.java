package eldritch.surge.game;

import eldritch.surge.EldritchSurge;
import net.fabricmc.fabric.api.gamerule.v1.GameRule;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleCategory;

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
}
