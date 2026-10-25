package eldritch.surge.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public abstract class ExtendedArmorHudMixin {
    @Shadow @Final private static Identifier ARMOR_EMPTY_SPRITE;
    @Shadow @Final private static Identifier ARMOR_HALF_SPRITE;
    @Shadow @Final private static Identifier ARMOR_FULL_SPRITE;

    @Inject(method = "extractArmor", at = @At("RETURN"))
    private static void eldrichSurge$drawArmorOverflow(
            GuiGraphicsExtractor graphics,
            Player player,
            int screenWidth,
            int rowCount,
            int spacing,
            int y,
            CallbackInfo ci
    ) {
        int overflow = Math.min(20, player.getArmorValue() - 20);
        if (overflow <= 0) return;

        int left = screenWidth - (rowCount - 1) * spacing - 10;
        int overflowY = y - 10;
        for (int icon = 0; icon < 10; icon++) {
            int points = overflow - icon * 2;
            Identifier sprite = points >= 2 ? ARMOR_FULL_SPRITE
                    : points == 1 ? ARMOR_HALF_SPRITE : ARMOR_EMPTY_SPRITE;
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite,
                    left + icon * spacing, overflowY, 9, 9);
        }
    }
}
