package com.vanhal.progressiveautomation.compat.mods;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class ImmersiveEngineering extends Vanilla {
	
	protected Block hemp;
	
	public ImmersiveEngineering() {
		modID = "ImmersiveEngineering";
	}
	
	private boolean haveBlocks() {
		if (hemp==null) {
			hemp = GameRegistry.findBlock(modID, "hemp");
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
	
	/*@Override
	public ArrayList<ItemStack> harvestPlant(Point3I plantPoint, Block plantBlock, IBlockState state, World worldObj) {
		ArrayList<ItemStack> items = plantBlock.getDrops(worldObj, plantPoint.getX(), plantPoint.getY(), plantPoint.getZ(), metadata, 0);
		
		//get the top of the plant
		Block oneUp = worldObj.getBlock(plantPoint.getX(), plantPoint.getY() + 1, plantPoint.getZ());
		int oneUpMeta = worldObj.getBlockMetadata(plantPoint.getX(), plantPoint.getY() + 1, plantPoint.getZ());
		if (this.isPlant(oneUp, oneUpMeta)) {
			items.addAll(oneUp.getDrops(worldObj, plantPoint.getX(), plantPoint.getY() + 1, plantPoint.getZ(), oneUpMeta, 0));
			worldObj.setBlockToAir(plantPoint.getX(), plantPoint.getY() + 1, plantPoint.getZ());
		}
		worldObj.setBlockToAir(plantPoint.getX(), plantPoint.getY(), plantPoint.getZ());
		return items;
	}*/
}
