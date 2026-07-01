package eldritch.surge.enchantment;

import com.mojang.serialization.MapCodec;
import eldritch.surge.EldritchSurge;
import eldritch.surge.enchantment.effect.GameruleDamageEnchantmentEffect;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public final class EldritchEnchantmentEffects {
    public static final MapCodec<? extends EnchantmentEntityEffect> GAMERULE_DAMAGE =
            register("gamerule_damage", GameruleDamageEnchantmentEffect.CODEC);

    private EldritchEnchantmentEffects() {
    }

    public static void initialize() {
        EldritchSurge.LOGGER.debug("Registered Eldritch Surge enchantment effect components.");
    }

    private static <T extends EnchantmentEntityEffect> MapCodec<T> register(String path, MapCodec<T> codec) {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, EldritchSurge.id(path), codec);
    }
}
