package com.vanhal.progressiveautomation.ref;

import java.util.List;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.util.Point3I;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

/*
 * Seed Mapping:
 * 0: Squid - Water
 * 1: Fire - Netherrack
 * 2: Creeper - Dirt, Grass, Farmland
 * 3: Slime - Dirt, Grass, Farmland
 * 4: Rain - Dirt, Grass, Farmland
 * 5: Ender - Endstone
 * 6: Lightning - Dirt, Grass, Farmland
 * 7: 
 * 8: Burst - Dirt, Grass, Farmland
 * 9: Potion - Dirt, Grass, Farmland
 * 10: Repulsion - Dirt, Grass, Farmland
 * 11: Helium - Netherrack (Upside down)
 * 12: Chopper - Dirt, Grass, Farmland
 * 13: 
 * 14: Propulsion - Dirt, Grass, Farmland
 * 15: Flying - Dirt, Grass, Farmland
 *
 */

public class Pneumaticcraft {
	private ItemStack seed = null;
	private int seedID = -1;
	
	public static boolean isSeed(ItemStack item) {
		return (item.getUnlocalizedName().startsWith("item.plasticPlant"));
	}
	
	public Pneumaticcraft(ItemStack itemStack) {
		if (isSeed(itemStack)) {
			seed = itemStack;
			seedID = itemStack.getItemDamage();
		}
	}
	
	public boolean validBlock(World worldObj, Point3I point) {
		return validBlock(worldObj, seed, point);
	}
	
	public static boolean validBlock(World worldObj, ItemStack itemStack, Point3I testPoint) {
		Point3I point = new Point3I(testPoint);
		
		int seedMeta = itemStack.getItemDamage();
		//check the planting block is air first
		if (!worldObj.isAirBlock(point.getX(), point.getY(), point.getZ())) {
			return false;
		}
		
		//select which block we want to check.
		if (seedMeta == 11) {
			point.setY(point.getY()+1);
		} else {
			point.setY(point.getY()-1);
		}
		
		//grab the block
		Block testBlock = worldObj.getBlock(point.getX(), point.getY(), point.getZ());
		
		//now test it
		if (seedMeta == 0) {
			if (testBlock == Blocks.water) return true;
		} else if (seedMeta == 5) {
			if (testBlock == Blocks.end_stone) return true;
		} else if ( (seedMeta == 1) || (seedMeta == 11) ) {
			if (testBlock == Blocks.netherrack) return true;
		} else {
			if ( (testBlock == Blocks.dirt) || (testBlock == Blocks.grass) || (testBlock == Blocks.farmland) ) return true;
		}
		return false;
	}
	
	public static boolean placeSeed(World world, ItemStack itemStack, Point3I point) {
		if (itemStack != null) {
			int seedMeta = itemStack.getItemDamage();
			ItemStack items = new ItemStack(itemStack.getItem(), 1, seedMeta);
			EntityItem entItem = new EntityItem(world, (float)point.getX() + 0.5f, (float)point.getY() + 0.5f, (float)point.getZ() + 0.5f, items);
			entItem.motionX = 0.0f;
			entItem.motionY = 0.0f;
			entItem.motionZ = 0.0f;
			entItem.delayBeforeCanPickup = 20;
			entItem.hoverStart = 0.0f;
			entItem.yOffset = 0.0f;
			if (items.hasTagCompound()) {
				entItem.getEntityItem().setTagCompound((NBTTagCompound)items.getTagCompound().copy());
	        }
			
			
			world.spawnEntityInWorld(entItem);
			return true;
		}
		return false;
	}
	
	public static boolean checkClear(World world, Point3I point) {
		AxisAlignedBB block = AxisAlignedBB.getBoundingBox(point.getX(), point.getY() - 1, point.getZ(), point.getX()+1, point.getY()+2, point.getZ()+1);
		
		List entities = world.getEntitiesWithinAABB(EntityItem.class, block);
		if (entities.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isPlant(Block plant) {
		return (plant.getClass().getName().startsWith("pneumaticCraft.common.block.pneumaticPlants"));
	}
	
	public static boolean isGrown(World world, Point3I point) {
		Block testBlock = world.getBlock(point.getX(), point.getY(), point.getZ());
		int meta = world.getBlockMetadata(point.getX(), point.getY(), point.getZ());
		
		return (testBlock.getDrops(world, point.getX(), point.getY(), point.getZ(), meta, 0).size()>=2);
	}
}
