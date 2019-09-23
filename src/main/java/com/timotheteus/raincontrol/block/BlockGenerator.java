package com.timotheteus.raincontrol.block;

import com.timotheteus.raincontrol.items.Items;
import com.timotheteus.raincontrol.tileentities.IGUITile;
import com.timotheteus.raincontrol.tileentities.TileEntityBase;
import com.timotheteus.raincontrol.tileentities.TileEntityGeneratorBlock;
import com.timotheteus.raincontrol.util.Names;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BlockGenerator extends BlockContainerBase {

    public BlockGenerator() {
        super(TileEntityGeneratorBlock.class,
                Properties.create(Material.IRON)
                        .hardnessAndResistance(1.5f, 10f)
                        .sound(SoundType.METAL),
                Names.BLOCK_GENERATOR);
    }

    @Override
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityGeneratorBlock) {
            NetworkHooks.openGui((EntityPlayerMP) player, (ILockableContainer) ((TileEntityGeneratorBlock)te).createContainer(player), pos);
            return true;
        }
        return false;
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        if (te instanceof TileEntityGeneratorBlock) {
            ((TileEntityGeneratorBlock) te).dropInventory(worldIn, pos, (TileEntityBase) te);
        }
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }

    @Override
    public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune) {
        return Items.blockgenerator_item;
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockReader world, BlockPos pos, @Nullable EnumFacing side) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(IBlockState state, IBlockReader world) {
        return new TileEntityGeneratorBlock();
    }

}
