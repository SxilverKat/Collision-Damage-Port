package com.sxilverr.collisiondamage.fabric;

import com.sxilverr.collisiondamage.handler.ClientEventHandler;
import net.fabricmc.api.ClientModInitializer;

public final class CollisionDamageFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientEventHandler.register();
    }
}
