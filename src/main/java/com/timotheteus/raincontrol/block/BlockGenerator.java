package com.timotheteus.raincontrol.block;

import com.timotheteus.raincontrol.RainControl;
import com.timotheteus.raincontrol.handlers.GuiHandler;
import com.timotheteus.raincontrol.tileentities.TileEntityGeneratorBlock;
import com.timotheteus.raincontrol.util.Names;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockGenerator extends BlockBase implements ITileEntityProvider {

    public BlockGenerator() {
        super(Material.IRON, Names.BLOCK_GENERATOR);
        this.setHardness(1.5F)
                .setResistance(10f)
                .setCreativeTab(CreativeTabs.MISC);
        this.setSoundType(SoundType.METAL);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        TileEntity te = worldIn.getTileEntity(pos);
        if (!(te instanceof TileEntityGeneratorBlock)) {
            return false;
        }
        playerIn.openGui(RainControl.instance, GuiHandler.GUY_FURNACEBLOCK, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityGeneratorBlock();
    }
}
