package com.timotheteus.raincontrol.packets;

public class PacketTypes {


    public enum SERVER {

        /**
         * needs BlockPos, int energy
         */
        ENERGY(),
        /**
         * needs BlockPos, int burnTime
         */
        BURN_TIME();

        SERVER() {
        }

        public static SERVER[] getTypes(int[] ids) {
            SERVER[] results = new SERVER[ids.length];
            for (int i = 0; i < ids.length; i++) {
                results[i] = values()[i];
            }
            return results;
        }

        public static int[] getIds(SERVER[] types) {
            int[] results = new int[types.length];
            for (int i = 0; i < types.length; i++) {
                SERVER type = types[i];
                results[i] = type.ordinal();
            }
            return results;
        }
    }

    public enum CLIENT {
        ;

        CLIENT() {

        }

        public static CLIENT[] getTypes(int[] ids) {
            CLIENT[] results = new CLIENT[ids.length];
            for (int i = 0; i < ids.length; i++) {
                results[i] = values()[i];
            }
            return results;
        }

        public static int[] getIds(CLIENT[] types) {
            int[] results = new int[types.length];
            for (int i = 0; i < types.length; i++) {
                CLIENT type = types[i];
                results[i] = type.ordinal();
            }
            return results;
        }
    }


}
