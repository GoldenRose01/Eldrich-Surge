package eldritch.surge.mixin;

import eldritch.surge.config.EnchantmentCapsConfig;
import eldritch.surge.EldritchSurge;
import eldritch.surge.enchantment.mechanics.SpellBookItems;
import eldritch.surge.enchantment.mechanics.MobCategoryDamageMechanics;
import eldritch.surge.menu.EnchantingReagentStorage;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin {
    private static final Map<String, Identifier> VANILLA_REPLACEMENTS = Map.of(
            "minecraft:sharpness", Identifier.fromNamespaceAndPath("eldrich-surge", "katana"),
            "minecraft:smite", Identifier.fromNamespaceAndPath("eldrich-surge", "undead_slayer"),
            "minecraft:bane_of_arthropods", Identifier.fromNamespaceAndPath("eldrich-surge", "creeping_threat")
    );

    @Shadow
    @Final
    private ContainerLevelAccess access;

    @Shadow
    @Final
    private Container enchantSlots;

    @Shadow
    public abstract void slotsChanged(Container container);

    @Shadow
    public abstract int getEnchantmentSeed();

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("RETURN"))
    private void eldritchSurge$loadStoredReagent(int syncId, Inventory inventory, ContainerLevelAccess access, CallbackInfo ci) {
        access.evaluate((world, pos) -> {
            ItemStack stored = EnchantingReagentStorage.take(world, pos);
            if (!stored.isEmpty() && stored.is(Items.LAPIS_LAZULI)) {
                enchantSlots.setItem(1, stored);
                enchantSlots.setChanged();
                slotsChanged(enchantSlots);
            }
            return true;
        }, false);
    }

    @Inject(method = "removed", at = @At("HEAD"))
    private void eldritchSurge$storeReagentSlot(Player player, CallbackInfo ci) {
        access.evaluate((world, pos) -> {
            ItemStack reagent = enchantSlots.getItem(1);
            if (world.getBlockState(pos).is(Blocks.ENCHANTING_TABLE) && reagent.is(Items.LAPIS_LAZULI)) {
                EnchantingReagentStorage.store(world, pos, reagent);
                enchantSlots.setItem(1, ItemStack.EMPTY);
                enchantSlots.setChanged();
            } else {
                EnchantingReagentStorage.clear(world, pos);
            }
            return true;
        }, false);
    }

    @Inject(method = "getEnchantmentList", at = @At("RETURN"), cancellable = true)
    private void eldritchSurge$filterByTable(
            RegistryAccess registryAccess,
            ItemStack stack,
            int slot,
            int level,
            CallbackInfoReturnable<List<EnchantmentInstance>> cir
    ) {
        int categoryEnchantments = MobCategoryDamageMechanics.categoryComponentCount(stack);
        int seed = getEnchantmentSeed();
        List<EnchantmentInstance> filtered = new java.util.ArrayList<>();
        for (EnchantmentInstance original : cir.getReturnValue()) {
            EnchantmentInstance instance = replaceVanillaDamageEnchantments(registryAccess, original);
            boolean allowed = instance.enchantment().unwrapKey()
                    .map(key -> isAllowed(key.identifier(), stack, slot, level, seed))
                    .orElse(true);
            if (!allowed) continue;
            String enchantmentId = instance.enchantment().unwrapKey().map(key -> key.identifier().toString()).orElse("");
            if (MobCategoryDamageMechanics.isCategoryComponent(enchantmentId)) {
                if (categoryEnchantments >= 3) continue;
                categoryEnchantments++;
            }
            filtered.add(instance);
        }

        cir.setReturnValue(filtered);
    }

    private static EnchantmentInstance replaceVanillaDamageEnchantments(RegistryAccess registryAccess, EnchantmentInstance instance) {
        Identifier original = instance.enchantment().unwrapKey()
                .map(key -> key.identifier())
                .orElse(null);
        Identifier replacement = original == null ? null : VANILLA_REPLACEMENTS.get(original.toString());
        if (replacement == null) {
            return instance;
        }

        return registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(net.minecraft.resources.ResourceKey.create(Registries.ENCHANTMENT, replacement))
                .<EnchantmentInstance>map(enchantment -> new EnchantmentInstance((Holder<Enchantment>) enchantment, instance.level()))
                .orElse(instance);
    }

    private static boolean isAllowed(Identifier enchantmentId, ItemStack stack, int slot, int level, int seed) {
        if (isLootOnlyEnchantment(enchantmentId)) {
            return false;
        }
        if (MobCategoryDamageMechanics.isCategoryComponent(enchantmentId.toString())
                && MobCategoryDamageMechanics.categoryComponentCount(stack) >= 3) {
            return false;
        }

        if (SpellBookItems.isCastSpell(enchantmentId)) {
            return false;
        }

        return EnchantmentCapsConfig.isAllowedInNormalTable(enchantmentId)
                && EnchantmentCapsConfig.isAvailableInEnchLib(enchantmentId)
                && EnchantmentCapsConfig.isAllowedForItem(enchantmentId, stack)
                && EnchantmentCapsConfig.isCompatibleWithExistingEnchantments(enchantmentId, stack)
                && EnchantmentCapsConfig.passesRarity(enchantmentId, "minecraft:enchanting_table", stack, slot, level, seed);
    }

    private static boolean isLootOnlyEnchantment(Identifier enchantmentId) {
        return enchantmentId.equals(EldritchSurge.id("deathbreak"))
                || enchantmentId.equals(EldritchSurge.id("blade_of_apocalypse"))
                || enchantmentId.equals(EldritchSurge.id("star_fate"))
                || enchantmentId.equals(EldritchSurge.id("gream_reaper"));
    }
}
