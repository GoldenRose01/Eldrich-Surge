package eldritch.surge.creative;

import eldritch.surge.EldritchSurge;
import eldritch.surge.block.EldritchBlocks;
import eldritch.surge.config.EnchantmentCapsConfig;
import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinitions;
import eldritch.surge.enchantment.mechanics.SpellBookItems;
import eldritch.surge.item.EldritchItems;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class EldritchCreativeTabs {
    private static final Map<String, ModEnchantmentDefinition> DEFINITIONS = ModEnchantmentDefinitions.BY_ID;
    private static final Set<Identifier> UNIVERSAL_VANILLA_ENCHANTMENTS = Set.of(
            Identifier.withDefaultNamespace("vanishing"),
            Identifier.withDefaultNamespace("binding_curse"),
            Identifier.withDefaultNamespace("mending"),
            Identifier.withDefaultNamespace("unbreaking")
    );
    private static final List<ToolPage> TOOL_PAGES = List.of(
            page("swords", "swords", Items.NETHERITE_SWORD),
            page("axes", "axes", Items.NETHERITE_AXE),
            page("pickaxes", "pickaxes", Items.NETHERITE_PICKAXE),
            page("shovels", "shovels", Items.NETHERITE_SHOVEL),
            page("hoes", "hoes", Items.NETHERITE_HOE),
            page("bows", "bows", Items.BOW),
            page("crossbows", "crossbows", Items.CROSSBOW),
            page("spears", "spears", Items.NETHERITE_SPEAR),
            page("tridents", "tridents", Items.TRIDENT),
            page("maces", "maces", Items.MACE),
            page("fishing_rods", "fishing_rods", Items.FISHING_ROD),
            page("helmets", "helmets", Items.NETHERITE_HELMET),
            page("chestplates", "chestplates", Items.NETHERITE_CHESTPLATE),
            page("leggings", "leggings", Items.NETHERITE_LEGGINGS),
            page("boots", "boots", Items.NETHERITE_BOOTS),
            page("elytra", "elytra", Items.ELYTRA),
            page("shields", "shields", Items.SHIELD)
    );
    private static final ResourceKey<CreativeModeTab> EXTRA_KEY = key("extra");
    private static final List<CreativeModeTab> TOOL_TABS = TOOL_PAGES.stream()
            .map(EldritchCreativeTabs::registerToolPage).toList();
    public static final CreativeModeTab EXTRA = registerExtra();

    private EldritchCreativeTabs() {}

    public static void initialize() {
        CreativeModeTabEvents.MODIFY_OUTPUT_ALL.register((tab, output) -> {
            Identifier id = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab);
            if (id == null || !"minecraft".equals(id.getNamespace())) return;
            output.getDisplayStacks().removeIf(stack -> stack.is(Items.ENCHANTED_BOOK));
            output.getSearchTabStacks().removeIf(stack -> stack.is(Items.ENCHANTED_BOOK));
        });
        EldritchSurge.LOGGER.debug("Registered per-item Eldritch Surge creative tabs.");
    }

    private static ToolPage page(String id, String title, Item icon) {
        return new ToolPage(key(id), "itemGroup.eldrich-surge." + title, icon);
    }

    private static ResourceKey<CreativeModeTab> key(String path) {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, EldritchSurge.id(path));
    }

    private static CreativeModeTab registerToolPage(ToolPage page) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, page.key(),
                FabricCreativeModeTab.builder()
                        .title(Component.translatable(page.titleKey()))
                        .icon(() -> enchantedIcon(page.icon()))
                        .displayItems((parameters, output) -> {
                            ItemStack reference = new ItemStack(page.icon());
                            parameters.holders().lookupOrThrow(Registries.ENCHANTMENT)
                                    .listElements()
                                    .filter(enchantment -> belongsOnToolPage(enchantment, reference))
                                    .sorted(Comparator.comparing(EldritchCreativeTabs::sortKey)
                                            .thenComparing(EldritchCreativeTabs::idSortKey))
                                    .forEach(enchantment -> output.accept(createMaxLevelBook(enchantment),
                                            CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
                        })
                        .build());
    }

    private static CreativeModeTab registerExtra() {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, EXTRA_KEY,
                FabricCreativeModeTab.builder()
                        .title(Component.translatable("itemGroup.eldrich-surge.extra"))
                        .icon(() -> new ItemStack(Items.ENCHANTED_BOOK))
                        .displayItems((parameters, output) -> {
                            output.accept(EldritchBlocks.ADVANCED_ENCHANTING_TABLE_ITEM);
                            List<Holder.Reference<Enchantment>> enchantments = parameters.holders()
                                    .lookupOrThrow(Registries.ENCHANTMENT).listElements()
                                    .filter(EldritchCreativeTabs::belongsInExtra)
                                    .sorted(Comparator.comparing(EldritchCreativeTabs::sortKey)
                                            .thenComparing(EldritchCreativeTabs::idSortKey))
                                    .toList();
                            enchantments.stream().filter(SpellBookItems::isCastSpell)
                                    .forEach(enchantment -> output.accept(createMaxLevelBook(enchantment),
                                            CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
                            enchantments.stream().filter(enchantment -> !SpellBookItems.isCastSpell(enchantment))
                                    .forEach(enchantment -> output.accept(createMaxLevelBook(enchantment),
                                            CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS));
                        })
                        .build());
    }

    private static ItemStack enchantedIcon(Item item) {
        ItemStack stack = new ItemStack(item);
        stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        return stack;
    }

    private static ItemStack createMaxLevelBook(Holder<Enchantment> enchantment) {
        int maxLevel = maxCreativeLevel(enchantment);
        ItemStack book;
        if (SpellBookItems.isCastSpell(enchantment)) {
            book = new ItemStack(EldritchItems.SPELL_BOOK);
            book.enchant(enchantment, maxLevel);
        } else {
            book = EnchantmentHelper.createBook(new EnchantmentInstance(enchantment, maxLevel));
        }
        SpellBookItems.markIfSpellBook(book);
        return book;
    }

    private static int maxCreativeLevel(Holder<Enchantment> enchantment) {
        int registeredMaximum = Math.max(1, enchantment.value().getMaxLevel());
        return enchantment.unwrapKey()
                .map(key -> Math.max(1, EnchantmentCapsConfig.resolveAnvilCap(key.identifier(), registeredMaximum)))
                .orElse(registeredMaximum);
    }

    private static boolean belongsOnToolPage(Holder.Reference<Enchantment> enchantment, ItemStack reference) {
        if (!enchantment.value().isSupportedItem(reference)) return false;
        return enchantment.unwrapKey().map(key -> {
            if (!EldritchSurge.MOD_ID.equals(key.identifier().getNamespace())) {
                return !UNIVERSAL_VANILLA_ENCHANTMENTS.contains(key.identifier());
            }
            ModEnchantmentDefinition definition = DEFINITIONS.get(key.identifier().getPath());
            return definition != null && !definition.isCastSpell()
                    && CreativeEnchantmentClassifier.categoryOf(definition) != CreativeCategory.EXTRA;
        }).orElse(false);
    }

    private static boolean belongsInExtra(Holder.Reference<Enchantment> enchantment) {
        return enchantment.unwrapKey().map(key -> {
            if (!EldritchSurge.MOD_ID.equals(key.identifier().getNamespace())) {
                return UNIVERSAL_VANILLA_ENCHANTMENTS.contains(key.identifier());
            }
            ModEnchantmentDefinition definition = DEFINITIONS.get(key.identifier().getPath());
            return definition != null && (definition.isCastSpell()
                    || CreativeEnchantmentClassifier.categoryOf(definition) == CreativeCategory.EXTRA);
        }).orElse(false);
    }

    private static String sortKey(Holder<Enchantment> enchantment) {
        return enchantment.value().description().getString().toLowerCase(Locale.ROOT);
    }

    private static String idSortKey(Holder<Enchantment> enchantment) {
        return enchantment.unwrapKey().map(key -> key.identifier().toString())
                .orElseGet(() -> enchantment.value().description().getString());
    }

    private record ToolPage(ResourceKey<CreativeModeTab> key, String titleKey, Item icon) {}
}
