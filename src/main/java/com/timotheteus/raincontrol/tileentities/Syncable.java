package com.timotheteus.raincontrol.tileentities;

public class Syncable {

    public interface Sync {
        void markDirty(boolean sync);
    }

    public interface Energy {

        void setEnergy(int a, boolean sync);

        /**
         * @param a
         * @param sync
         * @return anything changed
         */
        boolean changeEnergy(int a, boolean sync);
    }

    public interface Generator {

        void setProduce(int a, boolean sync);
    }

    public interface BurnTime {

        void setBurnTime(int a, boolean sync);

        void setMaxBurnTime(int a, boolean sync);

        int getBurnTime();

        int getMaxBurnTime();
    }

}
