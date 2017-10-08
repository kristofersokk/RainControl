package com.timotheteus.raincontrol.block;

import com.timotheteus.raincontrol.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockBase extends ItemBlock {

    private String id;

    public ItemBlockBase(Block block, String id) {
        super(block);
        this.setUnlocalizedName(ModUtil.MOD_ID + "." + id);
        this.setRegistryName(id);
        this.id = id;
    }

    public String getModelName() {
        return ModUtil.MOD_ID + ":" + id;
    }

}
