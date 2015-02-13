package com.vanhal.progressiveautomation.entities.planter;

import java.util.ArrayList;
import java.util.List;

import com.vanhal.progressiveautomation.upgrades.UpgradeType;

import net.minecraft.block.Block;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldServer;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.compat.ModHelper;
import com.vanhal.progressiveautomation.entities.UpgradeableTileEntity;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.util.OreHelper;
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
		extDirection = EnumFacing.DOWN;
		
		//slots
		SLOT_HOE = 2;
		SLOT_UPGRADE = 3;
	}
		
	protected void setHarvestTime(int time) {
		harvestTime = time;
	}
	
	@Override
	public void update() {
		super.update();
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
		
<<<<<<< HEAD
		Block actualBlock = worldObj.getBlock(currentBlock.getX(), currentBlock.getY(), currentBlock.getZ());
		int metaData = worldObj.getBlockMetadata( currentBlock.getX(), currentBlock.getY(), currentBlock.getZ() );
=======
		Block actualBlock = worldObj.getBlockState(currentBlock.toPosition()).getBlock();
		if ( (actualBlock == Blocks.reeds) || (actualBlock == Blocks.cactus) ) {
			currentBlock.setY(currentBlock.getY() + 1);
			actualBlock = worldObj.getBlockState(currentBlock.toPosition()).getBlock();
		}
		
		BlockPos currentPosition = currentBlock.toPosition();;
		
		IBlockState currentState = worldObj.getBlockState(currentPosition);
		int metaData = actualBlock.getMetaFromState(currentState);
>>>>>>> First Version where it looks like everything is working.....
		
		ArrayList<ItemStack> items = ModHelper.harvestPlant(currentBlock, actualBlock, metaData, worldObj);
		if (items!=null) {
			for (ItemStack item : items) {
				addToInventory(item);
			}
<<<<<<< HEAD
			damageHoe(currentBlock);
		}

		/*
		    PlayerFake faker = new PlayerFake((WorldServer)worldObj);
		    if (actualBlock.onBlockActivated(worldObj, currentBlock.getX(), currentBlock.getY(), currentBlock.getZ(), faker, metaData, 0, 0, 0)) {
=======
			//this may be wrong...
			worldObj.setBlockState(currentPosition, actualBlock.getDefaultState(), 2);
		} else if (actualBlock.onBlockActivated(worldObj, currentPosition, currentState, faker, EnumFacing.DOWN, 0.0f, 0.0f, 0.0f)) {
>>>>>>> First Version where it looks like everything is working.....
			IInventory inv = faker.inventory;
			for (int i = 0; i < inv.getSizeInventory(); i++){
				if (inv.getStackInSlot(i)!=null) {
					addToInventory(inv.getStackInSlot(i));
				}
			}
		} */

<<<<<<< HEAD
=======
		if (checkPlant(n)) {
			List<ItemStack> items = actualBlock.getDrops(worldObj, currentPosition, currentState, 0);
			//get the drops
			for (ItemStack item : items) {
				addToInventory(item);
			}
	
			worldObj.removeTileEntity( currentPosition );
			worldObj.setBlockToAir(currentPosition);
			hoeGround(n, true);
		}
>>>>>>> First Version where it looks like everything is working.....
	}
	
	protected boolean plantSeed(int n, boolean doAction) {
		if (slots[SLOT_SEEDS]!=null) {
			if (slots[SLOT_SEEDS].stackSize>0) {
				Point3I point = getPoint(n);
				
<<<<<<< HEAD
				if (ModHelper.shouldHoe(slots[SLOT_SEEDS])) {
					hoeGround(n);
				}
				
				if (ModHelper.placeSeed(worldObj, slots[SLOT_SEEDS], point, doAction)) {
					if (doAction) {
						slots[SLOT_SEEDS].stackSize--;
						 if (slots[SLOT_SEEDS].stackSize==0) {
							 slots[SLOT_SEEDS] = null;
						 }
=======
				//if the seed is from pnematiccraft
				 /*if (Pneumaticcraft.isSeed(slots[SLOT_SEEDS])) {
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
				} else */if (isPlantable(slots[SLOT_SEEDS])) {
					IBlockState plant = null;
					if (slots[SLOT_SEEDS].getItem() instanceof IPlantable) {
						//normal crops
						plant = ((IPlantable)slots[SLOT_SEEDS].getItem()).getPlant(worldObj, point.toPosition());
					} else if (slots[SLOT_SEEDS].getItem() == Items.reeds) {
						//sugarcane
						plant = Blocks.reeds.getDefaultState();
					} else if (Block.getBlockFromItem(slots[SLOT_SEEDS].getItem()) == Blocks.cactus) {
						//cactus
						plant = Blocks.cactus.getDefaultState();
					}
					
					//place the block
					if (plant != null) {
						if (plant.getClass().getName().startsWith("com.pam.harvestcraft")) {
							hoeGround(n, false);
						} else if (!plant.getBlock().canPlaceBlockAt(worldObj, point.toPosition())) {
							//hoe the ground if we can
							hoeGround(n, false);
						}
						if (
							(plant.getBlock().canPlaceBlockAt(worldObj, point.toPosition())) &&
							(worldObj.getBlockState(point.toPosition()) != plant)
						) {
							if (doAction) {
								worldObj.setBlockState(point.toPosition(), 
										plant.getBlock().getStateFromMeta(slots[SLOT_SEEDS].getItem().getDamage(slots[SLOT_SEEDS])), 7);
								slots[SLOT_SEEDS].stackSize--;
								if (slots[SLOT_SEEDS].stackSize==0) {
									slots[SLOT_SEEDS] = null;
								}
							}
							
							return true;
						}
>>>>>>> First Version where it looks like everything is working.....
					}
					return true;
				}
			}
		}
		return false;
	}

	protected boolean checkPlant(int n) {
		Point3I plantPoint = getPoint(n);
		BlockPos plantPosition = plantPoint.toPosition();
		IBlockState blockState = worldObj.getBlockState(plantPosition);
		Block plantBlock = blockState.getBlock();
		int metadata = plantBlock.getMetaFromState(blockState);
		

		return ModHelper.isGrown(plantPoint, plantBlock, metadata, worldObj);
	}
	
	
	protected Point3I getPoint(int n) {
		Point2I p1 = spiral(n+1, getPos().getX(), getPos().getZ());
		return new Point3I(p1.getX(), getPos().getY() + 2, p1.getY());
	}
	
	protected void hoeGround(int n) {
		hoeGround(n, false);
	}
	
	protected void hoeGround(int n, boolean reverse) {
		Point3I plantPoint = getPoint(n);
		BlockPos plantPosition = plantPoint.toPosition();
		IBlockState plantState = worldObj.getBlockState(plantPosition);
		Block plantBlock = plantState.getBlock();
		
		Point3I dirtPoint = new Point3I(plantPoint.getX(), plantPoint.getY() - 1, plantPoint.getZ());
		BlockPos dirtPosition = dirtPoint.toPosition();
		IBlockState dirtState = worldObj.getBlockState(dirtPosition);
		Block dirtBlock = plantState.getBlock();
		
		if (reverse) {
			if (dirtBlock == Blocks.farmland) {
				worldObj.setBlockState(dirtPosition, Blocks.dirt.getDefaultState());
			}
		} else {
			if (slots[SLOT_HOE]!=null) {
				if (plantBlock.isAir(worldObj, plantPosition)) {
					if ((dirtBlock == Blocks.grass || dirtBlock == Blocks.dirt)) {
<<<<<<< HEAD
						worldObj.setBlock(dirtPoint.getX(), dirtPoint.getY(), dirtPoint.getZ(), Blocks.farmland);
						damageHoe(dirtPoint);
=======
						worldObj.setBlockState(dirtPosition, Blocks.farmland.getDefaultState());
						if (ToolHelper.damageTool(slots[SLOT_HOE], worldObj, dirtPoint.getX(), dirtPoint.getY(), dirtPoint.getZ())) {
							slots[SLOT_HOE] = null;
						}
>>>>>>> First Version where it looks like everything is working.....
					}
				}
			}
		}
	}
	
	protected void damageHoe(Point3I point) {
		if (ToolHelper.damageTool(slots[SLOT_HOE], worldObj, point.getX(), point.getY(), point.getZ())) {
			slots[SLOT_HOE] = null;
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

<<<<<<< HEAD
	@Override
	/*public boolean readyToBurn() {
=======

	@Override
	public boolean readyToBurn() {
>>>>>>> First Version where it looks like everything is working.....
		if (slots[SLOT_HOE]!=null) {
			if (doSearch()) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isPlantable(ItemStack item) {
<<<<<<< HEAD
		if ( (ModHelper.isPlantible(item)) && (!ModHelper.checkSapling(item)) ) {
=======
		if ( (
					(item.getItem() instanceof IPlantable) //normal crops
					|| (item.getItem() == Items.reeds) // sugar cane
					|| (Block.getBlockFromItem(item.getItem()) == Blocks.cactus)  // cactus
//					|| (Pneumaticcraft.isSeed(item)) //pnumaticraft plants
				) && (!OreHelper.testOre("treeSapling", item)) 
			) {
>>>>>>> First Version where it looks like everything is working.....
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


	//ISided Stuff
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if ( (slot == SLOT_SEEDS) && (isPlantable(stack)) ) {
    		return true;
    	}
		return super.isItemValidForSlot(slot, stack);
	}
}
