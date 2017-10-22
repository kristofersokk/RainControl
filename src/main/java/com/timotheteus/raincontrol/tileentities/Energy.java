package com.timotheteus.raincontrol.tileentities;

import cofh.redstoneflux.api.IEnergyProvider;
import cofh.redstoneflux.api.IEnergyReceiver;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraftforge.energy.IEnergyStorage;

public abstract interface Energy extends IEnergyStorage, ITeslaHolder {

    interface Consumer extends Energy, ITeslaConsumer, IEnergyReceiver {
    }

    interface Producer extends Energy, ITeslaProducer, IEnergyProvider {
    }

    interface Battery extends Energy, Consumer, Producer {
    }
}
