package com.vanhal.progressiveautomation.entities;

import com.vanhal.progressiveautomation.ProgressiveAutomation;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;

public class BaseTileEntity extends TileEntity implements ISidedInventory {
	protected ItemStack[] slots;
	protected int progress = 0;
	protected int burnLevel = 0;
	
	public BaseTileEntity(int numSlots) {
		slots = new ItemStack[numSlots+1];
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
					if (slots[0]!=null) {
						if (isFuel()) {
							burnLevel = progress = getBurnTime();
							ProgressiveAutomation.logger.info("Burning fuel for: "+getBurnTime());
							slots[0].stackSize--;
							if (slots[0].stackSize==0) {
								slots[0] = null;
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
		return null;
	}

	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		if (slots[slot] != null) {
			int availSpace = this.getInventoryStackLimit() - slots[slot].stackSize;
			if (availSpace>0) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}

	public boolean canExtractItem(int slot, ItemStack stack, int side) {
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
			float done = (burnLevel - progress);
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
		return TileEntityFurnace.getItemBurnTime(slots[0]);
	}
	
	public boolean isFuel() {
		return (getBurnTime()>0);
	}
	
	
}
