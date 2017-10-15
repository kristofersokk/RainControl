package com.timotheteus.raincontrol.tileentities;

import com.timotheteus.raincontrol.tileentities.modules.EnergyDispenserModule;
import com.timotheteus.raincontrol.tileentities.modules.Module;
import com.timotheteus.raincontrol.tileentities.modules.ModuleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

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

    protected boolean atTick(int a) {
        return Math.floorMod(tick, a) == 0;
    }


}
