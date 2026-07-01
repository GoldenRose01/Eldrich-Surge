package eldritch.surge.enchantment;

import com.mojang.serialization.MapCodec;
import eldritch.surge.EldritchSurge;
import eldritch.surge.enchantment.effect.GameruleDamageEnchantmentEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;

public final class EldritchEnchantmentEffects {
    public static final MapCodec<? extends EnchantmentEntityEffect> GAMERULE_DAMAGE =
            register("gamerule_damage", GameruleDamageEnchantmentEffect.CODEC);

    private EldritchEnchantmentEffects() {
    }

    public static void initialize() {
        EldritchSurge.LOGGER.debug("Registered Eldritch Surge enchantment effect components.");
    }

    private static <T extends EnchantmentEntityEffect> MapCodec<T> register(String path, MapCodec<T> codec) {
        return Registry.register(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, EldritchSurge.id(path), codec);
    }
}
