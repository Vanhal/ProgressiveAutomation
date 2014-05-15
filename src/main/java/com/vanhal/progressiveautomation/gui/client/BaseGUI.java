package com.vanhal.progressiveautomation.gui.client;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class BaseGUI extends GuiContainer {
	protected ResourceLocation background;

	public BaseGUI(Container container, ResourceLocation texture) {
		super(container);
		background = texture;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, 176, 166);
		
	}

}
