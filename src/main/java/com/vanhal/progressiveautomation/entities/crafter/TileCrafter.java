package com.vanhal.progressiveautomation.entities.crafter;

import java.util.ArrayList;
import java.util.List;

import com.vanhal.progressiveautomation.entities.UpgradeableTileEntity;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.ref.WrenchModes;
import com.vanhal.progressiveautomation.util.BlockHelper;
import com.vanhal.progressiveautomation.util.OreHelper;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileCrafter extends UpgradeableTileEntity {
	
	public int CRAFT_GRID_START = 1;
	public int CRAFT_GRID_END = 9;
	public int CRAFT_RESULT = 10;
	public int OUTPUT_SLOT = 11;
	
	public int craftTime = 100;
	public int currentTime = 0;

	public TileCrafter() {
		super(20);
		setUpgradeLevel(ToolHelper.LEVEL_WOOD);
		setCraftTime(120);
	}
	
	public void setCraftTime(int time) {
		this.craftTime = time;
	}
	
	public void update() {
		super.update();
		if (!worldObj.isRemote) {
			outputItems();
			
			if (isBurning()) {
				if (readyToBurn()) {
					if ( (currentTime > 0) && (currentTime<=craftTime) ) {
						currentTime++;
						addPartialUpdate("currentTime", currentTime);
					} else if (currentTime>craftTime) {
						currentTime = 0;
						addPartialUpdate("currentTime", currentTime);
						if (consumeIngredients()) {
							//create an item, put it in the right slot
							if (slots[OUTPUT_SLOT]!=null) {
								if (canCraft()) {
									slots[OUTPUT_SLOT].stackSize += slots[CRAFT_RESULT].stackSize;
								}
							} else {
								slots[OUTPUT_SLOT] = slots[CRAFT_RESULT].copy();
							}
						}
					} else if (currentTime != 1){
						currentTime = 1;
						addPartialUpdate("currentTime", currentTime);
					}
				} else if (currentTime != 0){
					currentTime = 0;
					addPartialUpdate("currentTime", currentTime);
				}
			}
		}
	}
	
	protected void outputItems() {
		for (int x = 0; x < 6; x++) {
			if (sides[x] == WrenchModes.Mode.Output) {
				if (slots[OUTPUT_SLOT]!=null) {
					EnumFacing testSide = EnumFacing.getFront(x);
					TileEntity tile = BlockHelper.getAdjacentTileEntity(this, testSide);
					if (tile != null) {
						if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite())) {
							IItemHandler inv = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
							addtoExtInventory(inv, OUTPUT_SLOT);
						} else if (tile instanceof ISidedInventory) {
							ISidedInventory externalInv = (ISidedInventory) tile;
							addtoSidedExtInventory(externalInv, OUTPUT_SLOT);
						} else if (tile instanceof IInventory) {
							IInventory externalInv = (IInventory) tile;
							addtoExtInventory(externalInv, OUTPUT_SLOT);
						}
					}
				}
			}
		}
	}

	public boolean readyToBurn() {
		if ( (validRecipe()) && (canCraft()) ) {
			if (hasIngredients()) {
				return true;
			}
		}
		return false;
	}
	
	public void writeCommonNBT(NBTTagCompound nbt) {
		super.writeCommonNBT(nbt);
		//save the current chopping time
		nbt.setInteger("currentTime", currentTime);
	}

	public void readCommonNBT(NBTTagCompound nbt) {
		super.readCommonNBT(nbt);
		//load the current chopping time
		if (nbt.hasKey("currentTime")) currentTime = nbt.getInteger("currentTime");
	}
	
	//test to see if the output slot can accept the resulting craft
	public boolean canCraft() {
		return ( (slots[OUTPUT_SLOT]==null) || 
			( (slots[OUTPUT_SLOT].isItemEqual(slots[CRAFT_RESULT])) && 
			((slots[OUTPUT_SLOT].stackSize + slots[CRAFT_RESULT].stackSize) <= slots[OUTPUT_SLOT].getMaxStackSize()) ) 
		);
	}
	
	public boolean validRecipe() {
		return (slots[CRAFT_RESULT]!=null);
	}
	
	public boolean hasIngredients() {
		return checkIngredients(false);
	}
	
	public boolean consumeIngredients() {
		if (checkIngredients(false)) {
			return checkIngredients(true);
		} else {
			return false;
		}
	}
	
	//need to deal with ore dic as well :(
	protected boolean checkIngredients(boolean consume) {
		List<ItemStack> required = new ArrayList<ItemStack>();
		//get the list of things we need
		for (int i = CRAFT_GRID_START; i <= CRAFT_GRID_END; i++) {
			if (this.slots[i] != null) {
				required.add(slots[i].copy());
			}
		}
		if (required.size()==0) return false;
		
		//go through the inventory and see if anything matches up with the requirements
		for (int i = SLOT_INVENTORY_START; i <= SLOT_INVENTORY_END; i++) {
			if (slots[i]!=null) {
				int amtItems = slots[i].stackSize;
				for (int j = 0; j < required.size(); j++) {
					if (required.get(j)!=null) {
						if ( OreHelper.ItemOreMatch(required.get(j), slots[i]) ) {
							if (amtItems>0) {
								amtItems--;
								required.set(j, null);
							}
						}
					}
				}
				//actually consume the item
				if (consume) {
					if (amtItems<=0) {
						if (slots[i].getItem().hasContainerItem(slots[i])) {
							ItemStack container = slots[i].getItem().getContainerItem(slots[i]);
							this.addToInventory(new ItemStack(container.getItem(), slots[i].stackSize, container.getItemDamage()));
						}
						slots[i] = null;
					} else if ( (slots[i]!=null) && (slots[i].stackSize != amtItems) ) {
						if (slots[i].getItem().hasContainerItem(slots[i])) {
							ItemStack container = slots[i].getItem().getContainerItem(slots[i]);
							this.addToInventory(new ItemStack(container.getItem(), slots[i].stackSize - amtItems, container.getItemDamage()));
						}
						slots[i].stackSize = amtItems;
					}
				}
			}
		}
		
		//check to see if it's all good
		for (int j = 0; j < required.size(); j++) {
			if (required.get(j)!=null) {
				required = null;
				return false;
			}
		}
		required = null;
		return true;
	}
	
	public float getPercentCrafted() {
		if ( currentTime != 0 ) {
			float done = (float)(craftTime - currentTime);
			done = done/(float)craftTime;
			done = 1 - done;
			return done;
		} else {
			return 0;
		}
	}
	
	public int getScaledCrafted(int scale) {
		return (int) Math.floor(scale * getPercentCrafted());
	}
	

	/* ISided Stuff */
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if ( (slot >= SLOT_INVENTORY_START) && (slot <= SLOT_INVENTORY_END) ) {
    		return true;
    	}
		return super.isItemValidForSlot(slot, stack);
	}
	
	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing side) {
		if ( (side == null) && (slot==OUTPUT_SLOT) ) return true;
		if (side == null) return false;
		if (sides[side.ordinal()] == WrenchModes.Mode.Disabled) return false;
		if ( (sides[side.ordinal()] == WrenchModes.Mode.Normal) || (sides[side.ordinal()] == WrenchModes.Mode.Output) ) {
			if (slot==OUTPUT_SLOT) {
				return true;
			}
		}
		if ( (sides[side.ordinal()] == WrenchModes.Mode.Normal) || (sides[side.ordinal()] == WrenchModes.Mode.Input) ) {
			if ( (slot>=SLOT_INVENTORY_START) && (slot<=SLOT_INVENTORY_END) ) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		int[] output = new int[slots.length-9];
		output[0] = 0;
		for (int i=1; i<(slots.length-9); i++) {
			output[i] = i+9;
		}
		return output;
	}

}
