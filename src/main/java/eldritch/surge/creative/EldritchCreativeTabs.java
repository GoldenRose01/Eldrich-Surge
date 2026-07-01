package eldritch.surge.creative;

import eldritch.surge.EldritchSurge;
import eldritch.surge.block.EldritchBlocks;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Comparator;

public final class EldritchCreativeTabs {
    public static final ResourceKey<CreativeModeTab> ENCHANTMENTS_KEY =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, EldritchSurge.id("enchantments"));

    public static final CreativeModeTab ENCHANTMENTS = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
            ENCHANTMENTS_KEY,
            FabricCreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.eldritch-surge.enchantments"))
                    .icon(() -> new ItemStack(Items.ENCHANTED_BOOK))
                    .displayItems((parameters, output) -> {
                        output.accept(EldritchBlocks.ADVANCED_ENCHANTING_TABLE_ITEM);
                        parameters.holders()
                                .lookupOrThrow(Registries.ENCHANTMENT)
                                .listElements()
                                .sorted(Comparator.comparing(EldritchCreativeTabs::sortKey))
                                .map(EldritchCreativeTabs::createMaxLevelBook)
                                .forEach(output::accept);
                    })
                    .build()
    );

    private EldritchCreativeTabs() {
    }

    public static void initialize() {
        EldritchSurge.LOGGER.debug("Registered Eldritch Surge creative tabs.");
    }

    private static ItemStack createMaxLevelBook(Holder<Enchantment> enchantment) {
        ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
        book.enchant(enchantment, enchantment.value().getMaxLevel());
        return book;
    }

    private static String sortKey(Holder.Reference<Enchantment> enchantment) {
        return enchantment.unwrapKey()
                .map(key -> key.identifier().toString())
                .orElseGet(() -> enchantment.value().description().getString());
    }
}
