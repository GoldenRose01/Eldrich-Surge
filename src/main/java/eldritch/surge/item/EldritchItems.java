package eldritch.surge.item;

import eldritch.surge.EldritchSurge;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public final class EldritchItems {
    public static final ResourceKey<Item> SPELL_BOOK_KEY = ResourceKey.create(Registries.ITEM, EldritchSurge.id("spell_book"));
    public static final Item SPELL_BOOK = Registry.register(
            BuiltInRegistries.ITEM,
            SPELL_BOOK_KEY,
            new Item(new Item.Properties().setId(SPELL_BOOK_KEY))
    );

    private EldritchItems() {
    }

    public static void initialize() {
        EldritchSurge.LOGGER.debug("Registered Eldritch Surge spell book item.");
    }
}
