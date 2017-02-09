package com.vanhal.progressiveautomation.gui.container;

import com.vanhal.progressiveautomation.blocks.network.NetworkHandler;
import com.vanhal.progressiveautomation.blocks.network.PartialTileNBTUpdateMessage;
import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.gui.slots.SlotBurn;
import com.vanhal.progressiveautomation.gui.slots.SlotPower;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class BaseContainer extends Container {
	public static final byte TICKS_PER_MESSAGE = 5;
	
	protected BaseTileEntity entity;
	
	public BaseContainer(BaseTileEntity inEntity, int x, int y) {
		this(inEntity, x, y, true);
	}
	
	public BaseContainer(BaseTileEntity inEntity, int x, int y, boolean canPower) {
		entity = inEntity;
		if (canPower) {
			this.addSlotToContainer(new SlotPower(entity, entity.SLOT_FUEL, x, y)); //burnable and power
		} else {
			this.addSlotToContainer(new SlotBurn(entity, entity.SLOT_FUEL, x, y)); //just burnable
		}
	}
	
	public BaseContainer(BaseTileEntity inEntity) { //allow to create a GUI without default fuel slots
		entity = inEntity;
	}
	
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		if (!entity.isLooked()) {
			entity.setLooked();
		}
	}
	
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
		ItemStack stack = null;
		Slot slotObject = (Slot) inventorySlots.get(slot);
		
		if (slotObject!=null && slotObject.getHasStack()) {
			ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();
            
            if (slot < entity.getSizeInventory()) {
                if (!this.mergeItemStack(stackInSlot, entity.getSizeInventory(), inventorySlots.size(), true)) {
                	return null;
                }
            } else {
            	boolean foundSlot = false;
            	for (Object targetSlot: inventorySlots) {
            		if (targetSlot instanceof Slot) {
            			Slot theTargetSlot = (Slot) targetSlot;
            			int slotNum = theTargetSlot.slotNumber;
            			if (slot!=slotNum) {
	            			if ( (slotNum >= entity.SLOT_INVENTORY_START) && (entity.SLOT_INVENTORY_START != entity.SLOT_INVENTORY_END) && (entity.SLOT_INVENTORY_START != -1) ) {
	            				if (!this.mergeItemStack(stackInSlot, entity.SLOT_INVENTORY_START, entity.SLOT_INVENTORY_END + 1, false)) {
	                         		return null;
	                         	}
	            				foundSlot = true;
	            				break;
	            			} else if ( (theTargetSlot.isItemValid(stackInSlot) ) 
	            					&& (theTargetSlot.getSlotStackLimit()>1) 
	            					&& ( (!theTargetSlot.getHasStack()) || (theTargetSlot.getStack().stackSize < theTargetSlot.getSlotStackLimit()) ) ){
	            				if (!this.mergeItemStack(stackInSlot, slotNum, slotNum+1, false)) {
	                    			return null;
	                    		}
	            				foundSlot = true;
	                    		break;
	            			}
            			}
            		}
            	}
            	if (!foundSlot) return null;
            }
            
            

            if (stackInSlot.stackSize == 0) {
                    slotObject.putStack(null);
            } else {
                    slotObject.onSlotChanged();
            }
		}
		
		return stack;
	}
	
	/* Add the players Inventory */
	public void addPlayerInventory(InventoryPlayer inv) {
		addPlayerInventory(inv, 8, 84);
	}
	
	public void addPlayerInventory(InventoryPlayer inv, int y) {
		addPlayerInventory(inv, 8, y);
	}
	
	public void addPlayerInventory(InventoryPlayer inv, int x, int y) {
		for(int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
					this.addSlotToContainer(new Slot(inv, j + (i+1)*9, x + j*18, y + i*18));
				}
		}
		for (int i = 0; i < 9; i++) {
			this.addSlotToContainer(new Slot(inv, i, 8 + i*18, y+58));
		}
	}
	
	/* Adds another inventory to the container */
	public void addInventory(IInventory inventory, int startSlot, int x, int y, int width, int height) {
		int i = 0;
		for(int h = 0; h < height; h++) {
			for(int w = 0; w < width; w++) {
				this.addSlotToContainer(new Slot(inventory, startSlot + i++, x + (w*18), y + (h*18)));
			}
		}
	}
	
	
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		if (entity.isDirty() && entity.getWorld().getWorldTime() % TICKS_PER_MESSAGE == 0) {
			PartialTileNBTUpdateMessage message = entity.getPartialUpdateMessage();
			
			for (Object o : this.listeners) {
				if (o instanceof EntityPlayerMP) {
					EntityPlayerMP player = (EntityPlayerMP) o;
					NetworkHandler.sendToPlayer(message, player);
				}
			}
		}		
	}
}
