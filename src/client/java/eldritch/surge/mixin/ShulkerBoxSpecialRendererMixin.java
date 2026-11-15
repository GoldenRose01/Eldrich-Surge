package eldritch.surge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import eldritch.surge.client.ShulkerGlintState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.ShulkerBoxRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.special.ShulkerBoxSpecialRenderer;
import net.minecraft.client.resources.model.sprite.SpriteId;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShulkerBoxSpecialRenderer.class)
public abstract class ShulkerBoxSpecialRendererMixin {
    @WrapOperation(
            method = "submit",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/blockentity/ShulkerBoxRenderer;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IIFLnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;Lnet/minecraft/client/resources/model/sprite/SpriteId;I)V"
            )
    )
    private void eldritchSurge$propagateGlint(
            ShulkerBoxRenderer renderer,
            PoseStack poseStack,
            SubmitNodeCollector collector,
            int light,
            int overlay,
            float openness,
            ModelFeatureRenderer.CrumblingOverlay crumblingOverlay,
            SpriteId sprite,
            int outlineColor,
            Operation<Void> original,
            PoseStack outerPoseStack,
            SubmitNodeCollector outerCollector,
            int outerLight,
            int outerOverlay,
            boolean hasFoil,
            int outerOutlineColor
    ) {
        if (!hasFoil) {
            original.call(renderer, poseStack, collector, light, overlay, openness, crumblingOverlay, sprite, outlineColor);
            return;
        }
        ShulkerGlintState.set(true);
        try {
            original.call(renderer, poseStack, collector, light, overlay, openness, crumblingOverlay, sprite, outlineColor);
        } finally {
            ShulkerGlintState.set(false);
        }
    }
}
