package com.vanhal.progressiveautomation.items;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.ref.Ref;

import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemCoalPellet extends BaseItem implements IFuelHandler  {
	protected int burnTime = 100;

	public ItemCoalPellet() {
		super("CoalPellet");
		//setTextureName(Ref.MODID+":Coal_Pellet");
		int coalTime = TileEntityFurnace.getItemBurnTime(new ItemStack(Items.coal));
		burnTime = coalTime / 8;
	}

    protected void addNormalRecipe() {
    	ShapelessOreRecipe recipe = new ShapelessOreRecipe(new ItemStack(this, 8), Items.coal);
		GameRegistry.addRecipe(recipe);
		recipe = new ShapelessOreRecipe(new ItemStack(this, 8), new ItemStack(Items.coal, 1, 1));
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
