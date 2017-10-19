package com.timotheteus.raincontrol.tileentities;

import com.timotheteus.raincontrol.tileentities.modules.EnergyDispenserModule;
import com.timotheteus.raincontrol.tileentities.modules.Module;
import com.timotheteus.raincontrol.tileentities.modules.ModuleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class TileEntityBase extends TileEntity implements ITickable {

    private int tick = 0;
    private Module[] modules;
    private ModuleTypes[] moduleTypes;

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
        this.moduleTypes = moduleTypes;
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


}
