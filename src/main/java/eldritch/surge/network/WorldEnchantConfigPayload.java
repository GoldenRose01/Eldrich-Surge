package eldritch.surge.network;

import eldritch.surge.EldritchSurge;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/** Complete server-authoritative enchantment configuration for the active world. */
public record WorldEnchantConfigPayload(String json) implements CustomPacketPayload {
    public static final Type<WorldEnchantConfigPayload> TYPE =
            new Type<>(EldritchSurge.id("world_enchant_config"));
    public static final StreamCodec<RegistryFriendlyByteBuf, WorldEnchantConfigPayload> CODEC =
            StreamCodec.composite(ByteBufCodecs.STRING_UTF8, WorldEnchantConfigPayload::json, WorldEnchantConfigPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
