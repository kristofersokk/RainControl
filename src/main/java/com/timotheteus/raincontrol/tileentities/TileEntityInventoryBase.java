package com.timotheteus.raincontrol.tileentities;

import com.timotheteus.raincontrol.gui.CustomSlot;
import com.timotheteus.raincontrol.tileentities.modules.ModuleTypes;
import com.timotheteus.raincontrol.util.CustomItemStackHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public class TileEntityInventoryBase extends TileEntityBase implements Inventory, IInventory {

    private static final int SIZE = 1;
    private static final ItemStack EMPTY = ItemStack.EMPTY;
    final CustomItemStackHandler itemStackHandler;
    private final CustomSlot.StackFilter filter;

    TileEntityInventoryBase(ModuleTypes[] moduleTypes, Object[][] objects, @Nullable CustomSlot.StackFilter filter) {
        super(moduleTypes, objects);
        itemStackHandler = new CustomItemStackHandler(SIZE, filter) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }
        };
        this.filter = filter;
    }

    @Override
    public int getSizeInventory() {
        return SIZE;
    }

    @Override
    public CustomItemStackHandler getSlotsHandler() {
        return itemStackHandler;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return itemStackHandler.getStackInSlot(index);
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : itemStackHandler.getAllSlots())
            if (!stack.isEmpty())
                return false;
        return true;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = getStackInSlot(index).copy();
        if (stack.getCount() > count) {
            stack.setCount(stack.getCount() - count);
            return stack;
        } else {
            return EMPTY.copy();
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        itemStackHandler.setStackInSlot(index, EMPTY.copy());
        return EMPTY.copy();
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index < 0 || index >= this.getSizeInventory())
            return;
        itemStackHandler.validateSlotIndex(index);

        if (stack.getCount() > this.getInventoryStackLimit())
            stack.setCount(this.getInventoryStackLimit());

        if (stack.getCount() == 0)
            stack = EMPTY;

        if (isItemValidForSlot(index, stack))
            itemStackHandler.setStackInSlot(index, stack.copy());
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.world.getTileEntity(this.getPos()) == this && player.getDistanceSq(this.pos.add(0.5, 0.5, 0.5)) <= 64;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return filter == null || filter.isValid(stack);
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.getSizeInventory(); i++)
            this.setInventorySlotContents(i, EMPTY.copy());
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

}
