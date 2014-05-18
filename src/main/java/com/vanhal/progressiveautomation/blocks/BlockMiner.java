package com.vanhal.progressiveautomation.blocks;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.TileMiner;
import com.vanhal.progressiveautomation.ref.ToolInfo;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMiner extends BaseBlock {

	public BlockMiner() {
		this("Miner");
	}
	
	public BlockMiner(String name) {
		super(name);
		GUIid = ProgressiveAutomation.guiHandler.MinerGUI;
	}
	
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileMiner();
	}
}
