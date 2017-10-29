package com.timotheteus.raincontrol.config;

import com.timotheteus.raincontrol.util.ModUtil;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public class ModConfig {

    public static Configuration config;

    public static boolean isFEMode() {
        return ActivationType.FE.equals(RAINBLOCK.type);
    }

    public static void load(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        updateConfigValues();
    }

    public static void updateConfigValues() {
        String categoryGenerator = "generator";
        String categoryRainBlock = "rainblock";
        String categorySensor = "sensor";

        RAINBLOCK.FE_activation = config.getInt("FE_activation", categoryRainBlock, 1000000, 0, Integer.MAX_VALUE, "[FE] FE needed to activate the block");
        RAINBLOCK.FE_maxInput = config.getInt("FE_maxInput", categoryRainBlock, 4000, 0, Integer.MAX_VALUE, "[FE] max FE input");
        RAINBLOCK.FE_capacity = config.getInt("FE_capacity", categoryRainBlock, 10000000, 0, Integer.MAX_VALUE, "[FE] inner FE capacity");
        RAINBLOCK.XP_levels = config.getInt("XP_levels", categoryRainBlock, 10, 1, Integer.MAX_VALUE, "[XP] levels needed to activate");
        RAINBLOCK.needsSky = config.getBoolean("needsSky", categoryRainBlock, true, "Does it need sky above it to work?");
        RAINBLOCK.type = config.getString("type", categoryRainBlock, "FE", "What way of activation?", new String[]{"FE", "FREE", "XP"});

        GENERATOR.generation = config.getInt("generation", categoryGenerator, 40, 0, Integer.MAX_VALUE, "FE generation");
        GENERATOR.maxOutput = config.getInt("maxOutput", categoryGenerator, 2000, 0, Integer.MAX_VALUE, "maximum FE output per side");
        GENERATOR.capacity = config.getInt("capacity", categoryGenerator, 200000, 0, Integer.MAX_VALUE, "inner FE capacity");

        SENSOR.delay = config.get(categorySensor, "delay", 40, "Sensor delay [needs world restart]", 1, Integer.MAX_VALUE).setRequiresWorldRestart(true).getInt();

        config.save();
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(ModUtil.MOD_ID)) {
            updateConfigValues();
        }
    }

    private enum ActivationType {
        FE("FE"),
        FREE("FREE"),
        XP("XP");

        public String value;

        ActivationType(String a) {
            this.value = a;
        }

        public boolean equals(String a) {
            return Objects.equals(a, this.value);
        }
    }

    public static class RAINBLOCK {
        public static String type = "FE";
        public static boolean needsSky = true;
        public static int FE_activation = 1000000;
        public static int FE_maxInput = 4000;
        public static int FE_capacity = 10000000;
        public static int XP_levels = 10;
    }

    public static class GENERATOR {
        public static int generation = 40;
        public static int maxOutput = 2000;
        public static int capacity = 100000;
    }

    public static class SENSOR {
        public static int delay = 40;
    }
}
