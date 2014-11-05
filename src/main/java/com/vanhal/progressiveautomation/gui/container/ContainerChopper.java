package com.vanhal.progressiveautomation.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.oredict.OreDictionary;

import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.entities.chopper.TileChopper;
import com.vanhal.progressiveautomation.gui.slots.SlotDictionary;
import com.vanhal.progressiveautomation.gui.slots.SlotItem;
import com.vanhal.progressiveautomation.gui.slots.SlotSaplings;
import com.vanhal.progressiveautomation.gui.slots.SlotTool;
import com.vanhal.progressiveautomation.gui.slots.SlotUpgrades;
import com.vanhal.progressiveautomation.items.ItemRFEngine;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerChopper extends BaseContainer {
	protected ItemStack updateType;

	public ContainerChopper(InventoryPlayer inv, TileEntity entity) {
		super((BaseTileEntity)entity, 11, 52);
		TileChopper chopper = (TileChopper) entity;
		
		updateType = ToolHelper.getUpgradeType(chopper.getUpgradeLevel());

		//add slots
		this.addSlotToContainer(new SlotSaplings(chopper, chopper.SLOT_SAPLINGS, 11, 16)); //saplings
		this.addSlotToContainer(new SlotTool(ToolHelper.TYPE_AXE,  chopper.getUpgradeLevel(), chopper, chopper.SLOT_AXE, 49, 52)); //axe
		this.addSlotToContainer(new SlotUpgrades(chopper.getUpgradeLevel(), chopper, chopper.SLOT_UPGRADE, 76, 52)); //upgrades

		//output slots
		addInventory(chopper, chopper.SLOT_INVENTORY_START, 112, 16, 3, 3);

		addPlayerInventory(inv);
	}
	
	/* deal with updates */
	protected int lastUpgrades = -1;
	
	public void sendUpdates(ICrafting i) {
		TileChopper chopper = (TileChopper) entity;
		
		if (lastUpgrades != chopper.getUpgrades()) {
			lastUpgrades = chopper.getUpgrades();
			i.sendProgressBarUpdate(this, 4, lastUpgrades);
		}
		
		if (chopper.isChopping() != chopper.chopping) {
			chopper.chopping = chopper.isChopping();
			i.sendProgressBarUpdate(this, 5, (chopper.chopping)?1:0);
		}

		if (chopper.isPlanting() != chopper.planting) {
			chopper.planting = chopper.isPlanting();
			i.sendProgressBarUpdate(this, 6, (chopper.planting)?1:0);
		}
	}
	
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int i, int value) {
		super.updateProgressBar(i, value);
		TileChopper chopper = (TileChopper) entity;
		if (i==4) {
			chopper.setUpgrades(value);
		} else if (i==5) {
			chopper.chopping = (value==1);
		} else if (i==6) {
			chopper.planting = (value==1);
		}
	}
	
}
