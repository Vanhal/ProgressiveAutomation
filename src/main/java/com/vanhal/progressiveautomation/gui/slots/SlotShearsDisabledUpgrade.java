package com.vanhal.progressiveautomation.gui.slots;

import com.vanhal.progressiveautomation.entities.UpgradeableTileEntity;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;

public class SlotShearsDisabledUpgrade extends Slot {
	protected UpgradeableTileEntity upgrateEntity;
	UpgradeType upgradeRequired = null;

	public SlotShearsDisabledUpgrade(UpgradeType upgradeType, UpgradeableTileEntity entity, int par2, int par3, int par4) {
		super(entity, par2, par3, par4);
		upgrateEntity = entity;
		upgradeRequired = upgradeType;
	}

	@Override
	public boolean isItemValid(ItemStack itemStack) {
		if (upgrateEntity.hasUpgrade(upgradeRequired)) {
			return itemStack.getItem() instanceof ItemShears;
		}
		return false;
	}
}
