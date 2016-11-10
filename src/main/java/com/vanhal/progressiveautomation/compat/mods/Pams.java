package com.vanhal.progressiveautomation.compat.mods;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.util.Point3I;

public class Pams extends Vanilla {
	
	public Pams() {
		modID = "harvestcraft";
	}
	
	@Override
	public boolean shouldLoad() {
		return checkModLoad();
	}
	
	@Override
	public boolean isPlantible(ItemStack item) {
		if (item.getItem() instanceof IPlantable) {
			if (Item.REGISTRY.getNameForObject(item.getItem()).getResourceDomain().equals(modID)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean shouldHoe(ItemStack item) {
		return true;
	}
	
	@Override
	public boolean isSapling(ItemStack stack) {
		if (stack.getUnlocalizedName().contains("sapling")) {
			return Item.REGISTRY.getNameForObject(stack.getItem()).getResourceDomain().equals(modID);
		}
		return false;
	}
	
	@Override
	public boolean isPlant(Block plantBlock, IBlockState state) {
		if (super.isPlant(plantBlock, state)) {
			if (plantBlock.getClass().getName().startsWith("com.pam.harvestcraft")) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean validBlock(World worldObj, ItemStack itemStack, Point3I point) {
		if (Item.REGISTRY.getNameForObject(itemStack.getItem()).getResourceDomain().equals(modID)) {
			IBlockState plantState = getPlantBlock(worldObj, itemStack, point);
			Block plant = plantState.getBlock();
			
			if (plant!=null) {
				Point3I dirtPoint = new Point3I(point.getX(), point.getY() - 1, point.getZ());
				IBlockState dirtBlockState = worldObj.getBlockState(dirtPoint.toPosition());
				Block dirtBlock = dirtBlockState.getBlock();
				
				if (dirtBlock == Blocks.FARMLAND) {
					return (plant.canPlaceBlockAt(worldObj, point.toPosition())) &&
						(worldObj.getBlockState(point.toPosition()).getBlock() != plant);
				}
			}
		}
		return false;
	}
	
	
	@Override
	public List<ItemStack> harvestPlant(Point3I plantPoint, Block plantBlock, IBlockState state, World worldObj) {
		List<ItemStack> items = plantBlock.getDrops(worldObj, plantPoint.toPosition(), state, 0);
		worldObj.setBlockState(plantPoint.toPosition(), plantBlock.getDefaultState(), 2);
		return items;
	}
}
