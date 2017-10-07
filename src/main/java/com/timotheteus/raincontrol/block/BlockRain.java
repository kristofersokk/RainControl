package com.timotheteus.raincontrol.block;

import com.timotheteus.raincontrol.items.itemblocks.ItemRainBlock;
import com.timotheteus.raincontrol.tileentities.TileEntityRainBlock;
import com.timotheteus.raincontrol.util.ModUtil;
import com.timotheteus.raincontrol.util.Names;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockRain extends BlockBase implements ITileEntityProvider{

	public BlockRain() {
		super(Material.IRON, Names.BLOCK_RAIN);
		this.setHardness(1.5F)
        .setRegistryName(this.getNameinRegistry())
        .setResistance(10f)
        .setUnlocalizedName(ModUtil.MOD_ID + "." + this.getNameinRegistry())
        .setCreativeTab(CreativeTabs.MISC);
		this.setSoundType(SoundType.METAL);
	}

	@SideOnly(Side.CLIENT)
    public void initModel(){
        ModelLoader.setCustomModelResourceLocation(new ItemRainBlock(new BlockRain()), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te != null && te instanceof TileEntityRainBlock){
            try {
                TileEntityRainBlock rainTE = (TileEntityRainBlock)te;
                rainTE.activated(playerIn, Keyboard.isKeyDown(Keyboard.KEY_LSHIFT));
            }catch (Exception e){}
        }
        return true;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Blocks.blockRain_item;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        TileEntityRainBlock te = world.getTileEntity(pos) instanceof TileEntityRainBlock ? (TileEntityRainBlock) world.getTileEntity(pos) : null;
        if (te != null) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setInteger("energy", te.getEnergyStored());
            ItemStack stack = new ItemStack(Blocks.blockRain_item);
            stack.setTagCompound(nbt);
            drops.add(stack);
        }
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        if (willHarvest) return true; //If it will harvest, delay deletion of the block until after getDrops
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    /**
     * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
     * Block.removedByPlayer
     */
    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack tool)
    {
        super.harvestBlock(world, player, pos, state, te, tool);
        world.setBlockToAir(pos);
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityRainBlock();
    }

}
