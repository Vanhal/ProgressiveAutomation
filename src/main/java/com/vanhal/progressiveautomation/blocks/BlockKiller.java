package com.vanhal.progressiveautomation.blocks;

import com.vanhal.progressiveautomation.entities.killer.TileKiller;
import com.vanhal.progressiveautomation.entities.killer.TileKillerDiamond;
import com.vanhal.progressiveautomation.entities.killer.TileKillerIron;
import com.vanhal.progressiveautomation.entities.killer.TileKillerStone;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockKiller extends BaseBlock {

	public BlockKiller(int level) {
		super("Killer", level);
	}
	
	public static final Block firstTier = Blocks.furnace;

	@Override
	public TileEntity createNewTileEntity(World world, int var2) {
		if (blockLevel >= ToolHelper.LEVEL_DIAMOND) return new TileKillerDiamond();
		else if (blockLevel == ToolHelper.LEVEL_IRON) return new TileKillerIron();
		else if (blockLevel == ToolHelper.LEVEL_STONE) return new TileKillerStone();
		else return new TileKiller();
	}
	
	@Override
	public void addRecipe(Block previousTier) {
		ShapedOreRecipe recipe = null;
		
		if (blockLevel == ToolHelper.LEVEL_STONE) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sss", "scs", "sps", 's', Blocks.stone, 'c', previousTier, 'p', Items.stone_sword});
		} else if (blockLevel == ToolHelper.LEVEL_IRON) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sbs", "scs", "sps", 's', Items.iron_ingot, 'c', previousTier, 'p', Items.iron_sword, 'b', Blocks.iron_block});
		} else if (blockLevel == ToolHelper.LEVEL_DIAMOND) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sss", "scs", "sps", 's', Items.diamond, 'c', previousTier, 'p', Items.diamond_sword});
		} else {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"scs", "srs", "sps", 's', "logWood", 'r', previousTier, 'c', Blocks.chest, 'p', Items.wooden_sword});
		}
		
		
		GameRegistry.addRecipe(recipe);
	}
}
