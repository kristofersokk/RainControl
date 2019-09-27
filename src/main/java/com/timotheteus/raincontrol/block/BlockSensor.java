package com.timotheteus.raincontrol.block;

import com.timotheteus.raincontrol.block.properties.RainSensorProperty;
import com.timotheteus.raincontrol.config.ConfigHandler;
import com.timotheteus.raincontrol.items.Items;
import com.timotheteus.raincontrol.tileentities.TileEntitySensor;
import com.timotheteus.raincontrol.util.Delay;
import com.timotheteus.raincontrol.util.Names;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockSensor extends BlockTileBase {

    private static final RainSensorProperty POWER = new RainSensorProperty();
    private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D);
    private Delay rain;

    public BlockSensor() {
        super(TileEntitySensor.class,
                Properties.create(Material.IRON)
                    .hardnessAndResistance(6f, 10f)
                    .sound(SoundType.METAL),
                Names.BLOCK_SENSOR);
//        setDefaultState(getDefaultState().with(POWER, 0));
        rain = new Delay(ConfigHandler.SENSOR.delay.get());

    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockReader world, BlockPos pos, @Nullable EnumFacing side) {
        return true;
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockReader blockAccess, BlockPos pos, EnumFacing side) {
        return blockState.get(POWER);
    }

    public void updatePower(World world, BlockPos pos){
        rain.tick(world.getWorldInfo().isRaining());
        IBlockState blockState = world.getBlockState(pos);
        if (blockState.get(POWER) == 15 && rain.all(false)//it hasn't been raining the last 70 ticks
                || blockState.get(POWER) == 0 && rain.all(true))
            world.setBlockState(pos, blockState.with(POWER, rain.getLast() ? 15 : 0));
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockReader world, BlockPos pos) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    public int getMetaFromState(IBlockState state) {
        return state.get(POWER);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Override
    public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune) {
        return Items.blocksensor_item;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(IBlockState state, IBlockReader world) {
        return null;
    }
}
