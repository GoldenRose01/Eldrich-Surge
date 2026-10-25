package eldritch.surge.enchantment;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.file.Files;
import java.nio.file.Path;

/** Regression checks for weapon enchantments whose attack damage is shown as an item attribute. */
public final class WeaponEnchantmentDamageTest {
    public static void main(String[] args) throws Exception {
        assertAttackDamagePerLevel("katana", 1.25);
        assertAttackDamagePerLevel("neptunes_will", 1.0);
        assertTooltip("it_it", "Rende la lama ultratagliente.");
        assertTooltip("en_us", "Makes the blade razor-sharp.");
        System.out.println("Validated Katana and Neptune's Will attack damage attributes and tooltips.");
    }

    private static void assertAttackDamagePerLevel(String enchantment, double expected) throws Exception {
        Path path = Path.of("src/main/resources/data/eldrich-surge/enchantment/" + enchantment + ".json");
        JsonObject root = JsonParser.parseString(Files.readString(path)).getAsJsonObject();
        JsonObject modifier = root.getAsJsonObject("effects")
                .getAsJsonArray("minecraft:attributes").get(0).getAsJsonObject();
        JsonObject amount = modifier.getAsJsonObject("amount");
        if (!"minecraft:attack_damage".equals(modifier.get("attribute").getAsString())
                || !"minecraft:linear".equals(amount.get("type").getAsString())
                || Double.compare(expected, amount.get("base").getAsDouble()) != 0
                || Double.compare(expected, amount.get("per_level_above_first").getAsDouble()) != 0) {
            throw new AssertionError("Unexpected attack damage scaling for " + enchantment);
        }
    }

    private static void assertTooltip(String locale, String expectedText) throws Exception {
        Path path = Path.of("src/main/resources/assets/eldrich-surge/lang/" + locale + ".json");
        JsonObject translations = JsonParser.parseString(Files.readString(path)).getAsJsonObject();
        String key = "tooltip.eldrich-surge.enchantment.eldrich-surge.katana";
        if (!translations.has(key) || !translations.get(key).getAsString().startsWith(expectedText)) {
            throw new AssertionError("Missing or outdated Katana tooltip in " + locale);
        }
    }
}
