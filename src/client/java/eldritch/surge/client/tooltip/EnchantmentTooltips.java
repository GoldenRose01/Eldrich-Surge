package eldritch.surge.client.tooltip;

import eldritch.surge.config.EnchantmentCapsConfig;
import eldritch.surge.item.EldritchItems;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class EnchantmentTooltips {
    private static final String PREFIX = "tooltip.eldrich-surge.enchantment.";
    private static final Map<String, CompatibilityCache> COMPATIBILITY_CACHE = new java.util.concurrent.ConcurrentHashMap<>();
    private static final List<TooltipPage> CREATIVE_PAGES = List.of(
            new TooltipPage("swords", Items.NETHERITE_SWORD),
            new TooltipPage("axes", Items.NETHERITE_AXE),
            new TooltipPage("pickaxes", Items.NETHERITE_PICKAXE),
            new TooltipPage("shovels", Items.NETHERITE_SHOVEL),
            new TooltipPage("hoes", Items.NETHERITE_HOE),
            new TooltipPage("bows", Items.BOW),
            new TooltipPage("crossbows", Items.CROSSBOW),
            new TooltipPage("spears", Items.NETHERITE_SPEAR),
            new TooltipPage("tridents", Items.TRIDENT),
            new TooltipPage("maces", Items.MACE),
            new TooltipPage("fishing_rods", Items.FISHING_ROD),
            new TooltipPage("helmets", Items.NETHERITE_HELMET),
            new TooltipPage("chestplates", Items.NETHERITE_CHESTPLATE),
            new TooltipPage("leggings", Items.NETHERITE_LEGGINGS),
            new TooltipPage("boots", Items.NETHERITE_BOOTS),
            new TooltipPage("elytra", Items.ELYTRA),
            new TooltipPage("shields", Items.SHIELD)
    );

    private EnchantmentTooltips() {
    }

    public static void initialize() {
        ItemTooltipCallback.EVENT.register(EnchantmentTooltips::appendEnchantmentTooltips);
    }

    private static void appendEnchantmentTooltips(ItemStack stack, Item.TooltipContext context, TooltipFlag type, List<Component> lines) {
        Set<String> addedKeys = new HashSet<>();
        ItemEnchantments direct = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        ItemEnchantments stored = stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
        appendFrom(direct, lines, addedKeys);
        appendFrom(stored, lines, addedKeys);
        if (stack.is(Items.ENCHANTED_BOOK) || stack.is(EldritchItems.SPELL_BOOK)) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null && !minecraft.player.getAbilities().instabuild) {
                appendSurvivalCreativeTabs(direct, lines);
                appendSurvivalCreativeTabs(stored, lines);
            }
        }
    }

    private static void appendSurvivalCreativeTabs(ItemEnchantments enchantments, List<Component> lines) {
        Set<String> added = new HashSet<>();
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
            for (TooltipPage page : CREATIVE_PAGES) {
                if (entry.getKey().value().isSupportedItem(new ItemStack(page.icon()))
                        && added.add(page.id())) {
                    lines.add(Component.translatable("itemGroup.eldrich-surge." + page.id())
                            .withStyle(ChatFormatting.BLUE));
                }
            }
        }
    }

    private static void appendCompatibility(ItemEnchantments enchantments, List<Component> lines, Set<String> added) {
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
            Identifier id = entry.getKey().unwrapKey().map(key -> key.identifier()).orElse(null);
            if (id == null || !added.add(id.toString())) continue;

            List<CompatibilityItem> compatible = compatibleItems(id, entry.getKey());
            if (compatible.isEmpty()) continue;

            MutableComponent line = Component.translatable("tooltip.eldrich-surge.book_tag.compatibility")
                    .append(Component.literal(" "))
                    .append(Component.translatable("enchantment." + id.getNamespace() + "." + id.getPath()))
                    .append(Component.literal(": "));
            for (int index = 0; index < compatible.size(); index++) {
                if (index > 0) line = line.append(Component.literal(", "));
                CompatibilityItem item = compatible.get(index);
                line = line.append(Component.literal(item.emoji() + " ")).append(item.label());
            }
            lines.add(line.withStyle(ChatFormatting.GRAY));
        }
    }

    private static List<CompatibilityItem> compatibleItems(Identifier enchantmentId, Holder<Enchantment> enchantment) {
        List<String> rules = EnchantmentCapsConfig.supportedItemRules(enchantmentId);
        CompatibilityCache cached = COMPATIBILITY_CACHE.get(enchantmentId.toString());
        if (cached != null && cached.rules().equals(rules)) return cached.items();

        Map<String, CompatibilityItem> matches = new LinkedHashMap<>();
        if (rules.stream().anyMatch(rule -> rule.equalsIgnoreCase("all") || rule.equalsIgnoreCase("all_items"))) {
            CompatibilityItem universal = grouped("universal", "✨");
            List<CompatibilityItem> result = List.of(universal);
            COMPATIBILITY_CACHE.put(enchantmentId.toString(), new CompatibilityCache(rules, result));
            return result;
        }
        for (Item item : BuiltInRegistries.ITEM) {
            ItemStack stack = new ItemStack(item);
            boolean allowed = rules.isEmpty()
                    ? enchantment.value().isSupportedItem(stack)
                    : EnchantmentCapsConfig.isAllowedForItem(enchantmentId, stack);
            if (!allowed) continue;

            CompatibilityItem group = classify(stack, rules);
            String key = group.key();
            matches.putIfAbsent(key, group);
        }

        List<CompatibilityItem> result = List.copyOf(matches.values());
        COMPATIBILITY_CACHE.put(enchantmentId.toString(), new CompatibilityCache(rules, result));
        return result;
    }

    private static CompatibilityItem classify(ItemStack stack, List<String> rules) {
        if (stack.is(ItemTags.BOW_ENCHANTABLE)) return grouped("bow", "🏹");
        if (stack.is(ItemTags.CROSSBOW_ENCHANTABLE)) return grouped("crossbow", "🏹");
        if (stack.is(ItemTags.TRIDENT_ENCHANTABLE)) return grouped("trident", "🔱");
        if (stack.is(ItemTags.MACE_ENCHANTABLE)) return grouped("mace", "🔨");
        if (stack.is(ItemTags.HEAD_ARMOR_ENCHANTABLE) || stack.is(ItemTags.HEAD_ARMOR)) return grouped("helmet", "🪖");
        if (stack.is(ItemTags.CHEST_ARMOR_ENCHANTABLE) || stack.is(ItemTags.CHEST_ARMOR)) return grouped("chestplate", "🦺");
        if (stack.is(ItemTags.LEG_ARMOR_ENCHANTABLE) || stack.is(ItemTags.LEG_ARMOR)) return grouped("leggings", "👖");
        if (stack.is(ItemTags.FOOT_ARMOR_ENCHANTABLE) || stack.is(ItemTags.FOOT_ARMOR)) return grouped("boots", "👢");
        if (stack.is(ItemTags.ARMOR_ENCHANTABLE)) return grouped("armor", "🛡️");
        if (stack.is(ItemTags.FISHING_ENCHANTABLE)) return grouped("fishing", "🎣");
        if (stack.is(enchantableTag("sword")) || stack.is(ItemTags.SWORDS)) return grouped("swords", "⚔️");
        if (stack.is(enchantableTag("axe")) || stack.is(ItemTags.AXES)) return grouped("axes", "🪓");
        if (stack.is(enchantableTag("pickaxe")) || stack.is(ItemTags.PICKAXES)) return grouped("pickaxes", "⛏️");
        if (stack.is(enchantableTag("shovel")) || stack.is(ItemTags.SHOVELS)) return grouped("shovels", "🪏");
        if (stack.is(enchantableTag("hoe")) || stack.is(ItemTags.HOES)) return grouped("hoes", "🌾");
        if (stack.is(Items.SHIELD) || BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath().endsWith("_shield")) {
            return grouped("shield", "🛡️");
        }
        if (stack.is(Items.SHULKER_BOX) || BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath().endsWith("_shulker_box")) {
            return grouped("storage", "📦");
        }
        if (stack.is(Items.ENCHANTED_BOOK) || stack.is(Items.BOOK) || stack.is(EldritchItems.SPELL_BOOK)) {
            return grouped("book", "📖");
        }
        String path = BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
        if (path.endsWith("_spear")) return grouped("spear", "🔱");
        if (rules.size() == 1 && !rules.getFirst().startsWith("#") && rules.getFirst().contains(":")) {
            return new CompatibilityItem("item:" + path, "📦", stack.getHoverName());
        }
        return grouped("other", "🧩");
    }

    private static CompatibilityItem grouped(String id, String emoji) {
        return new CompatibilityItem("group:" + id, emoji,
                Component.translatable("tooltip.eldrich-surge.book_item." + id));
    }

    private static TagKey<Item> enchantableTag(String path) {
        return TagKey.create(Registries.ITEM, Identifier.withDefaultNamespace("enchantable/" + path));
    }

    private record CompatibilityItem(String key, String emoji, Component label) {}
    private record CompatibilityCache(List<String> rules, List<CompatibilityItem> items) {}
    private record TooltipPage(String id, Item icon) {}

    private static void appendFrom(ItemEnchantments enchantments, List<Component> lines, Set<String> addedKeys) {
        Language language = Language.getInstance();

        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
            Holder<Enchantment> enchantment = entry.getKey();
            enchantment.unwrapKey()
                    .map(key -> tooltipKey(key.identifier()))
                    .filter(addedKeys::add)
                    .filter(language::has)
                    .map(key -> Component.translatable(key).withStyle(ChatFormatting.GRAY))
                    .ifPresent(component -> wrapTooltipText(component.getString()).forEach(part ->
                            lines.add(Component.literal(part).withStyle(ChatFormatting.GRAY))));
        }
    }

    /** Avoids font/render access because Minecraft also builds search text on a worker thread. */
    static List<String> wrapTooltipText(String text) {
        List<String> result = new java.util.ArrayList<>();
        StringBuilder line = new StringBuilder();
        for (String word : text.split("\\s+")) {
            if (word.isEmpty()) continue;
            if (line.length() > 0 && line.length() + 1 + word.length() > 40) {
                result.add(line.toString());
                line.setLength(0);
            }
            if (word.codePointCount(0, word.length()) <= 40) {
                if (line.length() > 0) line.append(' ');
                line.append(word);
                continue;
            }
            int offset = 0;
            while (offset < word.length()) {
                int end = word.offsetByCodePoints(offset, Math.min(40, word.codePointCount(offset, word.length())));
                if (line.length() > 0) {
                    result.add(line.toString());
                    line.setLength(0);
                }
                result.add(word.substring(offset, end));
                offset = end;
            }
        }
        if (line.length() > 0) result.add(line.toString());
        return result;
    }

    private static String tooltipKey(Identifier id) {
        return PREFIX + id.getNamespace() + "." + id.getPath();
    }
}
