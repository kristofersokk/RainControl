package com.timotheteus.raincontrol.gui;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class CustomSlot extends SlotItemHandler {

    StackFilter filter;

    public CustomSlot(IItemHandler handler, int index, int xPosition, int yPosition, @Nullable StackFilter filter) {
        super(handler, index, xPosition, yPosition);
        this.filter = filter;
    }

    public static boolean isValid(ItemStack stack, StackFilter filter) {
        switch (filter) {
            case GENERATOR:
                if (TileEntityFurnace.getItemBurnTime(stack) == 0)
                    return false;
        }
        return true;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return isValid(stack, filter);
    }

    public enum StackFilter {

        GENERATOR;

        StackFilter() {
        }
    }
}
