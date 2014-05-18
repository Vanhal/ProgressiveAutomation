package com.vanhal.progressiveautomation.blocks;

import com.vanhal.progressiveautomation.entities.TileMiner;
import com.vanhal.progressiveautomation.ref.ToolInfo;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMiner extends BaseBlock {

	public BlockMiner() {
		super("Miner");
	}
	
	public TileEntity createNewTileEntity(World world, int var2) {
		TileMiner ent = new TileMiner();
		ent.setMiningLevel(ToolInfo.LEVEL_STONE);
		return ent;
	}
}
