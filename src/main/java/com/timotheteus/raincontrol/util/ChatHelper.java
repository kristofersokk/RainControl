package com.timotheteus.raincontrol.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

import java.util.List;

public class ChatHelper {

    /**
     * usually called on server
     * @param player
     */
    public static void chatMessageServer(EntityPlayer player, String message){
        try {
            player.sendStatusMessage(new TextComponentString(message), false);
        } catch (Exception e) {
        }
    }

    public static void chatMessageServer(List<EntityPlayer> players, String message){
        try {
            for(EntityPlayer player : players)
                player.sendStatusMessage(new TextComponentString(message), false);
        } catch (Exception e) {
        }
    }
}
