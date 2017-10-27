package com.timotheteus.raincontrol.block;

import com.timotheteus.raincontrol.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

abstract class BlockBase extends Block implements BaseBlock {

    BlockBase(Material blockMaterialIn, String id) {
        super(blockMaterialIn, MapColor.GRAY);
        this.setUnlocalizedName(ModUtil.MOD_ID + "." + id);
        this.setRegistryName(id);
        this.id = id;
    }

    private final String id;

    @Override
    public String getModelName() {
        return ModUtil.MOD_ID + ":" + id;
    }

    @Override
    public String getTileEntityName() {
        return ModUtil.MOD_ID + "_" + id;
    }

    @Override
    public Block getBlock() {
        return this;
    }

    @Override
    public String getId() {
        return id;
    }
}
