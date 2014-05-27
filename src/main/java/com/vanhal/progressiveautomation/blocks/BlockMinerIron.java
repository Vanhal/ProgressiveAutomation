package com.vanhal.progressiveautomation.blocks;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.vanhal.progressiveautomation.entities.TileMinerIron;

import cpw.mods.fml.common.registry.GameRegistry;

public class BlockMinerIron extends BlockMiner {
	public BlockMinerIron() {
		super("MinerIron");
	}
	
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileMinerIron();
	}
	
	public void addRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"sbs", "scs", "sps", 's', Items.iron_ingot, 'c', PABlocks.stoneMiner, 'p', Items.iron_pickaxe, 'b', Blocks.iron_block});
		GameRegistry.addRecipe(recipe);
	}
}
