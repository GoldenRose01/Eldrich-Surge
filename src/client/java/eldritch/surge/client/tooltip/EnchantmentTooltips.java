package eldritch.surge.client.tooltip;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class EnchantmentTooltips {
    private static final String PREFIX = "tooltip.eldritch-surge.enchantment.";

    private EnchantmentTooltips() {
    }

    public static void initialize() {
        ItemTooltipCallback.EVENT.register(EnchantmentTooltips::appendEnchantmentTooltips);
    }

    private static void appendEnchantmentTooltips(ItemStack stack, Item.TooltipContext context, TooltipFlag type, List<Component> lines) {
        Set<String> addedKeys = new HashSet<>();
        appendFrom(stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY), lines, addedKeys);
        appendFrom(stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY), lines, addedKeys);
    }

    private static void appendFrom(ItemEnchantments enchantments, List<Component> lines, Set<String> addedKeys) {
        Language language = Language.getInstance();

        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
            Holder<Enchantment> enchantment = entry.getKey();
            enchantment.unwrapKey()
                    .map(key -> tooltipKey(key.identifier()))
                    .filter(addedKeys::add)
                    .filter(language::has)
                    .map(key -> Component.translatable(key).withStyle(ChatFormatting.GRAY))
                    .ifPresent(lines::add);
        }
    }

    private static String tooltipKey(Identifier id) {
        return PREFIX + id.getNamespace() + "." + id.getPath();
    }
}
