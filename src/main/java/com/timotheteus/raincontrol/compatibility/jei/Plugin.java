package com.timotheteus.raincontrol.compatibility.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class Plugin implements IModPlugin {


//    @Override
//    public void register(IModRegistry registry) {
//        //add descriptions
//        registry.addDescription(new ItemStack(Blocks.blockRain),
//                " This block uses 1,000,000 FE/RF/T to toggle rain.",
//                "No GUI, only right, shift-right clicking or redstone.",
//                "Max capacity 10,000,000 FE",
//                "Use the generator to get energy.",
//                "Holds energy when picked up."
//        );
//        registry.addDescription(new ItemStack(Blocks.blockGenerator),
//                "This block generates constant 40 (FE/RF/T)/tick.",
//                "Can be automated.",
//                "Disperses energy into neighboring block.",
//                "Hover over stack in GUI to see more info."
//        );
//        registry.addDescription(new ItemStack(Blocks.blockSensor),
//                "This block gives redstone signal when raining."
//        );
//
//    }


    @Override
    public ResourceLocation getPluginUid() {
        return null;
    }
}
