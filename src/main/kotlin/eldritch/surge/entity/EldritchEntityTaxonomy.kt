package eldritch.surge.entity

import eldritch.surge.EldritchSurge
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity

object EldritchEntityTaxonomy {
    @JvmField val TAMEABLE: TagKey<EntityType<*>> = tag("tameable")
    @JvmField val RIDEABLE: TagKey<EntityType<*>> = tag("rideable")
    @JvmField val BREEDABLE: TagKey<EntityType<*>> = tag("breedable")
    @JvmField val ANIMALS: TagKey<EntityType<*>> = tag("animals")
    @JvmField val MAGIK: TagKey<EntityType<*>> = tag("magik")
    @JvmField val UNDEAD: TagKey<EntityType<*>> = tag("undead")
    @JvmField val REBEL: TagKey<EntityType<*>> = tag("rebel")
    @JvmField val FUNGI: TagKey<EntityType<*>> = tag("fungi")
    @JvmField val HELL: TagKey<EntityType<*>> = tag("hell")
    @JvmField val VOID: TagKey<EntityType<*>> = tag("void")
    @JvmField val IS_END_MOB: TagKey<EntityType<*>> = tag("is_end_mob")
    @JvmField val WATER: TagKey<EntityType<*>> = tag("water")
    @JvmField val ARTHROPODS: TagKey<EntityType<*>> = tag("arthropods")
    @JvmField val CUBIC: TagKey<EntityType<*>> = tag("cubic")
    @JvmField val FLYING: TagKey<EntityType<*>> = tag("flying")

    // Geographical / Habitat Taxonomy Tags
    @JvmField val GEO_SURFACE: TagKey<EntityType<*>> = tag("geographical/surface")
    @JvmField val GEO_UNDERGROUND: TagKey<EntityType<*>> = tag("geographical/underground")
    @JvmField val GEO_AQUATIC: TagKey<EntityType<*>> = tag("geographical/aquatic")
    @JvmField val GEO_AERIAL: TagKey<EntityType<*>> = tag("geographical/aerial")
    @JvmField val GEO_NETHER: TagKey<EntityType<*>> = tag("geographical/nether")
    @JvmField val GEO_END: TagKey<EntityType<*>> = tag("geographical/end")
    @JvmField val GEO_ARID: TagKey<EntityType<*>> = tag("geographical/arid")
    @JvmField val GEO_COLD: TagKey<EntityType<*>> = tag("geographical/cold")

    @JvmField
    val GEOGRAPHICAL: List<TagKey<EntityType<*>>> = listOf(
        GEO_SURFACE,
        GEO_UNDERGROUND,
        GEO_AQUATIC,
        GEO_AERIAL,
        GEO_NETHER,
        GEO_END,
        GEO_ARID,
        GEO_COLD,
    )

    @JvmField
    val ALL: List<TagKey<EntityType<*>>> = listOf(
        TAMEABLE,
        RIDEABLE,
        BREEDABLE,
        ANIMALS,
        MAGIK,
        UNDEAD,
        REBEL,
        FUNGI,
        HELL,
        VOID,
        IS_END_MOB,
        WATER,
        ARTHROPODS,
        CUBIC,
        FLYING,
        GEO_SURFACE,
        GEO_UNDERGROUND,
        GEO_AQUATIC,
        GEO_AERIAL,
        GEO_NETHER,
        GEO_END,
        GEO_ARID,
        GEO_COLD,
    )

    @JvmField
    val BLADE_OF_APOCALYPSE_DAMAGE_TAGS: List<TagKey<EntityType<*>>> = listOf(
        ANIMALS,
        UNDEAD,
        VOID,
        HELL,
        WATER,
    )

    @JvmStatic
    fun initialize() {
        EldritchSurge.LOGGER.debug("Loaded Eldritch Surge entity taxonomy tags.")
    }

    @JvmStatic
    fun matches(entity: LivingEntity, tag: TagKey<EntityType<*>>): Boolean =
        entity.type.builtInRegistryHolder().`is`(tag)

    @JvmStatic
    fun countMatches(entity: LivingEntity, tags: Iterable<TagKey<EntityType<*>>>): Int =
        tags.count { matches(entity, it) }

    private fun tag(path: String): TagKey<EntityType<*>> =
        TagKey.create(Registries.ENTITY_TYPE, EldritchSurge.id(path))
}
