package com.timotheteus.raincontrol.tileentities;

import com.timotheteus.raincontrol.packets.PacketTypes;

public interface Syncable {

    /**
     * @param packet
     * @param message ENERGY; int
     *                BURN_TIME; int[]{burntime, maxburntime}
     * @param sync
     */
    void sync(PacketTypes.SERVER packet, Object message, boolean sync);

    void markDirty(boolean sync);

    interface Energy {

        void setEnergy(int energy, boolean sync);

        boolean changeEnergy(int energy, boolean sync);
    }

    interface BurnTime {

        void setBurnTime(int burntime, boolean sync);

        void setMaxBurnTime(int maxBurnTime, boolean sync);
    }

}
