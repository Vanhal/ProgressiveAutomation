package com.vanhal.progressiveautomation.blocks;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.TileMiner;
import com.vanhal.progressiveautomation.entities.TileMinerStone;

import cpw.mods.fml.common.registry.GameRegistry;

public class BlockMinerStone extends BlockMiner {
	public BlockMinerStone() {
		super("MinerStone");
	}
	
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileMinerStone();
	}
	
	public void addRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"sss", "scs", "sps", 's', Blocks.stone, 'c', PABlocks.woodenMiner, 'p', Items.stone_pickaxe});
		GameRegistry.addRecipe(recipe);
	}
}
