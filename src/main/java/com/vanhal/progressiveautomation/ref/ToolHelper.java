package com.vanhal.progressiveautomation.ref;

import java.util.Random;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.items.PAItems;
import com.vanhal.progressiveautomation.util.PlayerFake;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ToolHelper {
	//tools
	public static int TYPE_PICKAXE = 0;
	public static int TYPE_SHOVEL = 1;
	public static int TYPE_AXE = 2;
	public static int TYPE_SWORD = 3;
	public static int TYPE_HOE = 4;

	//levels
	public static int LEVEL_WOOD = 0;
	public static int LEVEL_STONE = 1;
	public static int LEVEL_IRON = 2;
	public static int LEVEL_GOLD = 0;
	public static int LEVEL_DIAMOND =3;
	
	//random
	protected static Random RND = new Random();

	public static int getType(Item item) {
		if (item instanceof ItemPickaxe) {
			return TYPE_PICKAXE;
		} else if (item instanceof ItemAxe) {
			return TYPE_AXE;
		} else if (item instanceof ItemSpade) {
			return TYPE_SHOVEL;
		} else if (item instanceof ItemSword) {
			return TYPE_SWORD;
		} else if (item instanceof ItemHoe) {
			return TYPE_HOE;
		} else if (tinkersType(item)>=0) { //see if it's a tinkers type
			return tinkersType(item);
		} else {
			return -1;
		}
	}

	public static int getLevel(ItemStack itemStack) {
		if (itemStack.getItem() instanceof ItemTool) {
			//Vanilla Tools
			if (itemStack.getItem() instanceof ItemTool) {
				return ((ItemTool)itemStack.getItem()).func_150913_i().getHarvestLevel();
			} else {
				String material = "";
				if (itemStack.getItem() instanceof ItemSword) material = ((ItemSword)itemStack.getItem()).getToolMaterialName();
				else if (itemStack.getItem() instanceof ItemHoe) material = ((ItemHoe)itemStack.getItem()).getToolMaterialName();
				if (material.equals("WOOD")) return LEVEL_WOOD;
				else if (material.equals("STONE")) return LEVEL_STONE;
				else if (material.equals("IRON")) return LEVEL_IRON;
				else if (material.equals("GOLD")) return LEVEL_GOLD;
				else if (material.equals("EMERALD")) return LEVEL_DIAMOND;
			}
			
			
		} else {
			//Tinkers Tools
			if (itemStack.hasTagCompound()) {
				return tinkersLevel(itemStack);
			}
		}

		return -1;
	}

	//check for the tinkers tag
	public static int tinkersLevel(ItemStack item) {
		if (item.getTagCompound().hasKey("InfiTool")) {
			NBTTagCompound tags = item.getTagCompound().getCompoundTag("InfiTool");
			int toolLevel = tags.getInteger("HarvestLevel");
			return toolLevel;
		}
		return -1;
	}
	
	//Get the type of tinker tool
	public static int tinkersType(Item item) {
		String name = item.getUnlocalizedName();
		if (name.length()>=14) {
			if (name.substring(5, 13).equalsIgnoreCase("InfiTool")) {
				if (name.substring(14).equalsIgnoreCase("pickaxe")) {
					return TYPE_PICKAXE;
				} else if (name.substring(14).equalsIgnoreCase("axe")) {
					return TYPE_AXE;
				} else if (name.substring(14).equalsIgnoreCase("shovel")) {
					return TYPE_SHOVEL;
				} else if (name.substring(14).equalsIgnoreCase("hoe")) {
					return TYPE_HOE;
				} else if ( (name.substring(14).equalsIgnoreCase("Broadsword")) ||
						(name.substring(14).equalsIgnoreCase("Rapier")) ||
						(name.substring(14).equalsIgnoreCase("Cutlass")) ) {
					return TYPE_SWORD;
				} else {
					Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(name.substring(14)));
				}
			}
		}
		return -1;
	}

	public static ItemStack getUpgradeType(int level) {
		if (level>=ToolHelper.LEVEL_DIAMOND) {
			return new ItemStack(PAItems.diamondUpgrade);
		} else if (level==ToolHelper.LEVEL_IRON) {
			return new ItemStack(PAItems.ironUpgrade);
		} else if (level==ToolHelper.LEVEL_STONE) {
			return new ItemStack(PAItems.stoneUpgrade);
		} else {
			return new ItemStack(PAItems.woodUpgrade);
		}
	}

	public static int getHarvestLevel(ItemStack item) {
		int value = getLevel(item);
		return value;
	}
	
	public static boolean damageTool(ItemStack tool, World world, int x, int y, int z) {
		if (tool.getItem() instanceof ItemTool) {
			if (tool.attemptDamageItem(1, RND)) {
				return true;
			}
		} else {
			Block mineBlock = world.getBlock(x, y, z);
			PlayerFake fakePlayer = new PlayerFake((WorldServer)world);
			tool.getItem().onBlockDestroyed(tool, world, mineBlock, x, y, z, fakePlayer);
		}
		return false;
	}
}
