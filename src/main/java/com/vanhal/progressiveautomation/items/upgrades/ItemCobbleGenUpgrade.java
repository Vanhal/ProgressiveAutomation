package com.vanhal.progressiveautomation.items.upgrades;

import java.util.List;

import static com.vanhal.progressiveautomation.blocks.PABlocks.miner;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.vanhal.progressiveautomation.blocks.BlockMiner;
import com.vanhal.progressiveautomation.blocks.PABlocks;
import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCobbleGenUpgrade extends ItemUpgrade {
	public ItemCobbleGenUpgrade() {
		super("CobbleUpgrade", -1);
		this.setTextureName(Ref.MODID+":Cobble_Upgrade");
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
			"ppp", "lrw", "ppp", 'p', Blocks.stone, 'r', blockMiner, 'l', Items.lava_bucket, 'w', Items.water_bucket});
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
		list.add(EnumChatFormatting.GRAY + "Uses extra pick durability to mine cobble from an internal cobble gen");
       
    }
}
