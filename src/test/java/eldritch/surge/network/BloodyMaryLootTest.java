package eldritch.surge.network;

import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.level.storage.loot.LootTable;

import java.nio.file.Files;
import java.nio.file.Path;

public final class BloodyMaryLootTest {
    public static void main(String[] args) throws Exception {
        net.minecraft.SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        var root = Path.of("src/main/resources/data/eldritch-surge/loot_table/bloody_mary");
        java.util.List<Path> tables;
        try (var paths = Files.walk(root)) {
            tables = paths.filter(p -> p.toString().endsWith(".json")).toList();
        }
        var provider = new net.minecraft.core.RegistrySetBuilder()
                .add(net.minecraft.core.registries.Registries.LOOT_TABLE, context -> {
                    for (Path path : tables) {
                        String id = root.relativize(path).toString().replace(java.io.File.separatorChar, '/').replace(".json", "");
                        context.register(net.minecraft.resources.ResourceKey.create(
                                net.minecraft.core.registries.Registries.LOOT_TABLE,
                                net.minecraft.resources.Identifier.fromNamespaceAndPath("eldritch-surge", "bloody_mary/" + id)), LootTable.EMPTY);
                    }
                }).build(net.minecraft.data.registries.VanillaRegistries.createWorldLookup());
        var ops = RegistryOps.create(JsonOps.INSTANCE, provider);
        int count = 0;
        try (var paths = Files.walk(Path.of("src/main/resources/data/eldritch-surge/loot_table/bloody_mary"))) {
            for (Path path : paths.filter(p -> p.toString().endsWith(".json")).toList()) {
                var json = JsonParser.parseString(Files.readString(path));
                var result = LootTable.DIRECT_CODEC.parse(ops, json);
                if (result.error().isPresent()) throw new AssertionError(path + ": " + result.error().get().message());
                count++;
            }
        }
        var nativeHeads = java.util.Set.of("zombie", "skeleton", "wither_skeleton", "creeper", "piglin", "ender_dragon", "giant");
        for (var type : BuiltInRegistries.ENTITY_TYPE) {
            var id = BuiltInRegistries.ENTITY_TYPE.getKey(type);
            if (type.getCategory() != net.minecraft.world.entity.MobCategory.MISC
                    && !nativeHeads.contains(id.getPath())
                    && !Files.exists(root.resolve("entities/" + id.getPath() + ".json"))) {
                throw new AssertionError("Missing mob head: " + id);
            }
        }
        System.out.println("Validated " + count + " Bloody Mary loot tables with Minecraft's codec.");
    }
}
