package com.vanhal.progressiveautomation.entities;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.ref.ToolInfo;

import net.minecraft.block.Block;
import net.minecraft.item.ItemTool;

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
				if (
					(tryBlock.getBlockHardness(worldObj, xCoord, newY, zCoord)>=0) &&
					(tryBlock.getHarvestLevel(0)>=0)
					) {
					boolean mine = false;
					if (tryBlock.getHarvestTool(0)=="pickaxe") {
						if (getToolMineLevel(2)>=tryBlock.getHarvestLevel(0)) {
							totalMineBlocks++;
							mine = true;
						}
					} else if (tryBlock.getHarvestTool(0)=="shovel") {
						if (getToolMineLevel(3)>=tryBlock.getHarvestLevel(0)) {
							totalMineBlocks++;
							mine = true;
						}
					} else {
						totalMineBlocks++;
						mine = true;
					}
					ProgressiveAutomation.logger.info("Block: "+newY+", Harvest Tool: "+
							tryBlock.getHarvestTool(0)+", Harvest Level: "+tryBlock.getHarvestLevel(0)+
							". Mine: "+mine);
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
	
	public int getToolMineLevel(int slot) {
		if (getStackInSlot(slot).getItem() instanceof ItemTool) {
			ItemTool tool = (ItemTool) getStackInSlot(slot).getItem();
			return ToolInfo.getHarvestLevel(tool);
		}
		return -1;
	}
	
	public int getMineBlocks() {
		if (totalMineBlocks==-1) {
			scanBlocks();
		}
		return totalMineBlocks;
	}

}
