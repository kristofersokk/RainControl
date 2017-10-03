package com.timotheteus.raincontrol.block;

import com.timotheteus.raincontrol.util.ModUtil;
import com.timotheteus.raincontrol.util.Names;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class Blocks {

    @GameRegistry.ObjectHolder(ModUtil.MOD_ID + ":" + Names.BLOCK_RAIN)
    public static BlockRain blockRain;

    @SideOnly(Side.CLIENT)
    public static void initModels(){
        blockRain.initModel();
    }

}
