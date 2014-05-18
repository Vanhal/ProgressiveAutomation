package com.vanhal.progressiveautomation.blocks;

import com.vanhal.progressiveautomation.entities.TileMinerDiamond;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;


public class BlockMinerDiamond extends BlockMiner {
	public BlockMinerDiamond() {
		super("MinerDiamond");
	}
	
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileMinerDiamond();
	}
}
