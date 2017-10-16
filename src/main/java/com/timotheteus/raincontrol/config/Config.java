package com.timotheteus.raincontrol.config;

import com.timotheteus.raincontrol.proxy.CommonProxy;
import com.timotheteus.raincontrol.util.ModUtil;
import net.minecraftforge.common.config.Configuration;

public class Config {

    private static final String CATEGORY_GENERAL = "general";

    public static int generatorProduce = 0;

    public static void readConfig() {
        Configuration cfg = CommonProxy.config;
        try {
            cfg.load();
            initGeneralConfig(cfg);
        } catch (Exception e) {
            ModUtil.LOGGER.log(java.util.logging.Level.CONFIG, "Problem loading config file!", e);
        } finally {
            if (cfg.hasChanged())
                cfg.save();
        }
    }

    private static void initGeneralConfig(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
        generatorProduce = cfg.getInt("generator output", CATEGORY_GENERAL, 40, 0, Integer.MAX_VALUE, "The FE output of the generator");
    }


}
