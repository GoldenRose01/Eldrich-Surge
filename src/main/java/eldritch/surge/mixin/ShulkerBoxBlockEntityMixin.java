package eldritch.surge.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBoxBlockEntity.class)
public abstract class ShulkerBoxBlockEntityMixin extends BlockEntity {
    @Unique
    private static final String ENCHANTMENTS_KEY = "eldrich_enchantments";

    @Unique
    private ItemEnchantments eldritchSurge$enchantments = ItemEnchantments.EMPTY;

    protected ShulkerBoxBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void eldritchSurge$saveEnchantments(ValueOutput output, CallbackInfo ci) {
        if (!eldritchSurge$enchantments.isEmpty()) {
            output.store(ENCHANTMENTS_KEY, ItemEnchantments.CODEC, eldritchSurge$enchantments);
        }
    }

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    private void eldritchSurge$loadEnchantments(ValueInput input, CallbackInfo ci) {
        eldritchSurge$enchantments = input.read(ENCHANTMENTS_KEY, ItemEnchantments.CODEC)
                .orElse(ItemEnchantments.EMPTY);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        eldritchSurge$enchantments = components.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        super.collectImplicitComponents(builder);
        if (!eldritchSurge$enchantments.isEmpty()) {
            builder.set(DataComponents.ENCHANTMENTS, eldritchSurge$enchantments);
        }
    }
}
