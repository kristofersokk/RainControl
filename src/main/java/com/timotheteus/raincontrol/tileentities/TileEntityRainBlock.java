package com.timotheteus.raincontrol.tileentities;

import com.pengu.hammercore.common.capabilities.CapabilityEJ;
import com.pengu.hammercore.energy.iPowerStorage;
import com.timotheteus.raincontrol.packets.PacketServerToClient;
import com.timotheteus.raincontrol.packets.PacketTypes;
import com.timotheteus.raincontrol.tileentities.modules.ModuleTypes;
import com.timotheteus.raincontrol.util.ChatHelper;
import com.timotheteus.raincontrol.util.WorldHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;

public class TileEntityRainBlock extends TileEntityBase implements IEnergyStorage, Syncable.Sync, Syncable.Energy, iPowerStorage {

    private static final int cooldownLength = 100;

    private int timer = 0;

    private static final int maxStorage = 10000000;
    private static final int maxInput = 20000;
    private boolean redstone = false;
    private int prevEnergy = 0;
    private int energy = 0;
    private int cooldown = 0;
    private static final Capability[] capabilities = new Capability[]{
            CapabilityEnergy.ENERGY,
            CapabilityEJ.ENERGY
    };
    private static final PacketTypes.SERVER[] packets = new PacketTypes.SERVER[]{PacketTypes.SERVER.ENERGY};

    public TileEntityRainBlock() {
        super(new ModuleTypes[]{}, new Object[][]{});
    }

    public void activated(@Nullable EntityPlayer player, boolean shiftKeyDown){
        try {
            World world = player.getEntityWorld();
            if (!world.isRemote) {
                if (!shiftKeyDown){
                    player.sendStatusMessage(new TextComponentString("Energy: " + ChatHelper.getFormattedInt(energy) + " FE"), false);
                }else if (cooldown == 0){
                    if (energy >= 1000000){
                        changeEnergy(-1000000, true);
                        cooldown = cooldownLength;
                        if (world.getWorldInfo().isRaining()){
                            world.getWorldInfo().setRaining(false);
                            ChatHelper.chatMessageServer(player, "Stopping rain");
                        }else{
                            world.getWorldInfo().setRaining(true);
                            ChatHelper.chatMessageServer(player, "Starting rain");
                        }
                    }else{
                        ChatHelper.chatMessageServer(player, "Not enough energy. Energy: " + ChatHelper.getFormattedInt(energy) + " FE");
                        cooldown = 20;
                    }
                }
            }
        } catch (NullPointerException e) {
            //redstone activation
            if (!world.isRemote) {
                if (cooldown == 0){
                    ArrayList<EntityPlayer> players = WorldHelper.getPlayersWithinRange(world, pos, 5, WorldHelper.Shape.ROUND);
                    if (energy >= 1000000){
                        changeEnergy(-1000000, true);
                        cooldown = cooldownLength;
                        if (world.getWorldInfo().isRaining()){
                            world.getWorldInfo().setRaining(false);
                            ChatHelper.chatMessageServer(players, "Stopping rain");
                        }else{
                            world.getWorldInfo().setRaining(true);
                            ChatHelper.chatMessageServer(players, "Starting rain");
                        }
                    }else{
                        ChatHelper.chatMessageServer(players, "Not enough energy. Energy: " + ChatHelper.getFormattedInt(energy) + " FE");
                        cooldown = 20;
                    }
                }
            }
        }
    }

    @Override
    public void update() {
        //server
        if (!world.isRemote){
            timer++;

            boolean prevRedstone = redstone;
            redstone = getWorld().isBlockPowered(this.pos);
            if (!prevRedstone && redstone)
                activated(null, false);

            if (cooldown > 0)
                cooldown--;

            if (timer >= 2) {
                if (prevEnergy != energy){
                    new PacketServerToClient(this, new PacketTypes.SERVER[]{PacketTypes.SERVER.ENERGY}, getEnergyStored());
                    prevEnergy = energy;
                }
                timer = 0;
            }
        }
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

    // Forge Energy

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
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int received = Math.min(maxInput, Math.min(maxStorage - energy, maxReceive));
        if (!simulate) {
            changeEnergy(received, true);
        }
        return received;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return Arrays.asList(capabilities).contains(capability) || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (Arrays.asList(capabilities).contains(capability))
            return (T) this;
        return super.getCapability(capability, facing);
    }

    @Override
    public void markDirty(boolean sync) {
        super.markDirty();
        if (sync) {
            new PacketServerToClient(
                    this,
                    packets,
                    getEnergyStored()
            )
                    .sendToDimension();
        }
    }

}
