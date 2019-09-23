package com.timotheteus.raincontrol.config;

import com.timotheteus.raincontrol.util.ModUtil;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;

public class ConfigHandler {

    public enum ActivationType {
        FE,
        FREE,
        XP
    }

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final RainBlockConfig RAIN_BLOCK = new RainBlockConfig(BUILDER);
    public static final GeneratorConfig GENERATOR = new GeneratorConfig(BUILDER);
    public static final SensorConfig SENSOR = new SensorConfig(BUILDER);
    public static final ForgeConfigSpec spec = BUILDER.build();

    public static class RainBlockConfig {

        public final ForgeConfigSpec.ConfigValue<ActivationType> type;
        public final ForgeConfigSpec.BooleanValue needsSky;
        public final ForgeConfigSpec.IntValue FE_activation;
        public final ForgeConfigSpec.IntValue FE_maxInput;
        public final ForgeConfigSpec.IntValue FE_capacity;
        public final ForgeConfigSpec.IntValue XP_levels;

        RainBlockConfig(ForgeConfigSpec.Builder builder) {
            builder.push("RainBlock");
            type = builder
                    .comment("What way of activation?")
                    .define("type", ActivationType.FE);
            needsSky = builder
                    .comment("Does it need sky above it to work?")
                    .define("needsSky", true);
            FE_activation = builder
                    .comment("[FE] FE needed to activate the block")
                    .defineInRange("FE_activation", 1000000, 0, Integer.MAX_VALUE);
            FE_maxInput = builder
                    .comment("[FE] max FE input")
                    .defineInRange("FE_maxInput", 4000, 0, Integer.MAX_VALUE);
            FE_capacity = builder
                    .comment("[FE] inner FE capacity")
                    .defineInRange("FE_capacity", 10000000, 0, Integer.MAX_VALUE);
            XP_levels = builder
                    .comment("[XP] levels needed to activate")
                    .defineInRange("XP_levels", 10, 0, Integer.MAX_VALUE);
            builder.pop();

        }

    }

    public static class GeneratorConfig {

        public final ForgeConfigSpec.ConfigValue<Integer> generation;
        public final ForgeConfigSpec.ConfigValue<Integer> maxOutput;
        public final ForgeConfigSpec.ConfigValue<Integer> capacity;

        GeneratorConfig(ForgeConfigSpec.Builder builder) {
            builder.push("Generator");
            generation = builder
                    .comment("FE generation")
                    .defineInRange("generation", 40, 1, Integer.MAX_VALUE);
            maxOutput = builder
                    .comment("maximum FE output per side")
                    .defineInRange("maxOutput", 2000, 1, Integer.MAX_VALUE);
            capacity = builder
                    .comment("inner FE capacity")
                    .defineInRange("capacity", 200000, 1, Integer.MAX_VALUE);
            builder.pop();
        }

    }

    public static class SensorConfig {

        public final ForgeConfigSpec.ConfigValue<Integer> delay;

        SensorConfig(ForgeConfigSpec.Builder builder) {
            builder.push("Sensor");
            delay = builder
                    .comment("Sensor delay [needs world restart]")
                    .defineInRange("delay", 40, 0, Integer.MAX_VALUE);
            builder.pop();
        }

    }

//    @Mod.EventBusSubscriber(modid = ModUtil.MOD_ID)
//    private static class EventHandler {
//
//        /**
//         * Inject the new values and save to the config file when the config has been changed from the GUI.
//         *
//         * @param event The event
//         */
//        @SubscribeEvent
//        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
//            event.getConfigID();
//            if (event.getModID().equals(ModUtil.MOD_ID)) {
//                ConfigHelper.;
//            }
//        }
//    }
}
