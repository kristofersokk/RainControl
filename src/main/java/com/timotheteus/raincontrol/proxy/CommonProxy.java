package com.timotheteus.raincontrol.proxy;

import com.timotheteus.raincontrol.RainControl;
import com.timotheteus.raincontrol.block.Blocks;
import com.timotheteus.raincontrol.handlers.GuiHandler;
import com.timotheteus.raincontrol.handlers.PacketHandler;
import com.timotheteus.raincontrol.items.Items;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {


    public void preInit(FMLPreInitializationEvent event){
        PacketHandler.registerMessages();
    }

    public void init(FMLInitializationEvent event){
        NetworkRegistry.INSTANCE.registerGuiHandler(RainControl.instance, new GuiHandler());
    }

    public void postInit(FMLPostInitializationEvent event){

    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event){
        Blocks.init(event);
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event){
        Items.init(event);
    }
}
