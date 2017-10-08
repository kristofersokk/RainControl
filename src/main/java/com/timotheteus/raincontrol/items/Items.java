package com.timotheteus.raincontrol.items;

import com.timotheteus.raincontrol.block.BlockRain;
import com.timotheteus.raincontrol.block.Blocks;
import com.timotheteus.raincontrol.block.ItemBlockBase;
import com.timotheteus.raincontrol.items.itemblocks.ItemRainBlock;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Items {

    public static ItemRainBlock blockRain_item;

    public static void init(RegistryEvent.Register<Item> event) {
        blockRain_item = new ItemRainBlock(Blocks.blockRain);
        event.getRegistry().register(blockRain_item);
//        ItemsHC.items.add(blockRain_item);
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
//        blockRain.initModel();
        registerItemBlockModel(new ItemRainBlock(new BlockRain()));
    }

    private static void registerItemBlockModel(ItemBlockBase item) {
        ModelResourceLocation location = new ModelResourceLocation(item.getModelName());
//        ModelBakery.registerItemVariants(item, location);
        ModelLoader.setCustomModelResourceLocation(item, 0, location);
    }
}