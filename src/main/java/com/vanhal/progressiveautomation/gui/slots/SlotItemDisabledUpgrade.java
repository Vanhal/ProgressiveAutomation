package com.vanhal.progressiveautomation.gui.slots;

import com.vanhal.progressiveautomation.entities.UpgradeableTileEntity;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

import net.minecraft.item.ItemStack;

public class SlotItemDisabledUpgrade extends SlotItem {
	protected UpgradeableTileEntity upgrateEntity;
	UpgradeType upgradeRequired = null;

	public SlotItemDisabledUpgrade(ItemStack baseItem, UpgradeType upgradeType, UpgradeableTileEntity entity, int par2, int par3, int par4) {
		super(baseItem, entity, par2, par3, par4);
		upgrateEntity = entity;
		upgradeRequired = upgradeType;
	}

	@Override
	public boolean isItemValid(ItemStack itemStack) {
		if (upgrateEntity.hasUpgrade(upgradeRequired)) {
			return slotItem.getItem() == itemStack.getItem();
		}
		return false;
	}
}
