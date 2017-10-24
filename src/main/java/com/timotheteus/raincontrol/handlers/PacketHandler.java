package com.timotheteus.raincontrol.handlers;

import com.timotheteus.raincontrol.packets.PacketConfig;
import com.timotheteus.raincontrol.util.ModUtil;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

    private static int packetID = 0;

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModUtil.MOD_ID);

    public PacketHandler() {
    }

    public static int nextID(){
        return packetID++;
    }

    public static void registerMessages(){
        INSTANCE.registerMessage(PacketConfig.Handler.class, PacketConfig.class, nextID(), Side.CLIENT);
    }
}
