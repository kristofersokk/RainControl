package com.timotheteus.raincontrol.block;

import net.minecraft.block.Block;

public interface BaseBlock {
    String getModelName();
    String getTileEntityName();
    Block getBlock();
    String getId();
}
