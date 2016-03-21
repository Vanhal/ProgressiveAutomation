package com.vanhal.progressiveautomation.gui.slots;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.entities.UpgradeableTileEntity;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotTool extends Slot {
	protected int level;
	protected int type;
	
	protected UpgradeableTileEntity entity;
	
	public SlotTool(int ToolType, int ToolLevel, UpgradeableTileEntity tile, int par2, int par3, int par4) {
		super(tile, par2, par3, par4);
		level = ToolLevel;
		type = ToolType;
		entity = tile;
	}

	@Override
	public boolean isItemValid(ItemStack itemStack) {
		if (ToolHelper.isBroken(itemStack)) return false;
		int curLevel = ToolHelper.getLevel(itemStack);
		int tool = ToolHelper.getType(itemStack);
		if (tool==type) {
			if ((curLevel > PAConfig.getToolConfigLevel(level))) {
				entity.setInvalidTool();
			}
		}
		return ( (tool==type) && (curLevel <= PAConfig.getToolConfigLevel(level)) );
	}
}
