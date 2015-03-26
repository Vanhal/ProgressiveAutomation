package com.vanhal.progressiveautomation.compat.mods;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.IGrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.compat.BaseMod;
import com.vanhal.progressiveautomation.util.Point3I;

import cpw.mods.fml.common.registry.GameRegistry;

public class GrowOres extends BaseMod {
	
	public GrowOres() {
		modID = "B0bGrowsOre";
	}

	@Override
	public boolean shouldLoad() {
		return checkModLoad();
	}
	
	@Override
	public boolean isPlantible(ItemStack item) {
		if (GameRegistry.findUniqueIdentifierFor(item.getItem()).modId.equals(modID)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean shouldHoe(ItemStack item) {
		return false;
	}
	
	@Override
	public boolean isPlant(Block plantBlock, int metadata) {
		if (GameRegistry.findUniqueIdentifierFor(plantBlock).modId.equals(modID)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isGrown(Point3I plantPoint, Block plantBlock, int metadata, World worldObj) {
		return (this.isPlant(worldObj.getBlock(plantPoint.getX(), plantPoint.getY() + 1, plantPoint.getZ()), 0));
	}
	
	@Override
	public boolean validBlock(World worldObj, ItemStack itemStack, Point3I testPoint) {
		
		if (worldObj.isAirBlock(testPoint.getX(), testPoint.getY(), testPoint.getZ())) {
			Point3I point = new Point3I(testPoint);
			
			point.setY(point.getY()-1);
			
			Block testBlock = worldObj.getBlock(point.getX(), point.getY(), point.getZ());
			if (testBlock.getClass().getName().contains("GrowingBlock")) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean placeSeed(World worldObj, ItemStack itemStack, Point3I point, boolean doAction) {
		if (itemStack.getItem() instanceof ItemBlock) {
			Block plant = ((ItemBlock)itemStack.getItem()).field_150939_a;
			if (plant!=null) {
				if (doAction) {
					worldObj.setBlock(point.getX(), point.getY(), point.getZ(), plant, itemStack.getItem().getDamage(itemStack), 7);
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public ArrayList<ItemStack> harvestPlant(Point3I plantPoint, Block plantBlock, int metadata, World worldObj) {
		plantPoint.setY(plantPoint.getY() + 1);
		plantBlock = worldObj.getBlock(plantPoint.getX(), plantPoint.getY(), plantPoint.getZ());
		metadata = worldObj.getBlockMetadata( plantPoint.getX(), plantPoint.getY(), plantPoint.getZ() );
		return super.harvestPlant(plantPoint, plantBlock, metadata, worldObj);
	}
}
