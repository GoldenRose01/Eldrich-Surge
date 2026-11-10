package eldritch.surge.client.tooltip;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.*;
import java.util.*;

/** A stationary, hoverable enchantment list with one adjacent description. */
public final class EnchantmentDetailsPanel {
    private ItemStack item = ItemStack.EMPTY;
    private List<Holder<Enchantment>> entries = List.of();
    private int x, y, selected = -1;
    private long lastHover;
    private final int panelWidth = 150;
    public void show(ItemStack stack, int anchorX, int anchorY, int width, int height) {
        if (stack.isEmpty()) return;
        var values = new LinkedHashSet<Holder<Enchantment>>();
        values.addAll(stack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).keySet());
        values.addAll(stack.getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).keySet());
        if (values.isEmpty()) return;
        if (!ItemStack.matches(item, stack)) { item = stack.copy(); entries = List.copyOf(values); selected = -1; }
        x = Math.clamp(anchorX, 4, Math.max(4, width - panelWidth - 4));
        y = Math.clamp(anchorY, 4, Math.max(4, height - 24 - entries.size() * 12));
        lastHover = System.currentTimeMillis();
    }
    public void show(Holder<Enchantment> enchantment, int anchorX, int anchorY, int width, int height) {
        var stack = new ItemStack(net.minecraft.world.item.Items.ENCHANTED_BOOK);
        stack.enchant(enchantment, 1);
        show(stack, anchorX, anchorY, width, height);
        selected = 0;
    }
    public void render(GuiGraphicsExtractor graphics, int mouseX, int mouseY, int width, int height) {
        if (entries.isEmpty()) return;
        var font = Minecraft.getInstance().font;
        int h = 22 + entries.size() * 12;
        boolean over = mouseX >= x - 10 && mouseX <= x + panelWidth + 10 && mouseY >= y && mouseY <= y + h;
        if (over) {
            lastHover = System.currentTimeMillis();
            int row = (mouseY - y - 18) / 12;
            if (mouseY >= y + 18 && row < entries.size()) selected = row;
        }
        int detailWidth = Math.min(200, Math.max(80, width - panelWidth - 16));
        int dx = x + panelWidth + 4;
        if (dx + detailWidth > width - 4) dx = Math.max(4, x - detailWidth - 4);
        var description = selected < 0 ? List.<net.minecraft.util.FormattedCharSequence>of() : font.split(description(entries.get(selected)), detailWidth - 12);
        int dh = 12 + description.size() * 10;
        int dy = Math.min(y, Math.max(4, height - dh - 4));
        if (selected >= 0 && mouseX >= dx - 4 && mouseX <= dx + detailWidth + 4 && mouseY >= dy && mouseY <= dy + dh) lastHover = System.currentTimeMillis();
        if (System.currentTimeMillis() - lastHover > 350) { entries = List.of(); item = ItemStack.EMPTY; return; }
        box(graphics, x, y, panelWidth, h);
        graphics.text(font, font.plainSubstrByWidth(item.getHoverName().getString(), panelWidth - 12), x + 6, y + 5, 0xffffffff, false);
        for (int i = 0; i < entries.size(); i++) {
            if (i == selected) graphics.fill(x + 3, y + 17 + i * 12, x + panelWidth - 3, y + 29 + i * 12, 0xff49355e);
            graphics.text(font, font.plainSubstrByWidth(entries.get(i).value().description().getString(), panelWidth - 12), x + 6, y + 19 + i * 12, 0xffd9b9ff, false);
        }
        if (selected >= 0) {
            box(graphics, dx, dy, detailWidth, dh);
            for (int i = 0; i < description.size(); i++) graphics.text(font, description.get(i), dx + 6, dy + 6 + i * 10, 0xffeeeeee, false);
        }
    }
    private static Component description(Holder<Enchantment> enchantment) {
        var id = enchantment.unwrapKey().orElseThrow().identifier();
        String key = "tooltip.eldrich-surge.enchantment." + id.getNamespace() + "." + id.getPath();
        return net.minecraft.locale.Language.getInstance().has(key) ? Component.translatable(key) : enchantment.value().description();
    }
    private static void box(GuiGraphicsExtractor g, int x, int y, int w, int h) {
        g.fill(x, y, x + w, y + h, 0xff180f24);
        g.outline(x, y, w, h, 0xff9874ba);
    }
}
