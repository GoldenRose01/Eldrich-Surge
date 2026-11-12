package eldritch.surge.enchantment.mechanics;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import eldritch.surge.enchantment.effect.LogarithmicLevelValue;
import java.nio.file.Files;
import java.nio.file.Path;

public final class EnchantmentScalingTest {
    public static void main(String[] args) throws Exception {
        int count = 0;
        for (var definition : ModEnchantmentDefinitions.ALL) {
            int max = definition.maxLevel();
            for (int rank = 0; rank <= max; rank++) {
                equal(rank, EnchantmentScaling.effective(definition.id(), rank));
                if (EnchantmentScaling.discrete(definition.id(), rank) != rank) throw new AssertionError(definition.id());
            }
            equal(0, EnchantmentScaling.effective(definition.id(), Integer.MIN_VALUE));
            double previous = max;
            for (int rank : new int[]{max + 1, max + 2, max + 28, 255, 32767, Integer.MAX_VALUE}) {
                double strength = EnchantmentScaling.effective(definition.id(), rank);
                if (!Double.isFinite(strength) || strength < previous || strength > max + 22) {
                    throw new AssertionError(definition.id() + " unsafe strength " + strength);
                }
                if (max > 1 && strength <= previous) throw new AssertionError("No overlevel growth: " + definition.id());
                previous = strength;
            }
            var file = Path.of("src/main/resources/data/eldrich-surge/enchantment/" + definition.id() + ".json");
            var json = JsonParser.parseString(Files.readString(file)).getAsJsonObject();
            if (json.get("max_level").getAsInt() != max) throw new AssertionError("Catalog maximum mismatch: " + definition.id());
            checkValues(json.get("effects"), max);
            count++;
        }
        equal(20, 4 * EnchantmentScaling.effective("armored", 5));
        equal(32.71221532139178, 4 * EnchantmentScaling.effective("armored", 28));
        for (int rank : new int[]{1, 5, 4800, 4801, Integer.MAX_VALUE}) {
            if (EnchantmentScaling.repairInterval(rank) < 1) throw new AssertionError("Zero repair divisor");
        }
        System.out.println("Validated normal ranks, logarithmic overlevels and JSON effects for " + count + " enchantments.");
    }

    private static void checkValues(JsonElement node, int max) {
        if (node == null || node.isJsonNull()) return;
        if (node.isJsonArray()) { node.getAsJsonArray().forEach(child -> checkValues(child, max)); return; }
        if (!node.isJsonObject()) return;
        var object = node.getAsJsonObject();
        if (object.has("type") && object.get("type").getAsString().equals("minecraft:linear") && max > 1) {
            throw new AssertionError("Unscaled native effect: " + node);
        }
        if (object.has("type") && object.get("type").getAsString().equals("eldrich-surge:logarithmic_level")) {
            var value = LogarithmicLevelValue.CODEC.codec().parse(JsonOps.INSTANCE, node).getOrThrow();
            if (value.maxLevel() != max) throw new AssertionError("JSON scaling threshold mismatch");
            for (int rank = 1; rank <= max; rank++) {
                equal(value.base() + value.perLevelAboveFirst() * (rank - 1), value.calculate(rank));
            }
            if (!Float.isFinite(value.calculate(Integer.MAX_VALUE))) throw new AssertionError("Non-finite JSON effect");
            if (value.perLevelAboveFirst() > 0 && value.calculate(max + 1) <= value.calculate(max)) {
                throw new AssertionError("No native overlevel growth");
            }
        }
        object.entrySet().forEach(entry -> checkValues(entry.getValue(), max));
    }

    private static void equal(double expected, double actual) {
        if (!Double.isFinite(actual) || Math.abs(expected - actual) > 1e-6) throw new AssertionError(expected + " != " + actual);
    }
}
