package eldritch.surge.enchantment.mechanics;

import eldritch.surge.item.EldritchItems;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class EnchantmentBookTags {
    private EnchantmentBookTags() {
    }

    public static Optional<BookTag> mostRelevantTag(ItemStack book) {
        if (!isEnchantmentBook(book)) return Optional.empty();

        ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(book);
        Holder<Enchantment> selected = null;
        int selectedLevel = -1;
        String selectedId = "";

        for (var entry : enchantments.entrySet()) {
            Holder<Enchantment> candidate = entry.getKey();
            int level = entry.getIntValue();
            String id = candidate.unwrapKey().map(key -> key.identifier().toString()).orElse("");
            if (selected == null
                    || candidate.value().getWeight() < selected.value().getWeight()
                    || candidate.value().getWeight() == selected.value().getWeight() && level > selectedLevel
                    || candidate.value().getWeight() == selected.value().getWeight() && level == selectedLevel
                    && id.compareTo(selectedId) < 0) {
                selected = candidate;
                selectedLevel = level;
                selectedId = id;
            }
        }

        return selected == null ? Optional.empty() : Optional.of(tagFor(selected));
    }

    /** Stores the tag in custom-model-data strings so item definitions can select a tag-specific model. */
    public static void updateBookModelTag(ItemStack book) {
        if (!isEnchantmentBook(book)) return;

        CustomModelData old = book.getOrDefault(DataComponents.CUSTOM_MODEL_DATA, CustomModelData.EMPTY);
        List<String> strings = new ArrayList<>(old.strings());
        Optional<BookTag> tag = mostRelevantTag(book);
        if (tag.isPresent()) {
            if (strings.isEmpty()) strings.add(tag.get().id());
            else strings.set(0, tag.get().id());
        } else if (!strings.isEmpty()) {
            strings.remove(0);
        }

        if (strings.equals(old.strings())) return;
        if (strings.isEmpty() && old.floats().isEmpty() && old.flags().isEmpty() && old.colors().isEmpty()) {
            book.remove(DataComponents.CUSTOM_MODEL_DATA);
        } else {
            book.set(DataComponents.CUSTOM_MODEL_DATA,
                    new CustomModelData(old.floats(), old.flags(), strings, old.colors()));
        }
    }

    private static boolean isEnchantmentBook(ItemStack stack) {
        return stack.is(Items.ENCHANTED_BOOK) || stack.is(EldritchItems.SPELL_BOOK);
    }

    private static BookTag tagFor(Holder<Enchantment> enchantment) {
        String namespace = enchantment.unwrapKey().map(key -> key.identifier().getNamespace()).orElse("");
        String path = enchantment.unwrapKey().map(key -> key.identifier().getPath()).orElse("");
        ModEnchantmentDefinition definition = namespace.equals("eldritch-surge")
                ? ModEnchantmentDefinitions.BY_ID.get(path)
                : null;
        if (definition != null) {
            BookTag fromCategory = fromCategory(definition.itemCategory());
            if (fromCategory != null) return fromCategory;
        }

        List<BookTag> supported = new ArrayList<>();
        addIfSupported(supported, enchantment, Items.BOW, BookTag.ARCHERY);
        addIfSupported(supported, enchantment, Items.CROSSBOW, BookTag.ARCHERY);
        addIfSupported(supported, enchantment, Items.TRIDENT, BookTag.TRIDENT);
        addIfSupported(supported, enchantment, Items.WOODEN_SPEAR, BookTag.SPEAR);
        addIfSupported(supported, enchantment, Items.MACE, BookTag.MACE);
        addIfSupported(supported, enchantment, Items.WOODEN_SWORD, BookTag.MELEE_WEAPON);
        addIfSupported(supported, enchantment, Items.WOODEN_AXE, BookTag.MELEE_WEAPON);
        addIfSupported(supported, enchantment, Items.WOODEN_PICKAXE, BookTag.TOOL);
        addIfSupported(supported, enchantment, Items.WOODEN_SHOVEL, BookTag.TOOL);
        addIfSupported(supported, enchantment, Items.WOODEN_HOE, BookTag.FARMING);
        addIfSupported(supported, enchantment, Items.SHIELD, BookTag.SHIELD);
        addIfSupported(supported, enchantment, Items.IRON_CHESTPLATE, BookTag.ARMOR);
        addIfSupported(supported, enchantment, Items.ELYTRA, BookTag.ARMOR);
        addIfSupported(supported, enchantment, Items.FISHING_ROD, BookTag.FISHING);
        addIfSupported(supported, enchantment, Items.SHULKER_BOX, BookTag.STORAGE);

        List<BookTag> distinct = supported.stream().distinct().toList();
        if (distinct.isEmpty()) return BookTag.UNIVERSAL;
        if (distinct.size() == 1) return distinct.getFirst();
        if (distinct.stream().allMatch(tag -> tag == BookTag.MELEE_WEAPON
                || tag == BookTag.TRIDENT || tag == BookTag.SPEAR || tag == BookTag.MACE)) {
            return BookTag.MELEE_WEAPON;
        }
        return BookTag.UNIVERSAL;
    }

    private static void addIfSupported(List<BookTag> result, Holder<Enchantment> enchantment,
                                      net.minecraft.world.item.Item item, BookTag tag) {
        if (enchantment.value().isSupportedItem(new ItemStack(item))) result.add(tag);
    }

    private static BookTag fromCategory(String category) {
        String value = category.toUpperCase(java.util.Locale.ROOT);
        if (value.contains("CAST SPELL")) return BookTag.SPELL;
        if (value.contains("SHULKER")) return BookTag.STORAGE;
        if (value.contains("SHIELD")) return BookTag.SHIELD;
        if (value.contains("BOW")) return BookTag.ARCHERY;
        if (value.contains("TRIDENT") && !value.contains("WEAPON")) return BookTag.TRIDENT;
        if (value.contains("SPEAR")) return BookTag.SPEAR;
        if (value.contains("MACE")) return BookTag.MACE;
        if (value.contains("ELYTRA") || value.contains("ARMOR") || value.contains("HELMET")
                || value.contains("CHESTPLATE") || value.contains("LEGGINGS") || value.contains("BOOTS")) {
            return BookTag.ARMOR;
        }
        if (value.contains("HOE")) return BookTag.FARMING;
        if (value.contains("TOOLS")) return BookTag.TOOL;
        if (value.contains("WEAPON")) return BookTag.MELEE_WEAPON;
        return BookTag.UNIVERSAL;
    }

    public enum BookTag {
        ARCHERY("archery", "🏹", "Arco e balestra"),
        TRIDENT("trident", "🔱", "Tridente"),
        SPEAR("spear", "🔱", "Lancia"),
        MELEE_WEAPON("weapon", "⚔", "Arma da mischia"),
        MACE("mace", "🔨", "Mazza"),
        TOOL("tool", "⛏", "Attrezzo"),
        FARMING("farming", "🌾", "Zappa"),
        ARMOR("armor", "🛡", "Armatura"),
        SHIELD("shield", "🛡", "Scudo"),
        FISHING("fishing", "🎣", "Pesca"),
        STORAGE("storage", "📦", "Shulker"),
        SPELL("spell", "📖", "Libro degli incantesimi"),
        UNIVERSAL("universal", "✨", "Universale");

        private final String id;
        private final String icon;
        private final String label;

        BookTag(String id, String icon, String label) {
            this.id = id;
            this.icon = icon;
            this.label = label;
        }

        public String id() { return id; }
        public String icon() { return icon; }
        public String label() { return label; }
    }
}
