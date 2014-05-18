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
import com.vanhal.progressiveautomation.entities.TileMiner;
import com.vanhal.progressiveautomation.gui.slots.SlotBurn;
import com.vanhal.progressiveautomation.gui.slots.SlotItem;
import com.vanhal.progressiveautomation.gui.slots.SlotTool;
import com.vanhal.progressiveautomation.items.PAItems;
import com.vanhal.progressiveautomation.ref.ToolInfo;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerMiner extends BaseContainer {
	protected ItemStack updateType;

	public ContainerMiner(InventoryPlayer inv, TileEntity entity) {
		super((BaseTileEntity)entity, 11, 52);
		TileMiner miner = (TileMiner) entity;
		
		ProgressiveAutomation.logger.info("Mining Level: "+miner.getMiningLevel());
		
		if (miner.getMiningLevel()==ToolInfo.LEVEL_DIAMOND) {
			updateType = new ItemStack(PAItems.diamondUpgrade);
		} else if (miner.getMiningLevel()==ToolInfo.LEVEL_IRON) {
			updateType = new ItemStack(PAItems.ironUpgrade);
		} else if (miner.getMiningLevel()==ToolInfo.LEVEL_GOLD) {
			updateType = new ItemStack(PAItems.ironUpgrade);
		} else if (miner.getMiningLevel()==ToolInfo.LEVEL_STONE) {
			updateType = new ItemStack(PAItems.stoneUpgrade);
		} else {
			updateType = new ItemStack(PAItems.woodUpgrade);
		}

		//add slots
		this.addSlotToContainer(new SlotItem(new ItemStack(Blocks.cobblestone), miner, 1, 11, 16)); //cobble
		this.addSlotToContainer(new SlotTool(ToolInfo.TYPE_PICKAXE,  miner.getMiningLevel(), miner, 2, 37, 52)); //pickaxe
		this.addSlotToContainer(new SlotTool(ToolInfo.TYPE_SHOVEL, miner.getMiningLevel(), miner, 3, 63, 52)); //shovel
		this.addSlotToContainer(new SlotItem(updateType, miner, 4, 89, 52)); //upgrades

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
            	if (stackInSlot.isItemEqual(new ItemStack(Blocks.cobblestone))) {
            		if (!this.mergeItemStack(stackInSlot, 1, entity.getSizeInventory(), false)) {
            			return null;
            		}
            	} else if (TileEntityFurnace.getItemBurnTime(stackInSlot)>0) {
            		if (!this.mergeItemStack(stackInSlot, 0, entity.getSizeInventory(), false)) {
            			return null;
            		}
            	} else if (ToolInfo.getType(stackInSlot.getItem()) == ToolInfo.TYPE_PICKAXE ) {
            		if (!this.mergeItemStack(stackInSlot, 2, entity.getSizeInventory(), false)) {
            			return null;
            		}
            	} else if (ToolInfo.getType(stackInSlot.getItem()) == ToolInfo.TYPE_SHOVEL ) {
             		if (!this.mergeItemStack(stackInSlot, 3, entity.getSizeInventory(), false)) {
             			return null;
             		}
             	} else if (stackInSlot.isItemEqual(updateType)) {
             		if (!this.mergeItemStack(stackInSlot, 4, entity.getSizeInventory(), false)) {
             			return null;
             		}
             	} else {
             		if (!this.mergeItemStack(stackInSlot, 5, entity.getSizeInventory(), true)) {
             			return null;
             		}
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
