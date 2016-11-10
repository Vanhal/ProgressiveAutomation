package com.vanhal.progressiveautomation.compat.mods;

import java.util.ArrayList;
import java.util.List;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.util.Point3I;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class ImmersiveEngineering extends Vanilla {
	
	protected Block hemp;
	
	public ImmersiveEngineering() {
		modID = "immersiveengineering";
	}
	
	private boolean haveBlocks() {
		if (hemp==null) {
			hemp = Block.REGISTRY.getObject(new ResourceLocation(modID, "hemp"));
			//GameRegistry.findBlock(modID, "hemp");
			if (hemp!=null) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean shouldLoad() {
		if (checkModLoad()) {
			this.haveBlocks();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isPlant(Block plantBlock, IBlockState state) {
		if (haveBlocks()) {
			if (hemp.equals(plantBlock)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<ItemStack> harvestPlant(Point3I plantPoint, Block plantBlock, IBlockState state, World worldObj) {
		List<ItemStack> items = plantBlock.getDrops(worldObj, plantPoint.toPosition(), state, 0);
		
		//get the top of the plant
		IBlockState oneUpState = worldObj.getBlockState(plantPoint.toPosition());
		Block oneUp = oneUpState.getBlock();
		if (this.isPlant(oneUp, oneUpState)) {
			items.addAll(oneUp.getDrops(worldObj, plantPoint.toPosition().up(), state, 0));
			worldObj.setBlockToAir(plantPoint.toPosition().up());
		}
		worldObj.setBlockToAir(plantPoint.toPosition());
		return items;
	}
}
