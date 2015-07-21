package com.vanhal.progressiveautomation.events;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.UpgradeableTileEntity;
import com.vanhal.progressiveautomation.items.PAItems;
import com.vanhal.progressiveautomation.util.Point3I;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent.Unload;

/**
 * Some of this code is partly based on code written by skyboy for minefactoryreloaded
 * That code, and the code based of it retains its original copyright and licence.
 */

public class EventRenderWorld {
	
	public static List<UpgradeableTileEntity> machines = new ArrayList<UpgradeableTileEntity>();
	
	public static boolean containsMachine(UpgradeableTileEntity machine) {
		if (machines.contains(machine)) return true;
		for (UpgradeableTileEntity testMachine : machines) {
			if ( (testMachine.xCoord == machine.xCoord) && (testMachine.yCoord == machine.yCoord) && (testMachine.zCoord == machine.zCoord) ) {
				return true;
			}
		}
		return false;
	}

	public static void addMachine(UpgradeableTileEntity machine) {
		if (!containsMachine(machine)) {
			machines.add(machine);
		}
	}
	
	public static void removeMachine(UpgradeableTileEntity machine) {
		if (containsMachine(machine)) {
			machines.remove(machine);
		}
	}
	
	@SubscribeEvent
	public void onPlayerChangedDimension(Unload world) {
		if (world.world.provider == null ||
				Minecraft.getMinecraft().thePlayer == null ||
				Minecraft.getMinecraft().thePlayer.worldObj == null ||
				Minecraft.getMinecraft().thePlayer.worldObj.provider == null) {
			return;
		}
		if (world.world.provider.dimensionId == Minecraft.getMinecraft().thePlayer.worldObj.provider.dimensionId) {
			machines.clear();
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void renderWorldLast(RenderWorldLastEvent e) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		boolean holdingWrench = false;
		if(player.inventory.getCurrentItem() == null) return;
		if (ProgressiveAutomation.proxy.isServer()) return;

		if (player.inventory.getCurrentItem().getItem().equals(PAItems.wrench)) holdingWrench = true;
		
		float playerOffsetX = -(float)(player.lastTickPosX + (player.posX - player.lastTickPosX) * e.partialTicks);
		float playerOffsetY = -(float)(player.lastTickPosY + (player.posY - player.lastTickPosY) * e.partialTicks);
		float playerOffsetZ = -(float)(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * e.partialTicks);

		GL11.glColorMask(true, true, true, true);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_FLAT);

		for (UpgradeableTileEntity machine : machines) {
			if (((TileEntity)machine).isInvalid()) continue;
			if (!machine.getWorldObj().isRemote) continue;
			if (!holdingWrench && !machine.displayRange()) continue;

			float r = randomColour(machine.xCoord);
			float g = randomColour(machine.yCoord);
			float b = randomColour(machine.zCoord);

			GL11.glPushMatrix();
			GL11.glColor4f(r, g, b, 0.4F);
			GL11.glTranslatef(playerOffsetX, playerOffsetY, playerOffsetZ);
			Point3I block = machine.getRangeBlock();
			renderBlock(block.getX(), block.getY(), block.getZ());
			GL11.glPopMatrix();
		}

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		
	}

	private int[] colors = {15, 26, 46, 52, 76, 89, 113, 156, 188, 204, 219, 231, 241};
	
	private float randomColour(int c) {
		int remainder = (int) Math.floor(c % colors.length);
		return (float)colors[remainder]/(float)255;
	}
	
	public static void renderBlock(int x, int y, int z) {
		double shrink = -0.005;

		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();

		tessellator.addVertex(x + shrink, y + 1 - shrink, z + shrink);
		tessellator.addVertex(x + 1 - shrink, y + 1 - shrink, z + shrink);
		tessellator.addVertex(x + 1 - shrink, y + shrink, z + shrink);
		tessellator.addVertex(x + shrink, y + shrink, z + shrink);

		tessellator.addVertex(x + shrink, y + shrink, z + 1 - shrink);
		tessellator.addVertex(x + 1 - shrink, y + shrink, z + 1 - shrink);
		tessellator.addVertex(x + 1 - shrink, y + 1 - shrink, z + 1 - shrink);
		tessellator.addVertex(x + shrink, y + 1 - shrink, z + 1 - shrink);

		tessellator.addVertex(x + shrink, y + shrink, z + shrink);
		tessellator.addVertex(x + 1 - shrink, y + shrink, z + shrink);
		tessellator.addVertex(x + 1 - shrink, y + shrink, z + 1 - shrink);
		tessellator.addVertex(x + shrink, y + shrink, z + 1 - shrink);

		tessellator.addVertex(x + shrink, y + 1 - shrink, z + 1 - shrink);
		tessellator.addVertex(x + 1 - shrink, y + 1 - shrink, z + 1 - shrink);
		tessellator.addVertex(x + 1 - shrink, y + 1 - shrink, z + shrink);
		tessellator.addVertex(x + shrink, y + 1 - shrink, z + shrink);

		tessellator.addVertex(x + shrink, y + shrink, z + 1 - shrink);
		tessellator.addVertex(x + shrink, y + 1 - shrink, z + 1 - shrink);
		tessellator.addVertex(x + shrink, y + 1 - shrink, z + shrink);
		tessellator.addVertex(x + shrink, y + shrink, z + shrink);

		tessellator.addVertex(x + 1 - shrink, y + shrink, z + shrink);
		tessellator.addVertex(x + 1 - shrink, y + 1 - shrink, z + shrink);
		tessellator.addVertex(x + 1 - shrink, y + 1 - shrink, z + 1 - shrink);
		tessellator.addVertex(x + 1 - shrink, y + shrink, z + 1 - shrink);
		tessellator.draw();
	}
}
