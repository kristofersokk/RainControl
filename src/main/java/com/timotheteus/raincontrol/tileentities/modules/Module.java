package com.timotheteus.raincontrol.tileentities.modules;

public abstract class Module {

    private int tick = 0;

    /**
     * @return needs sync
     */
    public boolean tick() {
        tick++;
        if (tick >= 32400)
            tick = 0;
        return false;
    }

    boolean atTick(int a) {
        return Math.floorMod(tick, a) == 0;
    }
}
