package eldritch.surge.mixin;

import eldritch.surge.EldritchSurge;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentNameColorMixin {
    @Inject(method = "getFullname", at = @At("RETURN"), cancellable = true)
    private static void eldrichSurge$colorSpecialEnchantments(
            Holder<Enchantment> enchantment,
            int level,
            CallbackInfoReturnable<Component> cir
    ) {
        String path = enchantment.unwrapKey()
                .filter(key -> key.identifier().getNamespace().equals(EldritchSurge.MOD_ID))
                .map(key -> key.identifier().getPath())
                .orElseGet(() -> enchantment.unwrapKey()
                        .filter(key -> key.identifier().getNamespace().equals("minecraft"))
                        .map(key -> key.identifier().getPath())
                        .orElse(""));

        if (path.equals("curse_of_fragility") || path.equals("curse_of_the_forest")) {
            cir.setReturnValue(cir.getReturnValue().copy().withStyle(ChatFormatting.RED));
        } else if (path.equals("deathbreak") || path.equals("enduring") || path.equals("soulbond")
                || path.equals("welding") || (enchantment.unwrapKey().map(key ->
                key.identifier().equals(net.minecraft.resources.Identifier.withDefaultNamespace("mending"))).orElse(false))) {
            cir.setReturnValue(cir.getReturnValue().copy().withStyle(ChatFormatting.GOLD));
        }
    }
}
