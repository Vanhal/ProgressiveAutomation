package com.vanhal.progressiveautomation.gui.slots;

import com.vanhal.progressiveautomation.items.upgrades.ItemUpgrade;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotUpgrades extends Slot {
	protected int level;
	protected ItemStack updateType;
	
	public SlotUpgrades(int UpgradeLevel, IInventory par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
		level = UpgradeLevel;
		updateType = ToolHelper.getUpgradeType(level);
	}

	public boolean isItemValid(ItemStack itemStack) {
		if (itemStack.getItem() instanceof ItemUpgrade) {
			ItemUpgrade currentUpgrade = (ItemUpgrade) itemStack.getItem();
			if (currentUpgrade.getLevel()==-1) {
				return true;
			} else {
				return updateType.isItemEqual(itemStack);
			}
		}
		return false;
	}
}
