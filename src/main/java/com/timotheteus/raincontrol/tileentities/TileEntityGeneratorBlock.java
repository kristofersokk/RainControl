package com.timotheteus.raincontrol.tileentities;

import com.pengu.hammercore.common.capabilities.CapabilityEJ;
import com.pengu.hammercore.energy.iPowerStorage;
import com.timotheteus.raincontrol.block.Syncable;
import com.timotheteus.raincontrol.packets.PacketServerToClient;
import com.timotheteus.raincontrol.packets.PacketTypes;
import com.timotheteus.raincontrol.util.CustomItemStackHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.Arrays;

public class TileEntityGeneratorBlock extends TileEntity implements IEnergyStorage, Syncable.Energy, Syncable.BurnTime, iPowerStorage, ITickable {

    public static final int SIZE = 1;
    public static final int maxStorage = 100000;
    public static final int maxOutput = 2000;
    public static final int PRODUCE = 40;
    static final Capability[] capabilities = new Capability[]{
            CapabilityEnergy.ENERGY,
            CapabilityEJ.ENERGY,
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
    };

    private CustomItemStackHandler itemStackHandler = new CustomItemStackHandler(SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };

    private int energy;
    private int burnTimeLeft;

    @Override
    public void update() {

        boolean sync = false;

        if (!world.isRemote) {
            //burning
            if (burnTimeLeft > 0) {
                burnTimeLeft--;
                if (changeEnergy(PRODUCE, false))
                    sync = true;
            } else {
                //not burning
                ItemStack stack = itemStackHandler.getStackInSlot(0);
                int newBurnTime = TileEntityFurnace.getItemBurnTime(stack);
                if (newBurnTime > 0) {
                    if (itemStackHandler.modifyStack(0, -1)) {
                        setBurnTime(newBurnTime, false);
                        sync = true;
                    }
                }
            }

            //TODO add energy distribution logic

            if (sync)
                markDirty(true);
        }

    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        energy = compound.getInteger("energy");
        burnTimeLeft = compound.getInteger("burnTime");
        if (compound.hasKey("items")) {
            itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
        }
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("energy", energy);
        compound.setInteger("burnTime", burnTimeLeft);
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
            changeEnergy(-extracted, true);
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

    @Override
    public void setEnergy(int energy, boolean sync) {
        this.energy = energy;
        markDirty(sync);
    }

    @Override
    public boolean changeEnergy(int a, boolean sync) {
        int prevEnergy = energy;
        energy += a;
        if (energy < 0)
            energy = 0;
        if (energy > maxStorage)
            energy = maxStorage;
        if (prevEnergy != energy) {
            markDirty(sync);
            return true;
        }
        return false;
    }

    @Override
    public void setBurnTime(int a, boolean sync) {
        burnTimeLeft = a;
        markDirty(sync);
    }

    public void markDirty(boolean sync) {
        super.markDirty();
        if (sync) {
            new PacketServerToClient(
                    this,
                    new PacketTypes.SERVER[]{PacketTypes.SERVER.BURN_TIME, PacketTypes.SERVER.ENERGY},
                    burnTimeLeft,
                    getEnergyStored()
            )
                    .sendToDimension();
        }
    }
}
