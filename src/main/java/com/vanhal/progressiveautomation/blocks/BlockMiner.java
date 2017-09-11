package com.vanhal.progressiveautomation.blocks;

import com.vanhal.progressiveautomation.entities.miner.TileMiner;
import com.vanhal.progressiveautomation.entities.miner.TileMinerDiamond;
import com.vanhal.progressiveautomation.entities.miner.TileMinerIron;
import com.vanhal.progressiveautomation.entities.miner.TileMinerStone;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
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
	
	public static final Block firstTier = Blocks.FURNACE;
	
	public void addRecipe(Block previousTier) {
		ShapedOreRecipe recipe = null;
		
		if (blockLevel == ToolHelper.LEVEL_STONE) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sss", "scs", "sps", 's', Blocks.STONE, 'c', previousTier, 'p', Items.STONE_PICKAXE});
		} else if (blockLevel == ToolHelper.LEVEL_IRON) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sbs", "scs", "sps", 's', Items.IRON_INGOT, 'c', previousTier, 'p', Items.IRON_PICKAXE, 'b', Blocks.IRON_BLOCK});
		} else if (blockLevel == ToolHelper.LEVEL_DIAMOND) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sss", "scs", "sps", 's', Items.DIAMOND, 'c', previousTier, 'p', Items.DIAMOND_PICKAXE});
		} else {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"scs", "srs", "sps", 's', "logWood", 'r', previousTier, 'c', "chestWood", 'p', Items.WOODEN_PICKAXE});
		}
		
		
		GameRegistry.addRecipe(recipe);
	}
	
	@Override
	public int getWeakPower(IBlockState blockState, IBlockAccess world, BlockPos pos, EnumFacing side) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileMiner) {
			return (((TileMiner)tile).isDone())?15:0;
		}
		return 0;
	}
	
	@Override
	public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.DOWN ? this.getWeakPower(state, world, pos, side) : 0;
	}
	
	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}
}
