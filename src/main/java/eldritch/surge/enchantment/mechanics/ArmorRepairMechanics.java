package eldritch.surge.enchantment.mechanics;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public final class ArmorRepairMechanics {
    private static final Identifier PHALANX_ARMOR = Identifier.fromNamespaceAndPath("eldrich-surge", "phalanx_armor");
    private static final Identifier PHALANX_TOUGHNESS = Identifier.fromNamespaceAndPath("eldrich-surge", "phalanx_toughness");
    private static final Identifier PHALANX_SPEED = Identifier.fromNamespaceAndPath("eldrich-surge", "phalanx_speed");
    private ArmorRepairMechanics() {
    }

    public static void initialize() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                var level = (net.minecraft.server.level.ServerLevel) player.level();
                applyPhalanxStance(player);
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    if (!slot.isArmor()) {
                        continue;
                    }
                    ItemStack armor = player.getItemBySlot(slot);
                    if (armor.isEmpty() || !armor.isDamaged()) {
                        continue;
                    }

                    int dwarf = EnchantmentLevels.onItem(level, armor, "dwarf_flakes");
                    if (dwarf > 0 && player.tickCount % (4800 / dwarf) == 0
                            && level.getMaxLocalRawBrightness(player.blockPosition()) < 8) {
                        repair(armor, 1);
                    }

                    int end = EnchantmentLevels.onItem(level, armor, "end_flakes");
                    if (end > 0 && player.tickCount % 20 == 0
                            && !player.onGround() && level.getRandom().nextInt(100) < end) {
                        repair(armor, 1);
                    }

                    int hell = EnchantmentLevels.onItem(level, armor, "hell_flakes");
                    if (hell > 0 && player.tickCount % 20 == 0 && player.isOnFire()) {
                        repair(armor, hell);
                    }

                    int sea = EnchantmentLevels.onItem(level, armor, "sea_flakes");
                    if (sea > 0 && player.tickCount % (4800 / sea) == 0
                            && (player.isInWater() || level.isRainingAt(player.blockPosition()))) {
                        repair(armor, 1);
                    }
                }
            }
        });
    }

    private static void applyPhalanxStance(ServerPlayer player) {
        int level = player.getMainHandItem().is(Items.SHIELD)
                && player.getOffhandItem().is(ItemTags.SPEARS)
                ? EnchantmentLevels.onItem(player.level(), player.getOffhandItem(), "phalanx_stance")
                : 0;
        TransientAttributeModifiers.set(player.getAttribute(Attributes.ARMOR), PHALANX_ARMOR, level * 2.0D);
        TransientAttributeModifiers.set(player.getAttribute(Attributes.ARMOR_TOUGHNESS), PHALANX_TOUGHNESS, level);
        TransientAttributeModifiers.set(player.getAttribute(Attributes.MOVEMENT_SPEED), PHALANX_SPEED, -0.1D * level);
    }

    private static void repair(ItemStack stack, int points) {
        stack.setDamageValue(Math.max(0, stack.getDamageValue() - points));
    }
}
