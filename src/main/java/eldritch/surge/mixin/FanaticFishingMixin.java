package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import eldritch.surge.enchantment.mechanics.SurvivalEnchantmentMath;
import eldritch.surge.enchantment.mechanics.SurvivalEnchantmentMechanics;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingHook.class)
public abstract class FanaticFishingMixin {
    @Shadow
    private int timeUntilLured;

    @Shadow
    private int timeUntilHooked;

    @Inject(method = "catchingFish", at = @At("TAIL"))
    private void eldrichSurge$fishermanFastCatch(BlockPos pos, CallbackInfo ci) {
        FishingHook hook = (FishingHook) (Object) this;
        if (!(hook.level() instanceof ServerLevel level)) {
            return;
        }
        Player owner = hook.getPlayerOwner();
        if (owner == null) {
            return;
        }
        boolean hasFisherman = EnchantmentLevels.onItem(level, owner.getMainHandItem(), "fisherman") > 0
                || EnchantmentLevels.onItem(level, owner.getOffhandItem(), "fisherman") > 0;
        if (!hasFisherman) {
            return;
        }
        if (this.timeUntilLured > 80) {
            this.timeUntilLured = 80;
        }
        if (this.timeUntilHooked > 20) {
            this.timeUntilHooked = 20;
        }
    }
    // This invocation only occurs for a completed catch, never an empty cast or hooked entity.
    @Redirect(method = "retrieve", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/LootTable;getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;"))
    private ObjectArrayList<ItemStack> eldrichSurge$treasure(LootTable original, LootParams params, ItemStack rod) {
        FishingHook hook = (FishingHook) (Object) this;
        if (EnchantmentLevels.onItem(params.getLevel(), rod, "fanatic") <= 0) return original.getRandomItems(params);
        int score = SurvivalEnchantmentMechanics.completeCatch(rod);
        // Vanilla fishing weights include Luck of the Sea and the player's luck attribute.
        double fish = Math.max(0, Math.floor(85 - params.getLuck()));
        double junk = Math.max(0, Math.floor(10 - 2 * params.getLuck()));
        double treasure = hook.isOpenWaterFishing() ? Math.max(0, Math.floor(5 + 2 * params.getLuck())) : 0;
        double total = fish + junk + treasure;
        double base = total > 0 ? treasure / total : 0;
        if (hook.getRandom().nextDouble() < SurvivalEnchantmentMath.treasureRerollChance(score, base)) {
            return params.getLevel().getServer().reloadableRegistries().getLootTable(BuiltInLootTables.FISHING_TREASURE).getRandomItems(params);
        }
        return original.getRandomItems(params);
    }
}
