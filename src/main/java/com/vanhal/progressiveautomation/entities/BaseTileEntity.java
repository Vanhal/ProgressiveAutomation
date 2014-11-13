package com.vanhal.progressiveautomation.entities;

import cofh.api.energy.IEnergyHandler;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.items.ItemRFEngine;
import com.vanhal.progressiveautomation.util.BlockHelper;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;

public class BaseTileEntity extends TileEntity implements ISidedInventory, IEnergyHandler {
	protected ItemStack[] slots;
	protected int progress = 0;
	protected int burnLevel = 0;
	
	/**
	 * Direction to auto output items to
	 */
	public ForgeDirection extDirection = ForgeDirection.UP;
	
	protected Random RND = new Random();
	
	//inventory slots variables
	public int SLOT_FUEL = 0;
	public int SLOT_PICKAXE = -1;
	public int SLOT_SHOVEL = -1;
	public int SLOT_AXE = -1;
	public int SLOT_SWORD = -1;
	public int SLOT_HOE = -1;
	public int SLOT_UPGRADE = -1;
	public int SLOT_INVENTORY_START = -1;
	public int SLOT_INVENTORY_END = -1;
	
	public BaseTileEntity(int numSlots) {
		slots = new ItemStack[numSlots+1];
		if (numSlots > 9) {
			SLOT_INVENTORY_START = numSlots - 8;
			SLOT_INVENTORY_END = numSlots;
			//ProgressiveAutomation.logger.info("Start: "+SLOT_INVENTORY_START+" End: "+SLOT_INVENTORY_END);
		} else {
			SLOT_INVENTORY_START = SLOT_INVENTORY_END = numSlots;
		}
	}
	
	//deal with NBT
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		
		NBTTagList contents = new NBTTagList();
		for (int i = 0; i < slots.length; i++) {
			if (slots[i] != null) {
				ItemStack stack = slots[i];
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte)i);
				stack.writeToNBT(tag);
				contents.appendTag(tag);
			}
		}
		nbt.setTag("Contents", contents);
		
		nbt.setInteger("Progress", progress);
		nbt.setInteger("BurnLevel", burnLevel);
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		
		NBTTagList contents = nbt.getTagList("Contents", 10);
		for (int i = 0; i < contents.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) contents.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot < slots.length) {
				slots[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
		
		progress = nbt.getInteger("Progress");
		burnLevel = nbt.getInteger("BurnLevel");
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (!worldObj.isRemote) {
			if (!isBurning()) {
				if (readyToBurn()) {
					if (slots[SLOT_FUEL]!=null) {
						if (isFuel()) {
							burnLevel = progress = getBurnTime();
							
							if (slots[SLOT_FUEL].getItem().hasContainerItem(slots[SLOT_FUEL])) {
								
								slots[SLOT_FUEL] = slots[SLOT_FUEL].getItem().getContainerItem(slots[SLOT_FUEL]);
							} else {
								slots[SLOT_FUEL].stackSize--;
								if (slots[SLOT_FUEL].stackSize==0) {
									slots[SLOT_FUEL] = null;
								}
							}
						} else if (hasEngine()) {
							if (useEnergy(PAConfig.rfCost, false) > 0) {
								//consumed a tick worth of energy
								burnLevel = progress = 1;
							}
						}
					}
				}
			} else {
				//ProgressiveAutomation.logger.info("Ticks Left for fuel: "+progress);
				progress--;
				if (progress<=0) {
					burnLevel = progress = 0;
					if ( (readyToBurn()) && (hasEngine()) ) {
						if (useEnergy(PAConfig.rfCost, false) > 0) {
							//consumed a tick worth of energy
							burnLevel = progress = 1;
						}
					}
				}
			}
			checkForPowerChange();
		}
	}
	
	public int getProgress() {
		return progress;
	}
	
	public void setProgress(int value) {
		progress = value;
	}
	
	public int getBurnLevel() {
		return burnLevel;
	}
	
	public void setBurnLevel(int value) {
		burnLevel = value;
	}
	
	/* Inventory methods */
	public int getSizeInventory() {
		return slots.length;
	}

	public ItemStack getStackInSlot(int slot) {
		return slots[slot];
	}

	public ItemStack decrStackSize(int slot, int amt) {
		if (slots[slot] != null) {
			ItemStack newStack;
			if (slots[slot].stackSize <= amt) {
				newStack = slots[slot];
				slots[slot] = null;
			} else {
				newStack = slots[slot].splitStack(amt);
				if (slots[slot].stackSize == 0) {
					slots[slot] = null;
				}
			}
			return newStack;
		}
		return null;
	}

	public ItemStack getStackInSlotOnClosing(int slot) {
		if (slots[slot]!=null) {
			ItemStack stack = slots[slot];
			slots[slot] = null;
			return stack;
		}
		return null;
	}

	public void setInventorySlotContents(int slot, ItemStack stack) {
		slots[slot] = stack;
		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}
	}

	public String getInventoryName() {
		return null;
	}

	public boolean hasCustomInventoryName() {
		return false;
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this &&
		 player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}

	public void openInventory() { }

	public void closeInventory() { }

	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}

	public int[] getAccessibleSlotsFromSide(int var1) {
		int[] output = new int[slots.length];
		for (int i=0; i<slots.length; i++) {
			output[i] = i;
		}
		return output;
	}

	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		if ( (slot<0) || (slot>SLOT_INVENTORY_END) ) return false;
		if ( (slots[slot] != null) && (slots[slot].isItemEqual(stack)) ) {
			int availSpace = this.getInventoryStackLimit() - slots[slot].stackSize;
			if (availSpace>0) {
				return true;
			}
		} else if (isItemValidForSlot(slot, stack)) {
			return true;
		}
		return false;
	}

	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		if ( (slot>=SLOT_INVENTORY_START) && (slot<=SLOT_INVENTORY_END) ) {
			return true;
		}
		return false;
	}
	
	public boolean readyToBurn() {
		return true;
	}
	
	public boolean isBurning() {
		return (progress>0);
	}
	
	public float getPercentDone() {
		if ( (isBurning()) && (burnLevel>0) ) {
			float done = (float)progress;//(burnLevel - progress);
			done = done/(float)burnLevel;
			return done;
		} else {
			return 0;
		}
	}
	
	public int getScaledDone(int scale) {
		return (int) Math.floor(scale * getPercentDone());
	}
	
	public int getBurnTime() {
		return getBurnTime(slots[0]);
	}
	
	public int getBurnTime(ItemStack item) {
		return TileEntityFurnace.getItemBurnTime(item) / PAConfig.fuelCost;
	}
	
	public boolean isFuel() {
		return (getBurnTime()>0);
	}
	
	public boolean hasFuel() {
		if (slots[SLOT_FUEL]!=null) {
			if (hasEngine()) {
				if (useEnergy(PAConfig.rfCost, true) > 0) {
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}
	
	/* do some checks for block specific items, must return -1 on failure */
	public int extraSlotCheck(int slot) {
		return -1;
	}
	
	/* Check the inventory, move any useful items to their correct slots */
	public void checkInventory() {
		if ( (SLOT_INVENTORY_START==-1) || (SLOT_INVENTORY_END==-1) ) return;
		for (int i = SLOT_INVENTORY_START; i <= SLOT_INVENTORY_END; i++) {
			if (slots[i]!=null) {
				int moveTo = extraSlotCheck(i);
				
				if (moveTo == -1) {
					if (getBurnTime(slots[i])>0) {
						if (slots[0]==null) {
							moveTo = 0;
						} else if (slots[i].isItemEqual(slots[0])) {
							moveTo = 0;
						}
					}
				}

				if (moveTo>=0) {
					if (slots[moveTo]==null) {
						slots[moveTo] = slots[i];
						slots[i] = null;
					} else if ( (slots[moveTo].stackSize < slots[moveTo].getMaxStackSize()) && (slots[moveTo].isItemEqual(slots[i])) ) {
						int avail = slots[moveTo].getMaxStackSize() - slots[moveTo].stackSize;
						if (avail >= slots[i].stackSize) {
							slots[moveTo].stackSize += slots[i].stackSize;
							slots[i] = null;
						} else {
							slots[i].stackSize -= avail;
							slots[moveTo].stackSize += avail;
						}
					}
				}
			}
		}
		//then check if there is any inventories on top of this block that we can output to
		if (BlockHelper.getAdjacentTileEntity(this, extDirection) instanceof IInventory) {
			IInventory externalInv = (IInventory) BlockHelper.getAdjacentTileEntity(this, extDirection);
			for (int i = SLOT_INVENTORY_START; i <= SLOT_INVENTORY_END; i++) {
				if (slots[i]!=null) {
					addtoExtInventory(externalInv, i);
				}
			}
		}
	}

	public boolean addtoExtInventory(IInventory inv, int fromSlot) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i)!=null) {
				if ( (inv.getStackInSlot(i).isItemEqual(slots[fromSlot])) && (inv.getStackInSlot(i).stackSize < inv.getStackInSlot(i).getMaxStackSize()) ) {
					int avail = inv.getStackInSlot(i).getMaxStackSize() - inv.getStackInSlot(i).stackSize;
					if (avail >= slots[fromSlot].stackSize) {
						inv.getStackInSlot(i).stackSize += slots[fromSlot].stackSize;
						slots[fromSlot] = null;
						return true;
					} else {
						slots[fromSlot].stackSize -= avail;
						inv.getStackInSlot(i).stackSize += avail;
					}
				}
			}
		}
		if ( (slots[fromSlot] != null) && (slots[fromSlot].stackSize>0) ) {
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				if ( (inv.getStackInSlot(i)==null) && (inv.isItemValidForSlot(i, slots[fromSlot])) ) {
					inv.setInventorySlotContents(i, slots[fromSlot]);
					slots[fromSlot] = null;
					return true;
				}
			}
		}
		return false;
	}

	public boolean addToInventory(ItemStack item) {
		if ( (SLOT_INVENTORY_START==-1) || (SLOT_INVENTORY_END==-1) ) return false;
		for (int i = SLOT_INVENTORY_START; i <= SLOT_INVENTORY_END; i++) {
			if (slots[i]!=null) {
				if (item!=null) {
					if ( (slots[i].isItemEqual(item)) && (slots[i].stackSize < slots[i].getMaxStackSize()) ) {
						int avail = slots[i].getMaxStackSize() - slots[i].stackSize;
						if (avail >= item.stackSize) {
							slots[i].stackSize += item.stackSize;
							item = null;
							return true;
						} else {
							item.stackSize -= avail;
							slots[i].stackSize += avail;
						}
					}
				}
			}
		}
		if ( (item != null) && (item.stackSize>0) ) {
			for (int i = SLOT_INVENTORY_START; i <= SLOT_INVENTORY_END; i++) {
				if (slots[i]==null) {
					slots[i] = item;
					item = null;
					return true;
				}
			}
		}
		if ( (item != null) && (item.stackSize==0) ) {
			item = null;
		}
		//if we still have an item, drop in on the ground
		if (item!=null) {
			EntityItem entItem = new EntityItem(worldObj, xCoord + 0.5f, yCoord + 1.5f, zCoord + 0.5f, item);
			entItem.delayBeforeCanPickup = 1;
			float f3 = 0.05F;
			entItem.motionX = (double)((float)worldObj.rand.nextGaussian() * f3);
			entItem.motionY = (double)((float)worldObj.rand.nextGaussian() * f3 + 0.2F);
			entItem.motionZ = (double)((float)worldObj.rand.nextGaussian() * f3);
			worldObj.spawnEntityInWorld(entItem);
		}

		return false;
	}
	
	
	//the following are for handling energy
	public boolean hasEngine() {
		if (slots[SLOT_FUEL]==null) return false;
		if (slots[SLOT_FUEL].getItem() instanceof ItemRFEngine) {
			return true;
		}
		return false;
	}
	
	protected ItemRFEngine getEngine() {
		if (hasEngine()) {
			return (ItemRFEngine) slots[SLOT_FUEL].getItem();
		}
		return null;
	}
	
	public boolean canConnectEnergy(ForgeDirection from) {
		if (worldObj.isRemote) return false;
		if (getEngine()==null) return false;
		else return true;
	}

	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		if (worldObj.isRemote) return 0;
		return addEnergy(maxReceive, simulate);
	}

	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return 0;
	}
	
	protected int useEnergy(int amount, boolean simulate) {
		ItemRFEngine engine = getEngine();
		if (getEngine()==null) return 0;
		
		int energyExtracted = Math.min(engine.getCharge(slots[SLOT_FUEL]), amount);

		if (energyExtracted!=amount) return 0;
		
		if (!simulate) {
			engine.addCharge(slots[SLOT_FUEL], (energyExtracted * -1));
		}
		return energyExtracted;
	}
	
	protected int addEnergy(int amount, boolean simulate) {
		ItemRFEngine engine = getEngine();
		if (getEngine()==null) return 0;
		
		int energyReceived = Math.min(engine.getMaxCharge() - engine.getCharge(slots[SLOT_FUEL]), Math.min(amount, PAConfig.rfRate));

		if (!simulate) {
			engine.addCharge(slots[SLOT_FUEL], energyReceived);
		}
		return energyReceived;
	}

	public int getEnergyStored(ForgeDirection from) {
		if (slots[SLOT_FUEL].getItem() instanceof ItemRFEngine) {
			return getEngine().getCharge(slots[SLOT_FUEL]);
		} else {
			return 0;
		}
	}

	public int getMaxEnergyStored(ForgeDirection from) {
		if (slots[SLOT_FUEL].getItem() instanceof ItemRFEngine) {
			return getEngine().getMaxCharge();
		} else {
			return 0;
		}
	}
	
	//this will just update the blocks around if the rfengine is added to a machine
	protected boolean lastEngine = false;
	protected void checkForPowerChange() {
		if ( ( (!lastEngine) && (hasEngine()) ) ||
				( (lastEngine) && (!hasEngine()) ) ) {
			lastEngine = hasEngine();
			notifyUpdate();
		}
	}
	
	protected void notifyUpdate() {
		Block minerBlock = worldObj.getBlock(xCoord, yCoord, zCoord);
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, minerBlock);
	}
}
