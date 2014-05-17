package com.vanhal.progressiveautomation.gui.client;

import org.lwjgl.opengl.GL11;

import com.vanhal.progressiveautomation.ref.Ref;

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
	public static final ResourceLocation flame = new ResourceLocation(Ref.MODID, "textures/gui/flame.png");
	
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
	
	protected void drawElements() {}

	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, 176, 166);
		drawElements();
	}
	
	protected void drawString(String text, int y, int colour) {
		drawString(text, ((xSize - fontRendererObj.getStringWidth(text)) / 2) , y, colour);
	}

	
	protected void drawString(String text, int x, int y, int colour) {
		fontRendererObj.drawString(text, x, y, colour);
	}
	
	protected void drawString(String text, int x, int w, int y, int colour) {
		drawString(text, x + ((w - fontRendererObj.getStringWidth(text)) / 2) , y, colour);
	}
	
	public void drawFlame(float progress, int x, int y) {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(flame);
		int level = (int)Math.floor(16*progress);
		drawTexturedModalRect(guiLeft + x, guiTop + y + 16 - level, 16, 16 - level, 16, level);
	}

}
