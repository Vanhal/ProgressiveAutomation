package com.vanhal.progressiveautomation.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.entities.miner.TileMiner;
import com.vanhal.progressiveautomation.gui.slots.SlotBurn;
import com.vanhal.progressiveautomation.gui.slots.SlotItem;
import com.vanhal.progressiveautomation.gui.slots.SlotTool;
import com.vanhal.progressiveautomation.gui.slots.SlotUpgrades;
import com.vanhal.progressiveautomation.items.ItemRFEngine;
import com.vanhal.progressiveautomation.items.ItemUpgrade;
import com.vanhal.progressiveautomation.items.PAItems;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerMiner extends BaseContainer {
	protected ItemStack updateType;

	public ContainerMiner(InventoryPlayer inv, TileEntity entity) {
		super((BaseTileEntity)entity, 11, 52);
		TileMiner miner = (TileMiner) entity;
		
		//ProgressiveAutomation.logger.info("Mining Level: "+miner.getMiningLevel());
		
		updateType = ToolHelper.getUpgradeType(miner.getUpgradeLevel());

		//add slots
		this.addSlotToContainer(new SlotItem(new ItemStack(Blocks.cobblestone), miner, 1, 11, 16)); //cobble
		this.addSlotToContainer(new SlotTool(ToolHelper.TYPE_PICKAXE,  miner.getUpgradeLevel(), miner, miner.SLOT_PICKAXE, 37, 52)); //pickaxe
		this.addSlotToContainer(new SlotTool(ToolHelper.TYPE_SHOVEL, miner.getUpgradeLevel(), miner, miner.SLOT_SHOVEL, 63, 52)); //shovel
		this.addSlotToContainer(new SlotUpgrades(miner.getUpgradeLevel(), miner, miner.SLOT_UPGRADE, 89, 52)); //upgrades

		//output slots
		addInventory(miner, miner.SLOT_INVENTORY_START, 112, 16, 3, 3);

		addPlayerInventory(inv);
	}
}
