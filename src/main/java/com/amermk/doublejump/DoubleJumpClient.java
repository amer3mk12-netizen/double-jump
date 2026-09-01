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

            // بعد ما يلمس الأرض، نسمح بالدبل جمب من جديد
            if (player.onGround()) {
                usedAirJump = false;
            }

            boolean jumpDown = client.options.keyJump.isDown();

            // القفزة الثانية
            if (jumpDown
                    && !wasJumpDown
                    && !player.onGround()
                    && !usedAirJump
                    && !player.getAbilities().flying) {

                // قوة القفز الطبيعية للاعب العادي
                double jumpVelocity = 1.00D;

                player.setDeltaMovement(
                        player.getDeltaMovement().x,
                        jumpVelocity,
                        player.getDeltaMovement().z
                );

                usedAirJump = true;
            }

            wasJumpDown = jumpDown;
        });
    }
}
