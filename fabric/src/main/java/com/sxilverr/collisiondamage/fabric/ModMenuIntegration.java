package com.sxilverr.collisiondamage.fabric;

import com.sxilverr.collisiondamage.config.CollisionDamageConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;

public final class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(CollisionDamageConfig.class, parent).get();
    }
}
