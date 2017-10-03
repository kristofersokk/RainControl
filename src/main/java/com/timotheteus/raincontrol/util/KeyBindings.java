package com.timotheteus.raincontrol.util;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyBindings {

    public static KeyBinding shift;

    public static void init(){
        shift = new KeyBinding("key.shift", Keyboard.KEY_LSHIFT, "key.categories." + ModUtil.MOD_ID);
        ClientRegistry.registerKeyBinding(shift);
    }
}
