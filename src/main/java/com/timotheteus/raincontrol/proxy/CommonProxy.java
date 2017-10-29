package com.timotheteus.raincontrol.proxy;

import com.timotheteus.raincontrol.RainControl;
import com.timotheteus.raincontrol.config.ModConfig;
import com.timotheteus.raincontrol.handlers.GuiHandler;
import com.timotheteus.raincontrol.handlers.PacketHandler;
import com.timotheteus.raincontrol.handlers.RecipeHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event){
        PacketHandler.registerMessages();
        ModConfig.load(event);
    }

    public void init(FMLInitializationEvent event){
        NetworkRegistry.INSTANCE.registerGuiHandler(RainControl.instance, new GuiHandler());
    }

    public void postInit(FMLPostInitializationEvent event){
        RecipeHandler.addRecipes();
//        if (config.hasChanged())
//            config.save();
    }

}
