package com.timotheteus.raincontrol.tileentities;

import com.timotheteus.raincontrol.util.Names;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.Supplier;

import static com.timotheteus.raincontrol.util.ModUtil.MOD_ID;

public class RainControlTileEntityType {

    @ObjectHolder(Names.TE_RAIN)
    public static TileEntityType<?> RAIN;

    @ObjectHolder(Names.TE_GENERATOR)
    public static TileEntityType<?> GENERATOR;

    @ObjectHolder(Names.TE_SENSOR)
    public static TileEntityType<?> SENSOR;

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration
    {
        @SubscribeEvent
        public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> e)
        {
            e.getRegistry().registerAll(
                    TileEntityType.Builder.create((Supplier<TileEntity>) TileEntityRainBlock::new).build(null).setRegistryName(Names.TE_RAIN),
                    TileEntityType.Builder.create((Supplier<TileEntity>) TileEntityGeneratorBlock::new).build(null).setRegistryName(Names.TE_GENERATOR),
                    TileEntityType.Builder.create((Supplier<TileEntity>) TileEntitySensor::new).build(null).setRegistryName(Names.TE_SENSOR)
            );
        }
    }

}
