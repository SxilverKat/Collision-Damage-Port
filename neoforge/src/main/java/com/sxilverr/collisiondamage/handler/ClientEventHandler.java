package com.sxilverr.collisiondamage.handler;

import com.sxilverr.collisiondamage.CollisionDamage;
import com.sxilverr.collisiondamage.config.Config;
import com.sxilverr.collisiondamage.network.PacketCollisionS;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = CollisionDamage.MODID, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player == null || !player.level().isClientSide) return;

        Entity source = player.isPassenger() ? player.getRootVehicle() : player;
        Vec3 motion = source.getDeltaMovement();
        double motionX = motion.x;
        double motionZ = motion.z;
        double squareSum = (motionX * motionX) + (motionZ * motionZ);
        if (Config.includeYAxis && motion.y > 0) {
            double motionY = motion.y;
            squareSum += motionY * motionY;
        }
        double curMotionCombined = ((double) ((int) (Math.sqrt(squareSum) * 20 * 100))) / 100;

        double prevMotionCombined = player.getPersistentData().getDouble("prevMotionCombined");
        player.getPersistentData().putDouble("prevMotionCombined", curMotionCombined);

        if (player.isFallFlying()) return;
        if (Config.ignoreWhenRiding && player.isPassenger()) return;
        if (Config.ignoreInWater && player.isInWater()) return;

        double accel = prevMotionCombined - curMotionCombined;
        boolean ceilingCollision = source.verticalCollision && !source.verticalCollisionBelow;
        boolean prevCeilingCollision = player.getPersistentData().getBoolean("prevCeilingCollision");
        player.getPersistentData().putBoolean("prevCeilingCollision", ceilingCollision);
        boolean newCeilingCollision = ceilingCollision && !prevCeilingCollision;
        boolean collided = source.horizontalCollision || (Config.includeYAxis && newCeilingCollision);
        if (accel > Config.accelerationThreshold && collided) {
            PacketDistributor.sendToServer(new PacketCollisionS(accel));
        }
    }
}
