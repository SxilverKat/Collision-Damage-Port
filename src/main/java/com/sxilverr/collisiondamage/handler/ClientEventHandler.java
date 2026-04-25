package com.sxilverr.collisiondamage.handler;

import com.sxilverr.collisiondamage.CollisionDamage;
import com.sxilverr.collisiondamage.config.Config;
import com.sxilverr.collisiondamage.network.PacketCollisionS;
import com.sxilverr.collisiondamage.network.PacketHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CollisionDamage.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START || event.player == null || !event.player.level().isClientSide) return;

        Player player = event.player;
        Vec3 motion = player.getDeltaMovement();
        double motionX = motion.x;
        double motionZ = motion.z;
        double curMotionCombined = ((double) ((int) (Math.sqrt((motionX * motionX) + (motionZ * motionZ)) * 20 * 100))) / 100;

        double prevMotionCombined = player.getPersistentData().getDouble("prevMotionCombined");
        player.getPersistentData().putDouble("prevMotionCombined", curMotionCombined);

        if (player.isFallFlying()) return;
        if (Config.ignoreWhenRiding && player.isPassenger()) return;
        if (Config.ignoreInWater && player.isInWater()) return;

        double accel = prevMotionCombined - curMotionCombined;
        if (accel > 5 && player.horizontalCollision) {
            PacketHandler.INSTANCE.sendToServer(new PacketCollisionS(accel));
        }
    }
}
