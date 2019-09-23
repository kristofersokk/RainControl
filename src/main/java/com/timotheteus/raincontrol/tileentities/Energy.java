package com.timotheteus.raincontrol.tileentities;

import net.minecraftforge.energy.IEnergyStorage;

interface Energy extends IEnergyStorage {

    interface Consumer extends Energy {
    }

    interface Producer extends Energy  {
        int getGeneration();
    }

    interface Battery extends Energy, Consumer, Producer {
    }
}
