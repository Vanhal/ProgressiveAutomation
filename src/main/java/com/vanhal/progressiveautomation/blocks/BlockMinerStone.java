package com.vanhal.progressiveautomation.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.TileMiner;
import com.vanhal.progressiveautomation.entities.TileMinerStone;

public class BlockMinerStone extends BlockMiner {
	public BlockMinerStone() {
		super("MinerStone");
	}
	
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileMinerStone();
	}
}
