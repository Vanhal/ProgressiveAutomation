package com.vanhal.progressiveautomation.blocks;

import com.vanhal.progressiveautomation.entities.farmer.TileFarmer;
import com.vanhal.progressiveautomation.entities.farmer.TileFarmerDiamond;
import com.vanhal.progressiveautomation.entities.farmer.TileFarmerIron;
import com.vanhal.progressiveautomation.entities.farmer.TileFarmerStone;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFarmer extends BaseBlock {

	public BlockFarmer(int level) {
		super("Farmer", level);
		this.rangeCount = 1;
	}
	
	public static final Block firstTier = Blocks.FURNACE;

	@Override
	public TileEntity createNewTileEntity(World world, int var2) {
		if (blockLevel >= ToolHelper.LEVEL_DIAMOND) return new TileFarmerDiamond();
		else if (blockLevel == ToolHelper.LEVEL_IRON) return new TileFarmerIron();
		else if (blockLevel == ToolHelper.LEVEL_STONE) return new TileFarmerStone();
		else return new TileFarmer();
	}
	
/*	@Override
	public void addRecipe(Block previousTier) {
		ShapedOreRecipe recipe = null;
		
		if (blockLevel == ToolHelper.LEVEL_STONE) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sss", "scs", "sps", 's', Blocks.STONE, 'c', previousTier, 'p', Items.SHEARS});
		} else if (blockLevel == ToolHelper.LEVEL_IRON) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sbs", "scs", "sps", 's', Items.IRON_INGOT, 'c', previousTier, 'p', Items.SHEARS, 'b', Blocks.IRON_BLOCK});
		} else if (blockLevel == ToolHelper.LEVEL_DIAMOND) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sss", "scs", "sps", 's', Items.DIAMOND, 'c', previousTier, 'p', Items.SHEARS});
		} else {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"scs", "srs", "sps", 's', "logWood", 'r', previousTier, 'c', "chestWood", 'p', Items.SHEARS});
		}
		
		
		GameRegistry.addRecipe(recipe);
	}
*/
}
