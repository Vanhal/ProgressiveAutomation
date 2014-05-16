package com.vanhal.progressiveautomation.items;

import net.minecraft.item.Item;

public class ItemUpgrade extends Item {
	public String itemName;
	public int level;
	
	public ItemUpgrade() {
		
	}
	
	public void setLevel(int useLevel) {
		level = useLevel;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setName(String newName) {
		itemName = newName;
	}
	
	
}
