package com.timotheteus.raincontrol.items.itemblocks;

import com.timotheteus.raincontrol.block.ItemBlockBase;
import com.timotheteus.raincontrol.tileentities.TileEntityRainBlock;
import com.timotheteus.raincontrol.util.TextHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.jline.utils.Log;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemRainBlock extends ItemBlockBase {

    public ItemRainBlock(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flagIn) {
        if (hasNbtTag(stack, "energy")) {
            int energy = stack.getTag().getInt("energy");
            tooltip.add(new TextComponentString("Energy: " + TextHelper.getEnergyText(energy) + " FE"));
        } else {
            tooltip.add(new TextComponentString("Energy: 0 FE"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    protected boolean placeBlock(BlockItemUseContext context, IBlockState state) {
        super.placeBlock(context, state);

        ItemStack stack = context.getItem();
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        if (hasNbtTag(stack,"energy")){
            try{
                int energy = stack.getTag().getInt("energy");
                TileEntityRainBlock te = (TileEntityRainBlock) world.getTileEntity(pos);
                if (te != null) {
                    te.setEnergy(energy, true);
                }
            }catch (Exception e){
                Log.error("Unable to set energy of RainBlock upon placing down");
            }
        }

        return true;
    }

    private static boolean hasNbtTag(ItemStack stack, String pNbtTag) {
        return stack.hasTag() && stack.getTag().hasUniqueId(pNbtTag);
    }
}
