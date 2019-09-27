package com.timotheteus.raincontrol.block;

import com.timotheteus.raincontrol.tileentities.TileEntityBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.extensions.IForgeTileEntity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BlockTileBase extends BlockBase {

    public BlockTileBase(Class<? extends TileEntityBase> tileEntity, Properties properties, String id) {
        super(id, properties);
        this.tileEntity = tileEntity;
    }

    Class<? extends TileEntity> tileEntity;

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public abstract TileEntity createTileEntity(IBlockState state, IBlockReader world);

}
