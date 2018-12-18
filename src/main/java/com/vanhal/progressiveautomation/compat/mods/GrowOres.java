package com.vanhal.progressiveautomation.compat.mods;

import java.util.List;

import com.vanhal.progressiveautomation.compat.BaseMod;
import com.vanhal.progressiveautomation.util.Point3I;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
//		if (GameRegistry.findUniqueIdentifierFor(item.getItem()).modId.equals(modID)) {
//			return true;
//		}
		return false;
	}
	
	@Override
	public boolean shouldHoe(ItemStack item) {
		return false;
	}
	
	@Override
	public boolean isPlant(Block plantBlock, IBlockState state) {
//		if (GameRegistry.findUniqueIdentifierFor(plantBlock).modId.equals(modID)) {
//			return true;
//		}
		return false;
	}
	
	@Override
	public boolean isGrown(Point3I plantPoint, Block plantBlock, IBlockState state, World worldObj) {
		IBlockState testBlock = worldObj.getBlockState(plantPoint.toPosition().up());
		return (this.isPlant(testBlock.getBlock(), testBlock));
	}
	
	@Override
	public boolean validBlock(World worldObj, ItemStack itemStack, Point3I testPoint) {
		
		if (worldObj.isAirBlock(testPoint.toPosition())) {
			Point3I point = new Point3I(testPoint);
			
			point.setY(point.getY()-1);
			
			Block testBlock = worldObj.getBlockState(testPoint.toPosition()).getBlock();
			if (testBlock.getClass().getName().contains("GrowingBlock")) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean placeSeed(World worldObj, ItemStack itemStack, Point3I point, boolean doAction) {
		if (itemStack.getItem() instanceof ItemBlock) {
			Block plant = ((ItemBlock)itemStack.getItem()).getBlock();
			if (plant!=null) {
				if (doAction) {
					worldObj.setBlockState(point.toPosition(), 
							plant.getStateFromMeta(itemStack.getItem().getDamage(itemStack)), 7);
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<ItemStack> harvestPlant(Point3I plantPoint, Block plantBlock, IBlockState state, World worldObj) {
		plantPoint.setY(plantPoint.getY() + 1);
		state = worldObj.getBlockState(plantPoint.toPosition());
		plantBlock = state.getBlock();
		return super.harvestPlant(plantPoint, plantBlock, state, worldObj);
	}
}
