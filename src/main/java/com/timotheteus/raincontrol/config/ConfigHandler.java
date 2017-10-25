package com.timotheteus.raincontrol.config;

import com.timotheteus.raincontrol.util.ModUtil;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = ModUtil.MOD_ID)
public class ConfigHandler {

//    public static Integer generatorProduce = 0;
//    public static Integer generatorProduceNew;

    public static final General GENERAL = new General();

    public static class General {

        @Config.Comment("The FE output of the generator")
        @Config.RangeInt(min = 0)
        public static Integer generatorGeneration = 40;

    }

//    public static void readConfig() {
//        Configuration cfg = CommonProxy.config;
//        try {
//            cfg.load();
//            initGeneralConfig(cfg);
//        } catch (Exception e) {
//            ModUtil.LOGGER.log(java.util.logging.Level.CONFIG, "Problem loading config file!", e);
//        } finally {
//            if (cfg.hasChanged())
//                cfg.save();
//        }
//    }

//    private static void initGeneralConfig(Configuration cfg) {
//        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
//        generatorGeneration = cfg.getInt("generator output", CATEGORY_GENERAL, 40, 0, Integer.MAX_VALUE, );
//    }


    @Mod.EventBusSubscriber(modid = ModUtil.MOD_ID)
    private static class EventHandler {

        /**
         * Inject the new values and save to the config file when the config has been changed from the GUI.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(ModUtil.MOD_ID)) {
                ConfigManager.sync(ModUtil.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }


}
