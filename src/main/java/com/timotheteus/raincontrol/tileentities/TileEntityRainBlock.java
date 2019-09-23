package com.timotheteus.raincontrol.tileentities;

import com.timotheteus.raincontrol.config.ConfigHandler;
import com.timotheteus.raincontrol.tileentities.modules.ModuleTypes;
import com.timotheteus.raincontrol.util.PlayerHelper;
import com.timotheteus.raincontrol.util.TextHelper;
import com.timotheteus.raincontrol.util.WorldHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.timotheteus.raincontrol.config.ConfigHandler.ActivationType.FE;

public class TileEntityRainBlock extends TileEntityBase implements Property.Energy, Energy.Consumer {

    private static final int cooldownLength = 10;
    private boolean redstone = false;
    private int prevEnergy = 0;
    private int energy = 0;
    private int cooldown = 0;
    private static final Capability[] capabilities = new Capability[]{
            CapabilityEnergy.ENERGY
    };

    public TileEntityRainBlock() {
        super(RainControlTileEntityType.RAIN, new ModuleTypes[]{}, new Object[][]{});
    }

    public void activated(@Nullable EntityPlayer player, boolean shiftKeyDown){
        switch (ConfigHandler.RAIN_BLOCK.type.get()){
            case FREE:
                activatedFree(player);
                break;
            case FE:
                activatedFE(player, shiftKeyDown);
                break;
            case XP:
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
            if (!needsSky() || world.canSeeSky(pos.up())) {
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
            if (!needsSky() || world.canSeeSky(pos.up())) {
                if (player != null){
                    if (!shiftKeyDown) {
                        TextHelper.chatMessageServer(player, "Energy: " + TextHelper.getEnergyText(energy) + " FE");
                    } else if (cooldown == 0) {
                        if (energy >= getActivation()) {
                            activateRainBlock(allPlayers);
                        } else {
                            TextHelper.chatMessageServer(player, "Not enough energy. Energy: " + TextHelper.getEnergyText(energy) + " FE");
                        }
                    }
                }else if (cooldown == 0){
                    if (energy >= getActivation()) {
                        activateRainBlock(allPlayers);
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

    private void activateRainBlock(List<EntityPlayer> allPlayers) {
        changeEnergy(-getActivation(), true);
        cooldown = cooldownLength;
        if (world.getWorldInfo().isRaining()) {
            world.getWorldInfo().setRaining(false);
            TextHelper.chatMessageServer(allPlayers, getDesciptiveString() + "Stopping rain");
        } else {
            world.getWorldInfo().setRaining(true);
            TextHelper.chatMessageServer(allPlayers, getDesciptiveString() + "Starting rain");
        }
    }

    private void activatedXP(@Nullable EntityPlayer player){
        if (!world.isRemote){
            ArrayList<EntityPlayer> somePlayers = WorldHelper.getPlayersWithinRange(world, pos, 30, WorldHelper.Shape.ROUND);
            List<EntityPlayer> allPlayers = WorldHelper.getAllPlayersInDimension(world);
            //just to check if there is a player
            if (player != null){
                if (!needsSky() || world.canSeeSky(pos.up())) {
                    if (cooldown == 0) {
                        if (PlayerHelper.removeXPLevels(player, ConfigHandler.RAIN_BLOCK.XP_levels.get(), pos, true)) {
                            cooldown = cooldownLength;
                            if (world.getWorldInfo().isRaining()) {
                                world.getWorldInfo().setRaining(false);
                                TextHelper.chatMessageServer(allPlayers, getDesciptiveString() + "Stopping rain");
                            } else {
                                world.getWorldInfo().setRaining(true);
                                TextHelper.chatMessageServer(allPlayers, getDesciptiveString() + "Starting rain");
                            }
                        } else {
                            TextHelper.chatMessageServer(player, "Not enough levels. " + ConfigHandler.RAIN_BLOCK.XP_levels.get() + " levels needed.");
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
    public void tick() {

        //server
        if (world != null && !world.isRemote){
            boolean prevRedstone = redstone;
            redstone = world.isBlockPowered(this.pos);
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
        return ConfigHandler.RAIN_BLOCK.needsSky.get();
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
        return ConfigHandler.RAIN_BLOCK.FE_capacity.get();
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return ConfigHandler.RAIN_BLOCK.type.get() == FE;
    }


    @Override
    public void read(NBTTagCompound compound) {
        energy = compound.getInt("energy");
        super.read(compound);
    }

    @Override
    @Nonnull
    public NBTTagCompound write(NBTTagCompound compound) {
        compound.putInt("energy", energy);
        return super.write(compound);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (ConfigHandler.RAIN_BLOCK.type.get() != FE)
            return 0;
        int received = Math.min(getMaxInput(), Math.min(getMaxEnergyStored() - energy, maxReceive));
        if (!simulate) {
            changeEnergy(received, true);
        }
        return received;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability) {
        if (Arrays.asList(capabilities).contains(capability)) {
            return capability.orEmpty(capability, LazyOptional.empty());
        }
        return LazyOptional.empty();
    }

    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = getTileData();
        compound.putInt("energy", energy);
        return compound;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagCompound compound = pkt.getNbtCompound();
        energy = compound.getInt("energy");
        sync();
    }

    public int getMaxInput() {
        return ConfigHandler.RAIN_BLOCK.FE_maxInput.get();
    }

    public int getActivation() {
        return ConfigHandler.RAIN_BLOCK.FE_activation.get();
    }
}
