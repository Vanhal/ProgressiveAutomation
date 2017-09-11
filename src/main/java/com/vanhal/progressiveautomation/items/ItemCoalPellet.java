package com.vanhal.progressiveautomation.items;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ItemCoalPellet extends BaseItem implements IFuelHandler  {
	protected int burnTime = 100;

	public ItemCoalPellet() {
		super("CoalPellet");
		//setTextureName(Ref.MODID+":Coal_Pellet");
		int coalTime = TileEntityFurnace.getItemBurnTime(new ItemStack(Items.COAL));
		burnTime = coalTime / 8;
	}

    protected void addNormalRecipe() {
    	ShapelessOreRecipe recipe = new ShapelessOreRecipe(new ItemStack(this, 8), Items.COAL);
		GameRegistry.addRecipe(recipe);
		recipe = new ShapelessOreRecipe(new ItemStack(this, 8), new ItemStack(Items.COAL, 1, 1));
		GameRegistry.addRecipe(recipe);

		GameRegistry.registerFuelHandler(this);
	}

	protected void addUpgradeRecipe() {
		addNormalRecipe();
	}

	public int getBurnTime(ItemStack fuel) {
		if (fuel.isItemEqual(new ItemStack(this))) {
			return burnTime;
		} else {
			return 0;
		}
	}
}
