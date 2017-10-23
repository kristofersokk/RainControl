package com.timotheteus.raincontrol.tileentities;

import com.timotheteus.raincontrol.tileentities.modules.EnergyDispenserModule;
import com.timotheteus.raincontrol.tileentities.modules.Module;
import com.timotheteus.raincontrol.tileentities.modules.ModuleTypes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class TileEntityBase extends TileEntity implements ITickable {

    private int tick = 0;
    private Module[] modules;

    TileEntityBase(ModuleTypes[] moduleTypes, Object[][] objects) {
        Module[] modules = new Module[moduleTypes.length];
        for (int i = 0; i < modules.length; i++) {
            ModuleTypes module = moduleTypes[i];
            switch (module) {
                case ENERGY_DISPENSER:
                    modules[i] = new EnergyDispenserModule(this, (int) objects[i][0]);
                    continue;
            }
        }
        this.modules = modules;
    }

    @Override
    public void update() {
        for (Module module : modules) {
            module.tick();
        }
        tick++;
        if (tick >= 1080)
            tick = 0;
    }

    public void dropInventory(World world, BlockPos pos, TileEntityBase te) {
        if (te != null && te instanceof Inventory) {
            ((Inventory) te).getSlotsHandler().dropInventory(world, pos);
        }
    }

    protected boolean atTick(int a) {
        return Math.floorMod(tick, a) == 0;
    }


    public void markDirty(boolean sync) {
        super.markDirty();
        if (sync) {
            sync();
        }
    }

    public void sync() {
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 1, getUpdateTag());
    }


}
