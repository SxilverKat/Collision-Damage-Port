package com.sxilverr.collisiondamage.network;

import com.sxilverr.collisiondamage.CollisionDamage;
import com.sxilverr.collisiondamage.config.Config;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
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
        });
    }
}
