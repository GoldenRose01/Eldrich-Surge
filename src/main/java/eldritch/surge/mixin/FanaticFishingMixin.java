package eldritch.surge.mixin;

import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import eldritch.surge.enchantment.mechanics.SurvivalEnchantmentMath;
import eldritch.surge.enchantment.mechanics.SurvivalEnchantmentMechanics;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FishingHook.class)
public abstract class FanaticFishingMixin {
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
