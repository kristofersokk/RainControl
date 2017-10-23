package com.timotheteus.raincontrol.tileentities;

import com.sun.istack.internal.NotNull;
import com.timotheteus.raincontrol.config.Config;
import com.timotheteus.raincontrol.gui.CustomSlot;
import com.timotheteus.raincontrol.tileentities.modules.ModuleTypes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.Arrays;

public class TileEntityGeneratorBlock extends TileEntityInventoryBase implements Property.Energy, Property.BurnTime, Energy.Producer {

    private static final int maxStorage = 200000;
    private static final int maxOutput = 1000;
    private static final Capability[] capabilities = new Capability[]{
            CapabilityEnergy.ENERGY,
            CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
    };
    private static Integer generation = Config.generatorProduce;

    private int energy;
    private int maxBurnTimeLeft;
    private int burnTimeLeft;

    public TileEntityGeneratorBlock() {
        super(new ModuleTypes[]{
                ModuleTypes.ENERGY_DISPENSER
        }, new Object[][]{
                {maxOutput}
                },
                CustomSlot.StackFilter.GENERATOR
        );
        markDirty(false);
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            boolean sync = false;
            super.update();

            //burning
            if (burnTimeLeft > 0) {
                if (energy != maxStorage) {
                    burnTimeLeft--;
                    if (changeEnergy(getGeneration(), false))
                        sync = true;
                }
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

            if (sync && atTick(10))
                markDirty(true);

        } else {
            //client-side
            if (getEnergyStored() != getMaxEnergyStored() && burnTimeLeft > 0) {
                burnTimeLeft--;
                changeEnergy(getGeneration(), false);
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

    @NotNull
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

    public int getBurnTime() {
        return burnTimeLeft;
    }

    public int getMaxBurnTime() {
        return maxBurnTimeLeft;
    }

    @NotNull
    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = getTileData();
        compound.setInteger("energy", energy);
        compound.setInteger("burnTime", burnTimeLeft);
        compound.setInteger("maxBurnTime", maxBurnTimeLeft);
        return compound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagCompound compound = pkt.getNbtCompound();
        energy = compound.getInteger("energy");
        burnTimeLeft = compound.getInteger("burnTime");
        maxBurnTimeLeft = compound.getInteger("maxBurnTime");
        sync();
    }

    @Override
    public long getStoredPower() {
        return energy;
    }

    @Override
    public long getCapacity() {
        return maxStorage;
    }

    public int getGeneration() {
        return generation;
    }

    @Override
    public long takePower(long power, boolean simulated) {
        long extracted = Math.min(power, Math.min(maxOutput, energy));
        if (!simulated) {
            changeEnergy(-(int) extracted, true);
        }
        return extracted;
    }

    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        int extracted = Math.min(maxExtract, Math.min(maxOutput, energy));
        if (!simulate) {
            changeEnergy(-extracted, true);
        }
        return extracted;
    }

    @Override
    public int getEnergyStored(EnumFacing from) {
        return energy;
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return maxStorage;
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return true;
    }
}
