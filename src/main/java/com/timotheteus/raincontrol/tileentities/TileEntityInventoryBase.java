package com.timotheteus.raincontrol.tileentities;

import com.timotheteus.raincontrol.gui.CustomSlot;
import com.timotheteus.raincontrol.tileentities.modules.ModuleTypes;
import com.timotheteus.raincontrol.util.CustomItemStackHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileEntityInventoryBase extends TileEntityBase implements Inventory, IInventory, IGUITile {

    private static final int SIZE = 1;
    private static final ItemStack EMPTY = ItemStack.EMPTY;
    @Nonnull final CustomItemStackHandler itemStackHandler;
    private final CustomSlot.StackFilter filter;

    TileEntityInventoryBase(TileEntityType type, ModuleTypes[] moduleTypes, Object[][] objects, @Nullable CustomSlot.StackFilter filter) {
        super(type, moduleTypes, objects);
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
    @Nonnull
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
    @Nonnull
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = getStackInSlot(index).copy();
        if (stack.getCount() > count) {
            stack.setCount(stack.getCount() - count);
            return stack;
        }
        return EMPTY.copy();
    }

    @Override
    @Nonnull
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
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
        return this.world.getTileEntity(this.getPos()) == this && player.getDistanceSq(this.pos.add(0.5, 0.5, 0.5)) <= 64;
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {
    }

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
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
    public abstract int getHeight();

    @Override
    public abstract int getWidth();

    @Override
    public abstract ITextComponent getName();

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return getName();
    }

    @Nullable
    @Override
    public ITextComponent getCustomName() {
        return new TextComponentString("");
    }

    @Override
    public abstract void tick();

}
