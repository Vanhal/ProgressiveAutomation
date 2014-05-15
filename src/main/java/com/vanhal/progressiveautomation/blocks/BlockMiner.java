package com.vanhal.progressiveautomation.blocks;

import com.vanhal.progressiveautomation.entities.TileMiner;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMiner extends BaseBlock {

	public BlockMiner() {
		super("Miner");
	}
	
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileMiner();
	}
}
