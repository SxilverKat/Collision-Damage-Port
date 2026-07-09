package com.sxilverr.collisiondamage.handler;

import com.sxilverr.collisiondamage.config.Config;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public class CollisionDamageHelper {

    public static void applyCollisionDamage(Entity target, double accel) {
        if (accel <= Config.accelerationThreshold) return;

        float damageValue = ((float) Math.round((accel - Config.accelerationThreshold) * 4 * Config.damageMultiplier)) / 4;
        if (Config.maxDamage > 0 && damageValue > Config.maxDamage) {
            damageValue = (float) Config.maxDamage;
        }
        if (damageValue <= 0) return;

        target.playSound(damageValue > 4 ? SoundEvents.GENERIC_BIG_FALL : SoundEvents.GENERIC_SMALL_FALL, 1.0F, 1.0F);

        DamageSource source = Config.damageTypeWall
                ? target.damageSources().flyIntoWall()
                : target.damageSources().fall();
        target.hurt(source, damageValue);
    }
}
