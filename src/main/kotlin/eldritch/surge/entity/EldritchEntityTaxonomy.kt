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
