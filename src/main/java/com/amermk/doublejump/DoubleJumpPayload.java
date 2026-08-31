package com.amermk.doublejump;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public record DoubleJumpPayload() implements CustomPacketPayload {

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(
                    DoubleJump.MOD_ID,
                    "double_jump"
            );

    public static final Type<DoubleJumpPayload> TYPE =
            new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, DoubleJumpPayload> CODEC =
            StreamCodec.unit(new DoubleJumpPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void register() {
        PayloadTypeRegistry.playC2S().register(TYPE, CODEC);

        ServerPlayNetworking.registerGlobalReceiver(
                TYPE,
                (payload, context) -> {
                    ServerPlayer player = context.player();

                    if (player.onGround()
                            || player.getAbilities().flying
                            || player.isSpectator()
                            || player.getPersistentData().getBoolean("doublejump_used")) {
                        return;
                    }

                    Vec3 velocity = player.getDeltaMovement();

                    player.setDeltaMovement(
                            velocity.x,
                            0.62D,
                            velocity.z
                    );

                    player.hurtMarked = true;
                    player.getPersistentData().putBoolean("doublejump_used", true);
                }
        );
    }
}

