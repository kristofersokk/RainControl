package com.timotheteus.raincontrol.items.itemblocks;

import com.timotheteus.raincontrol.block.ItemBlockBase;
import com.timotheteus.raincontrol.tileentities.TileEntityRainBlock;
import com.timotheteus.raincontrol.util.ChatHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRainBlock extends ItemBlockBase {

    public ItemRainBlock(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (hasNbtTag(stack, "energy")) {
            int energy = stack.getTagCompound().getInteger("energy");
            tooltip.add("Energy: " + ChatHelper.getFormattedInt(energy) + " FE");
        } else {
            tooltip.add("Energy: 0 FE");
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);

        if (hasNbtTag(stack,"energy")){
            try{
                int energy = stack.getTagCompound().getInteger("energy");
                TileEntityRainBlock te = (TileEntityRainBlock) world.getTileEntity(pos);
                te.setEnergy(energy);
            }catch (Exception e){}
        }

        return true;
    }

    private static boolean hasNbtTag(ItemStack stack, String pNbtTag) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey(pNbtTag);
    }
}
