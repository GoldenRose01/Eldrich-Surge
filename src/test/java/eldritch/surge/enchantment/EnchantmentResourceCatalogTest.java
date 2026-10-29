package eldritch.surge.enchantment;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinition;
import eldritch.surge.enchantment.mechanics.ModEnchantmentDefinitions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/** Keeps Java enchantment metadata, data packs, and translations in sync. */
public final class EnchantmentResourceCatalogTest {
    public static void main(String[] args) throws Exception {
        Path root = Path.of("src/main/resources/data/eldrich-surge/enchantment");
        Set<String> declared = new HashSet<>();
        for (ModEnchantmentDefinition definition : ModEnchantmentDefinitions.ALL) {
            String id = definition.id();
            if (!declared.add(id)) throw new AssertionError("Duplicate Java enchantment id: " + id);
            Path resource = root.resolve(id + ".json");
            if (!Files.isRegularFile(resource)) throw new AssertionError("Missing enchantment resource: " + resource);
            JsonObject json = JsonParser.parseString(Files.readString(resource)).getAsJsonObject();
            if (json.get("max_level").getAsInt() != definition.maxLevel()) {
                throw new AssertionError("max_level does not match Java catalog for " + id);
            }
            String translateKey = "enchantment.eldrich-surge." + id;
            if (!translateKey.equals(json.getAsJsonObject("description").get("translate").getAsString())) {
                throw new AssertionError("Incorrect description key for " + id);
            }
            String resourceSupportedItems = json.get("supported_items").getAsString();
            if (resourceSupportedItems.startsWith("#eldrich-surge:")) {
                Path tag = Path.of("src/main/resources/data/eldrich-surge/tags/item/"
                        + resourceSupportedItems.substring("#eldrich-surge:".length()) + ".json");
                if (!Files.isRegularFile(tag)) throw new AssertionError("Missing resource supported-item tag " + resourceSupportedItems + " for " + id);
            }
            if (json.has("exclusive_set")) {
                String exclusiveSet = json.get("exclusive_set").getAsString();
                if (exclusiveSet.startsWith("#eldrich-surge:")) {
                    Path tag = Path.of("src/main/resources/data/eldrich-surge/tags/enchantment/"
                            + exclusiveSet.substring("#eldrich-surge:".length()) + ".json");
                    if (!Files.isRegularFile(tag)) throw new AssertionError("Missing exclusive-set enchantment tag " + exclusiveSet + " for " + id);
                }
            }
            for (String itemTag : definition.supportedItems()) {
                if (itemTag.startsWith("#eldrich-surge:")) {
                    Path tag = Path.of("src/main/resources/data/eldrich-surge/tags/item/"
                            + itemTag.substring("#eldrich-surge:".length()) + ".json");
                    if (!Files.isRegularFile(tag)) throw new AssertionError("Missing supported-item tag " + itemTag + " for " + id);
                }
            }
            for (String locale : new String[]{"it_it", "en_us"}) {
                Path translationsPath = Path.of("src/main/resources/assets/eldrich-surge/lang/" + locale + ".json");
                JsonObject translations = JsonParser.parseString(Files.readString(translationsPath)).getAsJsonObject();
                String tooltipKey = "tooltip.eldrich-surge.enchantment.eldrich-surge." + id;
                if (!translations.has(translateKey) || !translations.has(tooltipKey)) {
                    throw new AssertionError("Missing " + locale + " name or tooltip for " + id);
                }
            }
        }
        Set<String> resources;
        try (var paths = Files.list(root)) {
            resources = paths.filter(path -> path.toString().endsWith(".json"))
                    .map(path -> path.getFileName().toString().replaceFirst("\\.json$", ""))
                    .collect(Collectors.toSet());
        }
        if (!resources.equals(declared)) {
            Set<String> missing = new HashSet<>(declared); missing.removeAll(resources);
            Set<String> orphaned = new HashSet<>(resources); orphaned.removeAll(declared);
            throw new AssertionError("Java/data enchantment catalog mismatch; missing=" + missing + ", unlisted=" + orphaned);
        }
        if (declared.contains("piercing")
                || Files.exists(root.resolve("piercing.json"))) {
            throw new AssertionError("Piercing must remain the vanilla enchantment, not a duplicate mod enchantment.");
        }
        JsonObject vanillaPiercing = JsonParser.parseString(Files.readString(
                Path.of("src/main/resources/data/minecraft/enchantment/piercing.json"))).getAsJsonObject();
        if (vanillaPiercing.get("max_level").getAsInt() != 10
                || !"#minecraft:enchantable/crossbow".equals(vanillaPiercing.get("supported_items").getAsString())) {
            throw new AssertionError("Vanilla Piercing must be crossbow-only and support level X.");
        }
        JsonObject swiftness = JsonParser.parseString(Files.readString(
                Path.of("src/main/resources/data/eldrich-surge/tags/item/enchantable/swiftness.json"))).getAsJsonObject();
        var swiftnessItems = swiftness.getAsJsonArray("values").asList().stream()
                .map(element -> element.getAsString()).collect(Collectors.toSet());
        if (!swiftnessItems.contains("#minecraft:enchantable/bow")
                || swiftnessItems.contains("#minecraft:enchantable/crossbow")
                || !swiftnessItems.contains("#minecraft:spears")
                || swiftnessItems.stream().anyMatch(value -> value.contains("armor"))) {
            throw new AssertionError("Swiftness must only support bows and spears.");
        }
        System.out.println("Validated " + declared.size() + " enchantment declarations, resources and translations.");
    }
}
