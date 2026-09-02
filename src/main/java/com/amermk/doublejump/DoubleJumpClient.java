package com.amermk.doublejump;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.player.LocalPlayer;

public class DoubleJumpClient implements ClientModInitializer {

    private boolean wasJumpDown = false;
    private boolean canDoubleJump = false;

    @Override
    public void onInitializeClient() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            LocalPlayer player = client.player;

            if (player == null) {
                return;
            }

            boolean jumpDown = client.options.keyJump.isDown();

            // اللاعب لمس الأرض -> نجهز دبل جمب جديد
            if (player.onGround()) {
                canDoubleJump = true;
            }

            // ضغطة Space جديدة
            boolean newJumpPress = jumpDown && !wasJumpDown;

            // القفزة الثانية
            if (newJumpPress
                    && canDoubleJump
                    && !player.onGround()
                    && !player.getAbilities().flying
                    && !player.isSpectator()) {

                // نفس قوة القفز الطبيعية
                player.setDeltaMovement(
                        player.getDeltaMovement().x,
                        0.42D,
                        player.getDeltaMovement().z
                );

                canDoubleJump = false;
            }

            wasJumpDown = jumpDown;
        });
    }
}
