package com.vanhal.progressiveautomation.items;

import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.ref.ToolInfo;

public class ItemIronUpgrade extends ItemUpgrade {
	public ItemIronUpgrade() {
		super("IronUpgrade", ToolInfo.LEVEL_IRON);
		this.setTextureName(Ref.MODID+":Iron_Upgrade");
	}
}
