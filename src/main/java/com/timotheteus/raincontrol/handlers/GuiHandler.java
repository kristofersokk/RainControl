package com.timotheteus.raincontrol.handlers;

import com.timotheteus.raincontrol.gui.FurnaceContainerGui;
import com.timotheteus.raincontrol.gui.container.ContainerFurnace;
import com.timotheteus.raincontrol.tileentities.TileEntityFurnaceBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler{

	public GuiHandler() {
		
	}

    public static final int GUY_FURNACEBLOCK = 0;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        switch (ID) {
            case GUY_FURNACEBLOCK:
                return new ContainerFurnace(player.inventory, (TileEntityFurnaceBlock) te);
        }

        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        switch (ID) {
            case GUY_FURNACEBLOCK:
                TileEntityFurnaceBlock containerTE = (TileEntityFurnaceBlock) te;
                return new FurnaceContainerGui(containerTE, new ContainerFurnace(player.inventory, containerTE));
        }

        return null;
    }

}
