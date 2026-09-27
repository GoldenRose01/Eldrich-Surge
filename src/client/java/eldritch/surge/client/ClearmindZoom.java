package eldritch.surge.client;

import eldritch.surge.enchantment.mechanics.EnchantmentLevels;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Items;

public final class ClearmindZoom {
    private static boolean active;
    private static int stillTicks;
    private static double x;
    private static double y;
    private static double z;

    private ClearmindZoom() {
    }

    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> tick(client));
    }

    public static boolean isZoomActive() {
        return active;
    }

    private static void tick(Minecraft client) {
        if (client.player == null) {
            active = false;
            stillTicks = 0;
            return;
        }

        var player = client.player;
        var weapon = player.getMainHandItem();
        var offhand = player.getOffhandItem();
        boolean holdingAimedWeapon = (weapon.is(Items.BOW) || weapon.is(Items.CROSSBOW))
                && EnchantmentLevels.onItem(player.level(), weapon, "clearmind") > 0
                || (offhand.is(Items.BOW) || offhand.is(Items.CROSSBOW))
                && EnchantmentLevels.onItem(player.level(), offhand, "clearmind") > 0;
        boolean holdingAim = client.options.keyUse.isDown();
        if (!holdingAimedWeapon || !holdingAim) {
            active = false;
            stillTicks = 0;
            return;
        }

        double dx = player.getX() - x;
        double dy = player.getY() - y;
        double dz = player.getZ() - z;
        if (dx * dx + dy * dy + dz * dz < 0.0001) {
            stillTicks++;
        } else {
            stillTicks = 0;
        }
        x = player.getX();
        y = player.getY();
        z = player.getZ();
        active = stillTicks >= 200;
    }
}
