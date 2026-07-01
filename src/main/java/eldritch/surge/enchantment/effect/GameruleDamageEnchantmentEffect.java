package eldritch.surge.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eldritch.surge.combat.AdditionalDamageCalculator;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.resources.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public record GameruleDamageEnchantmentEffect(
        EnchantmentLevelBasedValue amount,
        Optional<Identifier> entityTypeTag
) implements EnchantmentEntityEffect {
    public static final MapCodec<GameruleDamageEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            EnchantmentLevelBasedValue.CODEC.fieldOf("amount").forGetter(GameruleDamageEnchantmentEffect::amount),
            Identifier.CODEC.optionalFieldOf("entity_type_tag").forGetter(GameruleDamageEnchantmentEffect::entityTypeTag)
    ).apply(instance, GameruleDamageEnchantmentEffect::new));

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity target, Vec3d pos) {
        if (!(target instanceof LivingEntity victim) || !matchesTag(victim)) {
            return;
        }

        Entity owner = context.owner();
        float damage = AdditionalDamageCalculator.applyPvpModifier(world, victim, amount.getValue(level));
        DamageSource source = owner == null
                ? world.getDamageSources().magic()
                : world.getDamageSources().indirectMagic(owner, owner);

        victim.damage(world, source, damage);
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }

    private boolean matchesTag(LivingEntity victim) {
        return entityTypeTag
                .map(id -> victim.getType().isIn(TagKey.of(RegistryKeys.ENTITY_TYPE, id)))
                .orElse(true);
    }
}
