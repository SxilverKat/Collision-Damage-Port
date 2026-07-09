package com.sxilverr.collisiondamage.network;

import com.sxilverr.collisiondamage.CollisionDamage;
import com.sxilverr.collisiondamage.config.Config;
import com.sxilverr.collisiondamage.handler.CollisionDamageHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketCollisionS(double accel) implements CustomPacketPayload {

    public static final Type<PacketCollisionS> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(CollisionDamage.MODID, "collision_s"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PacketCollisionS> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.DOUBLE, PacketCollisionS::accel,
            PacketCollisionS::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(PacketCollisionS msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer player)) return;

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
    }
}
