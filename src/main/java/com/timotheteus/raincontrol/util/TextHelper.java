package com.timotheteus.raincontrol.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

import java.util.List;

public class TextHelper {

    /**
     * usually called on server
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


    public static String getEnergyText(int a) {
        String b = Integer.toString(a);
        int len = b.length();
        int mod = Math.floorMod(len, 3);
        int commas = Math.floorDiv(len - 1, 3);
        StringBuilder result = new StringBuilder();
        if (mod == 0) mod = 3;
        result.append(b.substring(0, mod));
        if (commas > 0) {
            for (int i = 0; i < commas; i++) {
                result.append(",").append(b.substring(i * 3 + mod, i * 3 + mod + 3));
            }
        }
        return result.toString();
    }

    /**
     * @return (250) "12s 10"
     */
    public static String getTimeText(int ticks) {
        String result = "";
        if (ticks > 1200) {
            int minutes = ticks / 1200;
            result += Integer.toString(minutes) + "min ";
            ticks %= 1200;
        }
        if (ticks > 20) {
            int seconds = ticks / 20;
            result += Integer.toString(seconds) + "s ";
        }
        return result;
    }
}
