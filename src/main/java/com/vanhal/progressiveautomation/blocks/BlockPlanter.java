package com.vanhal.progressiveautomation.blocks;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.planter.TilePlanter;
import com.vanhal.progressiveautomation.entities.planter.TilePlanterDiamond;
import com.vanhal.progressiveautomation.entities.planter.TilePlanterIron;
import com.vanhal.progressiveautomation.entities.planter.TilePlanterStone;
import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPlanter extends BaseBlock {
	public BlockPlanter(int level) {
		super("Planter", level);
	}

	public TileEntity createNewTileEntity(World world, int var2) {
		if (blockLevel >= ToolHelper.LEVEL_DIAMOND) return new TilePlanterDiamond();
		else if (blockLevel == ToolHelper.LEVEL_IRON) return new TilePlanterIron();
		else if (blockLevel == ToolHelper.LEVEL_STONE) return new TilePlanterStone();
		else return new TilePlanter();
	}
	

	
	public static final Block firstTier = Blocks.furnace;
	
	public void addRecipe(Block previousTier) {
		ShapedOreRecipe recipe = null;
		
		if (blockLevel == ToolHelper.LEVEL_STONE) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sss", "scs", "sps", 's', Blocks.stone, 'c', previousTier, 'p', Items.stone_hoe});
		} else if (blockLevel == ToolHelper.LEVEL_IRON) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sbs", "scs", "sps", 's', Items.iron_ingot, 'c', previousTier, 'p', Items.iron_hoe, 'b', Blocks.iron_block});
		} else if (blockLevel == ToolHelper.LEVEL_DIAMOND) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sss", "scs", "sps", 's', Items.diamond, 'c', previousTier, 'p', Items.diamond_hoe});
		} else {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"scs", "srs", "sps", 's', "logWood", 'r', previousTier, 'c', Blocks.chest, 'p', Items.wooden_hoe});
		}
		
		
		GameRegistry.addRecipe(recipe);
	}
}
