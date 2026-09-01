package com.amermk.doublejump;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.player.LocalPlayer;

public class DoubleJumpClient implements ClientModInitializer {

    private boolean wasJumpDown = false;
    private boolean usedAirJump = false;

    @Override
    public void onInitializeClient() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            LocalPlayer player = client.player;

            if (player == null) {
                return;
            }

            if (player.onGround()) {
                usedAirJump = false;
            }

            boolean jumpDown = client.options.keyJump.isDown();

            if (jumpDown && !wasJumpDown && !player.onGround()
                    && !player.isSpectator()
                    && !usedAirJump
                    && !player.getAbilities().flying) {

                player.setDeltaMovement(
                        player.getDeltaMovement().x,
                        0.42D,
                        player.getDeltaMovement().z
                );

                usedAirJump = true;
            }

            wasJumpDown = jumpDown;
        });
    }
}
