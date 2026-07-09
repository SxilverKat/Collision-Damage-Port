package com.sxilverr.collisiondamage.config;

import com.sxilverr.collisiondamage.CollisionDamage;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = CollisionDamage.MODID)
public class ConfigSpec {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.DoubleValue ACCELERATION_THRESHOLD = BUILDER
            .comment("How large the player's deceleration must be before they will begin taking damage. (Measured in meters per second)")
            .defineInRange("accelerationThreshold", 12.0D, 5.0D, 100.0D);

    private static final ModConfigSpec.DoubleValue DAMAGE_MULTIPLIER = BUILDER
            .comment("Multiplies the damage taken when over the threshold. Default 1.0x is 1 damage per 1m/s/s over threshold.")
            .defineInRange("damageMultiplier", 1.0D, 0.0D, 100.0D);

    private static final ModConfigSpec.BooleanValue DAMAGE_TYPE_WALL = BUILDER
            .comment("Use damage type FLY_INTO_WALL? If false, will instead use FALL. (Set this to false if you want stuff like feather-falling to affect collision damage as well)")
            .define("damageTypeWall", true);

    private static final ModConfigSpec.DoubleValue MAX_DAMAGE = BUILDER
            .comment("Maximum damage per hit. Set to 0 for no cap.")
            .defineInRange("maxDamage", 0.0D, 0.0D, 1000.0D);

    private static final ModConfigSpec.BooleanValue IGNORE_WHEN_RIDING = BUILDER
            .comment("Skip collision damage while riding (boats, horses, etc).")
            .define("ignoreWhenRiding", false);

    private static final ModConfigSpec.BooleanValue IGNORE_IN_WATER = BUILDER
            .comment("Skip collision damage while in water.")
            .define("ignoreInWater", false);

    private static final ModConfigSpec.BooleanValue INCLUDE_Y_AXIS = BUILDER
            .comment("Include Y axis collision damage from upward impacts.")
            .define("includeYAxis", false);

    private static final ModConfigSpec.BooleanValue DAMAGE_VEHICLE = BUILDER
            .comment("Damage the vehicle when the rider triggers collision damage.")
            .define("damageVehicle", true);

    private static final ModConfigSpec.BooleanValue GLOBAL_COLLISION_DAMAGE = BUILDER
            .comment("Apply collision damage to all living entities.")
            .define("globalCollisionDamage", false);

    public static final ModConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) return;
        Config.accelerationThreshold = ACCELERATION_THRESHOLD.get();
        Config.damageMultiplier = DAMAGE_MULTIPLIER.get();
        Config.damageTypeWall = DAMAGE_TYPE_WALL.get();
        Config.maxDamage = MAX_DAMAGE.get();
        Config.ignoreWhenRiding = IGNORE_WHEN_RIDING.get();
        Config.ignoreInWater = IGNORE_IN_WATER.get();
        Config.includeYAxis = INCLUDE_Y_AXIS.get();
        Config.damageVehicle = DAMAGE_VEHICLE.get();
        Config.globalCollisionDamage = GLOBAL_COLLISION_DAMAGE.get();
    }
}
