package com.timotheteus.raincontrol.items;

import com.timotheteus.raincontrol.block.Blocks;
import com.timotheteus.raincontrol.block.ItemBlockBase;
import com.timotheteus.raincontrol.items.itemblocks.ItemRainBlock;
import com.timotheteus.raincontrol.util.ModUtil;
import com.timotheteus.raincontrol.util.Names;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static net.minecraft.item.ItemGroup.MISC;

public class Items {

    @ObjectHolder(Names.BLOCK_RAIN)
    public static ItemRainBlock blockRain_item;

    @ObjectHolder(Names.BLOCK_GENERATOR)
    public static ItemBlockBase blockgenerator_item;

    @ObjectHolder(Names.BLOCK_SENSOR)
    public static ItemBlockBase blocksensor_item;

    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ModUtil.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {

        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event)
        {
            IForgeRegistry<Item> itemRegistry = event.getRegistry();

            Item.Properties properties = (new Item.Properties()).group(MISC);

            itemRegistry.register(new ItemRainBlock(Blocks.blockRain, properties));
            itemRegistry.register(new ItemBlockBase(Blocks.blockGenerator, properties));
            itemRegistry.register(new ItemBlockBase(Blocks.blockSensor, properties));
        }
    }


}
