package com.vanhal.progressiveautomation.items.upgrades;

import java.util.List;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWitherUpgrade extends ItemUpgrade {
	public ItemWitherUpgrade() {
		super("WitherUpgrade", UpgradeType.WITHER);
	}
	
/*	protected void addNormalRecipe() {
		if (PAConfig.allowDiamondLevel) {
			ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"dgd", "gng", "dgd", 'd', PAItems.diamondUpgrade, 'g', Items.GOLD_INGOT, 'n', Items.NETHER_STAR});
			GameRegistry.addRecipe(recipe);
		} else {
			ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"dgd", "gng", "dgd", 'd', Blocks.DIAMOND_BLOCK, 'g', Items.GOLD_INGOT, 'n', Items.NETHER_STAR});
			GameRegistry.addRecipe(recipe);
		}
	}
*/	
	protected void addUpgradeRecipe() {
		this.addNormalRecipe();
	}
	
	protected void addTieredRecipe(Item previousTier) {
		this.addNormalRecipe();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public void addInformation(final ItemStack stack, final World worldIn,
			final List<String> tooltip, final ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GRAY + "Multiplies the range of a machine by "+PAConfig.witherMultiplier);
       
    }
	
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemStack) {
        return true;
    }
}
