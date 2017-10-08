package com.timotheteus.raincontrol.block;

import com.timotheteus.raincontrol.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockBase extends Block {

    public BlockBase(Material blockMaterialIn, String id) {
        super(blockMaterialIn, MapColor.GRAY);
        this.setUnlocalizedName(ModUtil.MOD_ID + "." + id);
        this.setRegistryName(id);
        this.id = id;
    }

    private String id;

    public String getModelName() {
        return ModUtil.MOD_ID + ":" + id;
    }

}
