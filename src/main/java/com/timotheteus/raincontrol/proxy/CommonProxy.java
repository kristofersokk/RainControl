package com.timotheteus.raincontrol.proxy;

import com.timotheteus.raincontrol.RainControl;
import com.timotheteus.raincontrol.handlers.RecipeHandler;
import com.timotheteus.raincontrol.handlers.GuiHandler;
import com.timotheteus.raincontrol.handlers.PacketHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

//    public static Configuration config;

    public void preInit(FMLPreInitializationEvent event){
        PacketHandler.registerMessages();
//        config = new Configuration(event.getSuggestedConfigurationFile());
//        ConfigHandler.readConfig();
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
