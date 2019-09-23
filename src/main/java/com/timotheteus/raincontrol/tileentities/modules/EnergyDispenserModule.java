package com.timotheteus.raincontrol.tileentities.modules;

import com.timotheteus.raincontrol.tileentities.Property;
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

    //TODO add advanced dispersal, atm to one machine

    private final TileEntity te;
    private final int maxOutput;
    private final ArrayList<TileEntity> neighbours;
    private final Map<TileEntity, EnumFacing> neighbourFaces;

    public EnergyDispenserModule(TileEntity te, int maxOutput) {
        this.te = te;
        this.maxOutput = maxOutput;
        neighbours = new ArrayList<>();
        neighbourFaces = new HashMap<>();
        refreshNeighbours();
    }


    @Override
    public boolean tick() {
        super.tick();
        if (atTick(8)) {
            refreshNeighbours();
        }
        return disperseEnergy();
    }

    private boolean disperseEnergy() {
        boolean sync = false;
        IEnergyStorage storage = (IEnergyStorage) this.te;
        if (storage.getEnergyStored() > 0)
            if (!neighbours.isEmpty())
                for (TileEntity te : neighbours)
                    if (sendEnergyTo(te, neighbourFaces.get(te)))
                        sync = true;
        return sync;
//            ((TileEntityBase) te).markDirty(true);
    }

    /**
     * @return energy sent
     */
    private boolean sendEnergyTo(TileEntity neighbour, EnumFacing facing) {
        IEnergyStorage localStorage = (IEnergyStorage) this.te;

        if (neighbour.getCapability(CapabilityEnergy.ENERGY, facing).isPresent() && neighbour instanceof IEnergyStorage) {
            IEnergyStorage storage = (IEnergyStorage) neighbour;
            if (storage.canReceive()) {
                int receive = storage.receiveEnergy(Math.min(localStorage.getEnergyStored(), maxOutput), false);
                if (receive > 0) {
                    Property.Energy syncableEnergy = (Property.Energy) this.te;
                    return syncableEnergy.changeEnergy(-receive, false);
                }
            }else {
                return false;
            }
        }
        return false;
    }

    private void refreshNeighbours() {
        neighbours.clear();
        neighbourFaces.clear();
        BlockPos pos = te.getPos();
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos neighbourPos = pos.offset(facing);
            World world = te.getWorld();
            if (world != null) {
                TileEntity tileEntity = te.getWorld().getTileEntity(neighbourPos);
                if (tileEntity instanceof IEnergyStorage) {
                    neighbours.add(tileEntity);
                    neighbourFaces.put(tileEntity, facing.getOpposite());
                }
            }
        }
    }


}
