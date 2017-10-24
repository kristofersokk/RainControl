package com.timotheteus.raincontrol.packets;

public class PacketTypes {

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
