package com.vanhal.progressiveautomation.items;

import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.ref.ToolInfo;

public class ItemWoodUpgrade extends ItemUpgrade {
	
	public ItemWoodUpgrade() {
		super("WoodUpgrade", ToolInfo.LEVEL_WOOD);
		this.setTextureName(Ref.MODID+":Wood_Upgrade");
	}
}
