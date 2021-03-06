package com.timotheteus.raincontrol.gui.gui;

import com.timotheteus.raincontrol.gui.CustomSlot;
import com.timotheteus.raincontrol.gui.container.ContainerGenerator;
import com.timotheteus.raincontrol.tileentities.TileEntityGeneratorBlock;
import com.timotheteus.raincontrol.util.ModUtil;
import com.timotheteus.raincontrol.util.TextHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GUIGenerator extends GuiContainer {

    private static final int WIDTH = 180;
    private static final int HEIGHT = 141;

    private static final ResourceLocation inventory = new ResourceLocation(ModUtil.MOD_ID, "textures/gui/inventory.png");
    private static final ResourceLocation generator = new ResourceLocation(ModUtil.MOD_ID, "textures/gui/generator/generator.png");
    private final TileEntityGeneratorBlock te;

    public GUIGenerator(TileEntityGeneratorBlock tileEntity, ContainerGenerator container) {
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
        l = getScaled(te.maxBurnTimeLeft, te.burnTimeLeft, 72);
        drawTexturedModalRect(guiLeft + 52 + 2, guiTop + 56 - 4, 0, 56, l, 4);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        //energy bar
        if (isPointInRegion(52 + 61, 5, 10, 43, mouseX, mouseY))
            drawHoveringText(new ArrayList<>(Arrays.asList("Energy:", TextHelper.getEnergyText(te.getEnergyStored()) + "/", TextHelper.getEnergyText(te.getMaxEnergyStored()) + " FE")), mouseX - guiLeft, mouseY - guiTop);

        //progress bar
        if (isPointInRegion(52 + 2, 56 - 4, 72, 4, mouseX, mouseY))
            drawHoveringText(new ArrayList<>(Arrays.asList("Burn time:", TextHelper.getTimeText(te.burnTimeLeft) + "/" + TextHelper.getTimeText(te.maxBurnTimeLeft))), mouseX - guiLeft, mouseY - guiTop);

//        renderHoveredToolTip(mouseX, mouseY);

        Slot slot = getSlotUnderMouse();
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            if (!stack.isEmpty()) {
                List<String> tooltip = this.getItemToolTip(stack);
                int max = stack.getMaxStackSize();
                int burnTime = CustomSlot.getBurnTime(stack);
                if (burnTime != 0) {
                    tooltip.add("Burn time / 1: " + TextHelper.getTimeText(burnTime));
                    int maxBurn = burnTime * max;
                    tooltip.add(String.format("Burn time / %s: ", Integer.toString(max)) + TextHelper.getTimeText(maxBurn));
                    int perItem = te.getGeneration() * burnTime;
                    tooltip.add("FE per item: " + TextHelper.getEnergyText(perItem) + " FE");
                    int total = perItem * stack.getCount();
                    tooltip.add("FE per this stack: " + TextHelper.getEnergyText(total) + " FE");
                }
                FontRenderer font = stack.getItem().getFontRenderer(stack);
                net.minecraftforge.fml.client.config.GuiUtils.preItemToolTip(stack);
                this.drawHoveringText(tooltip, mouseX - guiLeft, mouseY - guiTop, (font == null ? fontRenderer : font));
                net.minecraftforge.fml.client.config.GuiUtils.postItemToolTip();
            }
        }


    }
}
