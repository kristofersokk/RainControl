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
    public static Sensor sensor = new Sensor();

    public enum ActivationType {
        FE,
        FREE,
        XP
    }

    public static class RainBlock {

        RainBlock() {
        }

        @Config.Comment("What way of activation?")
        public ActivationType type = ActivationType.FE;

        @Config.Comment("Does it need sky above it to work?")
        public boolean needsSky = true;

        @Config.Comment("[FE] FE needed to activate the block")
        @Config.RangeInt(min = 0)
        public int FE_activation = 1000000;

        @Config.Comment("[FE] max FE input")
        @Config.RangeInt(min = 0)
        public int FE_maxInput = 4000;

        @Config.Comment("[FE] inner FE capacity")
        @Config.RangeInt(min = 0)
        public int FE_capacity = 10000000;

        @Config.Comment("[XP] levels needed to activate")
        @Config.RangeInt(min = 0)
        public int XP_levels = 10;

    }

    public static class Generator {
        Generator() {
        }

        @Config.Comment("FE generation")
        @Config.RangeInt(min = 0)
        public int generation = 40;

        @Config.Comment("maximum FE output per side")
        @Config.RangeInt(min = 0)
        public int maxOutput = 2000;

        @Config.Comment("inner FE capacity")
        @Config.RangeInt(min = 0)
        public int capacity = 200000;
    }

    public static class Sensor {
        Sensor() {
        }

        @Config.Comment("Sensor delay [needs world restart]")
        @Config.RangeInt(min = 1)
        public int delay = 40;
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
                ConfigManager.load(ModUtil.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
