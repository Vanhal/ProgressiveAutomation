package com.vanhal.progressiveautomation.entities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class BaseTileEntity extends TileEntity implements ISidedInventory {
	protected ItemStack[] slots;
	
	public BaseTileEntity(int numSlots) {
		slots = new ItemStack[numSlots];
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
	}
	
	/* Inventory methods */
	public int getSizeInventory() {
		return 0;
	}

	public ItemStack getStackInSlot(int slot) {
		return null;
	}

	public ItemStack decrStackSize(int slot, int amt) {
		return null;
	}

	public ItemStack getStackInSlotOnClosing(int slot) {
		return null;
	}

	public void setInventorySlotContents(int slot, ItemStack stack) {

	}

	public String getInventoryName() {
		return null;
	}

	public boolean hasCustomInventoryName() {
		return false;
	}

	public int getInventoryStackLimit() {
		return 0;
	}

	public boolean isUseableByPlayer(EntityPlayer var1) {
		return false;
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
		return false;
	}

	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		return false;
	}
	
}
