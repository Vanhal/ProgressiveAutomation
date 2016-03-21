package com.vanhal.progressiveautomation.compat.mods;

import com.vanhal.progressiveautomation.util.PlayerFake;


public class AgriCraft extends Vanilla {
	
	private PlayerFake faker = null;

	public AgriCraft() {
		this.modID = "AgriCraft";
	}
	
	/*@Override
	public boolean shouldLoad() {
		return checkModLoad();
	}
	
	
	@Override
	public boolean isPlant(Block plantBlock, int metadata) {
		if (super.isPlant(plantBlock, metadata)) {
			if (GameRegistry.findUniqueIdentifierFor(plantBlock).modId.equals(modID)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean validBlock(World worldObj, ItemStack itemStack, Point3I point) {
		Block plant = getPlantBlock(worldObj, itemStack, point);
		if (plant!=null) {
			TileEntity tile = worldObj.getTileEntity(point.getX(), point.getY(), point.getZ());
			if (tile != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tile.writeToNBT(tag);
				if ( (tag.hasKey("weed")) && (tag.hasKey("crossCrop")) ){
					if ( (!tag.hasKey("seed")) && (!tag.getBoolean("weed")) && (!tag.getBoolean("crossCrop")) ) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean placeSeed(World worldObj, ItemStack itemStack, Point3I point, boolean doAction) {
		int metaData = worldObj.getBlockMetadata(point.getX(), point.getY(), point.getZ());
		Block actualBlock = worldObj.getBlock(point.getX(), point.getY(), point.getZ());
		if (actualBlock!=null) {
			if (doAction) {
				PlayerFake faker = new PlayerFake((WorldServer)worldObj);
				faker.inventory.setInventorySlotContents(1, itemStack.copy());
				faker.setItemInHand(1);
				faker.setPosition(point.getX(), point.getY(), point.getZ());
				actualBlock.onBlockActivated(worldObj, point.getX(), point.getY(), point.getZ(), faker, metaData, 0, 0, 0);
				if ( (faker.inventory.getStackInSlot(1)!=null) && (faker.inventory.getStackInSlot(1).stackSize==itemStack.stackSize) ) {
					faker = null;
					return false;
				}
				faker = null;
			}
			return true;
		}
		return false;
	}
	
	@Override
	public ArrayList<ItemStack> harvestPlant(Point3I point, Block plantBlock, int metadata, World worldObj) {
		PlayerFake faker = new PlayerFake((WorldServer)worldObj);
		plantBlock.onBlockActivated(worldObj, point.getX(), point.getY(), point.getZ(), faker, metadata, 0, 0, 0);

		AxisAlignedBB block = AxisAlignedBB.getBoundingBox(point.getX(), point.getY(), point.getZ(), 
														point.getX()+1, point.getY()+1, point.getZ()+1);
		List<EntityItem> entities = worldObj.getEntitiesWithinAABB(EntityItem.class, block);
		if (entities.isEmpty()) {
			return null;
		}
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		for (EntityItem item: entities) {
			items.add(item.getEntityItem());
			worldObj.removeEntity(item);
		}
		
		return items;
	}*/
}
