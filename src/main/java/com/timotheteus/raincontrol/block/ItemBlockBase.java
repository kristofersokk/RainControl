package com.timotheteus.raincontrol.block;

import com.timotheteus.raincontrol.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockBase extends ItemBlock {

    private final String id;

    public ItemBlockBase(Block block, Properties properties) {
        super(block, properties);
        this.id = ((BaseBlock) block).getId();
        this.setRegistryName(ModUtil.MOD_ID + "." + id);
    }

    //TODO remove if not needed
    public String getModelName() {
        return ModUtil.MOD_ID + ":" + id;
    }

}
