package com.vanhal.progressiveautomation.compat.mods;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.IGrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.oredict.OreDictionary;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.compat.BaseMod;
import com.vanhal.progressiveautomation.util.OreHelper;
import com.vanhal.progressiveautomation.util.Point3I;

public class Vanilla extends BaseMod {
	
	@Override
	public boolean shouldLoad() {
		modID = "Vanilla";
		ProgressiveAutomation.logger.info("Vanilla Loaded");
		return true;
	}

	@Override
	public boolean isSapling(ItemStack stack) {
		return OreHelper.testOre("treeSapling", stack);
	}
	
	@Override
	public boolean isPlantible(ItemStack item) {
		if (item.getItem() instanceof IPlantable) return true;
		if (item.getItem() == Items.reeds) return true; // sugar cane
		if (Block.getBlockFromItem(item.getItem()) == Blocks.cactus) return true; // cactus
		return false;
	}
	
	@Override
	public boolean shouldHoe(ItemStack item) {
		if (item.getItem() instanceof IPlantable) return true;
		return false;
	}
	
	@Override
	public boolean isPlant(Block plantBlock, int metadata) {
		if (plantBlock instanceof IGrowable) return true;
		if (plantBlock instanceof BlockNetherWart) return true;
		if (plantBlock == Blocks.reeds) return true;
		if (plantBlock == Blocks.cactus) return true;
		return false;
	}
	
	@Override
	public boolean isGrown(Point3I plantPoint, Block plantBlock, int metadata, World worldObj) {
		if (plantBlock instanceof IGrowable) {
			return !((IGrowable)plantBlock).func_149851_a(worldObj, plantPoint.getX(), plantPoint.getY(), plantPoint.getZ(), true);
		} else if (plantBlock instanceof BlockNetherWart) { //nether wart
			return (metadata >= 3);
		} else if (plantBlock == Blocks.reeds) { // sugar cane
			return (worldObj.getBlock(plantPoint.getX(), plantPoint.getY() + 1, plantPoint.getZ()) == Blocks.reeds);
		} else if (plantBlock == Blocks.cactus) { //cactus
			return (worldObj.getBlock(plantPoint.getX(), plantPoint.getY() + 1, plantPoint.getZ()) == Blocks.cactus);
		}
		return false;
	}
	
	protected Block getPlantBlock(World worldObj, ItemStack itemStack, Point3I point) {
		Block plant = null;
		if (itemStack.getItem() instanceof IPlantable) {
			//normal crops
			plant = ((IPlantable)itemStack.getItem()).getPlant(worldObj,  point.getX(), point.getY(), point.getZ());
		} else if (itemStack.getItem() == Items.reeds) { //sugarcane
			plant = Blocks.reeds;
		} else if (Block.getBlockFromItem(itemStack.getItem()) == Blocks.cactus) { //cactus
			plant = Blocks.cactus;
		}
		return plant;
	}
	
	@Override
	public boolean validBlock(World worldObj, ItemStack itemStack, Point3I point) {
		Block plant = getPlantBlock(worldObj, itemStack, point);
		if (plant!=null) {
			return (plant.canPlaceBlockAt(worldObj, point.getX(), point.getY(), point.getZ())) &&
				(worldObj.getBlock(point.getX(), point.getY(), point.getZ()) != plant);
		}
		return false;
	}
	
	@Override
	public boolean placeSeed(World worldObj, ItemStack itemStack, Point3I point, boolean doAction) {
		Block plant = getPlantBlock(worldObj, itemStack, point);
		if (plant!=null) {
			if (doAction) {
				worldObj.setBlock(point.getX(), point.getY(), point.getZ(), plant, itemStack.getItem().getDamage(itemStack), 7);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public ArrayList<ItemStack> harvestPlant(Point3I plantPoint, Block plantBlock, int metadata, World worldObj) {
		if ( (plantBlock == Blocks.reeds) || (plantBlock == Blocks.cactus) ) {
			plantPoint.setY(plantPoint.getY() + 1);
			plantBlock = worldObj.getBlock(plantPoint.getX(), plantPoint.getY(), plantPoint.getZ());
			metadata = worldObj.getBlockMetadata( plantPoint.getX(), plantPoint.getY(), plantPoint.getZ() );
		}
		return super.harvestPlant(plantPoint, plantBlock, metadata, worldObj);
	}
}
