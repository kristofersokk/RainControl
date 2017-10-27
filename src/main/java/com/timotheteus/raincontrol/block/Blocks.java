package com.timotheteus.raincontrol.block;

import com.timotheteus.raincontrol.tileentities.TileEntityGeneratorBlock;
import com.timotheteus.raincontrol.tileentities.TileEntityRainBlock;
import com.timotheteus.raincontrol.tileentities.TileEntitySensor;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class Blocks {

    public static BlockRain blockRain;
    public static BlockGenerator blockGenerator;
    public static BlockSensor blockSensor;

    @SubscribeEvent
    public static void init(RegistryEvent.Register<Block> event) {
        blockRain = new BlockRain();
        blockGenerator = new BlockGenerator();
        blockSensor = new BlockSensor();

        GameRegistry.registerTileEntity(TileEntityRainBlock.class, blockRain.getTileEntityName());
        GameRegistry.registerTileEntity(TileEntityGeneratorBlock.class, blockGenerator.getTileEntityName());
        GameRegistry.registerTileEntity(TileEntitySensor.class, blockSensor.getTileEntityName());

        event.getRegistry().registerAll(
                blockRain,
                blockGenerator,
                blockSensor
        );
    }

    @SideOnly(Side.CLIENT)
    public static void initModels(){
        registerBlockModel(blockRain);
        registerBlockModel(blockGenerator);
        registerBlockModel(blockSensor, 0, 1);
    }

    @SideOnly(Side.CLIENT)
    private static void registerBlockModel(BaseBlock base) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(base.getBlock()), 0, new ModelResourceLocation(base.getModelName()));
    }

    @SideOnly(Side.CLIENT)
    private static void registerBlockModel(BaseBlock base, int meta) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(base.getBlock()), meta, new ModelResourceLocation(base.getModelName()));
    }

    @SideOnly(Side.CLIENT)
    private static void registerBlockModel(BaseBlock base, int meta_start, int meta_end) {
        for (int meta = meta_start; meta <= meta_end; meta++){
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(base.getBlock()), meta, new ModelResourceLocation(base.getModelName()));
        }
    }

    @SideOnly(Side.CLIENT)
    private static void registerBlockModel(BaseBlock base, int meta_start, int meta_end, String variant) {
        for (int meta = meta_start; meta <= meta_end; meta++){
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(base.getBlock()), meta, new ModelResourceLocation(base.getModelName(), variant));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerBlockModel(BaseBlock base, int meta, String variant) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(base.getBlock()), meta, new ModelResourceLocation(base.getModelName(), variant));
    }

}
