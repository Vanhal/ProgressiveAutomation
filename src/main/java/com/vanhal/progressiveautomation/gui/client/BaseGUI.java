package com.vanhal.progressiveautomation.gui.client;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class BaseGUI extends GuiContainer {
	public static int BLACK = 0x000000;
	public static int GRAY = 0x404040;
	public static int WHITE = 0xFFFFFF;
	public static int GREEN = 0x5CA028;
	public static int RED = 0xCC3333;
	public static int BLUE = 0x4C8BFF;
	
	
	public static final FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRenderer;
	
	protected ResourceLocation background;

	public BaseGUI(Container container, ResourceLocation texture) {
		super(container);
		background = texture;
	}
	
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		drawText();
		drawString("Inventory", 8, 74, GRAY);
	}
	
	protected void drawText() {	}

	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, 176, 166);
		
	}
	
	protected void drawString(String text, int y, int colour) {
		drawString(text, ((xSize - fontRendererObj.getStringWidth(text)) / 2) , y, colour);
	}

	
	protected void drawString(String text, int x, int y, int colour) {
		fontRendererObj.drawString(text, x, y, colour);
	}
	
	

}
