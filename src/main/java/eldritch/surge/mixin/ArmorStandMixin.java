package eldritch.surge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import eldritch.surge.EldritchSurge;
import eldritch.surge.enchantment.mechanics.MagneticArmorStand;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStand.class)
public abstract class ArmorStandMixin extends LivingEntity implements MagneticArmorStand {
    @Unique
    private static final String MAGNETIC_KEY = "eldrich_magnetic";

    @Unique
    private int eldritchSurge$magneticLevel;

    protected ArmorStandMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public int eldritchSurge$getMagneticLevel() {
        return eldritchSurge$magneticLevel;
    }

    @Override
    public void eldritchSurge$setMagneticLevel(int level) {
        this.eldritchSurge$magneticLevel = Math.max(0, level);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void eldritchSurge$saveMagnetic(ValueOutput output, CallbackInfo ci) {
        if (eldritchSurge$magneticLevel > 0) {
            output.putInt(MAGNETIC_KEY, eldritchSurge$magneticLevel);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void eldritchSurge$loadMagnetic(ValueInput input, CallbackInfo ci) {
        eldritchSurge$magneticLevel = Math.max(0, input.getIntOr(MAGNETIC_KEY, 0));
    }

    @Override
    public void eldritchSurge$tickMagnetic() {
        if (eldritchSurge$magneticLevel <= 0 || !(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        double centerX = this.getX();
        double centerY = this.getY() + 0.5D;
        double centerZ = this.getZ();
        for (ItemEntity item : serverLevel.getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(8.0D))) {
            if (!item.isAlive()) {
                continue;
            }
            double dx = centerX - item.getX();
            double dy = centerY - item.getY();
            double dz = centerZ - item.getZ();
            double distSq = dx * dx + dy * dy + dz * dz;
            if (distSq > 64.0D || distSq < 0.0625D) {
                continue;
            }
            double dist = Math.sqrt(distSq);
            double pull = 0.08D;
            item.setDeltaMovement(item.getDeltaMovement().add(dx / dist * pull, dy / dist * pull + 0.02D, dz / dist * pull));
        }
    }

    @WrapOperation(
            method = "brokenByPlayer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;popResource(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)V")
    )
    private void eldritchSurge$preserveMagneticOnBreak(Level level, BlockPos pos, ItemStack stack, Operation<Void> original) {
        if (eldritchSurge$magneticLevel > 0 && stack.is(Items.ARMOR_STAND) && level instanceof ServerLevel serverLevel) {
            serverLevel.registryAccess().lookup(Registries.ENCHANTMENT)
                    .flatMap(reg -> reg.get(ResourceKey.create(Registries.ENCHANTMENT, EldritchSurge.id("magnetic"))))
                    .ifPresent(holder -> stack.enchant(holder, eldritchSurge$magneticLevel));
        }
        original.call(level, pos, stack);
    }
}
