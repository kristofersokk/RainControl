package com.timotheteus.raincontrol.config;

import com.timotheteus.raincontrol.util.ModUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.List;
import java.util.stream.Collectors;

public class GuiConfig extends net.minecraftforge.fml.client.config.GuiConfig {

    public GuiConfig(GuiScreen parentScreen) {
        super(parentScreen, getConfigElements(), ModUtil.MOD_ID, false, false, I18n.format("raincontrol.config.title"));
    }

    private static List<IConfigElement> getConfigElements() {
        return ModConfig.config.getCategoryNames().stream()
                .map(categoryName -> new ConfigElement(ModConfig.config.getCategory(categoryName).setLanguageKey("raincontrol.config." + categoryName)))
                .collect(Collectors.toList());
    }
}
