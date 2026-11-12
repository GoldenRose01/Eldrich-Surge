package eldritch.surge.enchantment.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eldritch.surge.enchantment.mechanics.EnchantmentScaling;
import net.minecraft.world.item.enchantment.LevelBasedValue;

/** Linear at designed ranks, logarithmic beyond them, including native attribute effects. */
public record LogarithmicLevelValue(float base, float perLevelAboveFirst, int maxLevel) implements LevelBasedValue {
    public static final MapCodec<LogarithmicLevelValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.FLOAT.fieldOf("base").forGetter(LogarithmicLevelValue::base),
            Codec.FLOAT.fieldOf("per_level_above_first").forGetter(LogarithmicLevelValue::perLevelAboveFirst),
            Codec.intRange(2, 255).fieldOf("max_level").forGetter(LogarithmicLevelValue::maxLevel)
    ).apply(instance, LogarithmicLevelValue::new));

    @Override
    public float calculate(int level) {
        if (level <= 0) return 0;
        // Preserve vanilla float arithmetic exactly throughout the normal range.
        if (level <= maxLevel) return base + perLevelAboveFirst * (level - 1);
        return (float) (base + perLevelAboveFirst * (EnchantmentScaling.effective(level, maxLevel) - 1));
    }

    @Override
    public MapCodec<LogarithmicLevelValue> codec() { return CODEC; }
}
