package com.vanhal.progressiveautomation.entities.planter;

import java.util.ArrayList;

import com.vanhal.progressiveautomation.upgrades.UpgradeType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.IGrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraft.world.WorldServer;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.UpgradeableTileEntity;
import com.vanhal.progressiveautomation.ref.Pneumaticcraft;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.util.PlayerFake;
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
		setAllowedUpgrades(UpgradeType.WOODEN, UpgradeType.WITHER);
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
								addPartialUpdate("currentBlock", searchBlock);
							}
						} else {
							currentTime = 0;
							searchBlock = -1;
							addPartialUpdate("currentBlock", searchBlock);
						}
						addPartialUpdate("currentTime", currentTime);
					} else if (plantSeed(searchBlock, true)) {
						searchBlock = -1;
						addPartialUpdate("currentBlock", searchBlock);
					} else {
						if (checkPlant(searchBlock)) {
							currentTime = harvestTime;
							addPartialUpdate("currentTime", currentTime);
						}
					}
					
				} else {
					doSearch();
				}
				
			}
		}
	}


	public void writeCommonNBT(NBTTagCompound nbt) {
		super.writeCommonNBT(nbt);
		//save the current planting time
		nbt.setInteger("currentTime", currentTime);
		nbt.setInteger("currentBlock", searchBlock);
	}

	public void readCommonNBT(NBTTagCompound nbt) {
		super.readCommonNBT(nbt);
		//load the current planting time
		if (nbt.hasKey("currentTime")) currentTime = nbt.getInteger("currentTime");
		if (nbt.hasKey("currentBlock")) searchBlock = nbt.getInteger("currentBlock");
	}
	
	public boolean doSearch() {
		if (searchBlock>=0) return true;
		
		for (int i = 0; i < this.getRange(); i++) {
			if (checkPlant(i)) {
				searchBlock = i;
				addPartialUpdate("currentBlock", searchBlock);
				return true;
			}
		}
		
		//scan for blocks that we can plant on
		for (int i = 0; i < this.getRange(); i++) {
			if (plantSeed(i, false)) {
				searchBlock = i;
				addPartialUpdate("currentBlock", searchBlock);
				return true;
			}
		}
		return false;
	}
	
	
	protected void harvestPlant(int n) {
		Point3I currentBlock = getPoint(n);
		
		Block actualBlock = worldObj.getBlock(currentBlock.getX(), currentBlock.getY(), currentBlock.getZ());
		if ( (actualBlock == Blocks.reeds) || (actualBlock == Blocks.cactus) ) {
			currentBlock.setY(currentBlock.getY() + 1);
			actualBlock = worldObj.getBlock(currentBlock.getX(), currentBlock.getY(), currentBlock.getZ());
		}
		
		int metaData = worldObj.getBlockMetadata( currentBlock.getX(), currentBlock.getY(), currentBlock.getZ() );
		
		PlayerFake faker = new PlayerFake((WorldServer)worldObj);
		//try and right click it first, if not then break the block, unless it's pams harvestcraft
		if (actualBlock.getClass().getName().startsWith("com.pam.harvestcraft")) {
			ArrayList<ItemStack> items = actualBlock.getDrops(worldObj, currentBlock.getX(), currentBlock.getY(), currentBlock.getZ(), metaData, 0);
			//get the drops
			for (ItemStack item : items) {
				addToInventory(item);
			}
			worldObj.setBlock(currentBlock.getX(), currentBlock.getY(), currentBlock.getZ(), actualBlock, 0, 2);
		} else if (actualBlock.onBlockActivated(worldObj, currentBlock.getX(), currentBlock.getY(), currentBlock.getZ(), faker, metaData, 0, 0, 0)) {
			IInventory inv = faker.inventory;
			for (int i = 0; i < inv.getSizeInventory(); i++){
				if (inv.getStackInSlot(i)!=null) {
					addToInventory(inv.getStackInSlot(i));
				}
			}
		} 

		if (checkPlant(n)) {
			ArrayList<ItemStack> items = actualBlock.getDrops(worldObj, currentBlock.getX(), currentBlock.getY(), currentBlock.getZ(), metaData, 0);
			//get the drops
			for (ItemStack item : items) {
				addToInventory(item);
			}
	
			worldObj.removeTileEntity( currentBlock.getX(), currentBlock.getY(), currentBlock.getZ() );
			worldObj.setBlockToAir(currentBlock.getX(), currentBlock.getY(), currentBlock.getZ());
			hoeGround(n, true);
		}
	}
	
	protected boolean plantSeed(int n, boolean doAction) {
		if (slots[SLOT_SEEDS]!=null) {
			if (slots[SLOT_SEEDS].stackSize>0) {
				Point3I point = getPoint(n);
				//ProgressiveAutomation.logger.info("Plant: "+n+" "+point.getX()+", "+point.getY()+", "+point.getZ());
				
				//if the seed is from pnematiccraft
				 if (Pneumaticcraft.isSeed(slots[SLOT_SEEDS])) {
					 if (Pneumaticcraft.validBlock(worldObj, slots[SLOT_SEEDS], point)) {
						 if (Pneumaticcraft.checkClear(worldObj, point)) {
							 if (doAction) {
								 if (Pneumaticcraft.placeSeed(worldObj, slots[SLOT_SEEDS], point)) {
									 //placed
	
									 slots[SLOT_SEEDS].stackSize--;
									 if (slots[SLOT_SEEDS].stackSize==0) {
										 slots[SLOT_SEEDS] = null;
									 }
								 }
							 }
							 return true;
						 }
					 }
				} else if (isPlantable(slots[SLOT_SEEDS])) {
					Block plant = null;
					if (slots[SLOT_SEEDS].getItem() instanceof IPlantable) {
						//normal crops
						plant = ((IPlantable)slots[SLOT_SEEDS].getItem()).getPlant(worldObj,  point.getX(), point.getY(), point.getZ());
					} else if (slots[SLOT_SEEDS].getItem() == Items.reeds) {
						//sugarcane
						plant = Blocks.reeds;
					} else if (Block.getBlockFromItem(slots[SLOT_SEEDS].getItem()) == Blocks.cactus) {
						//cactus
						plant = Blocks.cactus;
					}
					
					//place the block
					if (plant != null) {
						if (plant.getClass().getName().startsWith("com.pam.harvestcraft")) {
							hoeGround(n, false);
						} else if (!plant.canPlaceBlockAt(worldObj, point.getX(), point.getY(), point.getZ())) {
							//hoe the ground if we can
							hoeGround(n, false);
						}
						if (
							(plant.canPlaceBlockAt(worldObj, point.getX(), point.getY(), point.getZ())) &&
							(worldObj.getBlock(point.getX(), point.getY(), point.getZ()) != plant)
						) {
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
		}
		return false;
	}

	protected boolean checkPlant(int n) {
		Point3I plantPoint = getPoint(n);
		Block plantBlock = worldObj.getBlock(plantPoint.getX(), plantPoint.getY(), plantPoint.getZ());
		int metadata = worldObj.getBlockMetadata(plantPoint.getX(), plantPoint.getY(), plantPoint.getZ());
		
		//for crops and anything that is IGrowable
		if (plantBlock instanceof IGrowable) {
			return !((IGrowable)plantBlock).func_149851_a(worldObj, plantPoint.getX(), plantPoint.getY(), plantPoint.getZ(), true);
		} else if (plantBlock instanceof BlockNetherWart) { //nether wart
			return (metadata >= 3);
		} else if (plantBlock == Blocks.reeds) { // sugar cane
			return (worldObj.getBlock(plantPoint.getX(), plantPoint.getY() + 1, plantPoint.getZ()) == Blocks.reeds);
		} else if (plantBlock == Blocks.cactus) { //cactus
			return (worldObj.getBlock(plantPoint.getX(), plantPoint.getY() + 1, plantPoint.getZ()) == Blocks.cactus);
		} else if (Pneumaticcraft.isPlant(plantBlock)) { //Pneumaticcraft
			return Pneumaticcraft.isGrown(worldObj, plantPoint);
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
	
	public static boolean isPlantable(ItemStack item) {
		if ( (
					(item.getItem() instanceof IPlantable) //normal crops
					|| (item.getItem() == Items.reeds) // sugar cane
					|| (Block.getBlockFromItem(item.getItem()) == Blocks.cactus)  // cactus
					|| (Pneumaticcraft.isSeed(item)) //pnumaticraft plants
				) && (OreDictionary.getOreID(item) != OreDictionary.getOreID("treeSapling")) 
			) {
			return true;
		}
		return false;
	}
	
	public int extraSlotCheck(ItemStack item) {
		if (isPlantable(item)) {
			return SLOT_SEEDS;
		}
		return super.extraSlotCheck(item);
	}


	/* ISided Stuff */
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if ( (slot == SLOT_SEEDS) && (isPlantable(stack)) ) {
    		return true;
    	}
		return super.isItemValidForSlot(slot, stack);
	}
}
