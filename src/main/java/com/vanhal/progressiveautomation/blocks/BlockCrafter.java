package com.vanhal.progressiveautomation.blocks;

import java.util.ArrayList;

import com.vanhal.progressiveautomation.entities.crafter.TileCrafter;
import com.vanhal.progressiveautomation.entities.crafter.TileCrafterDiamond;
import com.vanhal.progressiveautomation.entities.crafter.TileCrafterIron;
import com.vanhal.progressiveautomation.entities.crafter.TileCrafterStone;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockCrafter extends BaseBlock {

	public BlockCrafter(int level) {
		super("Crafter", level);
		
	}

	public TileEntity createNewTileEntity(World world, int var2) {
		if (blockLevel >= ToolHelper.LEVEL_DIAMOND) return new TileCrafterDiamond();
		else if (blockLevel == ToolHelper.LEVEL_IRON) return new TileCrafterIron();
		else if (blockLevel == ToolHelper.LEVEL_STONE) return new TileCrafterStone();
		else return new TileCrafter();
	}
	
	public static final Block firstTier = Blocks.furnace;
	
	public void addRecipe(Block previousTier) {
		ShapedOreRecipe recipe = null;
		
		if (blockLevel == ToolHelper.LEVEL_STONE) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sss", "scs", "sps", 's', Blocks.stone, 'c', previousTier, 'p', Blocks.crafting_table});
		} else if (blockLevel == ToolHelper.LEVEL_IRON) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sbs", "scs", "sps", 's', Items.iron_ingot, 'c', previousTier, 'p', Blocks.crafting_table, 'b', Blocks.iron_block});
		} else if (blockLevel == ToolHelper.LEVEL_DIAMOND) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sss", "scs", "sps", 's', Items.diamond, 'c', previousTier, 'p', Blocks.crafting_table});
		} else {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"scs", "srs", "sps", 's', "logWood", 'r', previousTier, 'c', Blocks.chest, 'p', Blocks.crafting_table});
		}
		
		
		GameRegistry.addRecipe(recipe);
	}
	
	@Override
	protected ArrayList<ItemStack> getInsides(World world, BlockPos pos) {
		TileCrafter crafter = (TileCrafter)world.getTileEntity(pos);
		if (crafter!=null) {
			crafter.setInventorySlotContents(crafter.CRAFT_RESULT, null);
		}
		return super.getInsides(world, pos);
	}
}
