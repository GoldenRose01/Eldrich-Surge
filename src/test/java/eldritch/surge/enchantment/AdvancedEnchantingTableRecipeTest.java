package eldritch.surge.enchantment;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.file.Files;
import java.nio.file.Path;

/** Guards the advanced table's craft and recipe-book unlock resources. */
public final class AdvancedEnchantingTableRecipeTest {
    public static void main(String[] args) throws Exception {
        JsonObject recipe = read("src/main/resources/data/eldrich-surge/recipe/advanced_enchanting_table.json");
        if (!"minecraft:crafting_shaped".equals(recipe.get("type").getAsString())
                || !"building".equals(recipe.get("category").getAsString())
                || !"eldrich-surge:advanced_enchanting_table".equals(recipe.getAsJsonObject("result").get("id").getAsString())) {
            throw new AssertionError("Advanced Enchanting Table crafting recipe is incomplete or miscategorized.");
        }
        JsonObject advancement = read("src/main/resources/data/eldrich-surge/advancement/recipes/decorations/advanced_enchanting_table.json");
        JsonObject criteria = advancement.getAsJsonObject("criteria");
        if (!criteria.has("has_amethyst_shard") || !criteria.has("has_enchanting_table") || !criteria.has("has_the_recipe")
                || !advancement.getAsJsonObject("rewards").getAsJsonArray("recipes").contains(
                com.google.gson.JsonParser.parseString("\"eldrich-surge:advanced_enchanting_table\""))) {
            throw new AssertionError("Recipe-book advancement does not unlock the advanced table recipe.");
        }
        var requirements = advancement.getAsJsonArray("requirements");
        if (requirements.size() != 1 || requirements.get(0).getAsJsonArray().size() != 3) {
            throw new AssertionError("Either crafting ingredient must independently unlock the vanilla recipe book entry.");
        }
        System.out.println("Validated the advanced table crafting recipe and recipe-book advancement.");
    }

    private static JsonObject read(String file) throws Exception {
        return JsonParser.parseString(Files.readString(Path.of(file))).getAsJsonObject();
    }
}
