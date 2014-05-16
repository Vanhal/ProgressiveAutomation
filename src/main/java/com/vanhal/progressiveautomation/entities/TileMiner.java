package com.vanhal.progressiveautomation.entities;

import com.vanhal.progressiveautomation.ProgressiveAutomation;

import net.minecraft.block.Block;

public class TileMiner extends BaseTileEntity {
	protected int totalMineBlocks = -1;
	protected int currentMineBlocks = 0;

	public TileMiner() {
		super(14);
	}
	
	public void scanBlocks() {
		totalMineBlocks = 0;
		boolean bedrock = false;
		int newY = this.yCoord - 1;
		while (!bedrock) {
			Block tryBlock = worldObj.getBlock(this.xCoord, newY, this.zCoord);
			if (tryBlock != null) {
				if (tryBlock.getBlockHardness(worldObj, xCoord, newY, zCoord)>=0) {
					totalMineBlocks++;
					ProgressiveAutomation.logger.info("Block: "+newY+", Harvest Tool: "+
							tryBlock.getHarvestTool(0)+", Harvest Level: "+tryBlock.getHarvestLevel(0));
				}
			}
			newY--;
			if (newY<0) bedrock = true;
		}
	}

	public int getRange() {
		if (this.getStackInSlot(4)==null) {
			return 1;
		} else {
			return this.getStackInSlot(4).stackSize + 1;
		}
	}
	
	public int getMineBlocks() {
		if (totalMineBlocks==-1) {
			scanBlocks();
		}
		return totalMineBlocks;
	}

}
