package com.vanhal.progressiveautomation.blocks;

import com.vanhal.progressiveautomation.entities.TileMinerDiamond;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;


public class BlockMinerDiamond extends BlockMiner {
	public BlockMinerDiamond() {
		super("MinerDiamond");
	}
	
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileMinerDiamond();
	}
	
	public void addRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"sss", "scs", "sps", 's', Items.diamond, 'c', PABlocks.ironMiner, 'p', Items.diamond_pickaxe});
		GameRegistry.addRecipe(recipe);
	}
}
