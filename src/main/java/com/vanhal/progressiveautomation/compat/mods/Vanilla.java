package com.vanhal.progressiveautomation.compat.mods;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
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
	public boolean isPlant(Block plantBlock, IBlockState state) {
		if (plantBlock instanceof IGrowable) return true;
		if (plantBlock instanceof BlockNetherWart) return true;
		if (plantBlock == Blocks.reeds) return true;
		if (plantBlock == Blocks.cactus) return true;
		return false;
	}
	
	@Override
	public boolean isGrown(Point3I plantPoint, Block plantBlock, IBlockState state, World worldObj) {
		int metadata = plantBlock.getMetaFromState(state);
		if (plantBlock instanceof IGrowable) {
			return !((IGrowable)plantBlock).canGrow(worldObj, plantPoint.toPosition(), state, true);
		} else if (plantBlock instanceof BlockNetherWart) { //nether wart
			return (metadata >= 3);
		} else if (plantBlock == Blocks.reeds) { // sugar cane
			return (worldObj.getBlockState(plantPoint.toPosition().up()).getBlock() == Blocks.reeds);
		} else if (plantBlock == Blocks.cactus) { //cactus
			return (worldObj.getBlockState(plantPoint.toPosition().up()).getBlock() == Blocks.cactus);
		}
		return false;
	}
	
	protected IBlockState getPlantBlock(World worldObj, ItemStack itemStack, Point3I point) {
		IBlockState plant = null;
		if (itemStack.getItem() instanceof IPlantable) {
			//normal crops
			plant = ((IPlantable)itemStack.getItem()).getPlant(worldObj, point.toPosition());
		} else if (itemStack.getItem() == Items.reeds) { //sugarcane
			plant = Blocks.reeds.getDefaultState();
		} else if (Block.getBlockFromItem(itemStack.getItem()) == Blocks.cactus) { //cactus
			plant = Blocks.cactus.getDefaultState();
		}
		return plant;
	}
	
	@Override
	public boolean validBlock(World worldObj, ItemStack itemStack, Point3I point) {
		IBlockState plant = getPlantBlock(worldObj, itemStack, point);
		if (plant!=null) {
			return (plant.getBlock().canPlaceBlockAt(worldObj, point.toPosition())) &&
				(worldObj.getBlockState(point.toPosition()).getBlock() != plant.getBlock());
		}
		return false;
	}
	
	@Override
	public boolean placeSeed(World worldObj, ItemStack itemStack, Point3I point, boolean doAction) {
		IBlockState plant = getPlantBlock(worldObj, itemStack, point);
		if (plant!=null) {
			if (doAction) {
				worldObj.setBlockState(point.toPosition(), 
						plant.getBlock().getStateFromMeta(itemStack.getItem().getDamage(itemStack)), 7);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public List<ItemStack> harvestPlant(Point3I plantPoint, Block plantBlock, IBlockState state, World worldObj) {
		if ( (plantBlock == Blocks.reeds) || (plantBlock == Blocks.cactus) ) {
			plantPoint.setY(plantPoint.getY() + 1);
			state = worldObj.getBlockState(plantPoint.toPosition());
			plantBlock = state.getBlock();
		}
		return super.harvestPlant(plantPoint, plantBlock, state, worldObj);
	}
}
