package com.timotheteus.raincontrol.tileentities;

import com.timotheteus.raincontrol.config.ConfigHandler;
import com.timotheteus.raincontrol.gui.CustomSlot;
import com.timotheteus.raincontrol.gui.container.ContainerGenerator;
import com.timotheteus.raincontrol.gui.gui.GUIGenerator;
import com.timotheteus.raincontrol.tileentities.modules.ModuleTypes;
import com.timotheteus.raincontrol.util.Names;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;

public class TileEntityGeneratorBlock extends TileEntityInventoryBase implements Property.Energy, Property.BurnTime, Energy.Producer {

    private int energy;
    public int maxBurnTimeLeft;
    public int burnTimeLeft;

    public TileEntityGeneratorBlock() {
        super(RainControlTileEntityType.GENERATOR,
                new ModuleTypes[]{
                ModuleTypes.ENERGY_DISPENSER
        }, new Object[][]{
                {getMaxOutput()}
                },
                CustomSlot.StackFilter.GENERATOR
        );
        markDirty(false);
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            //server-side
            boolean sync = false;
            if (energy > getMaxEnergyStored()){//if capacity was lowered below energy levels
                energy = getMaxEnergyStored();
                sync = true;
            }
            int prevEnergy = energy;
            if (super.sync)
                sync = true;
            //burning
            if (burnTimeLeft > 0) {
                if (energy != getMaxEnergyStored()) {
                    burnTimeLeft--;
                    if (changeEnergy(getGeneration(), false))
                        if (prevEnergy != energy)
                            sync = true;
                }
            } else {
                //not burning
                if (getWorld() != null && !getWorld().isBlockPowered(getPos())) {
                    ItemStack stack = itemStackHandler.getStackInSlot(0);
                    int newBurnTime = TileEntityFurnace.getBurnTimes().getOrDefault(stack.getItem(), 0);
                    if (newBurnTime > 0) {
                        if (itemStackHandler.modifyStack(0, -1)) {
                            setBurnTime(newBurnTime, false);
                            setMaxBurnTime(newBurnTime, false);
                            sync = true;
                        }
                    }
                }
            }
            if (sync)
                markDirty(true);
        } else {
            //client-side
            if (getEnergyStored() != getMaxEnergyStored() && burnTimeLeft > 0) {
                burnTimeLeft--;
            }
        }

    }

    @Override
    public void read(NBTTagCompound compound) {
        energy = compound.getInt("energy");
        burnTimeLeft = compound.getInt("burnTime");
        maxBurnTimeLeft = compound.getInt("maxBurnTime");
        if (compound.contains("items")) {
            itemStackHandler.deserializeNBT((NBTTagCompound) compound.get("items"));
        }
        super.read(compound);
    }

    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        compound.putInt("energy", energy);
        compound.putInt("burnTime", burnTimeLeft);
        compound.putInt("maxBurnTime", maxBurnTimeLeft);
        compound.put("items", itemStackHandler.serializeNBT());
        return super.write(compound);
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extracted = Math.min(maxExtract, Math.min(getMaxOutput(), energy));
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
        return ConfigHandler.GENERATOR.capacity.get();
    }

    public static int getMaxOutput() {
        return ConfigHandler.GENERATOR.maxOutput.get();
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
        if (energy > getMaxEnergyStored())
            energy = getMaxEnergyStored();
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
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = getTileData();
        compound.putInt("energy", energy);
        compound.putInt("burnTime", burnTimeLeft);
        compound.putInt("maxBurnTime", maxBurnTimeLeft);
        return compound;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        energy = tag.getInt("energy");
        burnTimeLeft = tag.getInt("burnTime");
        maxBurnTimeLeft = tag.getInt("maxBurnTime");
        super.handleUpdateTag(tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagCompound compound = pkt.getNbtCompound();
        energy = compound.getInt("energy");
        burnTimeLeft = compound.getInt("burnTime");
        maxBurnTimeLeft = compound.getInt("maxBurnTime");
        sync();
    }

    @Override
    public int getGeneration() {
        return ConfigHandler.spec.getInt("generation");
    }

    @Override
    public int getHeight() {
        return 1;
    }

    @Override
    public int getWidth() {
        return 1;
    }

    @Override
    public ITextComponent getName() {
        return new TextComponentTranslation(Names.TE_GENERATOR);
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return getName();
    }

    @Nullable
    @Override
    public ITextComponent getCustomName() {
        return null;
    }

    @Override
    public Container createContainer(EntityPlayer player) {
        return new ContainerGenerator(player.inventory, this);
    }

    @Override
    public GuiContainer createGui(EntityPlayer player) {
        return new GUIGenerator(this, new ContainerGenerator(player.inventory, this));
    }
}
