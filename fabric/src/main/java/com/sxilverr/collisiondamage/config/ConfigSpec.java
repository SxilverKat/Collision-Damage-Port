package com.sxilverr.collisiondamage.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

public final class ConfigSpec {

    private static ConfigHolder<CollisionDamageConfig> holder;

    private ConfigSpec() {
    }

    public static void register() {
        holder = AutoConfig.register(CollisionDamageConfig.class, GsonConfigSerializer::new);
        sync();
    }

    public static void sync() {
        if (holder == null) return;
        CollisionDamageConfig config = holder.getConfig();
        Config.accelerationThreshold = config.accelerationThreshold;
        Config.damageMultiplier = config.damageMultiplier;
        Config.damageTypeWall = config.damageTypeWall;
        Config.maxDamage = config.maxDamage;
        Config.ignoreWhenRiding = config.ignoreWhenRiding;
        Config.ignoreInWater = config.ignoreInWater;
        Config.includeYAxis = config.includeYAxis;
        Config.damageVehicle = config.damageVehicle;
        Config.globalCollisionDamage = config.globalCollisionDamage;
    }
}
