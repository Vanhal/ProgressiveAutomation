package com.vanhal.progressiveautomation.blocks;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.TileMiner;
import com.vanhal.progressiveautomation.ref.ToolInfo;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
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
	
	public void breakBlock(World world, int x, int y, int z, Block p_149749_5_, int p_149749_6_) {
		//check if it's a miner, if it is then we need to dump out the upgrades as well!
	    if (world.getTileEntity(x, y, z) instanceof TileMiner) {
	    	TileMiner tileMiner = (TileMiner)world.getTileEntity(x, y, z);
	    	int numUpgrades = tileMiner.getUpgrades();
	    	for (int i = 0; i < (int)Math.ceil(numUpgrades/64.0f); i++) {
	    		ItemStack upgrades = ToolInfo.getUpgradeType(tileMiner.getMiningLevel());
	    		if (numUpgrades<=64) {
	    			upgrades.stackSize = numUpgrades;
	    			numUpgrades = 0;
	    		} else {
	    			numUpgrades -= 0;
	    		}
	    	}
	    }
	    super.breakBlock(world, x, y, z, p_149749_5_, p_149749_6_);
	}
}
