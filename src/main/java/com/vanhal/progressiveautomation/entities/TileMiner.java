package com.vanhal.progressiveautomation.entities;

import net.minecraft.block.Block;

public class TileMiner extends BaseTileEntity {
	protected int totalMineBlocks = -1;
	protected int currentMineBlocks = 0;

	public TileMiner() {
		super(14);
		if (totalMineBlocks==-1) {
			scanBlocks();
		}
	}
	
	public void scanBlocks() {
		boolean bedrock = false;
		int newY = this.yCoord - 1;
		while (!bedrock) {
			Block tryBlock = worldObj.getBlock(this.xCoord, newY, this.zCoord);
		}
	}

	public int getRange() {
		if (this.getStackInSlot(4)==null) {
			return 1;
		} else {
			return this.getStackInSlot(4).stackSize + 1;
		}
	}

}
