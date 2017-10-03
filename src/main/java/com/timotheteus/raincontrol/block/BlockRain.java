package com.timotheteus.raincontrol.block;

import com.timotheteus.raincontrol.tileentities.TileEntityRainBlock;
import com.timotheteus.raincontrol.util.ModUtil;
import com.timotheteus.raincontrol.util.Names;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;

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
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
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

    //TODO add redstone control


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
