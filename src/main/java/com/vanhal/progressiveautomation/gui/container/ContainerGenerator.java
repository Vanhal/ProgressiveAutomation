package com.vanhal.progressiveautomation.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.oredict.OreDictionary;

import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.entities.chopper.TileChopper;
import com.vanhal.progressiveautomation.entities.generator.TileGenerator;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerGenerator extends BaseContainer {
	
	TileGenerator generator;

	public ContainerGenerator(InventoryPlayer inv, TileGenerator entity) {
		super((BaseTileEntity)entity, 23, 24, false);
		generator = entity;
		
		addPlayerInventory(inv, 53);
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
            	if (TileEntityFurnace.getItemBurnTime(stackInSlot)>0) {
            		if (!this.mergeItemStack(stackInSlot, entity.SLOT_FUEL, entity.SLOT_FUEL + 1, false)) {
            			return null;
            		}
            	} else {
            		return null;
            	}
            }

            if (stackInSlot.stackSize == 0) {
                    slotObject.putStack(null);
            } else {
                    slotObject.onSlotChanged();
            }
		}
		
		return stack;
	}
	
	
	//send updates
	int lastEnergy = -1;
	
	public void sendUpdates(ICrafting i) {
		if (lastEnergy != generator.getEnergyStored()) {
			lastEnergy = generator.getEnergyStored();
			i.sendProgressBarUpdate(this, 4, lastEnergy);
		}
	}
	
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int i, int value) {
		super.updateProgressBar(i, value);
		if (i==4) {
			generator.setEnergyStored(value);
		}
	}

}
