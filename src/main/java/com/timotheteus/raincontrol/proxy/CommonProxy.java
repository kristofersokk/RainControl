package com.timotheteus.raincontrol.proxy;

import com.timotheteus.raincontrol.block.BlockRain;
import com.timotheteus.raincontrol.block.Blocks;
import com.timotheteus.raincontrol.handlers.PacketHandler;
import com.timotheteus.raincontrol.tileentities.TileEntityRainBlock;
import com.timotheteus.raincontrol.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {


    public void preInit(FMLPreInitializationEvent event){
        PacketHandler.registerMessages();
    }

    public void init(FMLInitializationEvent event){

    }

    public void postInit(FMLPostInitializationEvent event){

    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event){
        event.getRegistry().register(new BlockRain());
        GameRegistry.registerTileEntity(TileEntityRainBlock.class, ModUtil.MOD_ID + "_rainblock");

    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event){
        event.getRegistry().register(new ItemBlock(Blocks.blockRain).setRegistryName(Blocks.blockRain.getRegistryName()));
    }
}
