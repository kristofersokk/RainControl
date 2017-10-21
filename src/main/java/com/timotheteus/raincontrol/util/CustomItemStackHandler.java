package com.timotheteus.raincontrol.util;

import com.timotheteus.raincontrol.gui.CustomSlot;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class CustomItemStackHandler extends ItemStackHandler {

    private CustomSlot.StackFilter filter;

    protected CustomItemStackHandler(int size, CustomSlot.StackFilter filter) {
        super(size);
        this.filter = filter;
    }

    public void dropInventory(World world, BlockPos pos) {
        for (ItemStack stack : super.stacks) {
            InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        }
    }

    /**
     * @return stack changed
     */
    public boolean modifyStack(int slot, int modify) {
        ItemStack stack;
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
                setStackInSlot(slot, ItemStack.EMPTY.copy());
            } else {
                stack.setCount(newAmount);
                setStackInSlot(slot, stack);
            }
            return true;
        }
        return false;
    }

    @Override
    public void validateSlotIndex(int slot) {
        super.validateSlotIndex(slot);
    }

    public NonNullList<ItemStack> getAllSlots() {
        return stacks;
    }

    @Override
    public void setSize(int size) {
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        stacks.set(slot, stack.copy());
    }

    @Override
    public int getSlots() {
        return stacks.size();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return stacks.get(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        ItemStack local = getStackInSlot(slot);
        if (filter.isValid(stack) && (local.isEmpty() || ItemStack.areItemsEqual(local, stack))) {
            int stacklimit = getStackLimit(slot, stack);
            if (local.getCount() == stacklimit) {
                return stack;
            } else if (stack.getCount() + local.getCount() <= stacklimit) {
                if (!simulate) {
                    ItemStack newStack = stack.copy();
                    newStack.setCount(stack.getCount() + local.getCount());
                    setStackInSlot(slot, newStack);
                }
                return ItemStack.EMPTY;
            } else {//there's a remainder
                ItemStack newStack = local.copy();
                int remainder = local.getMaxStackSize() - local.getCount();
                if (!simulate)
                    local.setCount(local.getMaxStackSize());
                newStack.setCount(remainder);
                return newStack;
            }
        }
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack local = getStackInSlot(slot);
        ItemStack newStack = local.copy();
        ItemStack newLocal = local.copy();
        if (local.getCount() == 0 || amount == 0) {
            return ItemStack.EMPTY;
        } else if (amount >= local.getCount()) {
            newStack.setCount(local.getCount());
            if (!simulate)
                setStackInSlot(slot, ItemStack.EMPTY.copy());
            return newStack;
        } else {
            newStack.setCount(amount);
            if (!simulate)
                newLocal.setCount(local.getCount() - amount);
            setStackInSlot(slot, newLocal);
            return newStack;
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
    }
}
