package com.vanhal.progressiveautomation.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.entities.TileMiner;
import com.vanhal.progressiveautomation.gui.slots.SlotBurn;
import com.vanhal.progressiveautomation.gui.slots.SlotItem;
import com.vanhal.progressiveautomation.gui.slots.SlotTool;
import com.vanhal.progressiveautomation.items.PAItems;
import com.vanhal.progressiveautomation.ref.ToolInfo;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerMiner extends BaseContainer {

	public ContainerMiner(InventoryPlayer inv, TileEntity entity) {
		super((BaseTileEntity)entity);
		TileMiner miner = (TileMiner) entity;
		

		//add slots
		this.addSlotToContainer(new SlotItem(new ItemStack(Blocks.cobblestone), miner, 0, 11, 16)); //cobble
		this.addSlotToContainer(new SlotBurn(miner, 1, 11, 52)); //burnable
		this.addSlotToContainer(new SlotTool(ToolInfo.TYPE_PICKAXE, ToolInfo.LEVEL_STONE, miner, 2, 37, 52)); //pickaxe
		this.addSlotToContainer(new SlotTool(ToolInfo.TYPE_SHOVEL, ToolInfo.LEVEL_STONE, miner, 3, 63, 52)); //shovel
		this.addSlotToContainer(new SlotItem(new ItemStack(PAItems.stoneUpgrade), miner, 4, 89, 52)); //upgrades

		//output slots
		this.addSlotToContainer(new Slot(miner, 5, 112, 16));
		this.addSlotToContainer(new Slot(miner, 6, 130, 16));
		this.addSlotToContainer(new Slot(miner, 7, 148, 16));
		this.addSlotToContainer(new Slot(miner, 8, 112, 34));
		this.addSlotToContainer(new Slot(miner, 9, 130, 34));
		this.addSlotToContainer(new Slot(miner, 10, 148, 34));
		this.addSlotToContainer(new Slot(miner, 11, 112, 52));
		this.addSlotToContainer(new Slot(miner, 12, 130, 52));
		this.addSlotToContainer(new Slot(miner, 13, 148, 52));

		addPlayerInventory(inv);
	}
	
	
	/* deal with updates */
	protected int lastMinedBlocks = -1;
	protected int lastMineBlocks = -1;
	
	public void sendUpdates(ICrafting i) {
		TileMiner miner = (TileMiner) entity;
		if (lastMineBlocks != miner.getMineBlocks()) {
			lastMineBlocks = miner.getMineBlocks();
			i.sendProgressBarUpdate(this, 2, lastMineBlocks);
		}
		
		if (lastMinedBlocks != miner.getMinedBlocks()) {
			lastMinedBlocks = miner.getMinedBlocks();
			i.sendProgressBarUpdate(this, 3, lastMinedBlocks);
		}
	}
	
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int i, int value) {
		super.updateProgressBar(i, value);
		if (i==2) {
			((TileMiner) entity).setMineBlocks(value);
		} else if (i==3) {
			((TileMiner) entity).setMinedBlocks(value);
		}
	}


}
