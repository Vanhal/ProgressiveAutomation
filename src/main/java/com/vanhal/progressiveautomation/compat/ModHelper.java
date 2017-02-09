package com.vanhal.progressiveautomation.compat;

import java.util.ArrayList;
import java.util.List;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.compat.mods.ImmersiveEngineering;
import com.vanhal.progressiveautomation.compat.mods.MFR;
import com.vanhal.progressiveautomation.compat.mods.Pams;
import com.vanhal.progressiveautomation.compat.mods.RightClick;
import com.vanhal.progressiveautomation.compat.mods.TiCon;
import com.vanhal.progressiveautomation.compat.mods.Vanilla;
import com.vanhal.progressiveautomation.util.Point3I;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ModHelper {

	//mod registry
	private static ArrayList<BaseMod> modsLoaded = new ArrayList<BaseMod>();
	
	private static void registerMods() {
		registerMod(new ImmersiveEngineering());
		registerMod(new Pams());
		//registerMod(new Pneumaticcraft());
		registerMod(new MFR());
		registerMod(new TiCon());
		//registerMod(new AgriCraft());
		//registerMod(new GrowOres());
		//registerMod(new ThaumCraft());

		
		//sudo "mod" to attempt to right click on plants before trying to break them
		registerMod(new RightClick());
		
		//vanilla should always be registered last since it's the main class
		//at the same time it probably should be loaded first....
		registerMod(new Vanilla());
	}
	
	public static void registerMod(BaseMod mod) {
		if (mod.shouldLoad()) {
			modsLoaded.add(mod);
		}
	}
	
	//initialisation function
	public static void init() {
		registerMods();
	}
	
	//checker functions, the main stuff
	//check if item is plantible in the chopper
	public static boolean checkSapling(ItemStack item) {
		for (BaseMod mod: modsLoaded) {
			if (mod.isSapling(item)) return true;
		}
		return false;
	}
	
	//check is an item is plantible in the planter
	public static boolean isPlantible(ItemStack item) {
		for (BaseMod mod: modsLoaded) {
			if (mod.isPlantible(item)) return true;
		}
		return false;
	}
	
	//check if an Itemstack is a log	
	public static boolean isLog(ItemStack item) {
		for (BaseMod mod: modsLoaded) {
			if (mod.isLog(item)) return true;
		}
		return false;
	}

	//check if an Itemstack is a leaf	
	public static boolean isLeaf(ItemStack item) {
		for (BaseMod mod: modsLoaded) {
			if (mod.isLeaf(item)) return true;
		}
		return false;
	}
	
	//check is an item is plantible in the planter
	public static boolean shouldHoe(ItemStack item) {
		for (BaseMod mod: modsLoaded) {
			if (mod.isPlantible(item)) {
				return mod.shouldHoe(item);
			}
		}
		return false;
	}
	
	//check if a block in the world is an ungrown plant	
	public static boolean isPlant(Block plantBlock, IBlockState state) {
		for (BaseMod mod: modsLoaded) {
			if (mod.isPlant(plantBlock, state)) return true;
		}
		return false;
	}

	//check to see if a plant is fully grown
	public static boolean isGrown(Point3I plantPoint, Block plantBlock, IBlockState state, World worldObj) {
		for (BaseMod mod: modsLoaded) {
			if (mod.isPlant(plantBlock, state)) {
				if (mod.isGrown(plantPoint, plantBlock, state, worldObj)) return true;
			}
		}
		return false;
	}
	
	//check if the ground is valid for planting the given seed
	public static boolean validBlock(World worldObj, ItemStack itemStack, Point3I testPoint) {
		for (BaseMod mod: modsLoaded) {
			if (mod.validBlock(worldObj, itemStack, testPoint)) return true;
		}
		return false;
	}
	
	//plant seed, return false if seed was not planted
	public static boolean placeSeed(World worldObj, ItemStack itemStack, Point3I testPoint, boolean doAction) {
		for (BaseMod mod: modsLoaded) {
			if (mod.validBlock(worldObj, itemStack, testPoint)) {
				if (mod.placeSeed(worldObj, itemStack, testPoint, doAction)) {
					return true;
				}
			}
		}
		return false;
	}

	//harvest the crop block
	public static List<ItemStack> harvestPlant(Point3I plantPoint, Block plantBlock, IBlockState state, World worldObj) {
		List<ItemStack> items = null;
		for (BaseMod mod: modsLoaded) {
			if (mod.isPlant(plantBlock, state)) {
				if (mod.isGrown(plantPoint, plantBlock, state, worldObj)) {
					items = mod.harvestPlant(plantPoint, plantBlock, state, worldObj);
					if (items!=null) return items;
				}
			}
		}
		return null;
	}
}
