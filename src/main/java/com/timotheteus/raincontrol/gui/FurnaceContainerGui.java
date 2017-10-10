package com.timotheteus.raincontrol.gui;

import com.timotheteus.raincontrol.gui.container.ContainerFurnace;
import com.timotheteus.raincontrol.tileentities.TileEntityFurnaceBlock;
import com.timotheteus.raincontrol.util.ModUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class FurnaceContainerGui extends GuiContainer {

    public static final int WIDTH = 180;
    public static final int HEIGHT = 141;

    private static final ResourceLocation inventory = new ResourceLocation(ModUtil.MOD_ID, "textures/gui/furnace/inventory.png");
    private static final ResourceLocation furnace = new ResourceLocation(ModUtil.MOD_ID, "textures/gui/furnace/furnace.png");

    public FurnaceContainerGui(TileEntityFurnaceBlock tileEntity, ContainerFurnace container) {
        super(container);

        xSize = WIDTH;
        ySize = HEIGHT;
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(inventory);
        drawTexturedModalRect(guiLeft, guiTop + 54, 0, 0, xSize, 87);

        mc.getTextureManager().bindTexture(furnace);
        drawTexturedModalRect(guiLeft + 52, guiTop, 0, 0, 76, 56);
    }
}
