package com.vanhal.progressiveautomation.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.vanhal.progressiveautomation.entities.TileMinerIron;

public class BlockMinerIron extends BlockMiner {
	public BlockMinerIron() {
		super("MinerIron");
	}
	
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileMinerIron();
	}
}
