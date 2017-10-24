package com.timotheteus.raincontrol.tileentities;

import com.timotheteus.raincontrol.tileentities.modules.ModuleTypes;
import com.timotheteus.raincontrol.util.TextHelper;
import com.timotheteus.raincontrol.util.WorldHelper;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;

public class TileEntityRainBlock extends TileEntityBase implements Property.Energy, Energy.Consumer {

    private static final int cooldownLength = 100;
    private static final int maxStorage = 10000000;
    private static final int maxInput = 4000;
    private static final int activation = 1000000;
    private boolean redstone = false;
    private int prevEnergy = 0;
    private int energy = 0;
    private int cooldown = 0;
    private static final Capability[] capabilities = new Capability[]{
            CapabilityEnergy.ENERGY,
            TeslaCapabilities.CAPABILITY_HOLDER,
            TeslaCapabilities.CAPABILITY_CONSUMER
    };

    public TileEntityRainBlock() {
        super(new ModuleTypes[]{}, new Object[][]{});
    }

    public void activated(@Nullable EntityPlayer player, boolean shiftKeyDown){
        try {
            //just to check if there is a player
            World world = player.getEntityWorld();
            if (!world.isRemote) {
                if (world.canSeeSky(pos.up())) {
                    if (!shiftKeyDown) {
                        player.sendStatusMessage(new TextComponentString("Energy: " + TextHelper.getEnergyText(energy) + " FE"), false);
                    } else if (cooldown == 0) {
                        if (energy >= activation) {
                            changeEnergy(-activation, true);
                            cooldown = cooldownLength;
                            if (world.getWorldInfo().isRaining()) {
                                world.getWorldInfo().setRaining(false);
                                TextHelper.chatMessageServer(player, "Stopping rain");
                            } else {
                                world.getWorldInfo().setRaining(true);
                                TextHelper.chatMessageServer(player, "Starting rain");
                            }
                        } else {
                            TextHelper.chatMessageServer(player, "Not enough energy. Energy: " + TextHelper.getEnergyText(energy) + " FE");
                            cooldown = 20;
                        }
                    }
                } else {
                    TextHelper.chatMessageServer(player, "Needs to see the sky");
                }
            }
        } catch (NullPointerException e) {//activated by redstone
            //redstone activation
            if (!world.isRemote) {
                if (cooldown == 0){
                    if (world.canSeeSky(pos.up())) {
                        ArrayList<EntityPlayer> players = WorldHelper.getPlayersWithinRange(world, pos, 5, WorldHelper.Shape.ROUND);
                        if (energy >= activation) {
                            changeEnergy(-activation, true);
                            cooldown = cooldownLength;
                            if (world.getWorldInfo().isRaining()) {
                                world.getWorldInfo().setRaining(false);
                                TextHelper.chatMessageServer(players, "Stopping rain");
                            } else {
                                world.getWorldInfo().setRaining(true);
                                TextHelper.chatMessageServer(players, "Starting rain");
                            }
                        } else {
                            TextHelper.chatMessageServer(players, "Not enough energy. Energy: " + TextHelper.getEnergyText(energy) + " FE");
                            cooldown = 20;
                        }
                    } else {
                        TextHelper.chatMessageServer(player, "Needs to see the sky");
                    }
                }
            }
        }
    }

    @Override
    public void update() {
        //server
        if (!world.isRemote){

            boolean prevRedstone = redstone;
            redstone = getWorld().isBlockPowered(this.pos);
            if (!prevRedstone && redstone)
                activated(null, false);
            if (cooldown > 0)
                cooldown--;
            if (atTick(4)) {
                if (prevEnergy != energy){
                    sync();
                    prevEnergy = energy;
                }
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
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (Arrays.asList(capabilities).contains(capability)) {
            return capability.cast((T) this);
        }
        return null;
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = getTileData();
        compound.setInteger("energy", energy);
        return compound;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagCompound compound = pkt.getNbtCompound();
        energy = compound.getInteger("energy");
        sync();
    }

    @Override
    public long givePower(long power, boolean simulated) {
        long received = Math.min(maxInput, Math.min(maxStorage - energy, power));
        if (!simulated) {
            changeEnergy((int) received, true);
        }
        return received;
    }

    @Override
    public long getStoredPower() {
        return energy;
    }

    @Override
    public long getCapacity() {
        return maxStorage;
    }

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        int received = Math.min(maxInput, Math.min(maxStorage - energy, maxReceive));
        if (!simulate) {
            changeEnergy(received, true);
        }
        return received;
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
