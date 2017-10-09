package com.timotheteus.raincontrol.tileentities;

import com.pengu.hammercore.common.capabilities.CapabilityEJ;
import com.pengu.hammercore.energy.iPowerStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Arrays;

public class TileEntityFurnaceBlock extends TileEntity implements IEnergyStorage, iPowerStorage, ITickable {

    public static final int SIZE = 1;
    public static final int maxStorage = 100000;
    public static final int maxOutput = 2000;
    Capability[] capabilities = new Capability[]{
            CapabilityEnergy.ENERGY,
            CapabilityEJ.ENERGY,
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
    };
    private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };
    private int energy = 0;

    @Override
    public void update() {

    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        energy = compound.getInteger("energy");
        if (compound.hasKey("items")) {
            itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
        }
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("energy", energy);
        compound.setTag("items", itemStackHandler.serializeNBT());
        return super.writeToNBT(compound);
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (Arrays.asList(capabilities).contains(capability))
            return true;
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (Arrays.asList(capabilities).contains(capability))
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
            } else {
                return (T) this;
            }
        return super.getCapability(capability, facing);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extracted = Math.min(maxExtract, Math.min(maxOutput, energy));
        if (!simulate) {
            changeEnergy(-extracted);
        }
        return extracted;
    }

    @Override
    public int getEnergyStored() {
        return energy;
    }

    @Override
    public int getMaxEnergyStored() {
        return maxStorage;
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return false;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
        markDirty();
    }

    public void changeEnergy(int energy) {
        this.energy += energy;
        markDirty();
    }
}
