package com.vanhal.progressiveautomation.client;

import com.vanhal.progressiveautomation.client.gui.PAGuiConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.util.Set;

public class PAGuiFactory implements IModGuiFactory {

    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public void initialize(Minecraft minecraftInstance) {
    }

    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new PAGuiConfig(parentScreen);
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }
}