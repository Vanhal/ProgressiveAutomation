package com.vanhal.progressiveautomation.client.events;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.api.PAItems;
import com.vanhal.progressiveautomation.common.entities.UpgradeableTileEntity;
import com.vanhal.progressiveautomation.common.util.Point3I;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent.Unload;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Some of this code is partly based on code written by skyboy for minefactoryreloaded
 * That code, and the code based of it retains its original copyright and licence.
 */
@SideOnly(Side.CLIENT)
public class EventRenderWorld {

    public static List<UpgradeableTileEntity> machines = new ArrayList<>();
    private static List<UpgradeableTileEntity> toRemove = new ArrayList<>();

    public static boolean containsMachine(UpgradeableTileEntity machine) {
        try {
            if (machines.contains(machine)) {
                return true;
            }

            List<UpgradeableTileEntity> tempList = new ArrayList<UpgradeableTileEntity>(machines);
            for (UpgradeableTileEntity testMachine : tempList) {
                if ((testMachine.getX() == machine.getX()) && (testMachine.getY() == machine.getY()) && (testMachine.getZ() == machine.getZ())) {
                    tempList = null;
                    return true;
                }
            }

            tempList = null;
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private static void removeMachines() {
        try {
            if (toRemove.size() > 0) {
                for (UpgradeableTileEntity remove : toRemove) {
                    if (containsMachine(remove)) {
                        machines.remove(remove);
                    }
                }
                toRemove.clear();
            }
        } catch (Exception e) {
        }
    }

    public static void addMachine(UpgradeableTileEntity machine) {
        if (!containsMachine(machine)) {
            machines.add(machine);
        }
    }

    public static void removeMachine(UpgradeableTileEntity machine) {
        if (containsMachine(machine)) {
            toRemove.add(machine);
        }
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(Unload world) {
        if (world.getWorld().provider == null ||
                Minecraft.getMinecraft().player == null ||
                Minecraft.getMinecraft().player.world == null ||
                Minecraft.getMinecraft().player.world.provider == null) {
            return;
        }
        if (world.getWorld().provider.getDimension() == Minecraft.getMinecraft().player.world.provider.getDimension()) {
            machines.clear();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void renderWorldLast(RenderWorldLastEvent e) {
        if (ProgressiveAutomation.proxy.isServer()) {
            return;
        }

        removeMachines();
        EntityPlayer player = Minecraft.getMinecraft().player;
        boolean holdingWrench = false;
        if (player.inventory.getCurrentItem().isEmpty()) {
            return;
        }

        if (player.inventory.getCurrentItem().getItem().equals(PAItems.WRENCH)) {
            holdingWrench = true;
        } else {
            return;
        }

        float playerOffsetX = -(float) (player.lastTickPosX + (player.posX - player.lastTickPosX) * e.getPartialTicks());
        float playerOffsetY = -(float) (player.lastTickPosY + (player.posY - player.lastTickPosY) * e.getPartialTicks());
        float playerOffsetZ = -(float) (player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * e.getPartialTicks());

        GL11.glPushMatrix();
        GL11.glColorMask(true, true, true, true);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_FLAT);

        for (UpgradeableTileEntity machine : machines) {
            if (machine == null) continue;
            if ((machine).isInvalid()) continue;
            if (machine.getWorldObj() == null) continue;
            if (!machine.getWorldObj().isRemote) continue;
            if (!holdingWrench && !machine.displayRange()) continue;

            float r = randomColour(machine.getX());
            float g = randomColour(machine.getY());
            float b = randomColour(machine.getZ());

            GL11.glPushMatrix();
            GL11.glColor4f(r, g, b, 0.4F);
            GL11.glTranslatef(playerOffsetX, playerOffsetY, playerOffsetZ);
            Point3I block = machine.getRangeBlock();
            renderBlock(block.getX(), block.getY(), block.getZ());
            GL11.glPopMatrix();
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }

    private int[] colors = {15, 26, 46, 52, 76, 89, 113, 156, 188, 204, 219, 231, 241};

    private float randomColour(int c) {
        c = Math.abs(c);
        int remainder = (int) Math.floor(c % colors.length);
        return (float) colors[remainder] / (float) 255;
    }

    public static void renderBlock(int x, int y, int z) {
        double shrink = -0.005;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder wr = tessellator.getBuffer();

        //Need to fix.
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

        wr.pos(x + shrink, y + 1 - shrink, z + shrink).endVertex();
        wr.pos(x + 1 - shrink, y + 1 - shrink, z + shrink).endVertex();
        wr.pos(x + 1 - shrink, y + shrink, z + shrink).endVertex();
        wr.pos(x + shrink, y + shrink, z + shrink).endVertex();

        wr.pos(x + shrink, y + shrink, z + 1 - shrink).endVertex();
        wr.pos(x + 1 - shrink, y + shrink, z + 1 - shrink).endVertex();
        wr.pos(x + 1 - shrink, y + 1 - shrink, z + 1 - shrink).endVertex();
        wr.pos(x + shrink, y + 1 - shrink, z + 1 - shrink).endVertex();

        wr.pos(x + shrink, y + shrink, z + shrink).endVertex();
        wr.pos(x + 1 - shrink, y + shrink, z + shrink).endVertex();
        wr.pos(x + 1 - shrink, y + shrink, z + 1 - shrink).endVertex();
        wr.pos(x + shrink, y + shrink, z + 1 - shrink).endVertex();

        wr.pos(x + shrink, y + 1 - shrink, z + 1 - shrink).endVertex();
        wr.pos(x + 1 - shrink, y + 1 - shrink, z + 1 - shrink).endVertex();
        wr.pos(x + 1 - shrink, y + 1 - shrink, z + shrink).endVertex();
        wr.pos(x + shrink, y + 1 - shrink, z + shrink).endVertex();

        wr.pos(x + shrink, y + shrink, z + 1 - shrink).endVertex();
        wr.pos(x + shrink, y + 1 - shrink, z + 1 - shrink).endVertex();
        wr.pos(x + shrink, y + 1 - shrink, z + shrink).endVertex();
        wr.pos(x + shrink, y + shrink, z + shrink).endVertex();

        wr.pos(x + 1 - shrink, y + shrink, z + shrink).endVertex();
        wr.pos(x + 1 - shrink, y + 1 - shrink, z + shrink).endVertex();
        wr.pos(x + 1 - shrink, y + 1 - shrink, z + 1 - shrink).endVertex();
        wr.pos(x + 1 - shrink, y + shrink, z + 1 - shrink).endVertex();

        tessellator.draw();
    }
}