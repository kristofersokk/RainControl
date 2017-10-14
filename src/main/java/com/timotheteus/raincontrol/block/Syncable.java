package com.timotheteus.raincontrol.block;

public class Syncable {

    public interface Energy {

        void setEnergy(int a, boolean sync);

        /**
         * @param a
         * @param sync
         * @return anything changed
         */
        boolean changeEnergy(int a, boolean sync);

    }

    public interface BurnTime {

        void setBurnTime(int a, boolean sync);
    }

}
