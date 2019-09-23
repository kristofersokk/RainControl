package com.timotheteus.raincontrol.gui.container;

import com.timotheteus.raincontrol.gui.CustomSlot;
import com.timotheteus.raincontrol.tileentities.TileEntityGeneratorBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ContainerGenerator extends Container {

    private final TileEntityGeneratorBlock te;
    private final int OWN_SIZE = 1;
    private final int inventoryStart = OWN_SIZE;
    private final int inventoryEnd = inventoryStart + 26;
    private final int hotbarStart = inventoryEnd + 1;
    private final int hotbarEnd = hotbarStart + 8;

    public ContainerGenerator(IInventory playerInventory, TileEntityGeneratorBlock te) {
        this.te = te;

        addOwnSlots();
        addPlayerSlots(playerInventory);
    }


    private void addPlayerSlots(IInventory playerInventory) {
        // Slots for the main inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 10 + col * 18;
                int y = row * 18 + 59;
                this.addSlot(new Slot(playerInventory, col + row * 9 + 10, x, y));
            }
        }

        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = 10 + row * 18;
            int y = 58 + 59;
            this.addSlot(new Slot(playerInventory, row, x, y));
        }
    }

    private static final CustomSlot.StackFilter filter = CustomSlot.StackFilter.GENERATOR;

    private void addOwnSlots() {
        if (this.te != null) {
            IItemHandler itemHandler = this.te.getSlotsHandler();
            int x = 70;
            int y = 21;

            // Add our own slots
            int slotIndex = 0;
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                addSlot(new CustomSlot(itemHandler, slotIndex, x, y, filter));
                slotIndex++;
                x += 18;
            }
        }

    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {

        Slot theSlot = this.inventorySlots.get(index);

        if (theSlot != null && theSlot.getHasStack()) {
            ItemStack newStack = theSlot.getStack();
            ItemStack currentStack = newStack.copy();

            //Other Slots in Inventory excluded
            if (index >= inventoryStart) {
                //Shift from Inventory
                if (CustomSlot.getBurnTime(newStack) > 0) {
                    if (!this.mergeItemStack(newStack, 0, 1, false)) {
                        return ItemStack.EMPTY.copy();
                    }
                }
                //

                else if (index <= inventoryEnd) {
                    if (!this.mergeItemStack(newStack, hotbarStart, hotbarEnd + 1, false)) {
                        return ItemStack.EMPTY.copy();
                    }
                } else if (index < hotbarEnd + 1 && !this.mergeItemStack(newStack, inventoryStart, inventoryEnd + 1, false)) {
                    return ItemStack.EMPTY.copy();
                }
            } else if (!this.mergeItemStack(newStack, inventoryStart, hotbarEnd + 1, false)) {
                return ItemStack.EMPTY.copy();
            }

            if (!filter.isValid(newStack)) {
                theSlot.putStack(ItemStack.EMPTY.copy());
            } else {
                theSlot.onSlotChanged();
            }

            if (newStack.getCount() == currentStack.getCount()) {
                return ItemStack.EMPTY.copy();
            }
            theSlot.onTake(playerIn, newStack);

            return currentStack;
        }
        return ItemStack.EMPTY.copy();
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return te.canInteractWith(playerIn);
    }
}
