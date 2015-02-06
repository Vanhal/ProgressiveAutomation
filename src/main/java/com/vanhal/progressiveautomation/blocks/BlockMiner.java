package com.vanhal.progressiveautomation.blocks;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.miner.TileMiner;
import com.vanhal.progressiveautomation.entities.miner.TileMinerDiamond;
import com.vanhal.progressiveautomation.entities.miner.TileMinerIron;
import com.vanhal.progressiveautomation.entities.miner.TileMinerStone;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockMiner extends BaseBlock {
	
	public BlockMiner(int level) {
		super("Miner", level);
	}
	
	public TileEntity createNewTileEntity(World world, int var2) {
		if (blockLevel >= ToolHelper.LEVEL_DIAMOND) return new TileMinerDiamond();
		else if (blockLevel == ToolHelper.LEVEL_IRON) return new TileMinerIron();
		else if (blockLevel == ToolHelper.LEVEL_STONE) return new TileMinerStone();
		else return new TileMiner();
	}
	
	public static final Block firstTier = Blocks.furnace;
	
	public void addRecipe(Block previousTier) {
		ShapedOreRecipe recipe = null;
		
		if (blockLevel == ToolHelper.LEVEL_STONE) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sss", "scs", "sps", 's', Blocks.stone, 'c', previousTier, 'p', Items.stone_pickaxe});
		} else if (blockLevel == ToolHelper.LEVEL_IRON) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sbs", "scs", "sps", 's', Items.iron_ingot, 'c', previousTier, 'p', Items.iron_pickaxe, 'b', Blocks.iron_block});
		} else if (blockLevel == ToolHelper.LEVEL_DIAMOND) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sss", "scs", "sps", 's', Items.diamond, 'c', previousTier, 'p', Items.diamond_pickaxe});
		} else {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"scs", "srs", "sps", 's', "logWood", 'r', previousTier, 'c', Blocks.chest, 'p', Items.wooden_pickaxe});
		}
		
		
		GameRegistry.addRecipe(recipe);
	}
	
	@Override
	public int isProvidingWeakPower(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing side) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileMiner) {
			return (((TileMiner)tile).isDone())?15:0;
		}
		return 0;
    }
	
	@Override
	public int isProvidingStrongPower(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing side) {
        return side == EnumFacing.DOWN ? this.isProvidingWeakPower(world, pos, state, side) : 0;
    }
	
	@Override
	public boolean canProvidePower() {
        return true;
    }
}
