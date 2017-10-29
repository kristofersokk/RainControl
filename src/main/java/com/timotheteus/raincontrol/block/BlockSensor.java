package com.timotheteus.raincontrol.block;

import com.timotheteus.raincontrol.config.ModConfig;
import com.timotheteus.raincontrol.tileentities.TileEntitySensor;
import com.timotheteus.raincontrol.util.Delay;
import com.timotheteus.raincontrol.util.Names;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockSensor extends BlockContainerBase {

    private static final PropertyBool POWER = PropertyBool.create("power");
    private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D);
    private Delay rain;

    public BlockSensor() {
        super(TileEntitySensor.class, Material.IRON, Names.BLOCK_SENSOR);
        setDefaultState(blockState.getBaseState().withProperty(POWER, false));
        setCreativeTab(CreativeTabs.MISC);
        setHardness(6);
        setSoundType(SoundType.METAL);
        rain = new Delay(ModConfig.SENSOR.delay);
    }

    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return blockState.getValue(POWER) ? 15 : 0;
    }

    public void updatePower(World world, BlockPos pos){
        rain.tick(world.getWorldInfo().isRaining());
        IBlockState blockState = world.getBlockState(pos);
        if (blockState.getValue(POWER) && rain.all(false)//it hasn't been raining the last 70 ticks
                || !blockState.getValue(POWER) && rain.all(true))
            world.setBlockState(pos, blockState.withProperty(POWER, rain.getLast()), 3);
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(POWER, meta != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(POWER) ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, POWER);
    }

//    @Override
//    @SuppressWarnings("deprecation")
//    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
//        return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
//    }

}
