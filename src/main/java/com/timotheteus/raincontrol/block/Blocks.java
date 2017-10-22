package com.timotheteus.raincontrol.block;

import com.timotheteus.raincontrol.tileentities.TileEntityGeneratorBlock;
import com.timotheteus.raincontrol.tileentities.TileEntityRainBlock;
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
    public static BlockGenerator blockGenerator;

    public static void init(RegistryEvent.Register<Block> event) {
        blockRain = new BlockRain();
        blockGenerator = new BlockGenerator();

        GameRegistry.registerTileEntity(TileEntityRainBlock.class, blockRain.getTileEntityName());
        GameRegistry.registerTileEntity(TileEntityGeneratorBlock.class, blockGenerator.getTileEntityName());

        event.getRegistry().registerAll(
                blockRain,
                blockGenerator
        );
    }

    @SideOnly(Side.CLIENT)
    public static void initModels(){
        registerBlockModel(blockRain);
        registerBlockModel(blockGenerator);
    }

    @SideOnly(Side.CLIENT)
    public static void registerBlockModel(BlockBase block) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getModelName()));
    }

    @SideOnly(Side.CLIENT)
    public static void registerBlockModel(BlockBase block, String variant) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getModelName(), variant));
    }

}
