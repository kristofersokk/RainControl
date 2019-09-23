package com.timotheteus.raincontrol.block;

import com.timotheteus.raincontrol.tileentities.TileEntityBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.extensions.IForgeTileEntity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BlockTileBase extends BlockBase implements BaseBlock, IForgeTileEntity {

    public BlockTileBase(Class<? extends TileEntityBase> tileEntity, Properties properties, String id) {
        super(id, properties);
        this.tileEntity = tileEntity;
    }

    Class<? extends TileEntity> tileEntity;

    @Override
    public NBTTagCompound getTileData() {
        return new NBTTagCompound();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side) {
        return LazyOptional.empty();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return LazyOptional.empty();
    }
}
