package com.sxilverr.collisiondamage.handler;

import com.sxilverr.collisiondamage.config.Config;
import com.sxilverr.collisiondamage.config.ConfigSpec;
import com.sxilverr.collisiondamage.network.PacketHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
//? if <1.21.1 {
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
//?}

@Environment(EnvType.CLIENT)
public final class ClientEventHandler {

    private ClientEventHandler() {
    }

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(ClientEventHandler::onClientTick);
    }

    private static void onClientTick(Minecraft client) {
        Player player = client.player;
        if (player == null || !player.level().isClientSide) return;
        ConfigSpec.sync();

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

        MotionTracker.Data data = MotionTracker.get(player);
        double prevMotionCombined = data.prevMotionCombined;
        data.prevMotionCombined = curMotionCombined;

        if (player.isFallFlying()) return;
        if (Config.ignoreWhenRiding && player.isPassenger()) return;
        if (Config.ignoreInWater && player.isInWater()) return;

        double accel = prevMotionCombined - curMotionCombined;
        boolean ceilingCollision = source.verticalCollision && !source.verticalCollisionBelow;
        boolean prevCeilingCollision = data.prevCeilingCollision;
        data.prevCeilingCollision = ceilingCollision;
        boolean newCeilingCollision = ceilingCollision && !prevCeilingCollision;
        boolean collided = source.horizontalCollision || (Config.includeYAxis && newCeilingCollision);
        if (accel > Config.accelerationThreshold && collided) {
            sendToServer(accel);
        }
    }

    private static void sendToServer(double accel) {
        //? if >=1.21.1 {
        /*ClientPlayNetworking.send(new PacketHandler.PacketCollisionS(accel));
        *///?}
        //? if <1.21.1 {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeDouble(accel);
        ClientPlayNetworking.send(PacketHandler.COLLISION_S, buf);
        //?}
    }
}
