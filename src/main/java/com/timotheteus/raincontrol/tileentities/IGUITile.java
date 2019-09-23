package com.timotheteus.raincontrol.tileentities;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public interface IGUITile {

    Container createContainer(EntityPlayer player);

    GuiContainer createGui(EntityPlayer player);

}
