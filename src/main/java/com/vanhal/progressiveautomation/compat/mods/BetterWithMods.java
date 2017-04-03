package com.vanhal.progressiveautomation.compat.mods;

import java.util.List;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.util.OreHelper;
import com.vanhal.progressiveautomation.util.Point3I;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.oredict.OreDictionary;

public class BetterWithMods extends Vanilla {
	public BetterWithMods() {
		modID = "betterwithmods";
	}
	
	@Override
	public boolean shouldLoad() {
		return checkModLoad();
	}
	
	@Override
	public boolean isPlantible(ItemStack item) {
		if (Item.REGISTRY.getNameForObject(item.getItem()).getResourceDomain().equals(modID)) {
			if (item.getItem() instanceof IPlantable) return true;
			if (item != null) {
				if (item.getItem() != null) {
					if (item.getItem().toString().contains("HempSeed")) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	@Override
	public boolean isPlant(Block plantBlock, IBlockState state) {
		if (super.isPlant(plantBlock, state)) {
			if (Block.REGISTRY.getNameForObject(plantBlock).getResourceDomain().equals(modID)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean shouldHoe(ItemStack item) {
		if (this.isPlantible(item)) return true;
		return false;
	}
	
	@Override
	protected IBlockState getPlantBlock(World worldObj, ItemStack itemStack, Point3I point) {
		if ( (itemStack != null) && (itemStack.getItem()!=null) ) {
			if (itemStack.getItem() instanceof ItemBlock) {
				return ((ItemBlock)itemStack.getItem()).block.getDefaultState();
			}
		}
		return null;
	}

	@Override
	public boolean isGrown(Point3I plantPoint, Block plantBlock, IBlockState state, World worldObj) {
		if (plantBlock.getUnlocalizedName().contains("hemp")) {
			return (worldObj.getBlockState(plantPoint.toPosition().up()).getBlock().getUnlocalizedName().contains("hemp"));
		}
		return false;
	}
	
	@Override
	public List<ItemStack> harvestPlant(Point3I plantPoint, Block plantBlock, IBlockState state, World worldObj) {
		IBlockState oneUpState = worldObj.getBlockState(plantPoint.toPosition());
		Block oneUp = oneUpState.getBlock();
		List<ItemStack> items = oneUp.getDrops(worldObj, plantPoint.toPosition().up(), state, 0);
		worldObj.setBlockToAir(plantPoint.toPosition().up());
		return items;
	}
}
