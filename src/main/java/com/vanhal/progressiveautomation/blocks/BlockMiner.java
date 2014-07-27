package com.vanhal.progressiveautomation.blocks;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.miner.TileMiner;
import com.vanhal.progressiveautomation.entities.miner.TileMinerDiamond;
import com.vanhal.progressiveautomation.entities.miner.TileMinerIron;
import com.vanhal.progressiveautomation.entities.miner.TileMinerStone;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockMiner extends BaseBlock {
	
	public BlockMiner(int level) {
		super("Miner", level);
		GUIid = ProgressiveAutomation.guiHandler.MinerGUI;
	}
	
	public TileEntity createNewTileEntity(World world, int var2) {
		if (blockLevel >= ToolHelper.LEVEL_DIAMOND) return new TileMinerDiamond();
		else if (blockLevel == ToolHelper.LEVEL_IRON) return new TileMinerIron();
		else if (blockLevel == ToolHelper.LEVEL_STONE) return new TileMinerStone();
		else return new TileMiner();
	}
	
	
	public void addRecipe() {
		ShapedOreRecipe recipe = null;
		
		if (blockLevel == ToolHelper.LEVEL_STONE) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sss", "scs", "sps", 's', Blocks.stone, 'c', PABlocks.miner[ToolHelper.LEVEL_WOOD], 'p', Items.stone_pickaxe});
		} else if (blockLevel == ToolHelper.LEVEL_IRON) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sbs", "scs", "sps", 's', Items.iron_ingot, 'c', PABlocks.miner[ToolHelper.LEVEL_STONE], 'p', Items.iron_pickaxe, 'b', Blocks.iron_block});
		} else if (blockLevel == ToolHelper.LEVEL_DIAMOND) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sss", "scs", "sps", 's', Items.diamond, 'c', PABlocks.miner[ToolHelper.LEVEL_IRON], 'p', Items.diamond_pickaxe});
		} else {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"scs", "srs", "sps", 's', Blocks.log, 'r', Blocks.furnace, 'c', Blocks.chest, 'p', Items.wooden_pickaxe});
		}
		
		
		GameRegistry.addRecipe(recipe);
	}
}
