package com.timotheteus.raincontrol.util;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeyBindings {

    private static KeyBinding shift;

    public static void init(){
        shift = new KeyBinding("key.shift", Keyboard.KEY_LSHIFT, "key.categories." + ModUtil.MOD_ID);
        ClientRegistry.registerKeyBinding(shift);
    }
}
