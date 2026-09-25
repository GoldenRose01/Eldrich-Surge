package eldritch.surge.mixin;

import eldritch.surge.client.ClearmindZoom;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class ClearmindCameraMixin {
    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void eldritchSurge$zoomWhenStillAiming(CallbackInfoReturnable<Float> cir) {
        if (ClearmindZoom.isZoomActive()) {
            cir.setReturnValue(cir.getReturnValue() * 0.5F);
        }
    }
}
