package eldritch.surge.client;

import eldritch.surge.menu.AdvancedEnchantmentMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.enchantment.Enchantment;
import java.util.*;

/** Infuser-style search, selectable enchantments, level controls and explicit confirmation. */
public final class AdvancedEnchantmentScreen extends AbstractContainerScreen<AdvancedEnchantmentMenu> {
    private EditBox search;
    private final List<Button> rows = new ArrayList<>();
    private final List<Integer> filtered = new ArrayList<>();
    private int offset;
    private Button previous, next, minus, plus, apply;

    public AdvancedEnchantmentScreen(AdvancedEnchantmentMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 256, 233);
        inventoryLabelX = 47;
        inventoryLabelY = 141;
    }
    @Override protected void init() {
        super.init();
        rows.clear();
        search = addRenderableWidget(new EditBox(font, leftPos + 47, topPos + 19, 201, 16, Component.translatable("gui.eldrich-surge.infuser.search")));
        search.setHint(Component.translatable("gui.eldrich-surge.infuser.search"));
        search.setResponder(value -> { offset = 0; updateButtons(); });
        for (int row = 0; row < 4; row++) {
            int position = row;
            rows.add(addRenderableWidget(Button.builder(Component.empty(), b -> {
                int i = offset + position;
                if (i < filtered.size()) send(100 + menu.offerId(filtered.get(i)));
            }).bounds(leftPos + 47, topPos + 38 + row * 18, 201, 18).build()));
        }
        previous = addRenderableWidget(Button.builder(Component.literal("<"), b -> { offset = Math.max(0, offset - 4); updateButtons(); }).bounds(leftPos + 47, topPos + 112, 20, 18).build());
        next = addRenderableWidget(Button.builder(Component.literal(">"), b -> { offset += 4; updateButtons(); }).bounds(leftPos + 70, topPos + 112, 20, 18).build());
        minus = addRenderableWidget(Button.builder(Component.literal("-"), b -> send(1)).bounds(leftPos + 96, topPos + 112, 20, 18).build());
        plus = addRenderableWidget(Button.builder(Component.literal("+"), b -> send(2)).bounds(leftPos + 139, topPos + 112, 20, 18).build());
        apply = addRenderableWidget(Button.builder(Component.translatable("gui.eldrich-surge.infuser.apply"), b -> send(3)).bounds(leftPos + 165, topPos + 112, 83, 18).build());
        updateButtons();
    }
    private void send(int button) {
        if (minecraft.gameMode != null) minecraft.gameMode.handleInventoryButtonClick(menu.containerId, button);
    }
    @Override protected void containerTick() { super.containerTick(); updateButtons(); }
    private void updateButtons() {
        if (search == null || previous == null) return;
        filtered.clear();
        String query = search.getValue().toLowerCase(Locale.ROOT);
        for (int i = 0; i < menu.offerCount(); i++) {
            var h = menu.enchantment(menu.offerId(i));
            if (h != null && (h.value().description().getString().toLowerCase(Locale.ROOT).contains(query)
                    || h.unwrapKey().orElseThrow().identifier().toString().contains(query))) filtered.add(i);
        }
        offset = Math.max(0, Math.min(offset, Math.max(0, ((filtered.size() - 1) / 4) * 4)));
        for (int row = 0; row < rows.size(); row++) {
            Button button = rows.get(row);
            int index = offset + row;
            button.visible = index < filtered.size();
            if (!button.visible) continue;
            int offer = filtered.get(index);
            var h = menu.enchantment(menu.offerId(offer));
            boolean selected = menu.offerId(offer) == menu.selectedId();
            String name = (selected ? "> " : "") + h.value().description().getString();
            String level = " " + menu.currentLevel(offer) + "/" + menu.maxLevel(offer);
            button.setMessage(Component.literal(font.plainSubstrByWidth(name, 183 - font.width(level)) + level));
            button.setTooltip(Tooltip.create(Enchantment.getFullname(h, selected ? menu.targetLevel() : menu.maxLevel(offer))));
        }
        previous.active = offset > 0;
        next.active = offset + 4 < filtered.size();
        minus.active = plus.active = menu.selectedId() >= 0;
        for (int i = 0; i < menu.offerCount(); i++) if (menu.offerId(i) == menu.selectedId()) {
            minus.active = menu.targetLevel() > menu.currentLevel(i) + 1;
            plus.active = menu.targetLevel() < menu.maxLevel(i);
        }
        var player = minecraft.player;
        apply.active = menu.xpCost() > 0 && player != null && (player.hasInfiniteMaterials()
                || (player.experienceLevel >= menu.xpCost() && menu.getSlot(1).getItem().getCount() >= menu.reagentCost()));
        apply.setTooltip(Tooltip.create(Component.translatable("gui.eldrich-surge.infuser.price", menu.xpCost(), menu.reagentCost())));
    }
    @Override public boolean mouseScrolled(double x, double y, double horizontal, double vertical) {
        if (x >= leftPos + 47 && x < leftPos + 249 && y >= topPos + 38 && y < topPos + 110) {
            offset += vertical < 0 ? 4 : -4;
            updateButtons(); return true;
        }
        return super.mouseScrolled(x, y, horizontal, vertical);
    }
    @Override public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        graphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, 0xffc6c6c6);
        graphics.outline(leftPos, topPos, imageWidth, imageHeight, 0xff37313f);
        graphics.fill(leftPos + 46, topPos + 37, leftPos + 249, topPos + 111, 0xff37313f);
        for (var slot : menu.slots) {
            int x = leftPos + slot.x, y = topPos + slot.y;
            graphics.fill(x - 1, y - 1, x + 17, y + 17, 0xff8b8b8b);
            graphics.horizontalLine(x - 1, x + 16, y - 1, 0xff373737);
            graphics.verticalLine(x - 1, y - 1, y + 16, 0xff373737);
        }
        super.extractContents(graphics, mouseX, mouseY, partialTick);
    }
    @Override protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(font, title, 8, 6, 0xff40304f, false);
        graphics.text(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0xff404040, false);
        graphics.text(font, String.valueOf(menu.targetLevel()), 120, 117, 0xff40304f, false);
        graphics.text(font, menu.xpCost() + " XP", 8, 105, 0xff40304f, false);
        graphics.text(font, "x" + menu.reagentCost(), 8, 117, 0xff40304f, false);
        if (filtered.isEmpty()) graphics.text(font, Component.translatable("gui.eldrich-surge.infuser.empty"), 52, 66, 0xffdddddd, false);
        graphics.text(font, Component.translatable(menu.hasRitual() ? "gui.eldrich-surge.infuser.ritual_ready" : "gui.eldrich-surge.infuser.ritual_needed"), 8, 131, 0xff503960, false);
    }
}
