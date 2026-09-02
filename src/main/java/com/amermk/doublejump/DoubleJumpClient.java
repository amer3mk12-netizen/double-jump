package com.amermk.doublejump;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.player.LocalPlayer;

public class DoubleJumpClient implements ClientModInitializer {

    private boolean wasOnGround = false;
    private boolean doubleJumpUsed = false;
    private boolean wasJumpPressed = false;

    @Override
    public void onInitializeClient() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            LocalPlayer player = client.player;

            if (player == null) {
                return;
            }

            boolean onGround = player.onGround();
            boolean jumpPressed = client.options.keyJump.isDown();

            // أول ما يلمس الأرض، نعيد الدبل جمب
            if (onGround) {
                doubleJumpUsed = false;
                wasOnGround = true;
            }

            // نعتبرها ضغطة جديدة فقط إذا الزر كان مرفوعًا ثم انضغط
            boolean newPress = jumpPressed && !wasJumpPressed;

            // الدبل جمب
            if (newPress
                    && !onGround
                    && !doubleJumpUsed
                    && !player.getAbilities().flying
                    && !player.isSpectator()) {

                // نفس قوة القفزة الطبيعية
                double jumpVelocity = 0.42D;

                player.setDeltaMovement(
                        player.getDeltaMovement().x,
                        jumpVelocity,
                        player.getDeltaMovement().z
                );

                doubleJumpUsed = true;
            }

            wasJumpPressed = jumpPressed;
            wasOnGround = onGround;
        });
    }
}
