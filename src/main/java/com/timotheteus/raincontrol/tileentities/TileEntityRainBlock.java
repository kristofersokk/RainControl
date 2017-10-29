package com.timotheteus.raincontrol.tileentities;

import com.timotheteus.raincontrol.config.ModConfig;
import com.timotheteus.raincontrol.tileentities.modules.ModuleTypes;
import com.timotheteus.raincontrol.util.PlayerHelper;
import com.timotheteus.raincontrol.util.TextHelper;
import com.timotheteus.raincontrol.util.WorldHelper;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TileEntityRainBlock extends TileEntityBase implements Property.Energy, Energy.Consumer {

    private static final int cooldownLength = 10;
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
        switch (ModConfig.RAINBLOCK.type) {
            case "FREE":
                activatedFree(player);
                break;
            case "FE":
                activatedFE(player, shiftKeyDown);
                break;
            case "XP":
                activatedXP(player);
                break;
        }
    }

    private String getDesciptiveString(){
        return String.format("[Rain Block @%s,%s,%s] ", pos.getX(), pos.getY(), pos.getZ());
    }

    private void activatedFree(@Nullable EntityPlayer player){
        if (!world.isRemote){
            List<EntityPlayer> somePlayers = WorldHelper.getPlayersWithinRange(world, pos, 30, WorldHelper.Shape.ROUND);
            List<EntityPlayer> allPlayers = WorldHelper.getAllPlayersInDimension(world);
            if (!needsSky() || needsSky() && world.canSeeSky(pos.up())) {
                if (cooldown == 0) {
                    cooldown = cooldownLength;
                    if (world.getWorldInfo().isRaining()) {
                        world.getWorldInfo().setRaining(false);
                        TextHelper.chatMessageServer(allPlayers, getDesciptiveString() + "Stopping rain");
                    } else {
                        world.getWorldInfo().setRaining(true);
                        TextHelper.chatMessageServer(allPlayers, getDesciptiveString() + "Starting rain");
                    }
                }
            } else {
                if (player == null){
                    TextHelper.chatMessageServer(somePlayers, getDesciptiveString() + "Needs to see the sky");
                }else{
                    TextHelper.chatMessageServer(player, "Needs to see the sky");
                }
            }
        }
    }

    private void activatedFE(@Nullable EntityPlayer player, boolean shiftKeyDown){
        if (!world.isRemote){
            ArrayList<EntityPlayer> somePlayers = WorldHelper.getPlayersWithinRange(world, pos, 30, WorldHelper.Shape.ROUND);
            List<EntityPlayer> allPlayers = WorldHelper.getAllPlayersInDimension(world);
            if (!needsSky() || needsSky() && world.canSeeSky(pos.up())) {
                if (player != null){
                    if (!shiftKeyDown) {
                        TextHelper.chatMessageServer(player, "Energy: " + TextHelper.getEnergyText(energy) + " FE");
                    } else if (cooldown == 0) {
                        if (energy >= getActivation()) {
                            changeEnergy(-getActivation(), true);
                            cooldown = cooldownLength;
                            if (world.getWorldInfo().isRaining()) {
                                world.getWorldInfo().setRaining(false);
                                TextHelper.chatMessageServer(allPlayers, getDesciptiveString() + "Stopping rain");
                            } else {
                                world.getWorldInfo().setRaining(true);
                                TextHelper.chatMessageServer(allPlayers, getDesciptiveString() + "Starting rain");
                            }
                        } else {
                            TextHelper.chatMessageServer(player, "Not enough energy. Energy: " + TextHelper.getEnergyText(energy) + " FE");
                        }
                    }
                }else if (cooldown == 0){
                    if (energy >= getActivation()) {
                        changeEnergy(-getActivation(), true);
                        cooldown = cooldownLength;
                        if (world.getWorldInfo().isRaining()) {
                            world.getWorldInfo().setRaining(false);
                            TextHelper.chatMessageServer(allPlayers, getDesciptiveString() + "Stopping rain");
                        } else {
                            world.getWorldInfo().setRaining(true);
                            TextHelper.chatMessageServer(allPlayers, getDesciptiveString() + "Starting rain");
                        }
                    } else {
                        TextHelper.chatMessageServer(somePlayers, getDesciptiveString() + "Not enough energy. Energy: " + TextHelper.getEnergyText(energy) + " FE");
                    }
                }
            } else {
                if (player == null){
                    TextHelper.chatMessageServer(somePlayers, getDesciptiveString() + "Needs to see the sky");
                }else{
                    TextHelper.chatMessageServer(player, "Needs to see the sky");
                }
            }
        }
    }

    private void activatedXP(@Nullable EntityPlayer player){
        if (!world.isRemote){
            ArrayList<EntityPlayer> somePlayers = WorldHelper.getPlayersWithinRange(world, pos, 30, WorldHelper.Shape.ROUND);
            List<EntityPlayer> allPlayers = WorldHelper.getAllPlayersInDimension(world);
            //just to check if there is a player
            if (player != null){
                if (!needsSky() || needsSky() && world.canSeeSky(pos.up())) {
                    if (cooldown == 0) {
                        if (PlayerHelper.removeXPLevels(player, ModConfig.RAINBLOCK.XP_levels, pos, true)) {
                            cooldown = cooldownLength;
                            if (world.getWorldInfo().isRaining()) {
                                world.getWorldInfo().setRaining(false);
                                TextHelper.chatMessageServer(allPlayers, getDesciptiveString() + "Stopping rain");
                            } else {
                                world.getWorldInfo().setRaining(true);
                                TextHelper.chatMessageServer(allPlayers, getDesciptiveString() + "Starting rain");
                            }
                        } else {
                            TextHelper.chatMessageServer(player, "Not enough levels. " + ModConfig.RAINBLOCK.XP_levels + " levels needed.");
                            cooldown = 10;
                        }
                    }
                } else {
                    if (needsSky())
                        TextHelper.chatMessageServer(player, "Needs to see the sky");
                }
            }else{
                //redstone activation
                if (cooldown == 0){
                    TextHelper.chatMessageServer(somePlayers, "Redstone mode not available when in XP mode [yet, WIP]");
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

    private boolean needsSky(){
        return ModConfig.RAINBLOCK.needsSky;
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
        return ModConfig.RAINBLOCK.FE_capacity;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return ModConfig.isFEMode();
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
        if (!ModConfig.isFEMode())
            return 0;
        int received = Math.min(getMaxInput(), Math.min(getMaxEnergyStored() - energy, maxReceive));
        if (!simulate) {
            changeEnergy(received, true);
        }
        return received;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return ModConfig.isFEMode() && (Arrays.asList(capabilities).contains(capability) || super.hasCapability(capability, facing));
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
        long received = Math.min(getMaxInput(), Math.min(getMaxEnergyStored() - energy, power));
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
        return getMaxEnergyStored();
    }

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        int received = Math.min(getMaxInput(), Math.min(getMaxEnergyStored() - energy, maxReceive));
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
        return getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return true;
    }

    public int getMaxInput() {
        return ModConfig.RAINBLOCK.FE_maxInput;
    }

    public int getActivation() {
        return ModConfig.RAINBLOCK.FE_activation;
    }
}
