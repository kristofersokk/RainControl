package com.timotheteus.raincontrol.block;

import com.timotheteus.raincontrol.tileentities.TileEntityRainBlock;
import com.timotheteus.raincontrol.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Blocks {

    public static BlockRain blockRain;

    public static void init(RegistryEvent.Register<Block> event) {
        blockRain = new BlockRain();
        event.getRegistry().register(blockRain);
        GameRegistry.registerTileEntity(TileEntityRainBlock.class, ModUtil.MOD_ID + "_rainblock");
    }

    @SideOnly(Side.CLIENT)
    public static void initModels(){
        registerBlockModel(blockRain);
    }

    public static void registerBlockModel(BlockBase block) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getModelName()));
    }

    public static void registerBlockModel(BlockBase block, String variant) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getModelName(), variant));
    }

}
