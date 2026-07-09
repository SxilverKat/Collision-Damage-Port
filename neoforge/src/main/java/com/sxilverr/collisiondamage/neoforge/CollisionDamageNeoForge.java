package com.sxilverr.collisiondamage.neoforge;

import com.sxilverr.collisiondamage.CollisionDamage;
import com.sxilverr.collisiondamage.config.ConfigSpec;
import com.sxilverr.collisiondamage.network.PacketHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(CollisionDamage.MODID)
public class CollisionDamageNeoForge {

    public CollisionDamageNeoForge(IEventBus modEventBus, ModContainer container) {
        modEventBus.addListener(PacketHandler::register);
        container.registerConfig(ModConfig.Type.SERVER, ConfigSpec.SPEC);
    }
}
