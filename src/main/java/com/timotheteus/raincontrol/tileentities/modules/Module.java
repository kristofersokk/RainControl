package com.timotheteus.raincontrol.tileentities.modules;

import net.minecraft.tileentity.TileEntity;

public abstract class Module {

    private int tick = 0;

    /**
     * empty
     */
    Module(TileEntity te) {
    }

    public void tick() {
        tick++;
        if (tick >= 1080)
            tick = 0;
    }

    boolean atTick(int a) {
        return Math.floorMod(tick, a) == 0;
    }
}
