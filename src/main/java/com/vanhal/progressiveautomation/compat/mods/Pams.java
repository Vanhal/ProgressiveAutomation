package com.vanhal.progressiveautomation.compat.mods;

public class Pams extends Vanilla {
	
	public Pams() {
		modID = "harvestcraft";
	}
	
	@Override
	public boolean shouldLoad() {
		return checkModLoad();
	}
	
	/*@Override
	public boolean isPlantible(ItemStack item) {
		if (item.getItem() instanceof IPlantable) {
			if (GameRegistry.findUniqueIdentifierFor(item.getItem()).modId.equals(modID)) {
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
		if (stack.getUnlocalizedName().contains("Sapling")) {
			return GameRegistry.findUniqueIdentifierFor(stack.getItem()).modId.equals(modID);
		}
		return false;
	}
	
	@Override
	public boolean isPlant(Block plantBlock, int metadata) {
		if (super.isPlant(plantBlock, metadata)) {
			if (plantBlock.getClass().getName().startsWith("com.pam.harvestcraft")) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean validBlock(World worldObj, ItemStack itemStack, Point3I point) {
		if (GameRegistry.findUniqueIdentifierFor(itemStack.getItem()).modId.equals(modID)) {
			Block plant = getPlantBlock(worldObj, itemStack, point);
			if (plant!=null) {
				Point3I dirtPoint = new Point3I(point.getX(), point.getY() - 1, point.getZ());
				Block dirtBlock = worldObj.getBlock(dirtPoint.getX(), dirtPoint.getY(), dirtPoint.getZ());
				
				if (dirtBlock == Blocks.farmland) {
					return (plant.canPlaceBlockAt(worldObj, point.getX(), point.getY(), point.getZ())) &&
						(worldObj.getBlock(point.getX(), point.getY(), point.getZ()) != plant);
				}
			}
		}
		return false;
	}
	
	
	@Override
	public ArrayList<ItemStack> harvestPlant(Point3I plantPoint, Block plantBlock, int metadata, World worldObj) {
		ArrayList<ItemStack> items = plantBlock.getDrops(worldObj, plantPoint.getX(), plantPoint.getY(), plantPoint.getZ(), metadata, 0);
		worldObj.setBlock(plantPoint.getX(), plantPoint.getY(), plantPoint.getZ(), plantBlock, 0, 2);
		return items;
	}*/
}
