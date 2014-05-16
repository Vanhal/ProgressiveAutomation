package com.vanhal.progressiveautomation.items;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class ItemUpgrade extends Item {
	public String itemName;
	public int level;
	
	public ItemUpgrade() {
		
	}
	
	public ItemUpgrade(String name, int setLevel) {
		setName(name);
		setLevel(setLevel);
	}
	
	public void setLevel(int useLevel) {
		level = useLevel;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setName(String newName) {
		itemName = newName;
		this.setUnlocalizedName(itemName);
	}
	
	public void preInit() {
		GameRegistry.registerItem(this, itemName);
	}
	
}
