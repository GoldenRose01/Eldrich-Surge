package eldritch.surge.block

import eldritch.surge.EldritchSurge
import eldritch.surge.menu.AdvancedEnchantmentMenu
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityType
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EnchantingTableBlock
import net.minecraft.world.level.block.entity.BlockEntityTypes
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

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
        AdvancedEnchantingTableBlock(
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

    private class AdvancedEnchantingTableBlock(properties: BlockBehaviour.Properties) : EnchantingTableBlock(properties) {
        override fun useWithoutItem(
            state: BlockState,
            level: Level,
            pos: BlockPos,
            player: Player,
            hitResult: BlockHitResult,
        ): InteractionResult {
            if (!level.isClientSide) {
                player.openMenu(getMenuProvider(state, level, pos))
            }

            return InteractionResult.SUCCESS
        }

        override fun getMenuProvider(state: BlockState, level: Level, pos: BlockPos): MenuProvider {
            return object : ExtendedMenuProvider<BlockPos> {
                override fun getDisplayName(): Component = Component.translatable("container.enchant")

                override fun createMenu(syncId: Int, inventory: Inventory, player: Player) =
                    AdvancedEnchantmentMenu(syncId, inventory, ContainerLevelAccess.create(level, pos))

                override fun getScreenOpeningData(player: ServerPlayer): BlockPos = pos
            }
        }
    }
}
