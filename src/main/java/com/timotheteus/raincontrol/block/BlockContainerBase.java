package com.timotheteus.raincontrol.block;

import com.timotheteus.raincontrol.tileentities.TileEntityBase;
import com.timotheteus.raincontrol.util.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.extensions.IForgeTileEntity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockContainerBase extends BlockContainer implements BaseBlock, IForgeTileEntity {

    public BlockContainerBase(Class<? extends TileEntityBase> tileEntity, Properties properties, String id) {
        super(properties);
        this.setRegistryName(ModUtil.MOD_ID, id);
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

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Block getBlock() {
        return this;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        try {
            return tileEntity.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
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

    @Override
    public NBTTagCompound getTileData() {
        return new NBTTagCompound();
    }
}
