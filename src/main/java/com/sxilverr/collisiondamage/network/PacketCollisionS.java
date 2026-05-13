package com.sxilverr.collisiondamage.network;

import com.sxilverr.collisiondamage.config.Config;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.event.network.CustomPayloadEvent;

public class PacketCollisionS {

    private final double accel;

    public PacketCollisionS(double accel) {
        this.accel = accel;
    }

    public static void encode(PacketCollisionS msg, FriendlyByteBuf buf) {
        buf.writeDouble(msg.accel);
    }

    public static PacketCollisionS decode(FriendlyByteBuf buf) {
        return new PacketCollisionS(buf.readDouble());
    }

    public static void handle(PacketCollisionS msg, CustomPayloadEvent.Context ctx) {
        ServerPlayer player = ctx.getSender();
        if (player == null) {
            ctx.setPacketHandled(true);
            return;
        }

        double accel = msg.accel;
        if (accel > Config.accelerationThreshold) {
            float damageValue = ((float) Math.round((accel - Config.accelerationThreshold) * 4 * Config.damageMultiplier)) / 4;

            if (Config.maxDamage > 0 && damageValue > Config.maxDamage) {
                damageValue = (float) Config.maxDamage;
            }

            player.playSound(damageValue > 4 ? SoundEvents.GENERIC_BIG_FALL : SoundEvents.GENERIC_SMALL_FALL, 1.0F, 1.0F);

            DamageSource source = Config.damageTypeWall
                    ? player.damageSources().flyIntoWall()
                    : player.damageSources().fall();
            player.hurt(source, damageValue);
        }
        ctx.setPacketHandled(true);
    }
}
