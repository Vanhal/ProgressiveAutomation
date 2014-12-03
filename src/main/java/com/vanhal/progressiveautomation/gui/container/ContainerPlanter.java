package com.vanhal.progressiveautomation.gui.container;

import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.entities.planter.TilePlanter;
import com.vanhal.progressiveautomation.gui.slots.SlotDictionary;
import com.vanhal.progressiveautomation.gui.slots.SlotItem;
import com.vanhal.progressiveautomation.gui.slots.SlotPlantable;
import com.vanhal.progressiveautomation.gui.slots.SlotTool;
import com.vanhal.progressiveautomation.gui.slots.SlotUpgrades;
import com.vanhal.progressiveautomation.items.ItemRFEngine;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.oredict.OreDictionary;

public class ContainerPlanter extends BaseContainer {
	
	TilePlanter planter;
	protected ItemStack updateType;

	public ContainerPlanter(InventoryPlayer inv, TileEntity entity) {
		super((BaseTileEntity)entity, 11, 52);
		planter = (TilePlanter) entity;
		
		updateType = ToolHelper.getUpgradeType(planter.getUpgradeLevel());
		
		//add slots
		this.addSlotToContainer(new SlotPlantable(planter, planter.SLOT_SEEDS, 11, 16)); //seeds
		this.addSlotToContainer(new SlotTool(ToolHelper.TYPE_HOE,  planter.getUpgradeLevel(), planter, planter.SLOT_HOE, 49, 52)); //hoe
		this.addSlotToContainer(new SlotUpgrades(planter.getUpgradeLevel(), planter, planter.SLOT_UPGRADE, 76, 52)); //upgrades

		//output slots
		addInventory(planter, planter.SLOT_INVENTORY_START, 112, 16, 3, 3);

		addPlayerInventory(inv);
	}
	
}
