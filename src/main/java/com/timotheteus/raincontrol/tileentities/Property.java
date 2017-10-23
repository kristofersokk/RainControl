package com.timotheteus.raincontrol.tileentities;

public interface Property {

    interface Energy {

        void setEnergy(int energy, boolean sync);

        boolean changeEnergy(int energy, boolean sync);
    }

    interface BurnTime {

        void setBurnTime(int burntime, boolean sync);

        void setMaxBurnTime(int maxBurnTime, boolean sync);
    }

}
