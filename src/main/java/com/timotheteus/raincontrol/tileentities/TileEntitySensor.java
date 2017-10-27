package com.timotheteus.raincontrol.tileentities;

import com.timotheteus.raincontrol.block.BlockSensor;

public class TileEntitySensor extends TileEntityBase {

    public TileEntitySensor() {
    }

    @Override
    public void update() {
        super.update();
        blockType = this.getBlockType();
        if (blockType instanceof BlockSensor)
            ((BlockSensor) blockType).updatePower(world, pos);
    }
}
