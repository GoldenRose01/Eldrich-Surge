package eldritch.surge.block

import eldritch.surge.EldritchSurge
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityType
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EnchantingTableBlock
import net.minecraft.world.level.block.entity.BlockEntityTypes
import net.minecraft.world.level.block.state.BlockBehaviour

object EldritchBlocks {
    @JvmField
    val ADVANCED_ENCHANTING_TABLE_BLOCK_KEY: ResourceKey<Block> =
        ResourceKey.create(Registries.BLOCK, EldritchSurge.id("advanced_enchanting_table"))

    @JvmField
    val ADVANCED_ENCHANTING_TABLE_ITEM_KEY: ResourceKey<Item> =
        ResourceKey.create(Registries.ITEM, EldritchSurge.id("advanced_enchanting_table"))

    @JvmField
    val ADVANCED_ENCHANTING_TABLE: Block = Registry.register(
        BuiltInRegistries.BLOCK,
        ADVANCED_ENCHANTING_TABLE_BLOCK_KEY,
        EnchantingTableBlock(
            BlockBehaviour.Properties
                .ofFullCopy(Blocks.ENCHANTING_TABLE)
                .setId(ADVANCED_ENCHANTING_TABLE_BLOCK_KEY),
        ),
    )

    @JvmField
    val ADVANCED_ENCHANTING_TABLE_ITEM: Item = Registry.register(
        BuiltInRegistries.ITEM,
        ADVANCED_ENCHANTING_TABLE_ITEM_KEY,
        BlockItem(
            ADVANCED_ENCHANTING_TABLE,
            Item.Properties()
                .useBlockDescriptionPrefix()
                .setId(ADVANCED_ENCHANTING_TABLE_ITEM_KEY),
        ),
    )

    @JvmStatic
    fun initialize() {
        (BlockEntityTypes.ENCHANTING_TABLE as FabricBlockEntityType).addValidBlock(ADVANCED_ENCHANTING_TABLE)
        EldritchSurge.LOGGER.debug("Registered Eldritch Surge blocks.")
    }
}
