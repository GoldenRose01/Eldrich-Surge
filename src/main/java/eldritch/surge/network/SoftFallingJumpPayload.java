package eldritch.surge.network;

import eldritch.surge.EldritchSurge;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record SoftFallingJumpPayload(boolean pressed) implements CustomPacketPayload {
    public static final Type<SoftFallingJumpPayload> TYPE =
            new Type<>(EldritchSurge.id("soft_falling_jump"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SoftFallingJumpPayload> CODEC =
            StreamCodec.composite(ByteBufCodecs.BOOL, SoftFallingJumpPayload::pressed, SoftFallingJumpPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
