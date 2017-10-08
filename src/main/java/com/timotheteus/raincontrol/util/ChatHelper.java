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


    public static String getFormattedInt(int a) {
        String b = Integer.toString(a);
        int len = b.length();
        int mod = Math.floorMod(len, 3);
        int commas = Math.floorDiv(len - 1, 3);
        String result = "";
        if (mod == 0) mod = 3;
        result += b.substring(0, mod);
        if (commas > 0) {
            for (int i = 0; i < commas; i++) {
                result += "," + b.substring(i * 3 + mod, i * 3 + mod + 3);
            }
        }
        return result;
    }
}
