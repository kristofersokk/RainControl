package com.timotheteus.raincontrol.util;

public class Delay {

    public Delay(int ticks) {
        this.ticks = ticks;
        this.bools = new boolean[ticks];
    }

    public void tick(boolean newBool){
        System.arraycopy(bools, 1, bools, 0, ticks - 1);
        bools[ticks-1] = newBool;
    }

    public boolean all(boolean test){
        for (boolean a : bools)
            if (a != test)
                return false;
        return true;
    }

    public boolean get(int a){
        return bools[a];
    }

    public boolean getLast(){
        return bools[ticks-1];
    }

    private boolean[] bools;
    private int ticks;
}
