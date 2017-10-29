package com.timotheteus.raincontrol.items;

import com.timotheteus.raincontrol.block.ModBlocks;
import com.timotheteus.raincontrol.block.ItemBlockBase;
import com.timotheteus.raincontrol.items.itemblocks.ItemRainBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ModItems {

    public static ItemRainBlock blockRain_item;
    public static ItemBlockBase blockgenerator_item;
    public static ItemBlockBase blocksensor_item;

    public static ItemModelMesher mesher;

    @SubscribeEvent
    public static void init(RegistryEvent.Register<Item> event) {
        blockRain_item = new ItemRainBlock(ModBlocks.blockRain);
        blockgenerator_item = new ItemBlockBase(ModBlocks.blockGenerator);
        blocksensor_item = new ItemBlockBase(ModBlocks.blockSensor);

        event.getRegistry().registerAll(
                blockRain_item,
                blockgenerator_item,
                blocksensor_item
        );
    }

    public static void initModels() {
        mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        registerItemBlockModel(blockRain_item);
        registerItemBlockModel(blockgenerator_item);
        registerItemBlockModel(blocksensor_item);
    }

    private static void registerItemBlockModel(ItemBlockBase item) {
        ModelResourceLocation location = new ModelResourceLocation(item.getModelName());
        ModelLoader.registerItemVariants(item, location);
        ModelLoader.setCustomModelResourceLocation(item, 0, location);
        mesher.register(item, 0, location);
    }
}
