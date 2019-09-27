package com.timotheteus.raincontrol.tileentities;

import com.timotheteus.raincontrol.gui.CustomSlot;
import com.timotheteus.raincontrol.tileentities.modules.ModuleTypes;
import com.timotheteus.raincontrol.util.CustomItemStackHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class TileEntityInventoryBase extends TileEntityBase implements Inventory, IGUITile {

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

    public void dropInventory(World world, BlockPos pos, TileEntityBase te) {
        if (te instanceof Inventory) {
            ((Inventory) te).getSlotsHandler().dropInventory(world, pos);
        }
    }

    @Override
    public CustomItemStackHandler getSlotsHandler() {
        return itemStackHandler;
    }

    @Override
    public abstract void tick();

}
