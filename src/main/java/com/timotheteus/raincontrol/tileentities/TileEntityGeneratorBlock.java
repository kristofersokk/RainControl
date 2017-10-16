package com.timotheteus.raincontrol.tileentities;

import com.pengu.hammercore.common.capabilities.CapabilityEJ;
import com.pengu.hammercore.energy.iPowerStorage;
import com.timotheteus.raincontrol.config.Config;
import com.timotheteus.raincontrol.packets.PacketServerToClient;
import com.timotheteus.raincontrol.packets.PacketTypes;
import com.timotheteus.raincontrol.tileentities.modules.ModuleTypes;
import com.timotheteus.raincontrol.util.CustomItemStackHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.Arrays;

public class TileEntityGeneratorBlock extends TileEntityBase implements IEnergyStorage, Syncable.Sync, Syncable.Energy, Syncable.Generator, Syncable.BurnTime, iPowerStorage {

    public static final int SIZE = 1;
    private static final int maxStorage = 200000;
    private static final int maxOutput = 1000;
    private static final PacketTypes.SERVER[] packets = new PacketTypes.SERVER[]{
            PacketTypes.SERVER.ENERGY,
            PacketTypes.SERVER.GENERATOR,
            PacketTypes.SERVER.BURN_TIME
    };
    private static final Capability[] capabilities = new Capability[]{
            CapabilityEnergy.ENERGY,
            CapabilityEJ.ENERGY,
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
    };
    private static int generation;

    private CustomItemStackHandler itemStackHandler = new CustomItemStackHandler(SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };

    private int energy;
    private int maxBurnTimeLeft;
    private int burnTimeLeft;

    public TileEntityGeneratorBlock() {
        super(new ModuleTypes[]{
                ModuleTypes.ENERGY_DISPENSER
        }, new Object[][]{
                {maxOutput}
        });
        generation = Config.generatorProduce;
        markDirty(false);
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            boolean sync = false;
            super.update();

            //burning
            if (burnTimeLeft > 0) {
                burnTimeLeft--;
                if (changeEnergy(generation, false))
                    sync = true;
            } else {
                //not burning
                if (!getWorld().isBlockPowered(getPos())) {
                    ItemStack stack = itemStackHandler.getStackInSlot(0);
                    int newBurnTime = TileEntityFurnace.getItemBurnTime(stack);
                    if (newBurnTime > 0) {
                        if (itemStackHandler.modifyStack(0, -1)) {
                            setBurnTime(newBurnTime, false);
                            setMaxBurnTime(newBurnTime, false);
                            sync = true;
                        }
                    }
                }

            }

            if (sync && atTick(8))
                markDirty(true);

            if (atTick(1000)) {
                //TODO separate generation packet here
            }

        } else {
            if (getEnergyStored() != getMaxEnergyStored() && burnTimeLeft > 0) {
                burnTimeLeft--;
                changeEnergy(generation, false);
            }
        }

    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        energy = compound.getInteger("energy");
        burnTimeLeft = compound.getInteger("burnTime");
        maxBurnTimeLeft = compound.getInteger("maxBurnTime");
        if (compound.hasKey("items")) {
            itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
        }
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("energy", energy);
        compound.setInteger("burnTime", burnTimeLeft);
        compound.setInteger("maxBurnTime", maxBurnTimeLeft);
        compound.setTag("items", itemStackHandler.serializeNBT());
        return super.writeToNBT(compound);
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return Arrays.asList(capabilities).contains(capability) || super.hasCapability(capability, facing);
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

    @Override
    public void setMaxBurnTime(int a, boolean sync) {
        maxBurnTimeLeft = a;
        markDirty(sync);
    }

    @Override
    public int getBurnTime() {
        return burnTimeLeft;
    }

    @Override
    public int getMaxBurnTime() {
        return maxBurnTimeLeft;
    }

    @Override
    public void markDirty(boolean sync) {
        super.markDirty();
        if (sync) {
            new PacketServerToClient(
                    this,
                    packets,
                    getEnergyStored(),
                    generation,
                    burnTimeLeft,
                    maxBurnTimeLeft
            )
                    .sendToDimension();
        }
    }

    @Override
    public void setProduce(int a, boolean sync) {
        generation = a;
        markDirty(sync);
    }
}
