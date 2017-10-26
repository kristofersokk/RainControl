package com.timotheteus.raincontrol.config;

import com.timotheteus.raincontrol.util.ModUtil;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = ModUtil.MOD_ID)
public class ConfigHandler {

    public static RainBlock rainBlock = new RainBlock();
    public static Generator generator = new Generator();

    //TODO use FE config numbers instead of fixed numbers

    //TODO find out how to use XP

    public static class RainBlock {
        @Config.Comment("What way of activation? [WIP]")
        public static ActivationType type = ActivationType.FE;

        public enum ActivationType {
            FE,
            FREE,
            XP_WIP
        }

        @Config.Comment("[FE] FE needed to activate the block")
        @Config.RangeInt(min = 0)
        public static int FE_activation = 1000000;

        @Config.Comment("[FE] max FE input")
        @Config.RangeInt(min = 0)
        public static int FE_input = 2000;

        @Config.Comment("[FE] inner FE capacity")
        @Config.RangeInt(min = 0)
        public static int FE_capacity = 10000000;

        @Config.Comment("[XP] levels needed to activate")
        @Config.RangeInt(min = 0)
        public static int levels = 10;


    }



    public static class Generator {
        @Config.Comment("FE generation")
        @Config.RangeInt(min = 0)
        public static int generation = 40;

        @Config.Comment("maximum FE output per side")
        @Config.RangeInt(min = 0)
        public static int output = 2000;

        @Config.Comment("inner FE capacity")
        @Config.RangeInt(min = 0)
        public static int capacity = 200000;
    }

    @Mod.EventBusSubscriber(modid = ModUtil.MOD_ID)
    private static class EventHandler {

        /**
         * Inject the new values and save to the config file when the config has been changed from the GUI.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            event.getConfigID();
            if (event.getModID().equals(ModUtil.MOD_ID)) {
                ConfigManager.sync(ModUtil.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
