package com.vanhal.progressiveautomation.items.upgrades;

import static com.vanhal.progressiveautomation.blocks.PABlocks.miner;

import java.util.List;

import com.vanhal.progressiveautomation.blocks.BlockMiner;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemCobbleGenUpgrade extends ItemUpgrade {
	public ItemCobbleGenUpgrade() {
		super("CobbleUpgrade", UpgradeType.COBBLE_GEN);
	}
	
	protected void addNormalRecipe() {
		// No sense to add the recipe with no miner present!
		if (miner.size() == 0) {
			return;
		}
		BlockMiner blockMiner = miner.get(0);
		if (blockMiner.getLevelName().equals("Wooden") && miner.size() > 1) {
			blockMiner = miner.get(1);
		}
		
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"ppp", "lrw", "ppp", 'p', Blocks.STONE, 'r', blockMiner, 'l', Items.LAVA_BUCKET, 'w', Items.WATER_BUCKET});
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
		list.add(TextFormatting.GRAY + "Uses extra pick durability to mine cobble from an internal cobble gen");
       
    }
}
