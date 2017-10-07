package com.timotheteus.raincontrol.block;

import com.timotheteus.raincontrol.items.itemblocks.ItemRainBlock;
import com.timotheteus.raincontrol.util.ModUtil;
import com.timotheteus.raincontrol.util.Names;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class Blocks {

    public static BlockRain blockRain = new BlockRain();
    public static ItemRainBlock blockRain_item = new ItemRainBlock(blockRain);

    @SideOnly(Side.CLIENT)
    public static void initModels(){
        blockRain.initModel();
        blockRain_item.initModel();
    }

}
