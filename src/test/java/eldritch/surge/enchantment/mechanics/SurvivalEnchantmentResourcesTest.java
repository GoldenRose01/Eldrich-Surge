package eldritch.surge.enchantment.mechanics;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;

/** Decode the new resources using Minecraft and resolve their actual vanilla/mod item tags. */
public final class SurvivalEnchantmentResourcesTest {
    public static void main(String[] args) throws Exception {
        net.minecraft.SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        var ops = RegistryOps.create(JsonOps.INSTANCE, net.minecraft.data.registries.VanillaRegistries.createWorldLookup());
        String[] ids = {"fanatic", "dodge", "fuse_cutter", "immortality", "curse_of_slippery",
                "curse_of_incompatibility", "curse_of_fatigue", "curse_of_corrosion", "blessing_of_the_sea",
                "accuracy_shot", "fear", "kinetic_protection", "happy_boost", "step_assist"};
        for (String id : ids) {
            var definition = ModEnchantmentDefinitions.BY_ID.get(id);
            var json = JsonParser.parseString(Files.readString(Path.of(
                    "src/main/resources/data/eldrich-surge/enchantment/" + id + ".json"))).getAsJsonObject();
            Set<String> items = new LinkedHashSet<>();
            resolve(json.get("supported_items").getAsString(), items);
            if (items.isEmpty()) throw new AssertionError("Empty item tag for " + id);
            JsonArray supported = new JsonArray();
            items.forEach(supported::add);
            json.add("supported_items", supported);
            Enchantment enchantment = Enchantment.DIRECT_CODEC.parse(ops, json).getOrThrow();
            if (enchantment.getMaxLevel() != definition.maxLevel()) throw new AssertionError(id + " level mismatch");
            for (String locale : new String[]{"en_us", "it_it"}) {
                var lang = JsonParser.parseString(Files.readString(Path.of(
                        "src/main/resources/assets/eldrich-surge/lang/" + locale + ".json"))).getAsJsonObject();
                if (!lang.has("enchantment.eldrich-surge." + id)
                        || !lang.has("tooltip.eldrich-surge.enchantment.eldrich-surge." + id)) throw new AssertionError(id + " translations");
            }
            if (id.equals("dodge") && (!enchantment.getSupportedItems().contains(Items.DIAMOND_LEGGINGS.builtInRegistryHolder())
                    || enchantment.getSupportedItems().contains(Items.DIAMOND_BOOTS.builtInRegistryHolder())
                    || !enchantment.matchingSlot(EquipmentSlot.LEGS))) throw new AssertionError("Dodge must be leggings-only");
            if (id.equals("curse_of_slippery")) {
                for (var item : new net.minecraft.world.item.Item[]{Items.DIAMOND_SWORD, Items.DIAMOND_AXE,
                        Items.DIAMOND_PICKAXE, Items.DIAMOND_SHOVEL, Items.DIAMOND_HOE, Items.NETHERITE_SPEAR,
                        Items.TRIDENT, Items.BOW, Items.CROSSBOW, Items.SHIELD, Items.FISHING_ROD}) {
                    if (!enchantment.getSupportedItems().contains(item.builtInRegistryHolder())) throw new AssertionError("Slippery excludes " + item);
                }
                if (enchantment.getSupportedItems().contains(Items.DIAMOND_CHESTPLATE.builtInRegistryHolder())) throw new AssertionError("Slippery includes armor");
            }
        }
        System.out.println("Validated all 14 new enchantments with Minecraft's codec, item tags, and translations.");
    }

    private static void resolve(String value, Set<String> items) throws Exception {
        if (!value.startsWith("#")) {
            Identifier id = Identifier.parse(value);
            if (!BuiltInRegistries.ITEM.containsKey(id)) throw new AssertionError("Unknown item " + value);
            items.add(value);
            return;
        }
        String[] id = value.substring(1).split(":", 2);
        String resource = "data/" + id[0] + "/tags/item/" + id[1] + ".json";
        Path local = Path.of("src/main/resources/" + resource);
        String text;
        if (Files.exists(local)) text = Files.readString(local);
        else try (var stream = SurvivalEnchantmentResourcesTest.class.getClassLoader().getResourceAsStream(resource)) {
            if (stream == null) throw new AssertionError("Unknown tag " + value);
            text = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
        for (var entry : JsonParser.parseString(text).getAsJsonObject().getAsJsonArray("values")) {
            if (entry.isJsonPrimitive()) resolve(entry.getAsString(), items);
            else if (!entry.getAsJsonObject().has("required") || entry.getAsJsonObject().get("required").getAsBoolean()) {
                resolve(entry.getAsJsonObject().get("id").getAsString(), items);
            }
        }
    }
}
