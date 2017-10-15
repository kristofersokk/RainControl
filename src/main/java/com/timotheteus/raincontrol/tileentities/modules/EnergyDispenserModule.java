package com.timotheteus.raincontrol.tileentities.modules;

import com.timotheteus.raincontrol.tileentities.Syncable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnergyDispenserModule extends Module {

    private TileEntity te;
    private World world;
    private int maxOutput;
    private ArrayList<TileEntity> neighbours;
    private Map<TileEntity, EnumFacing> neighbourFaces;

    public EnergyDispenserModule(TileEntity te, int maxOutput) {
        super(te);
        this.te = te;
        this.world = te.getWorld();
        this.maxOutput = maxOutput;
        neighbours = new ArrayList<>();
        neighbourFaces = new HashMap<>();
        refreshNeighbours();
    }

    @Override
    public void tick() {
        super.tick();
        if (atTick(4)) {
            refreshNeighbours();
        }
        disperseEnergy();
    }

    private void disperseEnergy() {
        boolean sync = false;
        IEnergyStorage storage = (IEnergyStorage) this.te;
        if (storage.getEnergyStored() > 0)
            if (!neighbours.isEmpty())
                for (TileEntity te : neighbours)
                    if (sendEnergyTo(te, neighbourFaces.get(te)))
                        sync = true;
        if (sync)
            ((Syncable.Sync) te).markDirty(true);
    }

    /**
     * @return energy sent
     */
    private boolean sendEnergyTo(TileEntity neighbour, EnumFacing facing) {
        IEnergyStorage localStorage = (IEnergyStorage) this.te;
        IEnergyStorage storage = (IEnergyStorage) neighbour;
        if (neighbour.hasCapability(CapabilityEnergy.ENERGY, facing)) {
            if (storage.canReceive()) {
                int receive = storage.receiveEnergy(Math.min(localStorage.getEnergyStored(), maxOutput), false);
                if (receive > 0) {
                    Syncable.Energy syncableEnergy = (Syncable.Energy) this.te;
                    return syncableEnergy.changeEnergy(receive, false);
                }
            }
        }
        return false;
    }

    private void refreshNeighbours() {
        neighbours.clear();
        neighbourFaces.clear();
        BlockPos pos = te.getPos();
        for (EnumFacing facing : EnumFacing.VALUES) {
            BlockPos neighbourPos = pos.offset(facing);
            TileEntity tileEntity = world.getTileEntity(neighbourPos);
            if (tileEntity != null)
                if (tileEntity instanceof IEnergyStorage)
                    neighbours.add(tileEntity);
            neighbourFaces.put(tileEntity, facing.getOpposite());
        }
    }


}
