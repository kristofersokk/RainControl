package com.timotheteus.raincontrol.gui;

import com.timotheteus.raincontrol.gui.container.ContainerFurnace;
import com.timotheteus.raincontrol.tileentities.TileEntityFurnaceBlock;
import com.timotheteus.raincontrol.util.ModUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class FurnaceContainerGui extends GuiContainer {

    public static final int WIDTH = 180;
    public static final int HEIGHT = 152;

    private static final ResourceLocation background = new ResourceLocation(ModUtil.MOD_ID, "textures/gui/furnace/background.png");

    public FurnaceContainerGui(TileEntityFurnaceBlock tileEntity, ContainerFurnace container) {
        super(container);

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
