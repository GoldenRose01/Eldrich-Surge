package eldritch.surge.entity;

import eldritch.surge.EldritchSurge;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;

import java.util.List;

public final class EldritchEntityCategories {
    public static final TagKey<EntityType<?>> TAMEABLE = of("tameable");
    public static final TagKey<EntityType<?>> RIDEABLE = of("rideable");
    public static final TagKey<EntityType<?>> BREEDABLE = of("breedable");
    public static final TagKey<EntityType<?>> ANIMALS = of("animals");
    public static final TagKey<EntityType<?>> MAGIK = of("magik");
    public static final TagKey<EntityType<?>> UNDEAD = of("undead");
    public static final TagKey<EntityType<?>> REBEL = of("rebel");
    public static final TagKey<EntityType<?>> FUNGI = of("fungi");
    public static final TagKey<EntityType<?>> HELL = of("hell");
    public static final TagKey<EntityType<?>> IS_END_MOB = of("is_end_mob");
    public static final TagKey<EntityType<?>> VOID = of("void");
    public static final TagKey<EntityType<?>> WATER = of("water");
    public static final TagKey<EntityType<?>> ARTHROPODS = of("arthropods");
    public static final TagKey<EntityType<?>> CUBIC = of("cubic");
    public static final TagKey<EntityType<?>> FLYING = of("flying");

    public static final List<TagKey<EntityType<?>>> ALL = List.of(
            TAMEABLE,
            RIDEABLE,
            BREEDABLE,
            ANIMALS,
            MAGIK,
            UNDEAD,
            REBEL,
            FUNGI,
            HELL,
            IS_END_MOB,
            VOID,
            WATER,
            ARTHROPODS,
            CUBIC,
            FLYING
    );

    private EldritchEntityCategories() {
    }

    public static void initialize() {
        EldritchSurge.LOGGER.debug("Loaded Eldritch Surge entity category tags.");
    }

    private static TagKey<EntityType<?>> of(String path) {
        return TagKey.create(Registries.ENTITY_TYPE, EldritchSurge.id(path));
    }
}
