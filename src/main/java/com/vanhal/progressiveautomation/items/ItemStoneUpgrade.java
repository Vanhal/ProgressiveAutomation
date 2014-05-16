package com.vanhal.progressiveautomation.items;

import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.ref.ToolInfo;

public class ItemStoneUpgrade extends ItemUpgrade {
	public ItemStoneUpgrade() {
		super("StoneUpgrade", ToolInfo.LEVEL_STONE);
		this.setTextureName(Ref.MODID+":Stone_Upgrade");
	}
}
