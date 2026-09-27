package eldritch.surge.mixin;

import eldritch.surge.block.EldritchBlocks;
import eldritch.surge.enchantment.mechanics.AdvancedEnchantingFormation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.EnchantingTableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnchantingTableBlockEntity.class)
public abstract class AdvancedEnchantingParticlesMixin {
    @Inject(method = "bookAnimationTick", at = @At("TAIL"))
    private static void eldritchSurge$emitParticlesFromGoldBlocks(
            Level level, BlockPos pos, BlockState state, EnchantingTableBlockEntity blockEntity, CallbackInfo ci
    ) {
        if (!(level instanceof ClientLevel clientLevel)
                || !state.is(EldritchBlocks.ADVANCED_ENCHANTING_TABLE)
                || !AdvancedEnchantingFormation.isValid(level, pos)
                || level.getRandom().nextInt(16) != 0) {
            return;
        }

        int corner = level.getRandom().nextInt(4);
        int offsetX = corner == 0 || corner == 2 ? -2 : 2;
        int offsetZ = corner < 2 ? -2 : 2;
        double sourceX = pos.getX() + offsetX + 0.5D;
        double sourceY = pos.getY() + 0.75D;
        double sourceZ = pos.getZ() + offsetZ + 0.5D;
        double targetX = pos.getX() + 0.5D;
        double targetY = pos.getY() + 0.5D;
        double targetZ = pos.getZ() + 0.5D;

        clientLevel.addParticle(
                ParticleTypes.ENCHANT,
                sourceX, sourceY, sourceZ,
                (targetX - sourceX) * 0.12D,
                (targetY - sourceY) * 0.12D,
                (targetZ - sourceZ) * 0.12D
        );
    }
}
