package com.timotheteus.raincontrol.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockBase extends Block {

    public BlockBase(Material blockMaterialIn, MapColor blockMapColorIn, String id) {
        super(blockMaterialIn, blockMapColorIn);
        this.id = id;
    }

    private String id;

    protected BlockBase(Material materialIn, String name) {
        super(materialIn);
        this.id = name;
    }

    public String getNameinRegistry(){
        return id;
    }

}
