package com.vanhal.progressiveautomation.items.upgrades;

import java.util.List;

import com.vanhal.progressiveautomation.upgrades.UpgradeType;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCobbleGenUpgrade extends ItemUpgrade {
	public ItemCobbleGenUpgrade() {
		super("CobbleUpgrade", UpgradeType.COBBLE_GEN);
	}
	
	protected void addUpgradeRecipe() {
		this.addNormalRecipe();
	}
	
	protected void addTieredRecipe(Item previousTier) {
		this.addNormalRecipe();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final World worldIn,
    		final List<String> tooltip, final ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GRAY + "Uses extra pick durability to mine cobble from an internal cobble gen");
       
    }
}
