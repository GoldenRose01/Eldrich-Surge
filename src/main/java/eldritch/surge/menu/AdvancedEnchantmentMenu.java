package eldritch.surge.menu;

import eldritch.surge.EldritchSurge;
import eldritch.surge.block.EldritchBlocks;
import eldritch.surge.config.EnchantmentCapsConfig;
import eldritch.surge.enchantment.mechanics.*;
import eldritch.surge.item.EldritchItems;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.*;
import net.minecraft.stats.Stats;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import java.util.*;

/** Deterministic infusing: offers and prices are computed and validated on the server. */
public final class AdvancedEnchantmentMenu extends AbstractContainerMenu implements AdvancedEnchantingMenuMarker {
    public static final int CAPACITY = 512;
    private final ContainerLevelAccess access;
    private final Player owner;
    private final SimpleContainer inputs = new SimpleContainer(2) {
        @Override public void setChanged() { super.setChanged(); slotsChanged(this); }
    };
    // count, ritual, selected registry id + 1, target level, XP price, reagent price
    private final int[] state = new int[6];
    private final int[] ids = new int[CAPACITY];
    private final int[] maxima = new int[CAPACITY];
    private final int[] existing = new int[CAPACITY];
    private boolean applying;

    public AdvancedEnchantmentMenu(int id, Inventory inventory, BlockPos pos) {
        this(id, inventory, ContainerLevelAccess.NULL);
    }

    public AdvancedEnchantmentMenu(int id, Inventory inventory, ContainerLevelAccess access) {
        super(EldritchMenus.ADVANCED_ENCHANTING_MENU, id);
        this.access = access;
        this.owner = inventory.player;
        addSlot(new Slot(inputs, 0, 15, 40) { @Override public int getMaxStackSize() { return 1; } });
        addSlot(new Slot(inputs, 1, 15, 76) {
            @Override public boolean mayPlace(ItemStack stack) { return isReagent(stack); }
            @Override public Identifier getNoItemIcon() { return Identifier.withDefaultNamespace("container/slot/amethyst_shard"); }
        });
        addStandardInventorySlots(inventory, 47, 153);
        for (int i = 0; i < state.length; i++) addDataSlot(DataSlot.shared(state, i));
        for (int i = 0; i < CAPACITY; i++) {
            addDataSlot(DataSlot.shared(ids, i));
            addDataSlot(DataSlot.shared(maxima, i));
            addDataSlot(DataSlot.shared(existing, i));
        }
        access.execute((world, pos) -> inputs.setItem(1, EnchantingReagentStorage.take(world, pos)));
    }

    private static boolean isReagent(ItemStack stack) { return stack.is(Items.AMETHYST_SHARD) || stack.is(Items.ECHO_SHARD); }
    public int offerCount() { return state[0]; }
    public boolean hasRitual() { return state[1] == 1; }
    public int selectedId() { return state[2] - 1; }
    public int targetLevel() { return state[3]; }
    public int xpCost() { return state[4]; }
    public int reagentCost() { return state[5]; }
    public int offerId(int index) { return ids[index] - 1; }
    public int maxLevel(int index) { return maxima[index]; }
    public int currentLevel(int index) { return existing[index]; }
    public Holder<Enchantment> enchantment(int id) {
        return owner.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).asHolderIdMap().byId(id);
    }

    @Override public void slotsChanged(Container container) {
        if (access == null || applying) return;
        state[2] = 0;
        state[3] = 0;
        refresh();
    }

    private void refresh() {
        access.execute((world, pos) -> {
            state[1] = AdvancedEnchantingFormation.isValid(world, pos) ? 1 : 0;
            ItemStack stack = inputs.getItem(0);
            var registry = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
            var map = registry.asHolderIdMap();
            var offers = registry.listElements().filter(h -> allowed(h, stack))
                    .sorted(Comparator.comparing(h -> h.key().identifier().toString())).limit(CAPACITY).toList();
            state[0] = offers.size();
            boolean selectedFound = false;
            for (int i = 0; i < CAPACITY; i++) {
                if (i >= offers.size()) { ids[i] = maxima[i] = existing[i] = 0; continue; }
                var h = offers.get(i);
                ids[i] = map.getId(h) + 1;
                int cap = EnchantmentCapsConfig.resolveEnchantingTableCap(h.key().identifier(), h.value().getMaxLevel());
                maxima[i] = Math.max(1, Math.min(255, hasRitual() ? cap : 1));
                existing[i] = EnchantmentHelper.getEnchantmentsForCrafting(stack).getLevel(h);
                if (ids[i] == state[2] && existing[i] < maxima[i]) {
                    selectedFound = true;
                    state[3] = Math.clamp(state[3], existing[i] + 1, maxima[i]);
                }
            }
            if (!selectedFound) { state[2] = 0; state[3] = 0; }
            state[4] = 0; state[5] = 0;
            for (int i = 0; i < state[0]; i++) if (ids[i] == state[2]) {
                var enchantment = enchantment(offerId(i));
                int cap = EnchantmentCapsConfig.resolveEnchantingTableCap(enchantment.unwrapKey().orElseThrow().identifier(), enchantment.value().getMaxLevel());
                state[4] = AdvancedInfusingCosts.experience(state[3] - existing[i], cap);
                state[5] = AdvancedInfusingCosts.reagents(state[3] - existing[i], cap);
            }
        });
    }

    private boolean allowed(Holder<Enchantment> holder, ItemStack stack) {
        if (stack.isEmpty()) return false;
        Identifier id = holder.unwrapKey().orElseThrow().identifier();
        String path = id.getPath();
        if (id.getNamespace().equals("eldrich-surge") && Set.of("deathbreak", "blade_of_apocalypse", "star_fate", "gream_reaper").contains(path)) return false;
        if (Set.of("minecraft:sharpness", "minecraft:smite", "minecraft:bane_of_arthropods").contains(id.toString())) return false;
        boolean spell = SpellBookItems.isCastSpell(id);
        boolean echo = inputs.getItem(1).is(Items.ECHO_SHARD);
        if (spell != echo || (spell && !stack.is(Items.BOOK))) return false;
        if (!EnchantmentCapsConfig.isAvailableInEnchLib(id) || !EnchantmentCapsConfig.isAllowedInAdvancedTable(id)) return false;
        boolean book = stack.is(Items.BOOK) || stack.is(Items.ENCHANTED_BOOK);
        if (!spell && !book && (!holder.value().isSupportedItem(stack) || !EnchantmentCapsConfig.isAllowedForItem(id, stack))) return false;
        var enchants = EnchantmentHelper.getEnchantmentsForCrafting(stack);
        for (var other : enchants.keySet()) {
            if (!other.equals(holder) && (!Enchantment.areCompatible(other, holder)
                    || !EnchantmentCapsConfig.isCompatibleWithExistingEnchantments(id, stack))) return false;
        }
        if (MobCategoryDamageMechanics.isCategoryComponent(id.toString()) && enchants.getLevel(holder) == 0
                && MobCategoryDamageMechanics.categoryComponentCount(stack) >= 3) return false;
        int configuredCap = EnchantmentCapsConfig.resolveEnchantingTableCap(id, holder.value().getMaxLevel());
        if (configuredCap < 1) return false;
        int cap = Math.min(255, hasRitual() ? configuredCap : 1);
        return enchants.getLevel(holder) < cap;
    }

    @Override public void broadcastChanges() {
        if (!applying) refresh();
        super.broadcastChanges();
    }

    @Override public boolean clickMenuButton(Player player, int button) {
        if (!stillValid(player)) return false;
        refresh();
        if (button >= 100) {
            int chosen = button - 100;
            for (int i = 0; i < state[0]; i++) if (offerId(i) == chosen) {
                state[2] = chosen + 1; state[3] = existing[i] + 1; refresh(); broadcastChanges(); return true;
            }
            return false;
        }
        if (state[2] == 0) return false;
        if (button == 1 || button == 2) {
            state[3] += button == 1 ? -1 : 1;
            refresh(); broadcastChanges(); return true;
        }
        if (button != 3 || xpCost() < 1) return false;
        ItemStack reagent = inputs.getItem(1);
        if (!player.hasInfiniteMaterials() && (!isReagent(reagent) || reagent.getCount() < reagentCost() || player.experienceLevel < xpCost())) return false;
        return access.evaluate((world, pos) -> {
            Holder<Enchantment> enchantment = enchantment(selectedId());
            if (enchantment == null || !allowed(enchantment, inputs.getItem(0))) return false;
            applying = true;
            try {
                ItemStack result = inputs.getItem(0).copy();
                int price = xpCost();
                if (result.is(Items.BOOK)) result = result.transmuteCopy(SpellBookItems.isCastSpell(enchantment) ? EldritchItems.SPELL_BOOK : Items.ENCHANTED_BOOK);
                result.enchant(enchantment, targetLevel());
                SpellBookItems.markIfSpellBook(result);
                player.onEnchantmentPerformed(result, price);
                reagent.consume(reagentCost(), player);
                inputs.setItem(0, result);
                player.awardStat(Stats.ENCHANT_ITEM);
                if (player instanceof ServerPlayer serverPlayer) CriteriaTriggers.ENCHANTED_ITEM.trigger(serverPlayer, result, price);
                world.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1, 1);
                state[2] = state[3] = 0;
            } finally { applying = false; }
            refresh(); broadcastChanges(); return true;
        }, false);
    }

    @Override public boolean stillValid(Player player) { return stillValid(access, player, EldritchBlocks.ADVANCED_ENCHANTING_TABLE); }
    @Override public void removed(Player player) {
        super.removed(player);
        access.execute((world, pos) -> {
            if (world.getBlockState(pos).is(EldritchBlocks.ADVANCED_ENCHANTING_TABLE) && isReagent(inputs.getItem(1))) {
                EnchantingReagentStorage.store(world, pos, inputs.getItem(1));
                inputs.setItem(1, ItemStack.EMPTY);
            }
            clearContainer(player, inputs);
        });
    }
    @Override public ItemStack quickMoveStack(Player player, int index) {
        if (index < 0 || index >= slots.size()) return ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();
        if (index < 2) {
            if (!moveItemStackTo(stack, 2, slots.size(), true)) return ItemStack.EMPTY;
        } else if (isReagent(stack)) {
            if (!moveItemStackTo(stack, 1, 2, false)) return ItemStack.EMPTY;
        } else if (!moveItemStackTo(stack, 0, 1, false)) return ItemStack.EMPTY;
        if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY); else slot.setChanged();
        if (stack.getCount() == original.getCount()) return ItemStack.EMPTY;
        slot.onTake(player, stack);
        return original;
    }
}
