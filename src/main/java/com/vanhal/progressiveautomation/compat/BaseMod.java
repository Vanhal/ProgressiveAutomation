package com.vanhal.progressiveautomation.compat;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.util.Point3I;

import cpw.mods.fml.common.Loader;

public class BaseMod {
	public String modID = "base";

	public boolean shouldLoad() {
		return checkModLoad();
	}
	
	protected boolean checkModLoad() {
		if (Loader.isModLoaded(modID)) {
			if (PAConfig.config.getBoolean(modID, "ModCompatibility", true, "Enable support for "+modID)) {
				ProgressiveAutomation.logger.info(modID+" Loaded");
				return true;
			} else {
				ProgressiveAutomation.logger.info(modID+" Found, but compatibility has been disabled in the configs");
				return false;
			}
		} else {
			ProgressiveAutomation.logger.info(modID+" not found, not loading");
			return false;
		}
	}
	
	public boolean isSapling(ItemStack item) {
		return false;
	}
	
	public boolean isLog(ItemStack item) {
		return false;
	}
	
	public boolean isLeaf(ItemStack item) {
		return false;
	}
	
	public boolean isPlantible(ItemStack item) {
		return false;
	}
	
	public boolean shouldHoe(ItemStack item) {
		return false;
	}

	//check if a block in the world is an ungrown plant	
	public boolean isPlant(Block plantBlock, int metadata) {
		return false;
	}

	//check to see if a plant is fully grown
	public boolean isGrown(Point3I plantPoint, Block plantBlock, int metadata, World worldObj) {
		return false;
	}

	//check if the ground is valid for planting the given seed
	public boolean validBlock(World worldObj, ItemStack itemStack, Point3I testPoint) {
		return false;
	}

	//actually place the seeds
	public boolean placeSeed(World worldObj, ItemStack itemStack, Point3I testPoint, boolean doAction) {
		return false;
	}
	
	//harvest the crop block and return the drops
	public ArrayList<ItemStack> harvestPlant(Point3I plantPoint, Block plantBlock, int metadata, World worldObj) {
		ArrayList<ItemStack> items = plantBlock.getDrops(worldObj, plantPoint.getX(), plantPoint.getY(), plantPoint.getZ(), metadata, 0);
		worldObj.removeTileEntity( plantPoint.getX(), plantPoint.getY(), plantPoint.getZ() );
		worldObj.setBlockToAir(plantPoint.getX(), plantPoint.getY(), plantPoint.getZ());
		
		return items;
	}
}
