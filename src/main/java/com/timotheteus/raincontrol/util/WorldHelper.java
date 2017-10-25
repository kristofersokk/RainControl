package com.timotheteus.raincontrol.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class WorldHelper {

    public enum Shape{
        ROUND,
        SQUARE
    }

    public static ArrayList<EntityPlayer> getPlayersWithinRange(World world, BlockPos pos, double range, Shape shape){
        List<EntityPlayer> allPlayers = world.playerEntities;
        ArrayList<EntityPlayer> inRange = new ArrayList<>();

        player:
        for (EntityPlayer player : allPlayers){
            if (Math.abs(player.posX - pos.getX()) <= range &&
                Math.abs(player.posY - pos.getY()) <= range &&
                Math.abs(player.posZ - pos.getZ()) <= range)
            {
                switch (shape){
                    case ROUND:
                        //outside the sphere
                        if (distanceEuclidean(player.posX - pos.getX(), player.posY - pos.getY(), player.posZ - pos.getZ()) > range) {
                            continue player;
                        }
                    case SQUARE:
                        inRange.add(player);
                }
            }
        }
        return inRange;
    }

    private static double distanceEuclidean(double x, double y, double z){
        return Math.sqrt(Math.pow(x, 2d) + Math.pow(y, 2d) + Math.pow(z, 2d));
    }
}
