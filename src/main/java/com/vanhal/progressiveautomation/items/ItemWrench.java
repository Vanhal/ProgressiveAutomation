package com.vanhal.progressiveautomation.items;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cofh.api.block.IDismantleable;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.ref.WrenchModes;
import com.vanhal.progressiveautomation.util.BlockHelper;
import com.vanhal.progressiveautomation.blocks.BaseBlock;
import com.vanhal.progressiveautomation.entities.BaseTileEntity;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemWrench extends BaseItem {
	public ItemWrench() {
		super("Wrench");
		setTextureName(Ref.MODID+":Wrench");
		this.setMaxStackSize(1);
		
	}
	
	private void setMode(ItemStack itemStack, WrenchModes.Mode mode) {
		this.setDamage(itemStack, mode.ordinal());
	}
	
	private WrenchModes.Mode getMode(ItemStack itemStack) {
		if (WrenchModes.modes.size()>itemStack.getItemDamage()) {
			return WrenchModes.modes.get(itemStack.getItemDamage());
		}
		return WrenchModes.Mode.Rotate;
	}
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean bool) {
		list.add(EnumChatFormatting.GRAY + "Current Mode: "+EnumChatFormatting.WHITE+getMode(itemStack));
	}
	
	
	@Override
	public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		Block block = world.getBlock(x, y, z);
		if (player.isSneaking()) {
			if (block instanceof IDismantleable) {
				IDismantleable dBlock = (IDismantleable)block;
				if (dBlock.canDismantle(player, world, x, y, z)) {
					if (!world.isRemote) dBlock.dismantleBlock(player, world, x, y, z, false);
				}
			}
		} else {
			if (getMode(itemStack)==WrenchModes.Mode.Rotate) {
				ForgeDirection face = ForgeDirection.getOrientation(side);
				block.rotateBlock(world, x, y, z, face);
			} else {
				if (block instanceof BaseBlock) {
					BaseTileEntity PABlock = (BaseTileEntity)world.getTileEntity(x, y, z);
					if (getMode(itemStack)==WrenchModes.Mode.Query) {
						if (world.isRemote) player.addChatMessage(new ChatComponentText(ForgeDirection.getOrientation(side)+" side currently set to: "+PABlock.getSide(ForgeDirection.getOrientation(side))));
					} else {
						PABlock.setSide(ForgeDirection.getOrientation(side), getMode(itemStack));
						if (world.isRemote) player.addChatMessage(new ChatComponentText(ForgeDirection.getOrientation(side)+" side set to: "+getMode(itemStack)));
					}
				}
			}
		}
        return !world.isRemote;
    }
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		if (player.isSneaking()) {
			int temp = getMode(itemStack).ordinal() + 1;
			if (temp>=WrenchModes.modes.size()) temp = 0;
			((ItemWrench)itemStack.getItem()).setMode(itemStack, WrenchModes.modes.get(temp));
			if (world.isRemote) player.addChatMessage(new ChatComponentText("Mode: "+WrenchModes.modes.get(temp)));
		}
        return itemStack;
    }
	
	public void dumpItems(World world, int x, int y, int z, ItemStack items) {
		EntityItem entItem = new EntityItem(world, (float)x + 0.5f, (float)y + 0.5f, (float)z + 0.5f, items);
		float f3 = 0.05F;
		entItem.motionX = (double)((float)world.rand.nextGaussian() * f3);
		entItem.motionY = (double)((float)world.rand.nextGaussian() * f3 + 0.2F);
		entItem.motionZ = (double)((float)world.rand.nextGaussian() * f3);
		
		world.spawnEntityInWorld(entItem);
	}
	
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"s s", " i ", " s ", 'i', Items.iron_ingot, 's', Items.stick});
		GameRegistry.addRecipe(recipe);
	}
	
	protected void addUpgradeRecipe() {
		addNormalRecipe();
	}
}
