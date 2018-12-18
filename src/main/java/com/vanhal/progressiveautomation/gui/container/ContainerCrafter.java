package com.vanhal.progressiveautomation.gui.container;

import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.entities.crafter.TileCrafter;
import com.vanhal.progressiveautomation.gui.slots.SlotCraftingLocked;
import com.vanhal.progressiveautomation.gui.slots.SlotFalseCopy;
import com.vanhal.progressiveautomation.gui.slots.SlotRemoveOnly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.tileentity.TileEntity;

public class ContainerCrafter extends BaseContainer {
	public TileCrafter crafter;
	
	public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
	
	protected boolean init = false;

	public ContainerCrafter(InventoryPlayer inv, TileEntity entity) {
		super((BaseTileEntity)entity, 150, 52);
		crafter = (TileCrafter)entity;
		
		//add the slots
		for (int i = 0; i<9; i++) {
			craftMatrix.setInventorySlotContents(i, crafter.getStackInSlot(crafter.CRAFT_GRID_START+i));
		}
		init = true;
		
		this.addSlotToContainer(new SlotCraftingLocked(inv.player, craftMatrix, crafter, crafter.CRAFT_RESULT, 69, 34));
		addCraftingGrid(craftMatrix, 0, 10, 16, 3, 3); //crafting grid
		
		
		this.addSlotToContainer(new SlotRemoveOnly(crafter, crafter.OUTPUT_SLOT, 150, 16));
		
		//output slots
		addInventory(crafter, crafter.SLOT_INVENTORY_START, 92, 16, 3, 3);
		addPlayerInventory(inv);
		
		onCraftMatrixChanged(craftMatrix);
	}
	
	@Override
    public void onCraftMatrixChanged(IInventory inv) {
    	crafter.setInventorySlotContents(crafter.CRAFT_RESULT, 
    			CraftingManager.findMatchingRecipe(this.craftMatrix, crafter.getWorld()).getRecipeOutput());
    	if (init) updateTile();
    }
    
	@Override
	public void onContainerClosed(EntityPlayer player) {
		updateTile();
	}
	
	protected void updateTile() {
		for (int i = 0; i<9; i++) {
			crafter.setInventorySlotContents(crafter.CRAFT_GRID_START+i, craftMatrix.getStackInSlot(i));
		}
	}
	
	public void addCraftingGrid(IInventory inventory, int startSlot, int x, int y, int width, int height) {
		int i = 0;
		for(int h = 0; h < height; h++) {
			for(int w = 0; w < width; w++) {
				this.addSlotToContainer(new SlotFalseCopy(inventory, startSlot + i++, x + (w*18), y + (h*18)));
			}
		}
	}	
}
