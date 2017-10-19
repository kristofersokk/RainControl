package com.timotheteus.raincontrol.util;

import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class CustomItemStackHandler extends ItemStackHandler {

    public CustomItemStackHandler() {
    }

    public CustomItemStackHandler(int size) {
        super(size);
    }

    public CustomItemStackHandler(NonNullList<ItemStack> stacks) {
        super(stacks);
    }

    public void dropInventory(World world, BlockPos pos) {
        for (ItemStack stack : super.stacks) {
            InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        }
    }

    /**
     * @param slot
     * @param modify
     * @return stack changed
     */
    public boolean modifyStack(int slot, int modify) {
        ItemStack stack = null;
        try {
            stack = getStackInSlot(slot).copy();
        } catch (Exception e) {
            return false;
        }
        if (!stack.isEmpty()) {
            int amount = stack.getCount();
            int newAmount = amount + modify;
            if (newAmount > stack.getMaxStackSize())
                newAmount = stack.getMaxStackSize();
            if (newAmount < 1) {
                setStackInSlot(slot, new ItemStack((Item) null));
            } else {
                stack.setCount(newAmount);
                setStackInSlot(slot, stack);
            }
            return true;
        }
        return false;
    }
}
