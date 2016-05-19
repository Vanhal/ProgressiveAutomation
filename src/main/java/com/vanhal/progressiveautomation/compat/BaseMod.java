package com.vanhal.progressiveautomation.compat;

import java.util.List;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.util.Point3I;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;


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
	public boolean isPlant(Block plantBlock, IBlockState state) {
		return false;
	}

	//check to see if a plant is fully grown
	public boolean isGrown(Point3I plantPoint, Block plantBlock, IBlockState state, World worldObj) {
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
	public List<ItemStack> harvestPlant(Point3I plantPoint, Block plantBlock, IBlockState state, World worldObj) {
		List<ItemStack> items = plantBlock.getDrops(worldObj, plantPoint.toPosition(), state, 0);
		worldObj.removeTileEntity( plantPoint.toPosition() );
		worldObj.setBlockToAir(plantPoint.toPosition());
		
		return items;
	}
}
