package com.vanhal.progressiveautomation.items.upgrades;

import java.util.List;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.items.PAItems;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemWitherUpgrade extends ItemUpgrade {
	public ItemWitherUpgrade() {
		super("WitherUpgrade", UpgradeType.WITHER);
		//this.setTextureName(Ref.MODID+":Wither_Upgrade");
	}
	
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"dgd", "gng", "dgd", 'd', PAItems.diamondUpgrade, 'g', Items.gold_ingot, 'n', Items.nether_star});
		GameRegistry.addRecipe(recipe);
	}
	
	protected void addUpgradeRecipe() {
		this.addNormalRecipe();
	}
	
	protected void addTieredRecipe(Item previousTier) {
		this.addNormalRecipe();
	}
	
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par) {
		list.add(TextFormatting.GRAY + "Multiplies the range of a machine by "+PAConfig.witherMultiplier);
       
    }
	
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemStack) {
        return true;
    }
}
