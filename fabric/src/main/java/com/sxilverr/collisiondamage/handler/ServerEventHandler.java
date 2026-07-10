package com.sxilverr.collisiondamage.handler;

import com.sxilverr.collisiondamage.config.Config;
import com.sxilverr.collisiondamage.config.ConfigSpec;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public final class ServerEventHandler {

    private ServerEventHandler() {
    }

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(ServerEventHandler::onServerTick);
    }

    private static void onServerTick(MinecraftServer server) {
        ConfigSpec.sync();
        if (!Config.globalCollisionDamage) return;

        for (ServerLevel level : server.getAllLevels()) {
            for (Entity entity : level.getAllEntities()) {
                if (!(entity instanceof LivingEntity living)) continue;
                if (living instanceof Player) continue;
                processEntity(living);
            }
        }
    }

    private static void processEntity(LivingEntity entity) {
        Entity source = entity.isPassenger() ? entity.getRootVehicle() : entity;
        Vec3 motion = source.getDeltaMovement();
        double motionX = motion.x;
        double motionZ = motion.z;
        double squareSum = (motionX * motionX) + (motionZ * motionZ);
        if (Config.includeYAxis && motion.y > 0) {
            double motionY = motion.y;
            squareSum += motionY * motionY;
        }
        double curMotionCombined = ((double) ((int) (Math.sqrt(squareSum) * 20 * 100))) / 100;

        MotionTracker.Data data = MotionTracker.get(entity);
        double prevMotionCombined = data.prevMotionCombined;
        data.prevMotionCombined = curMotionCombined;

        if (entity.isFallFlying()) return;
        if (Config.ignoreWhenRiding && entity.isPassenger()) return;
        if (Config.ignoreInWater && entity.isInWater()) return;

        double accel = prevMotionCombined - curMotionCombined;
        boolean ceilingCollision = source.verticalCollision && !source.verticalCollisionBelow;
        boolean prevCeilingCollision = data.prevCeilingCollision;
        data.prevCeilingCollision = ceilingCollision;
        boolean newCeilingCollision = ceilingCollision && !prevCeilingCollision;
        boolean collided = source.horizontalCollision || (Config.includeYAxis && newCeilingCollision);

        if (accel > Config.accelerationThreshold && collided) {
            CollisionDamageHelper.applyCollisionDamage(entity, accel);
        }
    }
}
