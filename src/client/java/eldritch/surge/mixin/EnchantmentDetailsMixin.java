package eldritch.surge.mixin;

import eldritch.surge.client.tooltip.EnchantmentDetailsPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class EnchantmentDetailsMixin {
    @Shadow protected Slot hoveredSlot;
    @Unique private Slot eldrichSurge$hovered() { return hoveredSlot; }
    @Shadow protected int leftPos;
    @Shadow protected int topPos;
    @Shadow protected int imageWidth;
    @Unique private final EnchantmentDetailsPanel eldrichSurge$details = new EnchantmentDetailsPanel();
    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void eldrichSurge$details(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        var screen = (net.minecraft.client.gui.screens.Screen) (Object) this;
        if (screen == null) return;
        Slot hoveredSlot = eldrichSurge$hovered();
        if (hoveredSlot != null && hoveredSlot.hasItem()) eldrichSurge$details.show(hoveredSlot.getItem(), leftPos + imageWidth + 6, topPos, screen.width, screen.height);
        eldrichSurge$details.render(graphics, mouseX, mouseY, screen.width, screen.height);
    }
}
