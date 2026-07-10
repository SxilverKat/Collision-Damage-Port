package com.sxilverr.collisiondamage.config;

import com.sxilverr.collisiondamage.CollisionDamage;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = CollisionDamage.MODID)
public class CollisionDamageConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    public double accelerationThreshold = 12.0D;

    @ConfigEntry.Gui.Tooltip
    public double damageMultiplier = 1.0D;

    @ConfigEntry.Gui.Tooltip
    public boolean damageTypeWall = true;

    @ConfigEntry.Gui.Tooltip
    public double maxDamage = 0.0D;

    @ConfigEntry.Gui.Tooltip
    public boolean ignoreWhenRiding = false;

    @ConfigEntry.Gui.Tooltip
    public boolean ignoreInWater = false;

    @ConfigEntry.Gui.Tooltip
    public boolean includeYAxis = false;

    @ConfigEntry.Gui.Tooltip
    public boolean damageVehicle = true;

    @ConfigEntry.Gui.Tooltip
    public boolean globalCollisionDamage = false;

    @Override
    public void validatePostLoad() {
        accelerationThreshold = clamp(accelerationThreshold, 5.0D, 100.0D);
        damageMultiplier = clamp(damageMultiplier, 0.0D, 100.0D);
        maxDamage = clamp(maxDamage, 0.0D, 1000.0D);
    }

    private static double clamp(double value, double min, double max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }
}
