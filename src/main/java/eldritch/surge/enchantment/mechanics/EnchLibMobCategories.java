package eldritch.surge.enchantment.mechanics;

import eldritch.surge.entity.EldritchEntityTaxonomy;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/** Optional runtime bridge to EnchLib's per-world mob categories. */
public final class EnchLibMobCategories {
    private static final Map<String, TagKey<EntityType<?>>> FALLBACK_TAGS = Map.ofEntries(
            Map.entry("animals", EldritchEntityTaxonomy.ANIMALS),
            Map.entry("magik", EldritchEntityTaxonomy.MAGIK),
            Map.entry("undead", EldritchEntityTaxonomy.UNDEAD),
            Map.entry("rebel", EldritchEntityTaxonomy.REBEL),
            Map.entry("fungi", EldritchEntityTaxonomy.FUNGI),
            Map.entry("hell", EldritchEntityTaxonomy.HELL),
            Map.entry("void", EldritchEntityTaxonomy.IS_END_MOB),
            Map.entry("water", EldritchEntityTaxonomy.WATER),
            Map.entry("arthropods", EldritchEntityTaxonomy.ARTHROPODS),
            Map.entry("cubic", EldritchEntityTaxonomy.CUBIC),
            Map.entry("flying", EldritchEntityTaxonomy.FLYING)
    );

    private static final Method ENCHLIB_HAS_CATEGORY = findApiMethod();

    private EnchLibMobCategories() {
    }

    public static boolean has(ServerLevel world, LivingEntity entity, String category) {
        if (FabricLoader.getInstance().isModLoaded("enchlib") && ENCHLIB_HAS_CATEGORY != null) {
            try {
                return (boolean) ENCHLIB_HAS_CATEGORY.invoke(null, world.getServer(), entity, category);
            } catch (ReflectiveOperationException | LinkageError exception) {
                // Keep Eldritch Surge functional if an older or incompatible EnchLib API is installed.
            }
        }

        TagKey<EntityType<?>> fallback = FALLBACK_TAGS.get(category);
        return fallback != null && EldritchEntityTaxonomy.matches(entity, fallback);
    }

    public static int count(ServerLevel world, LivingEntity entity, List<String> categories) {
        int matches = 0;
        for (String category : categories) {
            if (has(world, entity, category)) matches++;
        }
        return matches;
    }

    private static Method findApiMethod() {
        try {
            Class<?> api = Class.forName("goldenrose01.enchlib.api.EnchantLibAPI");
            return api.getMethod("hasMobCategory", net.minecraft.server.MinecraftServer.class,
                    net.minecraft.world.entity.Entity.class, String.class);
        } catch (ReflectiveOperationException | LinkageError exception) {
            return null;
        }
    }
}
