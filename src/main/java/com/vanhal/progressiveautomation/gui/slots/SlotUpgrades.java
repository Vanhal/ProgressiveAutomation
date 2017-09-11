package com.vanhal.progressiveautomation.gui.slots;

import com.vanhal.progressiveautomation.entities.IUpgradeable;
import com.vanhal.progressiveautomation.entities.UpgradeableTileEntity;
import com.vanhal.progressiveautomation.items.upgrades.ItemUpgrade;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotUpgrades extends Slot {
	
	public SlotUpgrades(UpgradeableTileEntity par1iInventory, int par2, int par3, int par4) {
		super(par1iInventory, par2, par3, par4);
	}

	public boolean isItemValid(ItemStack itemStack) {
		if (!(itemStack.getItem() instanceof ItemUpgrade)) {
			return false;
		}

		IUpgradeable upgradeableMachine = (IUpgradeable) inventory;
		ItemUpgrade currentUpgrade = (ItemUpgrade) itemStack.getItem();

		return upgradeableMachine.isAllowedUpgrade(currentUpgrade.getType());

	}
}
