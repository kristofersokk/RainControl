package com.timotheteus.raincontrol.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class PlayerHelper {

    /**
     * called on server-side
     */
    public static boolean removeXPLevels(EntityPlayer player, int levels, @Nullable BlockPos pos, boolean playSound){
        World world = player.world;
        if (player.experienceLevel >= levels){
            player.func_71013_b(levels);//borrowed from ContainerEnchantment.java:279
            if (playSound)
                //borrowed from ContainerEnchantment.java:322
                world.playSound(null, pos == null ? new BlockPos(player.posX, player.posY, player.posZ) : pos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 6F, world.rand.nextFloat() * 0.1F + 0.9F);
            return true;
        }
        return false;
    }
}
