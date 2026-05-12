package com.sxilverr.collisiondamage.config;

import com.sxilverr.collisiondamage.CollisionDamage;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = CollisionDamage.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.DoubleValue ACCELERATION_THRESHOLD = BUILDER
            .comment("How large the player's deceleration must be before they will begin taking damage. (Measured in meters per second)")
            .defineInRange("accelerationThreshold", 12.0D, 5.0D, 100.0D);

    private static final ForgeConfigSpec.DoubleValue DAMAGE_MULTIPLIER = BUILDER
            .comment("Multiplies the damage taken when over the threshold. Default 1.0x is 1 damage per 1m/s/s over threshold.")
            .defineInRange("damageMultiplier", 1.0D, 0.0D, 100.0D);

    private static final ForgeConfigSpec.BooleanValue DAMAGE_TYPE_WALL = BUILDER
            .comment("Use damage type FLY_INTO_WALL? If false, will instead use FALL. (Set this to false if you want stuff like feather-falling to affect collision damage as well)")
            .define("damageTypeWall", true);

    private static final ForgeConfigSpec.DoubleValue MAX_DAMAGE = BUILDER
            .comment("Maximum damage per hit. Set to 0 for no cap.")
            .defineInRange("maxDamage", 0.0D, 0.0D, 1000.0D);

    private static final ForgeConfigSpec.BooleanValue IGNORE_WHEN_RIDING = BUILDER
            .comment("Skip collision damage while riding (boats, horses, etc).")
            .define("ignoreWhenRiding", false);

    private static final ForgeConfigSpec.BooleanValue IGNORE_IN_WATER = BUILDER
            .comment("Skip collision damage while in water.")
            .define("ignoreInWater", false);

    private static final ForgeConfigSpec.BooleanValue INCLUDE_Y_AXIS = BUILDER
            .comment("Include Y axis collision damage from upward impacts.")
            .define("includeYAxis", false);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static double accelerationThreshold;
    public static double damageMultiplier;
    public static boolean damageTypeWall;
    public static double maxDamage;
    public static boolean ignoreWhenRiding;
    public static boolean ignoreInWater;
    public static boolean includeYAxis;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) return;
        accelerationThreshold = ACCELERATION_THRESHOLD.get();
        damageMultiplier = DAMAGE_MULTIPLIER.get();
        damageTypeWall = DAMAGE_TYPE_WALL.get();
        maxDamage = MAX_DAMAGE.get();
        ignoreWhenRiding = IGNORE_WHEN_RIDING.get();
        ignoreInWater = IGNORE_IN_WATER.get();
        includeYAxis = INCLUDE_Y_AXIS.get();
    }
}
