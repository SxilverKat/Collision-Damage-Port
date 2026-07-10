package com.sxilverr.collisiondamage.handler;

import net.minecraft.world.entity.Entity;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

final class MotionTracker {

    static final class Data {
        double prevMotionCombined;
        boolean prevCeilingCollision;
    }

    private static final Map<Entity, Data> STATE =
            Collections.synchronizedMap(new WeakHashMap<>());

    private MotionTracker() {
    }

    static Data get(Entity entity) {
        return STATE.computeIfAbsent(entity, key -> new Data());
    }
}
