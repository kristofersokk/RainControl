package com.timotheteus.raincontrol.proxy;

import com.timotheteus.raincontrol.RainControl;
import com.timotheteus.raincontrol.config.Config;
import com.timotheteus.raincontrol.handlers.GuiHandler;
import com.timotheteus.raincontrol.handlers.PacketHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {

    public static Configuration config;

    public void preInit(FMLPreInitializationEvent event){
        PacketHandler.registerMessages();
        config = new Configuration(event.getSuggestedConfigurationFile());
        Config.readConfig();
    }

    public void init(FMLInitializationEvent event){
        NetworkRegistry.INSTANCE.registerGuiHandler(RainControl.instance, new GuiHandler());
    }

    public void postInit(FMLPostInitializationEvent event){
        if (config.hasChanged())
            config.save();
    }

}
