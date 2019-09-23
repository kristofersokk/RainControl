package com.timotheteus.raincontrol.proxy;

import com.timotheteus.raincontrol.handlers.GuiHandler;
import com.timotheteus.raincontrol.handlers.PacketHandler;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber
public abstract class CommonProxy {

//    public static Configuration config;

    public void commonSetup(FMLCommonSetupEvent event){
        PacketHandler.registerMessages();
//        config = new Configuration(event.getSuggestedConfigurationFile());
//        ConfigHandler.readConfig();
    }

    public void clientSetup(FMLClientSetupEvent event){
        FMLJavaModLoadingContext.get().getModEventBus().register(new GuiHandler());
    }

}
