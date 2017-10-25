package com.timotheteus.raincontrol.handlers;

import com.timotheteus.raincontrol.gui.container.ContainerGenerator;
import com.timotheteus.raincontrol.gui.gui.GeneratorContainerGui;
import com.timotheteus.raincontrol.tileentities.TileEntityGeneratorBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;


public class GuiHandler implements IGuiHandler{

	public GuiHandler() {
		
	}

    public static final int GUY_FURNACEBLOCK = 0;

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        switch (ID) {
            case GUY_FURNACEBLOCK:
                return new ContainerGenerator(player.inventory, (TileEntityGeneratorBlock) te);
        }

        return null;
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        switch (ID) {
            case GUY_FURNACEBLOCK:
                TileEntityGeneratorBlock containerTE = (TileEntityGeneratorBlock) te;
                return new GeneratorContainerGui(containerTE, new ContainerGenerator(player.inventory, containerTE));
        }

        return null;
    }

}
