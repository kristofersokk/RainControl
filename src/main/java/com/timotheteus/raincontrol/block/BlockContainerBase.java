package com.timotheteus.raincontrol.block;

import com.timotheteus.raincontrol.tileentities.TileEntityBase;
import com.timotheteus.raincontrol.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockContainerBase extends BlockContainer implements BaseBlock {

    public BlockContainerBase(Class<? extends TileEntityBase> tileEntity, Material materialIn, String id) {
        super(materialIn, MapColor.GRAY);
        this.setUnlocalizedName(ModUtil.MOD_ID + "." + id);
        this.setRegistryName(id);
        this.id = id;
        this.tileEntity = tileEntity;
    }

    final String id;
    final Class<? extends TileEntityBase> tileEntity;

    @Override
    public String getModelName() {
        return ModUtil.MOD_ID + ":" + id;
    }

    @Override
    public String getTileEntityName() {
        return ModUtil.MOD_ID + "_" + id;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        try {
            return tileEntity.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Block getBlock() {
        return this;
    }
}
