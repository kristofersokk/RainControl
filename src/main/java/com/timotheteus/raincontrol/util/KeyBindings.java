package com.timotheteus.raincontrol.util;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {

    public static KeyBinding shift;

    public static void init(){
        shift = new KeyBinding("key.shift", GLFW.GLFW_KEY_LEFT_SHIFT, "key.categories." + ModUtil.MOD_ID);
        ClientRegistry.registerKeyBinding(shift);
    }
}
