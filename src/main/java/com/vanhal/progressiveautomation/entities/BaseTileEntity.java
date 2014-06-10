package com.vanhal.progressiveautomation.entities;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.ProgressiveAutomation;

import java.util.Random;

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

public class BaseTileEntity extends TileEntity implements ISidedInventory {
	protected ItemStack[] slots;
	protected int progress = 0;
	protected int burnLevel = 0;
	
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
		}
	}
	
	//deal with NBT
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		
		NBTTagList contents = new NBTTagList();
		for (int i = 0; i < slots.length; i++) {
			ItemStack stack = slots[i];
			if (stack != null) {
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
							if (slots[SLOT_FUEL].getItem() instanceof ItemBucket) {
								slots[SLOT_FUEL] = new ItemStack(Items.bucket);
							} else {
								slots[SLOT_FUEL].stackSize--;
								if (slots[SLOT_FUEL].stackSize==0) {
									slots[SLOT_FUEL] = null;
								}
							}
						}
					}
				}
			} else {
				//ProgressiveAutomation.logger.info("Ticks Left for fuel: "+progress);
				progress--;
				if (progress<=0) {
					burnLevel = progress = 0;
				}
			}
			
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
		if (slots[slot] != null) {
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
		if ( (SLOT_INVENTORY_START>=0) && (slot>=SLOT_INVENTORY_START) ) {
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
		return TileEntityFurnace.getItemBurnTime(item)/PAConfig.fuelCost;
	}
	
	public boolean isFuel() {
		return (getBurnTime()>0);
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
					} else if (slots[moveTo].stackSize < slots[moveTo].getMaxStackSize()) {
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
		if (worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof IInventory) {
			IInventory externalInv = (IInventory) worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
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
}
