package com.timotheteus.raincontrol.proxy;

import com.timotheteus.raincontrol.block.ModBlocks;
import com.timotheteus.raincontrol.handlers.InputHandler;
import com.timotheteus.raincontrol.items.ModItems;
import com.timotheteus.raincontrol.util.KeyBindings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        MinecraftForge.EVENT_BUS.register(new InputHandler());
        KeyBindings.init();

        ModBlocks.initModels();
        ModItems.initModels();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

}
