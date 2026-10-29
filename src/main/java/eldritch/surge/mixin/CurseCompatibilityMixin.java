package eldritch.surge.mixin;

import eldritch.surge.EldritchSurge;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class CurseCompatibilityMixin {
    @Inject(method = "areCompatible", at = @At("HEAD"), cancellable = true)
    private static void eldrichSurge$curseCompatibility(Holder<Enchantment> first, Holder<Enchantment> second,
                                                       CallbackInfoReturnable<Boolean> cir) {
        Identifier a = first.unwrapKey().map(key -> key.identifier()).orElse(null);
        Identifier b = second.unwrapKey().map(key -> key.identifier()).orElse(null);
        Identifier incompatible = EldritchSurge.id("curse_of_incompatibility");
        Identifier fatigue = EldritchSurge.id("curse_of_fatigue");
        Identifier efficiency = Identifier.withDefaultNamespace("efficiency");
        if (incompatible.equals(a) || incompatible.equals(b)
                || (fatigue.equals(a) && efficiency.equals(b)) || (fatigue.equals(b) && efficiency.equals(a))) {
            cir.setReturnValue(false);
        }
    }
}
