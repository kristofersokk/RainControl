package com.timotheteus.raincontrol.handlers;

import com.timotheteus.raincontrol.util.ModUtil;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class PacketHandler {

    private static int packetID = 0;

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModUtil.MOD_ID);

    public PacketHandler() {
    }

    private static int nextID(){
        return packetID++;
    }

    public static void registerMessages(){
//        INSTANCE.registerMessage(PacketConfig.Handler.class, PacketConfig.class, nextID(), Side.CLIENT);
    }
}
