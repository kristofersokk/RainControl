package com.timotheteus.raincontrol.tileentities;

import com.timotheteus.raincontrol.tileentities.modules.EnergyDispenserModule;
import com.timotheteus.raincontrol.tileentities.modules.Module;
import com.timotheteus.raincontrol.tileentities.modules.ModuleTypes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class TileEntityBase extends TileEntity implements ITickable {

    private int tick = 0;
    @Nullable
    private final Module[] modules;

    TileEntityBase(TileEntityType type, ModuleTypes[] moduleTypes, Object[][] objects) {
        super(type);
        Module[] modules = new Module[moduleTypes.length];
        for (int i = 0; i < modules.length; i++) {
            ModuleTypes module = moduleTypes[i];
            switch (module) {
                case ENERGY_DISPENSER:
                    modules[i] = new EnergyDispenserModule(this, (int) objects[i][0]);
            }
        }
        this.modules = modules;
    }

    public TileEntityBase(TileEntityType type) {
        super(type);
        modules = null;
    }

    boolean sync = false;

    @Override
    public void tick() {
        sync = false;
        if (modules != null){
            for (Module module : modules) {
                if (module.tick())
                    sync = true;
            }
        }
        tick++;
        if (tick >= 32400)
            tick = 0;
    }

    public void dropInventory(World world, BlockPos pos, TileEntityBase te) {
        if (te instanceof Inventory) {
            ((Inventory) te).getSlotsHandler().dropInventory(world, pos);
        }
    }

    boolean atTick(int a) {
        return Math.floorMod(tick, a) == 0;
    }


    void markDirty(boolean sync) {
        super.markDirty();
        if (sync) {
            sync();
        }
    }

    void sync() {
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 1, getUpdateTag());
    }


}
