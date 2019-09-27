package com.timotheteus.raincontrol.block;

import com.timotheteus.raincontrol.items.Items;
import com.timotheteus.raincontrol.tileentities.TileEntityRainBlock;
import com.timotheteus.raincontrol.util.KeyBindings;
import com.timotheteus.raincontrol.util.Names;
import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockRain extends BlockTileBase {

	public BlockRain() {
		super(TileEntityRainBlock.class,
                Properties.create(Material.IRON)
                    .hardnessAndResistance(1.5f, 10f)
                    .sound(SoundType.METAL),
                Names.BLOCK_RAIN
        );
	}

    @Override
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityRainBlock){
            try {
                TileEntityRainBlock rainTE = (TileEntityRainBlock)te;
                rainTE.activated(player, KeyBindings.shift.isKeyDown());
            }catch (Exception e){}
        }
        return true;
    }

    @Override
    public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune) {
        return Items.blockRain_item;
    }

    @Override
    public void getDrops(IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune) {
        TileEntityRainBlock te = world.getTileEntity(pos) instanceof TileEntityRainBlock ? (TileEntityRainBlock) world.getTileEntity(pos) : null;
        if (te != null) {
            int energy = te.getEnergyStored();
            ItemStack stack = new ItemStack(Items.blockRain_item);
            if (energy > 0) {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.putInt("energy", energy);
                stack.setTag(nbt);
            }
            drops.add(stack);
        }
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest, IFluidState fluid) {
        //If it will harvest, delay deletion of the block until after getDrops
        return willHarvest || super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
    }

//    /**
//     * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
//     * Block.removedByPlayer
//     */
//    @Override
//    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack tool)
//    {
//        super.harvestBlock(world, player, pos, state, te, tool);
//        world.setBlockState(pos, IBlockState.);
//    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockReader world, BlockPos pos, @Nullable EnumFacing side) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(IBlockState state, IBlockReader world) {
        return new TileEntityRainBlock();
    }

}
