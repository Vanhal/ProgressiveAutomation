package com.vanhal.progressiveautomation.items;

import com.vanhal.progressiveautomation.ProgressiveAutomation;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class ItemUpgrade extends BaseItem {
	public int level;
	
	public ItemUpgrade() {
		
	}
	
	public ItemUpgrade(String name, int setLevel) {
		super(name);
		setLevel(setLevel);
	}
	
	public void setLevel(int useLevel) {
		level = useLevel;
	}
	
	public int getLevel() {
		return level;
	}
	
}
