package com.vanhal.progressiveautomation.entities.planter;

import java.util.List;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.compat.ModHelper;
import com.vanhal.progressiveautomation.entities.UpgradeableTileEntity;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.util.Point2I;
import com.vanhal.progressiveautomation.util.Point3I;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

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
		setExtDirection(EnumFacing.DOWN);

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

			// Pause if we're full and told to
			if (isFull()) return;

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
		BlockPos currentPosition = currentBlock.toPosition();;
		
		IBlockState currentState = worldObj.getBlockState(currentPosition);
		Block actualBlock = currentState.getBlock();
		
		if (slots[SLOT_HOE]!=null) {
			List<ItemStack> items = ModHelper.harvestPlant(currentBlock, actualBlock, currentState, worldObj);
			if (items!=null) {
				for (ItemStack item : items) {
					addToInventory(item);
				}
				damageHoe(currentBlock);
			}
		}
	}
	
	protected boolean plantSeed(int n, boolean doAction) {
		if (slots[SLOT_SEEDS]!=null) {
			if (slots[SLOT_SEEDS].stackSize>0) {
				Point3I point = getPoint(n);
				
				if (ModHelper.shouldHoe(slots[SLOT_SEEDS])) {
					hoeGround(n);
				}
				
				if (ModHelper.placeSeed(worldObj, slots[SLOT_SEEDS], point, doAction)) {
					if (doAction) {
						slots[SLOT_SEEDS].stackSize--;
						 if (slots[SLOT_SEEDS].stackSize==0) {
							 slots[SLOT_SEEDS] = null;
						 }
					}
					return true;
				}
			}
		}
		return false;
	}

	protected boolean checkPlant(int n) {
		Point3I plantPoint = getPoint(n);
		IBlockState blockState = worldObj.getBlockState(plantPoint.toPosition());
		Block plantBlock = blockState.getBlock();

		return ModHelper.isGrown(plantPoint, plantBlock, blockState, worldObj);
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
		Block dirtBlock = dirtState.getBlock();
		
		if (reverse) {
			if (dirtBlock == Blocks.FARMLAND) {
				worldObj.setBlockState(dirtPosition, Blocks.DIRT.getDefaultState());
			}
		} else {
			if (slots[SLOT_HOE]!=null) {
				if (plantBlock.isAir(plantState, worldObj, plantPosition)) {
					if ((dirtBlock == Blocks.GRASS || dirtBlock == Blocks.DIRT)) {
						worldObj.setBlockState(dirtPosition, Blocks.FARMLAND.getDefaultState());
						damageHoe(dirtPoint);
					}
				}
			}
		}
	}
	
	protected void damageHoe(Point3I point) {
		if (ToolHelper.damageTool(slots[SLOT_HOE], worldObj, point.getX(), point.getY(), point.getZ())) {
			destroyTool(SLOT_HOE);
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

	@Override
	public boolean readyToBurn() {
		if (slots[SLOT_HOE]!=null) {
			if (doSearch()) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isPlantable(ItemStack item) {
		if ( (ModHelper.isPlantible(item)) && (!ModHelper.checkSapling(item)) ) {
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
	
	@Override
	protected Point3I adjustedSpiral(int n) {
		Point3I point = super.adjustedSpiral(n);
		point.setY(point.getY()+2);
		return point;
	}
}
