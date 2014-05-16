package com.vanhal.progressiveautomation.gui.client;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class BaseGUI extends GuiContainer {
	public static int WHITE = 0x000000;
	
	public static final FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRenderer;
	
	protected ResourceLocation background;

	public BaseGUI(Container container, ResourceLocation texture) {
		super(container);
		background = texture;
	}
	
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		
	}
	
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, 176, 166);
		
	}

	
	protected void drawString(String text, int x, int y, int colour) {
		
	}
}
