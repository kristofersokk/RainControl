package com.timotheteus.raincontrol.block;

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

@Mod.EventBusSubscriber
public class Blocks {

    public static Item.Properties itemBuilder;

    @ObjectHolder(Names.BLOCK_RAIN)
    public static BlockRain blockRain;

    @ObjectHolder(Names.BLOCK_GENERATOR)
    public static BlockGenerator blockGenerator;

    @ObjectHolder(Names.BLOCK_SENSOR)
    public static BlockSensor blockSensor;

    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ModUtil.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {

        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event)
        {
            IForgeRegistry<Block> blockRegistry = event.getRegistry();

            blockRegistry.register(new BlockRain());
            blockRegistry.register(new BlockGenerator());
            blockRegistry.register(new BlockSensor());

        }

    }

}
