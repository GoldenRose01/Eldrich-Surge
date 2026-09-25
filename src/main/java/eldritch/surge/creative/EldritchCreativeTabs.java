package eldritch.surge.creative;

import eldritch.surge.EldritchSurge;
import eldritch.surge.block.EldritchBlocks;
import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinitions;
import eldritch.surge.enchantment.mechanics.SpellBookItems;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import eldritch.surge.item.EldritchItems;

import java.util.Comparator;
import java.util.Locale;
import java.util.Map;

public final class EldritchCreativeTabs {
    private static final Map<String, ModEnchantmentDefinition> DEFINITIONS = ModEnchantmentDefinitions.BY_ID;

    public static final ResourceKey<CreativeModeTab> MELEE_WEAPONS_KEY = key("melee_weapons");
    public static final ResourceKey<CreativeModeTab> TOOLS_KEY = key("tools");
    public static final ResourceKey<CreativeModeTab> RANGED_WEAPONS_KEY = key("ranged_weapons");
    public static final ResourceKey<CreativeModeTab> BOWS_KEY = key("bows");
    public static final ResourceKey<CreativeModeTab> ARMOR_KEY = key("armor");
    public static final ResourceKey<CreativeModeTab> EXTRA_KEY = key("extra");

    public static final CreativeModeTab MELEE_WEAPONS = register(
            MELEE_WEAPONS_KEY,
            "itemGroup.eldritch-surge.melee_weapons",
            Items.DIAMOND_SWORD,
            CreativeCategory.MELEE_WEAPONS
    );
    public static final CreativeModeTab TOOLS = register(
            TOOLS_KEY,
            "itemGroup.eldritch-surge.tools",
            Items.DIAMOND_PICKAXE,
            CreativeCategory.TOOLS
    );
    public static final CreativeModeTab RANGED_WEAPONS = register(
            RANGED_WEAPONS_KEY,
            "itemGroup.eldritch-surge.ranged_weapons",
            Items.TRIDENT,
            CreativeCategory.RANGED_WEAPONS
    );
    public static final CreativeModeTab BOWS = register(
            BOWS_KEY,
            "itemGroup.eldritch-surge.bows",
            Items.BOW,
            CreativeCategory.BOWS
    );
    public static final CreativeModeTab ARMOR = register(
            ARMOR_KEY,
            "itemGroup.eldritch-surge.armor",
            Items.DIAMOND_CHESTPLATE,
            CreativeCategory.ARMOR
    );
    public static final CreativeModeTab EXTRA = register(
            EXTRA_KEY,
            "itemGroup.eldritch-surge.extra",
            Items.ENCHANTED_BOOK,
            CreativeCategory.EXTRA
    );

    private EldritchCreativeTabs() {
    }

    public static void initialize() {
        EldritchSurge.LOGGER.debug("Registered Eldritch Surge creative tabs.");
    }

    private static ResourceKey<CreativeModeTab> key(String path) {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, EldritchSurge.id(path));
    }

    private static CreativeModeTab register(
            ResourceKey<CreativeModeTab> key,
            String titleKey,
            net.minecraft.world.item.Item icon,
            CreativeCategory category
    ) {
        return Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                key,
                FabricCreativeModeTab.builder()
                        .title(Component.translatable(titleKey))
                        .icon(() -> new ItemStack(icon))
                        .displayItems((parameters, output) -> {
                            if (category == CreativeCategory.EXTRA) {
                                output.accept(EldritchBlocks.ADVANCED_ENCHANTING_TABLE_ITEM);
                            }

                            parameters.holders()
                                    .lookupOrThrow(Registries.ENCHANTMENT)
                                    .listElements()
                                    .filter(enchantment -> belongsTo(enchantment, category))
                                    .sorted(Comparator.comparing(EldritchCreativeTabs::sortKey))
                                    .map(EldritchCreativeTabs::createMaxLevelBook)
                                    .forEach(output::accept);
                        })
                        .build()
        );
    }

    private static ItemStack createMaxLevelBook(Holder<Enchantment> enchantment) {
        ItemStack book = new ItemStack(SpellBookItems.isCastSpell(enchantment) ? EldritchItems.SPELL_BOOK : Items.ENCHANTED_BOOK);
        book.enchant(enchantment, enchantment.value().getMaxLevel());
        SpellBookItems.markIfSpellBook(book);
        return book;
    }

    private static String sortKey(Holder.Reference<Enchantment> enchantment) {
        return enchantment.unwrapKey()
                .map(key -> key.identifier().toString())
                .orElseGet(() -> enchantment.value().description().getString());
    }

    private static boolean belongsTo(Holder.Reference<Enchantment> enchantment, CreativeCategory category) {
        return enchantment.unwrapKey()
                .map(key -> {
                    if (!key.identifier().getNamespace().equals(EldritchSurge.MOD_ID)) {
                        return false;
                    }

                    ModEnchantmentDefinition definition = DEFINITIONS.get(key.identifier().getPath());
                    return definition != null && categoryOf(definition) == category;
                })
                .orElse(false);
    }

    private static CreativeCategory categoryOf(ModEnchantmentDefinition definition) {
        String itemCategory = normalized(definition.itemCategory());
        String supportedItems = normalized(String.join(" ", definition.supportedItems()));

        if (containsAny(itemCategory, "helmet", "chestplate", "leggings", "boots", "armor", "elytra", "shield") ||
                containsAny(supportedItems, "armor", "head_armor", "chest_armor", "leg_armor", "foot_armor", "elytra", "shield")) {
            return CreativeCategory.ARMOR;
        }

        if (containsAny(itemCategory, "bows", "crossbows") || containsAny(supportedItems, "bow", "crossbow")) {
            return CreativeCategory.BOWS;
        }

        if (containsAny(itemCategory, "mace", "spear") ||
                containsAny(supportedItems, "mace", "spear", "trident") ||
                containsAny(definition.id(), "launch", "inking", "neptunes_will", "sea_breeze")) {
            return CreativeCategory.RANGED_WEAPONS;
        }

        if (containsAny(itemCategory, "tools") ||
                containsAny(supportedItems, "pickaxe", "pickaxes", "shovel", "shovels", "hoe", "hoes", "mining", "shears")) {
            return CreativeCategory.TOOLS;
        }

        if (containsAny(itemCategory, "weapons") ||
                containsAny(supportedItems, "sword", "swords", "axe", "axes", "weapon", "melee")) {
            return CreativeCategory.MELEE_WEAPONS;
        }

        return CreativeCategory.EXTRA;
    }

    private static boolean containsAny(String value, String... tokens) {
        for (String token : tokens) {
            if (value.contains(token)) {
                return true;
            }
        }
        return false;
    }

    private static String normalized(String value) {
        return value.toLowerCase(Locale.ROOT);
    }

    private enum CreativeCategory {
        MELEE_WEAPONS,
        TOOLS,
        RANGED_WEAPONS,
        BOWS,
        ARMOR,
        EXTRA
    }
}
