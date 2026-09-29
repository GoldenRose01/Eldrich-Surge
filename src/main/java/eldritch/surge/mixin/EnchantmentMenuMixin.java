package eldritch.surge.mixin;

import eldritch.surge.block.EldritchBlocks;
import eldritch.surge.config.EnchantmentCapsConfig;
import eldritch.surge.EldritchSurge;
import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinitions;
import eldritch.surge.enchantment.mechanics.AdvancedEnchantingFormation;
import eldritch.surge.enchantment.mechanics.SpellBookItems;
import eldritch.surge.enchantment.mechanics.MobCategoryDamageMechanics;
import eldritch.surge.menu.AdvancedEnchantingMenuMarker;
import eldritch.surge.menu.EnchantingReagentStorage;
import eldritch.surge.item.EldritchItems;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.RandomSource;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.Level;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin {
    @Unique
    private boolean eldritchSurge$spellBookOutput;
    private static final Map<String, Identifier> VANILLA_REPLACEMENTS = Map.of(
            "minecraft:sharpness", Identifier.fromNamespaceAndPath("eldritch-surge", "katana"),
            "minecraft:smite", Identifier.fromNamespaceAndPath("eldritch-surge", "undead_slayer"),
            "minecraft:bane_of_arthropods", Identifier.fromNamespaceAndPath("eldritch-surge", "creeping_threat")
    );

    @Shadow
    @Final
    private ContainerLevelAccess access;

    @Shadow
    @Final
    private Container enchantSlots;

    @WrapOperation(
            method = "lambda$slotsChanged$0",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getEnchantmentCost(Lnet/minecraft/util/RandomSource;IILnet/minecraft/world/item/ItemStack;)I")
    )
    private int eldritchSurge$useQuartzFormationForAdvancedOffers(
            RandomSource random, int slot, int bookshelfCount, ItemStack stack, Operation<Integer> original
    ) {
        boolean advancedTable = access.evaluate(
                (level, pos) -> level.getBlockState(pos).is(EldritchBlocks.ADVANCED_ENCHANTING_TABLE),
                false
        );
        if (!advancedTable) {
            return original.call(random, slot, bookshelfCount, stack);
        }

        if (slot == 0) {
            return original.call(random, slot, 0, stack);
        }

        boolean hasFormation = access.evaluate(AdvancedEnchantingFormation::isValid, false);
        if (!hasFormation) {
            return 0;
        }

        return slot == 1 ? 15 : slot == 2 ? 30 : 0;
    }

    @Shadow
    public abstract void slotsChanged(Container container);

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At("RETURN"))
    private void eldritchSurge$loadStoredReagent(int syncId, Inventory inventory, ContainerLevelAccess access, CallbackInfo ci) {
        access.evaluate((world, pos) -> {
            ItemStack stored = EnchantingReagentStorage.take(world, pos);
            if (!stored.isEmpty() && isValidStoredReagent(stored, world.getBlockState(pos).is(EldritchBlocks.ADVANCED_ENCHANTING_TABLE))) {
                enchantSlots.setItem(1, stored);
                enchantSlots.setChanged();
                slotsChanged(enchantSlots);
            }
            return true;
        }, false);
    }

    @WrapOperation(
            method = "*",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z")
    )
    private boolean eldritchSurge$useAmethystInAdvancedTable(ItemStack stack, Object item, Operation<Boolean> original) {
        if ((Object) this instanceof AdvancedEnchantingMenuMarker && item == Items.LAPIS_LAZULI) {
            return original.call(stack, Items.AMETHYST_SHARD) || original.call(stack, Items.ECHO_SHARD);
        }

        return original.call(stack, item);
    }

    @WrapOperation(
            method = "*",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;enchant(Lnet/minecraft/core/Holder;I)V")
    )
    private void eldritchSurge$markSpellBooks(ItemStack stack, Holder<Enchantment> enchantment, int level, Operation<Void> original) {
        original.call(stack, enchantment, level);
        SpellBookItems.markIfSpellBook(stack);
    }

    @WrapOperation(
            method = "*",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;transmuteCopy(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;")
    )
    private ItemStack eldritchSurge$createSpellBookForRitual(ItemStack stack, ItemLike item, Operation<ItemStack> original) {
        return original.call(stack, eldritchSurge$spellBookOutput ? EldritchItems.SPELL_BOOK : item);
    }

    @Inject(method = "removed", at = @At("HEAD"))
    private void eldritchSurge$storeReagentSlot(Player player, CallbackInfo ci) {
        access.evaluate((world, pos) -> {
            boolean advancedTable = world.getBlockState(pos).is(EldritchBlocks.ADVANCED_ENCHANTING_TABLE);
            boolean normalTable = world.getBlockState(pos).is(Blocks.ENCHANTING_TABLE);
            ItemStack reagent = enchantSlots.getItem(1);

            if ((advancedTable || normalTable) && isValidStoredReagent(reagent, advancedTable)) {
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
        boolean advancedTable = access.evaluate(
                (world, pos) -> world.getBlockState(pos).is(EldritchBlocks.ADVANCED_ENCHANTING_TABLE),
                false
        );
        boolean echoShardMode = advancedTable && enchantSlots.getItem(1).is(Items.ECHO_SHARD);

        int categoryEnchantments = MobCategoryDamageMechanics.categoryComponentCount(stack);
        List<EnchantmentInstance> filtered = new java.util.ArrayList<>();
        for (EnchantmentInstance original : cir.getReturnValue()) {
            EnchantmentInstance instance = replaceVanillaDamageEnchantments(registryAccess, original);
            boolean allowed = instance.enchantment().unwrapKey()
                    .map(key -> isAllowed(key.identifier(), advancedTable, echoShardMode, stack, slot, level))
                    .orElse(true);
            if (!allowed) continue;
            String enchantmentId = instance.enchantment().unwrapKey().map(key -> key.identifier().toString()).orElse("");
            if (MobCategoryDamageMechanics.isCategoryComponent(enchantmentId)) {
                if (categoryEnchantments >= 3) continue;
                categoryEnchantments++;
            }
            filtered.add(instance);
        }

        if (advancedTable && filtered.isEmpty()) {
            filtered = advancedTablePreset(registryAccess, stack, slot, level);
        }

        eldritchSurge$spellBookOutput = filtered.stream().anyMatch(instance -> SpellBookItems.isCastSpell(instance.enchantment()));

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

    private List<EnchantmentInstance> advancedTablePreset(RegistryAccess registryAccess, ItemStack stack, int slot, int level) {
        boolean echoShardMode = enchantSlots.getItem(1).is(Items.ECHO_SHARD);
        List<EnchantmentInstance> candidates = ModEnchantmentDefinitions.ALL.stream()
                .filter(ModEnchantmentDefinition::advancedTableDefault)
                .filter(definition -> isAllowed(EldritchSurge.id(definition.id()), true, echoShardMode, stack, slot, level))
                .map(definition -> presetInstance(registryAccess, definition, slot))
                .filter(Objects::nonNull)
                .toList();

        if (candidates.isEmpty()) {
            return List.of();
        }

        int index = Math.floorMod((stack.getItem() + "|" + slot + "|" + level).hashCode(), candidates.size());
        return List.of(candidates.get(index));
    }

    private static EnchantmentInstance presetInstance(RegistryAccess registryAccess, ModEnchantmentDefinition definition, int slot) {
        ResourceKey<Enchantment> key = ResourceKey.create(Registries.ENCHANTMENT, EldritchSurge.id(definition.id()));
        return registryAccess.lookupOrThrow(Registries.ENCHANTMENT)
                .get(key)
                .<EnchantmentInstance>map(enchantment -> new EnchantmentInstance(enchantment, presetLevel(definition.maxLevel(), slot)))
                .orElse(null);
    }

    private static int presetLevel(int maxLevel, int slot) {
        if (slot <= 0) {
            return 1;
        }
        if (slot == 1) {
            return Math.max(1, Math.min(maxLevel, (maxLevel + 1) / 2));
        }
        return Math.max(1, maxLevel);
    }

    private static boolean isAllowed(Identifier enchantmentId, boolean advancedTable, boolean echoShardMode, ItemStack stack, int slot, int level) {
        if (isLootOnlyEnchantment(enchantmentId)) {
            return false;
        }
        if (MobCategoryDamageMechanics.isCategoryComponent(enchantmentId.toString())
                && MobCategoryDamageMechanics.categoryComponentCount(stack) >= 3) {
            return false;
        }

        boolean castSpell = SpellBookItems.isCastSpell(enchantmentId);
        if (castSpell && (!advancedTable || !echoShardMode || !stack.is(Items.BOOK))) {
            return false;
        }
        if (!castSpell && advancedTable && echoShardMode) {
            return false;
        }
        if (castSpell) {
            return EnchantmentCapsConfig.isAvailableInEnchLib(enchantmentId)
                    && EnchantmentCapsConfig.isAllowedInAdvancedTable(enchantmentId)
                    && EnchantmentCapsConfig.passesRarity(enchantmentId, "eldritch-surge:advanced_enchanting_table", stack, slot, level);
        }

        boolean tableAllowed = advancedTable
                ? EnchantmentCapsConfig.isAllowedInAdvancedTable(enchantmentId)
                : EnchantmentCapsConfig.isAllowedInNormalTable(enchantmentId);

        return tableAllowed
                && EnchantmentCapsConfig.isAvailableInEnchLib(enchantmentId)
                && EnchantmentCapsConfig.isAllowedForItem(enchantmentId, stack)
                && EnchantmentCapsConfig.isCompatibleWithExistingEnchantments(enchantmentId, stack)
                && EnchantmentCapsConfig.passesRarity(enchantmentId, advancedTable ? "eldritch-surge:advanced_enchanting_table" : "minecraft:enchanting_table", stack, slot, level);
    }

    private static boolean isLootOnlyEnchantment(Identifier enchantmentId) {
        return enchantmentId.equals(EldritchSurge.id("deathbreak"))
                || enchantmentId.equals(EldritchSurge.id("blade_of_apocalypse"))
                || enchantmentId.equals(EldritchSurge.id("star_fate"))
                || enchantmentId.equals(EldritchSurge.id("gream_reaper"));
    }

    private static boolean isValidStoredReagent(ItemStack stack, boolean advancedTable) {
        return advancedTable ? stack.is(Items.AMETHYST_SHARD) || stack.is(Items.ECHO_SHARD) : stack.is(Items.LAPIS_LAZULI);
    }
}
