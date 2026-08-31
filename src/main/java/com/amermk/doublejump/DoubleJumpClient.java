package com.amermk.doublejump;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class DoubleJumpClient implements ClientModInitializer {

    private boolean previousJump = false;
    private boolean doubleJumpUsed = false;

    @Override
    public void onInitializeClient() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player == null) {
                return;
            }

            boolean onGround = client.player.onGround();
            boolean jumpPressed = client.options.keyJump.isDown();

            if (onGround) {
                doubleJumpUsed = false;
            }

            boolean newPress = jumpPressed && !previousJump;

            if (newPress
                    && !onGround
                    && !doubleJumpUsed
                    && !client.player.getAbilities().flying
                    && !client.player.isSpectator()) {

                ClientPlayNetworking.send(new DoubleJumpPayload());
                doubleJumpUsed = true;
            }

            previousJump = jumpPressed;
        });
    }
}

