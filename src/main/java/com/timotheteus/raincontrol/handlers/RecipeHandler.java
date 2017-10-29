package com.timotheteus.raincontrol.handlers;

import com.timotheteus.raincontrol.block.ModBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RecipeHandler {

    private static ItemStack lapis = new ItemStack(Items.DYE, 1, 4);

    public static void addRecipes(){
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockRain), "pep", "rir", "pep", 'i', Blocks.IRON_BLOCK, 'r', Items.REDSTONE, 'e', Items.ENDER_PEARL, 'p', Items.PRISMARINE_SHARD);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockGenerator), "scs", "rir", "scs", 'i', Blocks.IRON_BLOCK, 'r', Items.REDSTONE, 'c', Items.COAL, 's', Items.IRON_INGOT);
        GameRegistry.addRecipe(new ItemStack(ModBlocks.blockSensor), "ggg", "lrl", "iii", 'g', Blocks.GLASS_PANE, 'l', lapis.copy(), 'r', Items.REDSTONE, 'i', Items.IRON_INGOT);
    }
}
