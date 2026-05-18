package com.sxilverr.collisiondamage.handler;

import com.sxilverr.collisiondamage.CollisionDamage;
import com.sxilverr.collisiondamage.config.Config;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = CollisionDamage.MODID)
public class ServerEventHandler {

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;
        if (entity.level().isClientSide) return;
        if (!Config.globalCollisionDamage) return;
        if (entity instanceof Player) return;

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

        double prevMotionCombined = entity.getPersistentData().getDouble("prevMotionCombined");
        entity.getPersistentData().putDouble("prevMotionCombined", curMotionCombined);

        if (entity.isFallFlying()) return;
        if (Config.ignoreWhenRiding && entity.isPassenger()) return;
        if (Config.ignoreInWater && entity.isInWater()) return;

        double accel = prevMotionCombined - curMotionCombined;
        boolean ceilingCollision = source.verticalCollision && !source.verticalCollisionBelow;
        boolean prevCeilingCollision = entity.getPersistentData().getBoolean("prevCeilingCollision");
        entity.getPersistentData().putBoolean("prevCeilingCollision", ceilingCollision);
        boolean newCeilingCollision = ceilingCollision && !prevCeilingCollision;
        boolean collided = source.horizontalCollision || (Config.includeYAxis && newCeilingCollision);

        if (accel > Config.accelerationThreshold && collided) {
            CollisionDamageHelper.applyCollisionDamage(entity, accel);
        }
    }
}
