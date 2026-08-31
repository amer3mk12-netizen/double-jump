package com.amermk.doublejump;

import net.fabricmc.api.ModInitializer;

public class DoubleJump implements ModInitializer {

    public static final String MOD_ID = "doublejump";

    @Override
    public void onInitialize() {
        DoubleJumpPayload.register();
    }
}

