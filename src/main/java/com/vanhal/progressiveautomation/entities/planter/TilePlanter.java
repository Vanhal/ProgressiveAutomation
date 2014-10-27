package com.vanhal.progressiveautomation.entities.planter;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.UpgradeableTileEntity;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.util.Point2I;
import com.vanhal.progressiveautomation.util.Point3I;

public class TilePlanter extends UpgradeableTileEntity {
	
	protected int searchBlock = -1;
	public int SLOT_SEEDS = 1;
	
	public int harvestTime = 80;
	
	public int currentTime = 0;

	public TilePlanter() {
		super(12);
		setUpgradeLevel(ToolHelper.LEVEL_WOOD);
		setHarvestTime(80);
		
		// #36 Planter can't eject items to bottom
		extDirection = ForgeDirection.DOWN;
		
		//slots
		SLOT_HOE = 2;
		SLOT_UPGRADE = 3;
	}
	
	protected void setHarvestTime(int time) {
		harvestTime = time;
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (!worldObj.isRemote) {
			checkInventory();
			checkForChanges();

			if (isBurning()) {
				if (searchBlock > -1) {
					if (currentTime>0) {
						//harvesting the plant
						if (checkPlant(searchBlock)) {
							currentTime--;
							if (currentTime<=0) {
								//break Plant, unhoe the earth, collect seeds etc
								harvestPlant(searchBlock);
								searchBlock = -1;
							}
						} else {
							currentTime = 0;
						}
					} else if (plantSeed(searchBlock, true)) {
						searchBlock = -1;
					} else {
						if (checkPlant(searchBlock)) {
							currentTime = harvestTime;
						}
					}
					
				} else {
					doSearch();
				}
				
			}
		}
	}


	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		//save the current chopping time
		nbt.setInteger("currentTime", currentTime);
		nbt.setInteger("currentBlock", searchBlock);
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		//load the current chopping time
		currentTime = nbt.getInteger("choppingTime");
		searchBlock = nbt.getInteger("currentBlock");
	}
	
	public boolean doSearch() {
		if (searchBlock>=0) return true;
		
		for (int i = 0; i < this.getRange(); i++) {
			if (checkPlant(i)) {
				searchBlock = i;
				return true;
			}
		}
		
		//scan for blocks that we can plant on
		for (int i = 0; i < this.getRange(); i++) {
			if (plantSeed(i, false)) {
				searchBlock = i;
				return true;
			}
		}
		return false;
	}
	
	
	protected void harvestPlant(int n) {
		Point3I currentBlock = getPoint(n);
		
		Block actualBlock = worldObj.getBlock(currentBlock.getX(), currentBlock.getY(), currentBlock.getZ());
		int metaData = worldObj.getBlockMetadata( currentBlock.getX(), currentBlock.getY(), currentBlock.getZ() );
		ArrayList<ItemStack> items = actualBlock.getDrops(worldObj, currentBlock.getX(), currentBlock.getY(), currentBlock.getZ(), metaData, 0);
		//get the drops
		for (ItemStack item : items) {
			addToInventory(item);
		}

		worldObj.removeTileEntity( currentBlock.getX(), currentBlock.getY(), currentBlock.getZ() );
		worldObj.setBlockToAir(currentBlock.getX(), currentBlock.getY(), currentBlock.getZ());
		hoeGround(n, true);
	}
	
	protected boolean plantSeed(int n, boolean doAction) {
		if (slots[SLOT_SEEDS]!=null) {
			if (slots[SLOT_SEEDS].stackSize>0) {
				Point3I point = getPoint(n);
				//ProgressiveAutomation.logger.info("Plant: "+n+" "+point.getX()+", "+point.getY()+", "+point.getZ());
				
				if (isPlantable(slots[SLOT_SEEDS])) {
					Block plant = ((IPlantable)slots[SLOT_SEEDS].getItem()).getPlant(worldObj,  point.getX(), point.getY(), point.getZ());
					if (!plant.canPlaceBlockAt(worldObj, point.getX(), point.getY(), point.getZ())) {
						//hoe the ground if we can
						hoeGround(n, false);
					}
					if (plant.canPlaceBlockAt(worldObj, point.getX(), point.getY(), point.getZ())) {
						if (doAction) {
							worldObj.setBlock(point.getX(), point.getY(), point.getZ(), plant, slots[SLOT_SEEDS].getItem().getDamage(slots[SLOT_SEEDS]), 7);
							slots[SLOT_SEEDS].stackSize--;
							if (slots[SLOT_SEEDS].stackSize==0) {
								slots[SLOT_SEEDS] = null;
							}
						}
						
						return true;
					}
					
				}
			}
		}
		return false;
	}

	protected boolean checkPlant(int n) {
		Point3I plantPoint = getPoint(n);
		Block plantBlock = worldObj.getBlock(plantPoint.getX(), plantPoint.getY(), plantPoint.getZ());
		if (plantBlock instanceof IGrowable) {
			return !((IGrowable)plantBlock).func_149851_a(worldObj, plantPoint.getX(), plantPoint.getY(), plantPoint.getZ(), true);
		}
		return false;
	}
	
	
	protected Point3I getPoint(int n) {
		Point2I p1 = spiral(n+1, xCoord, zCoord);
		return new Point3I(p1.getX(), yCoord + 2, p1.getY());
	}
	
	protected void hoeGround(int n, boolean reverse) {
		Point3I plantPoint = getPoint(n);
		Block plantBlock = worldObj.getBlock(plantPoint.getX(), plantPoint.getY(), plantPoint.getZ());
		Point3I dirtPoint = new Point3I(plantPoint.getX(), plantPoint.getY() - 1, plantPoint.getZ());
		Block dirtBlock = worldObj.getBlock(dirtPoint.getX(), dirtPoint.getY(), dirtPoint.getZ());
		
		if (reverse) {
			if (dirtBlock == Blocks.farmland) {
				worldObj.setBlock(dirtPoint.getX(), dirtPoint.getY(), dirtPoint.getZ(), Blocks.dirt);
			}
		} else {
			if (slots[SLOT_HOE]!=null) {
				if (plantBlock.isAir(worldObj, plantPoint.getX(), plantPoint.getY(), plantPoint.getZ())) {
					if ((dirtBlock == Blocks.grass || dirtBlock == Blocks.dirt)) {
						worldObj.setBlock(dirtPoint.getX(), dirtPoint.getY(), dirtPoint.getZ(), Blocks.farmland);
						if (ToolHelper.damageTool(slots[SLOT_HOE], worldObj, dirtPoint.getX(), dirtPoint.getY(), dirtPoint.getZ())) {
							slots[SLOT_HOE] = null;
						}
					}
				}
			}
		}
	}
	
	/**
	 * This gives the current status of the Planter
	 * @return int 0 for waiting, 1 for harvesting, 2 for planting
	 */
	protected int statusSet = 0;
	
	public int getStatus() {
		if (worldObj.isRemote) {
			return statusSet;
		} else {
			if (searchBlock > -1) {
				if (currentTime>0) {
					if (checkPlant(searchBlock)) {
						return 1;
					}
				} else if (plantSeed(searchBlock, false)) {
					return 2;
				} 
			}
		}
		return 0;
	}
	
	public void setStatus(int status) {
		statusSet = status;
	}


	public boolean readyToBurn() {
		if (slots[SLOT_HOE]!=null) {
			if (doSearch()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isPlantable(ItemStack item) {
		if ( (item.getItem() instanceof IPlantable) && (OreDictionary.getOreID(item) != OreDictionary.getOreID("treeSapling")) ) {
			return true;
		}
		return false;
	}
	
	public int extraSlotCheck(int slot) {
		if (isPlantable(slots[slot])) {
			return SLOT_SEEDS;
		}
		return -1;
	}


	/* ISided Stuff */
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if ( (slot == SLOT_SEEDS) && (isPlantable(stack)) ) {
    		return true;
    	}
		return super.isItemValidForSlot(slot, stack);
	}
}
