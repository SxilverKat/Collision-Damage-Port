package com.sxilverr.collisiondamage;

import com.sxilverr.collisiondamage.config.Config;
import com.sxilverr.collisiondamage.network.PacketHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(CollisionDamage.MODID)
public class CollisionDamage {

    public static final String MODID = "collisiondamage";

    public CollisionDamage(IEventBus modEventBus, ModContainer container) {
        modEventBus.addListener(PacketHandler::register);
        container.registerConfig(ModConfig.Type.SERVER, Config.SPEC);
    }
}
