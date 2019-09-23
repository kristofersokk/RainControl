package com.timotheteus.raincontrol.handlers;

import com.timotheteus.raincontrol.tileentities.IGUITile;
import com.timotheteus.raincontrol.tileentities.TileEntityInventoryBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class GuiHandler implements IGuiHandler {

    public static GuiScreen openGui(FMLPlayMessages.OpenContainer openContainer) {
        BlockPos pos = openContainer.getAdditionalData().readBlockPos();
//        new GUIChest(type, (IInventory) Minecraft.getInstance().player.inventory, (IInventory) Minecraft.getInstance().world.getTileEntity(pos));
        TileEntityInventoryBase te = (TileEntityInventoryBase) Minecraft.getInstance().world.getTileEntity(pos);
        if (te != null) {
            return te.createGui(Minecraft.getInstance().player);
        }
        return null;
    }

    //TODO can remove these, I think

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IGUITile) {
            return ((IGUITile) te).createContainer(player);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IGUITile) {
            return ((IGUITile) te).createGui(player);
        }
        return null;
    }

}
