package eldritch.surge.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import eldritch.surge.combat.AdditionalDamageCalculator;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public record GameruleDamageEnchantmentEffect(
        LevelBasedValue amount,
        Optional<Identifier> entityTypeTag
) implements EnchantmentEntityEffect {
    public static final MapCodec<GameruleDamageEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LevelBasedValue.CODEC.fieldOf("amount").forGetter(GameruleDamageEnchantmentEffect::amount),
            Identifier.CODEC.optionalFieldOf("entity_type_tag").forGetter(GameruleDamageEnchantmentEffect::entityTypeTag)
    ).apply(instance, GameruleDamageEnchantmentEffect::new));

    @Override
    public void apply(ServerLevel world, int level, EnchantedItemInUse context, Entity target, Vec3 pos) {
        if (!(target instanceof LivingEntity victim) || !matchesTag(victim)) {
            return;
        }

        Entity owner = context.owner();
        float damage = AdditionalDamageCalculator.applyPvpModifier(world, victim, amount.calculate(level));
        DamageSource source = owner == null
                ? world.damageSources().magic()
                : world.damageSources().indirectMagic(owner, owner);

        victim.hurtServer(world, source, damage);
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }

    private boolean matchesTag(LivingEntity victim) {
        return entityTypeTag
                .map(id -> victim.getType().builtInRegistryHolder().is(TagKey.create(Registries.ENTITY_TYPE, id)))
                .orElse(true);
    }
}
