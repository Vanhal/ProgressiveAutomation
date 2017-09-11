package com.vanhal.progressiveautomation.compat.mods;

import java.util.List;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.compat.BaseMod;
import com.vanhal.progressiveautomation.util.OreHelper;
import com.vanhal.progressiveautomation.util.Point3I;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockStem;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

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
		if ( (item.getItem() == Items.DYE) && (EnumDyeColor.byDyeDamage(item.getMetadata()) == EnumDyeColor.BROWN) ) return true; //Coco beans
		if (item.getItem() == Items.REEDS) return true; // sugar cane
		if (Block.getBlockFromItem(item.getItem()) == Blocks.CACTUS) return true; // cactus
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
		if (plantBlock == Blocks.REEDS) return true;
		if (plantBlock == Blocks.CACTUS) return true;
		return false;
	}
	
	@Override
	public boolean isGrown(Point3I plantPoint, Block plantBlock, IBlockState state, World worldObj) {
		int metadata = plantBlock.getMetaFromState(state);
		//check pumpkins and mellons first
		if (plantBlock instanceof BlockStem) {
			for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL) {
				Block testBlock = worldObj.getBlockState(plantPoint.toPosition().offset(facing)).getBlock();
				if ( (testBlock == Blocks.MELON_BLOCK) || (testBlock == Blocks.PUMPKIN) )
					return true;
			}
		} else if (plantBlock instanceof IGrowable) {
			return !((IGrowable)plantBlock).canGrow(worldObj, plantPoint.toPosition(), state, true);
		} else if (plantBlock instanceof BlockNetherWart) { //nether wart
			return (metadata >= 3);
		} else if (plantBlock == Blocks.REEDS) { // sugar cane
			return (worldObj.getBlockState(plantPoint.toPosition().up()).getBlock() == Blocks.REEDS);
		} else if (plantBlock == Blocks.CACTUS) { //cactus
			return (worldObj.getBlockState(plantPoint.toPosition().up()).getBlock() == Blocks.CACTUS);
		}
		return false;
	}
	
	protected IBlockState getPlantBlock(World worldObj, ItemStack itemStack, Point3I point) {
		IBlockState plant = null;
		if (itemStack.getItem() instanceof IPlantable) {
			//normal crops
			plant = ((IPlantable)itemStack.getItem()).getPlant(worldObj, point.toPosition());
			plant = plant.getBlock().getStateFromMeta(itemStack.getItem().getDamage(itemStack));
		} else if (itemStack.getItem() == Items.REEDS) { //sugarcane
			plant = Blocks.REEDS.getDefaultState();
		} else if (Block.getBlockFromItem(itemStack.getItem()) == Blocks.CACTUS) { //cactus
			plant = Blocks.CACTUS.getDefaultState();
		} else if ( (itemStack.getItem() == Items.DYE) && (EnumDyeColor.byDyeDamage(itemStack.getMetadata()) == EnumDyeColor.BROWN) ) {
			plant = getCorrectCocoState(worldObj, point.toPosition());
		}
		return plant;
	}
	
	private IBlockState getCorrectCocoState(World worldObj, BlockPos pos) {
		if (worldObj.isAirBlock(pos)) {
			for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL) {
				BlockPos testPos = pos.offset(facing);
				IBlockState testState = worldObj.getBlockState(testPos);
	            Block testBlock = testState.getBlock();
				if (testBlock == Blocks.LOG && testState.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.JUNGLE) {
					return Blocks.COCOA.getDefaultState().withProperty(BlockHorizontal.FACING, facing);
				}
			}
		}
		return null;
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
		if (validBlock(worldObj, itemStack, point)) {
			if (doAction) {
				worldObj.setBlockState(point.toPosition(), getPlantBlock(worldObj, itemStack, point), 7);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public List<ItemStack> harvestPlant(Point3I plantPoint, Block plantBlock, IBlockState state, World worldObj) {
		if (plantBlock instanceof BlockStem) {
			for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL) {
				IBlockState testState = worldObj.getBlockState(plantPoint.toPosition().offset(facing));
				Block testBlock = testState.getBlock();
				if ( (testBlock == Blocks.MELON_BLOCK) || (testBlock == Blocks.PUMPKIN) ) {
					plantPoint.fromPosition(plantPoint.toPosition().offset(facing));
					return super.harvestPlant(plantPoint, testBlock, testState, worldObj);
				}
			}
		}
		if ( (plantBlock == Blocks.REEDS) || (plantBlock == Blocks.CACTUS) ) {
			plantPoint.setY(plantPoint.getY() + 1);
			state = worldObj.getBlockState(plantPoint.toPosition());
			plantBlock = state.getBlock();
		}
		return super.harvestPlant(plantPoint, plantBlock, state, worldObj);
	}
}
