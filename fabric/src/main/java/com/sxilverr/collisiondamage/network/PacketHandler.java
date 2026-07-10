package com.sxilverr.collisiondamage.network;

import com.sxilverr.collisiondamage.CollisionDamage;
import com.sxilverr.collisiondamage.config.Config;
import com.sxilverr.collisiondamage.handler.CollisionDamageHelper;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
//? if >=1.21.1 {
/*import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
*///?}

public final class PacketHandler {

    private PacketHandler() {
    }

    //? if >=1.21.1 {
    /*public record PacketCollisionS(double accel) implements CustomPacketPayload {

        public static final CustomPacketPayload.Type<PacketCollisionS> TYPE =
                new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(CollisionDamage.MODID, "collision_s"));

        public static final StreamCodec<RegistryFriendlyByteBuf, PacketCollisionS> STREAM_CODEC =
                StreamCodec.composite(ByteBufCodecs.DOUBLE, PacketCollisionS::accel, PacketCollisionS::new);

        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static void register() {
        PayloadTypeRegistry.playC2S().register(PacketCollisionS.TYPE, PacketCollisionS.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(PacketCollisionS.TYPE, (payload, context) -> {
            ServerPlayer player = context.player();
            double accel = payload.accel();
            player.getServer().execute(() -> handle(player, accel));
        });
    }
    *///?}

    //? if <1.21.1 {
    public static final ResourceLocation COLLISION_S = new ResourceLocation(CollisionDamage.MODID, "collision_s");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(COLLISION_S, (server, player, handler, buf, responseSender) -> {
            double accel = buf.readDouble();
            server.execute(() -> handle(player, accel));
        });
    }
    //?}

    private static void handle(ServerPlayer player, double accel) {
        if (player == null) return;

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
    }
}
