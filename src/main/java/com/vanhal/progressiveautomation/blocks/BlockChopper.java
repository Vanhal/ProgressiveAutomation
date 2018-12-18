package com.vanhal.progressiveautomation.blocks;

import com.vanhal.progressiveautomation.entities.chopper.TileChopper;
import com.vanhal.progressiveautomation.entities.chopper.TileChopperDiamond;
import com.vanhal.progressiveautomation.entities.chopper.TileChopperIron;
import com.vanhal.progressiveautomation.entities.chopper.TileChopperStone;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockChopper extends BaseBlock {

	public BlockChopper(int level) {
		super("Chopper", level);
		this.rangeCount = 1;
	}

	public TileEntity createNewTileEntity(World world, int var2) {
		if (blockLevel >= ToolHelper.LEVEL_DIAMOND) return new TileChopperDiamond();
		else if (blockLevel == ToolHelper.LEVEL_IRON) return new TileChopperIron();
		else if (blockLevel == ToolHelper.LEVEL_STONE) return new TileChopperStone();
		else return new TileChopper();
	}
	
	public static final Block firstTier = Blocks.FURNACE;
	
}
