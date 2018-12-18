package com.vanhal.progressiveautomation.gui.client;

import org.lwjgl.opengl.GL11;

import com.vanhal.progressiveautomation.events.EventPlayers;
import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.util.StringHelper;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

public class BaseGUI extends GuiContainer {
	public static int BLACK = 0x000000;
	public static int GRAY = 0x404040;
	public static int WHITE = 0xFFFFFF;
	public static int GREEN = 0x5CA028;
	public static int RED = 0xCC3333;
	public static int BLUE = 0x4C8BFF;
	public static int ORANGE = 0xFF9900;
	
	protected int guiHeight = 166;
	protected int guiWidth = 176;
	
	public static final FontRenderer fontRenderer = FMLClientHandler.instance().getClient().fontRenderer;
	public static final ResourceLocation flame = new ResourceLocation(Ref.MODID, "textures/gui/flame.png");
	
	protected ResourceLocation background;

	public BaseGUI(Container container, ResourceLocation texture) {
		super(container);
		background = texture;
	}
	
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		drawText();
		drawString("Inventory", 8, guiHeight - 92, GRAY);
	}
	
	public void setHeight(int height) {
		guiHeight = height;
	}
	
	public void setWidth(int width) {
		guiWidth = width;
	}
	
	protected void drawText() {	}
	
	protected void drawElements() {}

	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		mc.renderEngine.bindTexture(background);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, guiWidth, guiHeight);
		drawElements();
	}
	
	protected void drawString(String text, int y, int colour) {
		drawString(text, ((xSize - fontRenderer.getStringWidth(text)) / 2) , y, colour);
	}
	
	protected void drawString(String text, int x, int y, int colour) {
		fontRenderer.drawString(text, x, y, colour);
	}
	
	protected void drawString(String text, int x, int w, int y, int colour) {
		drawString(text, x + ((w - fontRenderer.getStringWidth(text)) / 2) , y, colour);
	}
	
	public void drawFlame(float progress, int x, int y) {
		int level = (int)Math.ceil(16*progress);
		drawTexturedModalRect(guiLeft + x, guiTop + y + 16 - level, 240, 240 + (16-level), 16, level);
	}

	public String getTextLine(int line, String text) {
		return EventPlayers.getPlayerLine(mc.player.getDisplayNameString(), line, StringHelper.localize(text));
	}
}
