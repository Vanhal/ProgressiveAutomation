package com.vanhal.progressiveautomation.entities;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.items.ItemCobbleGenUpgrade;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.util.Point2I;

public class UpgradeableTileEntity extends BaseTileEntity implements IUpgradeable {
	protected int toolLevel = ToolHelper.LEVEL_WOOD;
	protected int numberUpgrades = 0;

	public UpgradeableTileEntity(int numSlots) {
		super(numSlots);
	}
	
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("NumUpgrades", numberUpgrades);
		nbt.setBoolean("hasWitherUpgrade", hasWitherUpgrade);
		nbt.setBoolean("hasCobbleUpgrade", hasCobbleUpgrade);
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		numberUpgrades = nbt.getInteger("NumUpgrades");
		hasCobbleUpgrade = nbt.getBoolean("hasWitherUpgrade");
		hasWitherUpgrade = nbt.getBoolean("hasCobbleUpgrade");
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
		return (getUpgrades() * PAConfig.upgradeRange) + 1;
	}

	public int getUpgradeLevel() {
		return toolLevel;
	}

	public void setUpgradeLevel(int level) {
		toolLevel = level;
	}
	
	//check for changes to upgrades
	protected int lastUpgrades = 0;
	
	public boolean hasWitherUpgrade = false;
	public boolean hasCobbleUpgrade = false;
	
	public void checkForChanges() {
		this.upgradeChanges();
	}
	
	public boolean upgradeChanges() {
		//check upgrades
		if (getCurrentUpgrades() != lastUpgrades) {
			//remove the upgrade and add it to the upgrades var
			if (slots[SLOT_UPGRADE].isItemEqual(ToolHelper.getUpgradeType(getUpgradeLevel()))) {
				addUpgrades(getCurrentUpgrades());
				slots[SLOT_UPGRADE] = null;
				lastUpgrades = getCurrentUpgrades();
				return true;
			}
		}
		if ( (slots[SLOT_UPGRADE] != null) && (slots[SLOT_UPGRADE].stackSize>0) ) {
			if (slots[SLOT_UPGRADE].getItem() instanceof ItemCobbleGenUpgrade) {
				if (!hasCobbleUpgrade) {
					slots[SLOT_UPGRADE] = null;
					hasCobbleUpgrade = true;
					return true;
				}
			}
		}
		return false;
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
	
	//my function to get a point on a spiral around the block
	public static Point2I spiral(int n, int x, int y) {
		int dx, dy;

		int k = (int)Math.ceil( (Math.sqrt(n)-1)/2);
		int t = 2*k + 1;
		int m = t*t;
		t = t-1;

		if (n>=(m-t)) {
			dx = k-(m-n);
			dy = -k;
		} else {
			m = m-t;
			if (n>=(m-t)) {
				dx = -k;
				dy = -k + (m-n);
			} else {
				m = m-t;
				if (n>=(m-t)) {
					dx = -k + (m-n);
					dy = k;
				} else {
					dx = k;
					dy = k - (m-n-t);
				}
			}
		}

		return new Point2I(x + dx, y + dy);
	}

}
