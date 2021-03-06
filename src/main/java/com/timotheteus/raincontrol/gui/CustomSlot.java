package com.timotheteus.raincontrol.gui;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CustomSlot extends SlotItemHandler {

    private final StackFilter filter;

    public CustomSlot(IItemHandler handler, int index, int xPosition, int yPosition, @Nullable StackFilter filter) {
        super(handler, index, xPosition, yPosition);
        this.filter = filter;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return filter != null && filter.isValid(stack);
    }

    public static int getBurnTime(ItemStack stack) {
        return TileEntityFurnace.getBurnTimes().getOrDefault(stack.getItem(), 0) * stack.getCount();
    }

    public enum StackFilter {

        GENERATOR;

        StackFilter() {
        }

        public boolean isValid(ItemStack stack) {
            switch (this) {
                case GENERATOR:
                    if (getBurnTime(stack) == 0)
                        return false;
            }
            return true;
        }
    }


}
