package com.sxilverr.collisiondamage.network;

import com.sxilverr.collisiondamage.config.Config;
import com.sxilverr.collisiondamage.handler.CollisionDamageHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

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

    public static void handle(PacketCollisionS msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null) return;

            double accel = msg.accel;
            CollisionDamageHelper.applyCollisionDamage(player, accel);

            if (Config.damageVehicle && player.isPassenger()) {
                Entity vehicle = player.getRootVehicle();
                if (vehicle != player) {
                    boolean handledByGlobal = Config.globalCollisionDamage && vehicle instanceof LivingEntity;
                    if (!handledByGlobal) {
                        CollisionDamageHelper.applyCollisionDamage(vehicle, accel);
                    }
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}
