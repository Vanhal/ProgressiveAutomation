package com.vanhal.progressiveautomation.items;

import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.ref.ToolInfo;

public class ItemDiamondUpgrade extends ItemUpgrade {
	public ItemDiamondUpgrade() {
		super("DiamondUpgrade", ToolInfo.LEVEL_DIAMOND);
		this.setTextureName(Ref.MODID+":Diamond_Upgrade");
	}
}
