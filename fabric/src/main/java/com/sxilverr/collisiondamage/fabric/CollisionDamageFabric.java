package com.sxilverr.collisiondamage.fabric;

import com.sxilverr.collisiondamage.config.ConfigSpec;
import com.sxilverr.collisiondamage.handler.ServerEventHandler;
import com.sxilverr.collisiondamage.network.PacketHandler;
import net.fabricmc.api.ModInitializer;

public final class CollisionDamageFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ConfigSpec.register();
        PacketHandler.register();
        ServerEventHandler.register();
    }
}
