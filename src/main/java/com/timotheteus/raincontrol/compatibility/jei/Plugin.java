package com.timotheteus.raincontrol.compatibility.jei;

import com.timotheteus.raincontrol.block.ModBlocks;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class Plugin implements IModPlugin {

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
        
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {

    }

    @Override
    public void register(IModRegistry registry) {
        //add descriptions
        registry.addDescription(new ItemStack(ModBlocks.blockRain),
                " This block uses 1,000,000 FE/RF/T to toggle rain.",
                "No GUI, only right, shift-right clicking or redstone.",
                "Max capacity 10,000,000 FE",
                "Use the generator to get energy.",
                "Holds energy when picked up."
        );
        registry.addDescription(new ItemStack(ModBlocks.blockGenerator),
                "This block generates constant 40 (FE/RF/T)/tick.",
                "Can be automated.",
                "Disperses energy into neighboring blocks.",
                "Hover over stack in GUI to see more info."
        );
        registry.addDescription(new ItemStack(ModBlocks.blockSensor),
                "This block gives redstone signal when raining."
        );

    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

    }


}
