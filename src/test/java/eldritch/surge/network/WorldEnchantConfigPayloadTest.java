package eldritch.surge.network;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;

import java.nio.file.Files;
import java.nio.file.Path;

/** Regression checks for snapshots larger than Minecraft's default UTF-8 string limit. */
public final class WorldEnchantConfigPayloadTest {
    public static void main(String[] args) throws Exception {
        String largeJson = "{\"enchantments\":\"" + "enchanted-è-魔法".repeat(8_000) + "\"}";
        roundTrip(largeJson, true);
        roundTrip(largeJson, false);
        Path preset = Path.of("run/config/eldritch-surge/enchantment_caps.json");
        if (Files.exists(preset)) roundTrip(Files.readString(preset), true);
        try {
            ConfigSnapshotCompression.decode(new byte[]{1, 2, 3});
            throw new AssertionError("Invalid gzip accepted");
        } catch (DecoderException expected) { }
        try {
            ConfigSnapshotCompression.encode("x".repeat(ConfigSnapshotCompression.MAX_JSON_BYTES + 1));
            throw new AssertionError("Oversized JSON accepted");
        } catch (EncoderException expected) { }
        java.io.ByteArrayOutputStream oversized = new java.io.ByteArrayOutputStream();
        try (java.util.zip.GZIPOutputStream gzip = new java.util.zip.GZIPOutputStream(oversized)) {
            gzip.write(new byte[ConfigSnapshotCompression.MAX_JSON_BYTES + 1]);
        }
        try {
            ConfigSnapshotCompression.decode(oversized.toByteArray());
            throw new AssertionError("Oversized decompressed input accepted");
        } catch (DecoderException expected) { }
        System.out.println("World configuration codec regression checks passed.");
    }

    private static void roundTrip(String json, boolean editable) {
        RegistryFriendlyByteBuf buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), RegistryAccess.EMPTY);
        try {
            WorldEnchantConfigPayload original = new WorldEnchantConfigPayload(json, editable);
            WorldEnchantConfigPayload.CODEC.encode(buffer, original);
            if (buffer.readableBytes() >= 32_767) throw new AssertionError("Packet exceeds serverbound budget");
            WorldEnchantConfigPayload decoded = WorldEnchantConfigPayload.CODEC.decode(buffer);
            if (!original.equals(decoded) || buffer.isReadable()) throw new AssertionError("Snapshot changed in transit");
        } finally {
            buffer.release();
        }
    }
}
