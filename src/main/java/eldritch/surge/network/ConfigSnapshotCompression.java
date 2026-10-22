package eldritch.surge.network;

import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

final class ConfigSnapshotCompression {
    // Leave room for the payload identifier and editable flag in serverbound packets.
    static final int MAX_COMPRESSED_BYTES = 30_000;
    static final int MAX_JSON_BYTES = 1_048_576;

    private ConfigSnapshotCompression() {}

    static byte[] encode(String json) {
        byte[] raw = json.getBytes(StandardCharsets.UTF_8);
        if (raw.length > MAX_JSON_BYTES) throw new EncoderException("World enchantment configuration is too large");
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try (GZIPOutputStream gzip = new GZIPOutputStream(output)) {
                gzip.write(raw);
            }
            byte[] compressed = output.toByteArray();
            if (compressed.length > MAX_COMPRESSED_BYTES) {
                throw new EncoderException("Compressed world enchantment configuration is too large");
            }
            return compressed;
        } catch (IOException exception) {
            throw new EncoderException("Could not compress world enchantment configuration", exception);
        }
    }

    static String decode(byte[] compressed) {
        try (GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(compressed))) {
            byte[] raw = gzip.readNBytes(MAX_JSON_BYTES + 1);
            if (raw.length > MAX_JSON_BYTES) throw new DecoderException("World enchantment configuration is too large");
            return new String(raw, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new DecoderException("Invalid compressed world enchantment configuration", exception);
        }
    }
}
