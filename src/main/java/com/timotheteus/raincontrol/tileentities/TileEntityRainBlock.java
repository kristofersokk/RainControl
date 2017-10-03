package com.timotheteus.raincontrol.tileentities;

import com.pengu.hammercore.common.capabilities.CapabilityEJ;
import com.pengu.hammercore.energy.iPowerStorage;
import com.timotheteus.raincontrol.handlers.PacketHandler;
import com.timotheteus.raincontrol.packets.PacketEnergyChange;
import com.timotheteus.raincontrol.util.ChatHelper;
import com.timotheteus.raincontrol.util.WorldHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class TileEntityRainBlock extends TileEntity implements IEnergyStorage, iPowerStorage, ITickable {

    public void activated(@Nullable EntityPlayer player, boolean shiftKeyDown){
        try {
            World world = player.getEntityWorld();
            if (!world.isRemote) {
                if (!shiftKeyDown){
                    player.sendStatusMessage(new TextComponentString("Energy: " + Integer.toString(energy)), false);
                }else if (cooldown == 0){
                    if (energy >= 1000000){
                        changeEnergy(-1000000);
                        cooldown = 80;
                        if (world.getWorldInfo().isRaining()){
                            world.getWorldInfo().setRaining(false);
                            ChatHelper.chatMessageServer(player, "Stopping rain");
                        }else{
                            world.getWorldInfo().setRaining(true);
                            ChatHelper.chatMessageServer(player, "Starting rain");
                        }
                    }else{
                        ChatHelper.chatMessageServer(player, "Not enough energy. Energy: " + Integer.toString(energy));
                        cooldown = 20;
                    }
                }
            }
        } catch (NullPointerException e) {
            //redstone activation
            if (!world.isRemote) {
                if (cooldown == 0){
                    ArrayList<EntityPlayer> players = WorldHelper.getPlayersWithinRange(world, pos, 7, WorldHelper.Shape.ROUND);
                    if (energy >= 1000000){
                        changeEnergy(-1000000);
                        cooldown = 80;
                        if (world.getWorldInfo().isRaining()){
                            world.getWorldInfo().setRaining(false);
                            ChatHelper.chatMessageServer(players, "Stopping rain");
                        }else{
                            world.getWorldInfo().setRaining(true);
                            ChatHelper.chatMessageServer(players, "Starting rain");
                        }
                    }else{
                        ChatHelper.chatMessageServer(players, "Not enough energy. Energy: " + Integer.toString(energy));
                        cooldown = 20;
                    }
                }
            }
        }
    }


    private int timer = 0;

    @Override
    public void update() {

        if (!world.isRemote){
            timer++;

            prevRedstone = redstone;
            redstone = getWorld().isBlockPowered(this.pos);
            if (!prevRedstone && redstone)
                activated(null, false);

            if (cooldown > 0)
                cooldown--;

            if (timer >= 4){
                if (prevEnergy != energy){
                    PacketHandler.INSTANCE.sendToDimension(new PacketEnergyChange(this.pos, getEnergyStored()),world.provider.getDimension());
                    prevEnergy = energy;
                }
                timer = 0;
            }
        }
    }
    public boolean redstone = false;
    public boolean prevRedstone = false;

    public static final int maxStorage = 10000000;
    public static final int maxInput = 20000;
    public int prevEnergy = 0;
    protected int energy = 0;
    protected int cooldown = 0;

    //Forge Energy

    public void changeEnergy(int a){
        energy += a;
        markDirty();
    }

    public void setEnergy(int a){
        energy = a;
        markDirty();
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int received = Math.min(maxInput, Math.min(maxStorage - energy, maxReceive));
        if (!simulate){
            changeEnergy(received);
        }
        return received;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
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
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        energy = compound.getInteger("energy");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("energy", energy);
        return super.writeToNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY || capability == CapabilityEJ.ENERGY)
            return true;
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityEnergy.ENERGY || capability == CapabilityEJ.ENERGY)
            return (T) this;
        return super.getCapability(capability, facing);
    }


}
