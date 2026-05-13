package com.sxilverr.collisiondamage.network;

import com.sxilverr.collisiondamage.CollisionDamage;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(CollisionDamage.MODID).versioned(PROTOCOL_VERSION);
        registrar.playToServer(
                PacketCollisionS.TYPE,
                PacketCollisionS.STREAM_CODEC,
                PacketCollisionS::handle
        );
    }
}
