package com.vanhal.progressiveautomation.core;

import com.vanhal.progressiveautomation.gui.client.PAGuiConfig;
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
        // TODO Auto-generated method stub
    }

    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new PAGuiConfig(parentScreen);
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        // TODO Auto-generated method stub
        return null;
    }

}
