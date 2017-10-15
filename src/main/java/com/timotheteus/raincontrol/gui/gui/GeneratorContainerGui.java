package com.timotheteus.raincontrol.gui.gui;

import com.timotheteus.raincontrol.gui.container.ContainerGenerator;
import com.timotheteus.raincontrol.tileentities.TileEntityGeneratorBlock;
import com.timotheteus.raincontrol.util.ChatHelper;
import com.timotheteus.raincontrol.util.ModUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import scala.actors.threadpool.Arrays;

public class GeneratorContainerGui extends GuiContainer {

    private static final int WIDTH = 180;
    private static final int HEIGHT = 141;

    private static final ResourceLocation inventory = new ResourceLocation(ModUtil.MOD_ID, "textures/gui/inventory.png");
    private static final ResourceLocation generator = new ResourceLocation(ModUtil.MOD_ID, "textures/gui/generator/generator.png");
    private TileEntityGeneratorBlock te;

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
    private static int getScaled(int max, int a, int pixels) {
        if (max == 0) {
            return 0;
        } else {
            return Math.round((float) a / max * pixels);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(inventory);
        drawTexturedModalRect(guiLeft, guiTop + 54, 0, 0, xSize, 87);

        mc.getTextureManager().bindTexture(generator);
        drawTexturedModalRect(guiLeft + 52, guiTop, 0, 0, 76, 56);
        int l = getScaled(te.getMaxEnergyStored(), te.getEnergyStored(), 43);
        drawTexturedModalRect(guiLeft + 52 + 61, guiTop + 5 + (43 - l), 76, (43 - l), 10, l);
        l = getScaled(te.getMaxBurnTime(), te.getBurnTime(), 72);
        drawTexturedModalRect(guiLeft + 52 + 2 + (72 - l), guiTop + 56 - 4, 0, 56, l, 4);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        //energy bar
        if (isPointInRegion(52 + 61, 5, 10, 43, mouseX, mouseY))
            drawHoveringText(Arrays.asList(new String[]{"Energy:", ChatHelper.getFormattedInt(te.getEnergyStored()) + "/" + ChatHelper.getFormattedInt(te.getMaxEnergyStored()) + " FE"}), mouseX - guiLeft, mouseY - guiTop);

        //progress bar
        if (isPointInRegion(52 + 2, 56 - 4, 72, 4, mouseX, mouseY))
            drawHoveringText(Arrays.asList(new String[]{"Burn time:", ChatHelper.getFormattedInt(te.getBurnTime()) + "/" + ChatHelper.getFormattedInt(te.getMaxBurnTime())}), mouseX - guiLeft, mouseY - guiTop);

    }
}
