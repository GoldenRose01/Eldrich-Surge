package eldritch.surge.client.tooltip;

import eldritch.surge.item.EldritchItems;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class EnchantmentTooltips {
    private static final List<TooltipPage> CREATIVE_PAGES = List.of(
            new TooltipPage("swords", Items.NETHERITE_SWORD),
            new TooltipPage("axes", Items.NETHERITE_AXE),
            new TooltipPage("pickaxes", Items.NETHERITE_PICKAXE),
            new TooltipPage("shovels", Items.NETHERITE_SHOVEL),
            new TooltipPage("hoes", Items.NETHERITE_HOE),
            new TooltipPage("bows", Items.BOW),
            new TooltipPage("crossbows", Items.CROSSBOW),
            new TooltipPage("spears", Items.NETHERITE_SPEAR),
            new TooltipPage("tridents", Items.TRIDENT),
            new TooltipPage("maces", Items.MACE),
            new TooltipPage("fishing_rods", Items.FISHING_ROD),
            new TooltipPage("helmets", Items.NETHERITE_HELMET),
            new TooltipPage("chestplates", Items.NETHERITE_CHESTPLATE),
            new TooltipPage("leggings", Items.NETHERITE_LEGGINGS),
            new TooltipPage("boots", Items.NETHERITE_BOOTS),
            new TooltipPage("elytra", Items.ELYTRA),
            new TooltipPage("shields", Items.SHIELD)
    );

    private EnchantmentTooltips() {
    }

    public static void initialize() {
        ItemTooltipCallback.EVENT.register(EnchantmentTooltips::appendEnchantmentTooltips);
    }

    private static void appendEnchantmentTooltips(ItemStack stack, Item.TooltipContext context, TooltipFlag type, List<Component> lines) {
        Set<String> addedKeys = new HashSet<>();
        ItemEnchantments direct = stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        ItemEnchantments stored = stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
        if (stack.is(Items.FISHING_ROD) && direct.keySet().stream().anyMatch(holder -> holder.unwrapKey()
                .map(key -> key.identifier().toString().equals("eldrich-surge:fanatic")).orElse(false))) {
            lines.add(Component.translatable("tooltip.eldrich-surge.fanatic_score",
                    eldritch.surge.enchantment.mechanics.SurvivalEnchantmentMechanics.fishingScore(stack))
                    .withStyle(ChatFormatting.AQUA));
        }


        if (stack.is(Items.ENCHANTED_BOOK) || stack.is(EldritchItems.SPELL_BOOK)) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null && !minecraft.player.getAbilities().instabuild) {
                appendSurvivalCreativeTabs(direct, lines);
                appendSurvivalCreativeTabs(stored, lines);
            }
        }
    }

    private static void appendSurvivalCreativeTabs(ItemEnchantments enchantments, List<Component> lines) {
        Set<String> added = new HashSet<>();
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments.entrySet()) {
            for (TooltipPage page : CREATIVE_PAGES) {
                if (entry.getKey().value().isSupportedItem(new ItemStack(page.icon()))
                        && added.add(page.id())) {
                    lines.add(Component.translatable("itemGroup.eldrich-surge." + page.id())
                            .withStyle(ChatFormatting.BLUE));
                }
            }
        }
    }

    private record TooltipPage(String id, Item icon) {}
}
