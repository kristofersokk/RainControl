package com.timotheteus.raincontrol.tileentities;

import com.timotheteus.raincontrol.block.BlockSensor;
import net.minecraft.block.Block;

public class TileEntitySensor extends TileEntityBase {

    public TileEntitySensor() {
        super(RainControlTileEntityType.SENSOR);
    }

    @Override
    public void tick() {
        super.tick();
        Block block = this.getBlockState().getBlock();
        if (block instanceof BlockSensor)
            ((BlockSensor) block).updatePower(world, pos);
    }
}
