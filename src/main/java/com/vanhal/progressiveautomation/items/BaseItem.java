package com.vanhal.progressiveautomation.items;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.ref.Ref;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BaseItem extends Item {
	public String itemName;
	
	public BaseItem() {
		
	}
	
	public BaseItem(String name) {
		setName(name);
		setCreativeTab(ProgressiveAutomation.PATab);
	}
	
	
	public void setName(String newName) {
		itemName = newName;
		this.setUnlocalizedName(itemName);
	}
	
	public String getName() {
		return itemName;
	}
	
	public void preInit() {
		GameRegistry.registerItem(this, itemName);
		addUpgradeRecipe();
	}
	
	public void init() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
			.register(this, 0, new ModelResourceLocation(Ref.MODID + ":" + itemName, "inventory"));
	}

	protected void addUpgradeRecipe() {
		
	}

	protected void addNormalRecipe() {
		
	}
}
