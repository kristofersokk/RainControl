package com.timotheteus.raincontrol.handlers;

public class PacketHandler {

    private static int packetID = 0;

    public PacketHandler() {
    }

    private static int nextID(){
        return packetID++;
    }

    public static void registerMessages(){
//        INSTANCE.registerMessage(PacketConfig.Handler.class, PacketConfig.class, nextID(), Side.CLIENT);
    }
}
