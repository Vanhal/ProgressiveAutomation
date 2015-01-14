package com.vanhal.progressiveautomation.entities;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.items.upgrades.ItemCobbleGenUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemFillerUpgrade;
import com.vanhal.progressiveautomation.items.upgrades.ItemWitherUpgrade;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.util.Point2I;

public class UpgradeableTileEntity extends BaseTileEntity implements IUpgradeable {
	protected int toolLevel = ToolHelper.LEVEL_WOOD;
	protected int numberUpgrades = 0;
	
	//other types of upgrades
	public boolean hasWitherUpgrade = false;
	public boolean hasCobbleUpgrade = false;
	public boolean hasFillerUpgrade = false;

	public UpgradeableTileEntity(int numSlots) {
		super(numSlots);
	}
	
	public void writeCommonNBT(NBTTagCompound nbt) {
		super.writeCommonNBT(nbt);
		nbt.setInteger("NumUpgrades", numberUpgrades);
		nbt.setBoolean("hasWitherUpgrade", hasWitherUpgrade);
		nbt.setBoolean("hasCobbleUpgrade", hasCobbleUpgrade);
		nbt.setBoolean("hasFillerUpgrade", hasFillerUpgrade);
	}

	public void readCommonNBT(NBTTagCompound nbt) {
		super.readCommonNBT(nbt);
		if (nbt.hasKey("NumUpgrades")) numberUpgrades = nbt.getInteger("NumUpgrades");
		if (nbt.hasKey("hasWitherUpgrade")) hasWitherUpgrade = nbt.getBoolean("hasWitherUpgrade");
		if (nbt.hasKey("hasCobbleUpgrade")) hasCobbleUpgrade = nbt.getBoolean("hasCobbleUpgrade");
		if (nbt.hasKey("hasFillerUpgrade")) hasFillerUpgrade = nbt.getBoolean("hasFillerUpgrade");
	}
	
	/* IUpgradeable methods */
	public int getUpgrades() {
		return numberUpgrades;
	}

	public void setUpgrades(int value) {
		numberUpgrades = value;
	}

	public void addUpgrades(int addValue) {
		numberUpgrades += addValue;
	}

	public int getRange() {
		int range = (getUpgrades() * PAConfig.upgradeRange) + 1;
		if (hasWitherUpgrade) range = range * PAConfig.witherMultiplier;
		return range;
	}

	public int getUpgradeLevel() {
		return toolLevel;
	}

	public void setUpgradeLevel(int level) {
		toolLevel = level;
	}
	
	//check for changes to upgrades
	protected int lastUpgrades = 0;
	
	public void updateEntity() {
		super.updateEntity();
		if (!worldObj.isRemote) {
			ItemStack upgrade = SLOT_UPGRADE != -1 ? getStackInSlot(SLOT_UPGRADE) : null;
			
			// Something inside the upgrade slot
			if (upgrade != null && upgrade.stackSize > 0) {
				
				if (upgrade.isItemEqual(ToolHelper.getUpgradeType(getUpgradeLevel()))) {
					addUpgrades(upgrade.stackSize);
					upgrade.stackSize = 0;
					addPartialUpdate("NumUpgrades", getUpgrades());
				} else if (upgrade.getItem() instanceof ItemCobbleGenUpgrade && !hasCobbleUpgrade) {
					hasCobbleUpgrade = true;
					upgrade.stackSize--;
					addPartialUpdate("hasCobbleUpgrade", hasCobbleUpgrade);
				} else if (upgrade.getItem() instanceof ItemFillerUpgrade && !hasFillerUpgrade) {
					hasFillerUpgrade = true;
					upgrade.stackSize--;
					addPartialUpdate("hasFillerUpgrade", hasFillerUpgrade);
				} else if (upgrade.getItem() instanceof ItemWitherUpgrade && !hasWitherUpgrade) {
					hasWitherUpgrade = true;
					upgrade.stackSize--;
					addPartialUpdate("hasWitherUpgrade", hasWitherUpgrade);
				}
				
				// We've eaten all items in this stack, it should be disposed of
				if (upgrade.stackSize <= 0) {
					slots[SLOT_UPGRADE] = null;
				}
			}
			else if (upgrade != null) {
				// Malformed itemstack? Better delete it
				ProgressiveAutomation.logger.warn("Inserted ItemStack with stacksize <= 0. Deleting");
				slots[SLOT_UPGRADE] = null;
			}
		}
	}
	
	public void checkForChanges() {
//		this.upgradeChanges();
	}
	
	protected int getCurrentUpgrades() {
		if (SLOT_UPGRADE==-1) return 0;
		if (this.getStackInSlot(SLOT_UPGRADE)==null) {
			return 0;
		} else {
			return this.getStackInSlot(SLOT_UPGRADE).stackSize;
		}
	}
	
	//override isided stuff
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if ( (slot==SLOT_PICKAXE) && ( ToolHelper.getType(stack.getItem()) == ToolHelper.TYPE_PICKAXE ) ) {
    		if (ToolHelper.getLevel(stack) <= getUpgradeLevel()) {
    			return true;
    		}
    	} else if ( (slot==SLOT_SHOVEL) && ( ToolHelper.getType(stack.getItem()) == ToolHelper.TYPE_SHOVEL ) ) {
    		if (ToolHelper.getLevel(stack) <= getUpgradeLevel()) {
    			return true;
    		}
     	} else if ( (slot==SLOT_AXE) && ( ToolHelper.getType(stack.getItem()) == ToolHelper.TYPE_AXE ) ) {
    		if (ToolHelper.getLevel(stack) <= getUpgradeLevel()) {
    			return true;
    		}
     	} else if ( (slot==SLOT_SWORD) && ( ToolHelper.getType(stack.getItem()) == ToolHelper.TYPE_SWORD ) ) {
    		if (ToolHelper.getLevel(stack) <= getUpgradeLevel()) {
    			return true;
    		}
     	} else if ( (slot==SLOT_HOE) && ( ToolHelper.getType(stack.getItem()) == ToolHelper.TYPE_HOE ) ) {
    		if (ToolHelper.getLevel(stack) <= getUpgradeLevel()) {
    			return true;
    		}
     	} else if ( (slot==SLOT_FUEL) && (TileEntityFurnace.getItemBurnTime(stack)>0) && (ToolHelper.getType(stack.getItem())==-1) ) {
     		return true;
    	} else if ( (slot==SLOT_UPGRADE) && (stack.isItemEqual(ToolHelper.getUpgradeType(getUpgradeLevel()))) ) {
    		return true;
     	}
		return false;
	}
}
