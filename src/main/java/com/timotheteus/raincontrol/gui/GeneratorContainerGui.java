package com.timotheteus.raincontrol.gui;

import com.timotheteus.raincontrol.gui.container.ContainerGenerator;
import com.timotheteus.raincontrol.tileentities.TileEntityGeneratorBlock;
import com.timotheteus.raincontrol.util.ModUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GeneratorContainerGui extends GuiContainer {

    public static final int WIDTH = 180;
    public static final int HEIGHT = 141;

    private static final ResourceLocation inventory = new ResourceLocation(ModUtil.MOD_ID, "textures/gui/generator/inventory.png");
    private static final ResourceLocation generator = new ResourceLocation(ModUtil.MOD_ID, "textures/gui/generator/generator.png");
    public TileEntityGeneratorBlock te;

    public GeneratorContainerGui(TileEntityGeneratorBlock tileEntity, ContainerGenerator container) {
        super(container);

        xSize = WIDTH;
        ySize = HEIGHT;
        te = tileEntity;
    }

    /**
     * @param max    maximum amount
     * @param a      amount
     * @param pixels length in pixels
     * @return length in pixels
     */
    public static int getScaled(int max, int a, int pixels) {
        return Math.round(a / max * pixels);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int l;
        mc.getTextureManager().bindTexture(inventory);
        drawTexturedModalRect(guiLeft, guiTop + 54, 0, 0, xSize, 87);

        mc.getTextureManager().bindTexture(generator);
        drawTexturedModalRect(guiLeft + 52, guiTop, 0, 0, 76, 56);
        l = getScaled(te.getMaxEnergyStored(), te.getEnergyStored(), 43);
        drawTexturedModalRect(guiLeft + 52 + 60, guiTop + 5 + (43 - l), 52 + 76, 0, 10, l);
    }
}
