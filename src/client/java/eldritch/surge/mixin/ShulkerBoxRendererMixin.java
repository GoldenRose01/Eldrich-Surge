package eldritch.surge.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import eldritch.surge.client.ShulkerGlintState;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.ShulkerBoxRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShulkerBoxRenderer.class)
public abstract class ShulkerBoxRendererMixin {
    @WrapOperation(
            method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;IIFLnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;Lnet/minecraft/client/resources/model/sprite/SpriteId;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;IIILnet/minecraft/client/resources/model/sprite/SpriteId;Lnet/minecraft/client/resources/model/sprite/SpriteGetter;I)V"
            )
    )
    private <S> void eldritchSurge$renderShulkerGlint(
            SubmitNodeCollector collector,
            Model<S> model,
            S state,
            PoseStack poseStack,
            int light,
            int overlay,
            int tintColor,
            SpriteId sprite,
            SpriteGetter sprites,
            int outlineColor,
            Operation<Void> original
    ) {
        original.call(collector, model, state, poseStack, light, overlay, tintColor, sprite, sprites, outlineColor);
        if (ShulkerGlintState.isActive()) {
            collector.order(1).submitModel(
                    model,
                    state,
                    poseStack,
                    RenderTypes.patternedShieldGlint(),
                    light,
                    overlay,
                    -1,
                    sprites.get(sprite),
                    0
            );
        }
    }
}
