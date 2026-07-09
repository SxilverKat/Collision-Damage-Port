package com.sxilverr.collisiondamage.network;

import com.sxilverr.collisiondamage.CollisionDamage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(CollisionDamage.MODID, "main"))
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .simpleChannel();

    public static void register() {
        int id = 0;
        INSTANCE.registerMessage(id++,
                PacketCollisionS.class,
                PacketCollisionS::encode,
                PacketCollisionS::decode,
                PacketCollisionS::handle);
    }
}
