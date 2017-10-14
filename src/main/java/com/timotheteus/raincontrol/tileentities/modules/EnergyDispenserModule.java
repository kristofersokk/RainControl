package com.timotheteus.raincontrol.tileentities.modules;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class EnergyDispenserModule {

    private TileEntity te;
    private ArrayList<BlockPos> neighbours;

    public EnergyDispenserModule(TileEntity te) {
        this.te = te;
        refreshNeighbours();
    }

    public void disperseEnergy() {

    }

    public void refreshNeighbours() {
        neighbours.clear();
        BlockPos pos = te.getPos();
        for (EnumFacing facing : EnumFacing.VALUES) {
            //TODO recreate logic to disperse energy

        }
    }
}
