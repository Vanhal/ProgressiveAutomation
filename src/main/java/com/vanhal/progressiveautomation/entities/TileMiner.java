package com.vanhal.progressiveautomation.entities;

import org.lwjgl.util.Point;
import org.lwjgl.util.vector.Vector2f;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.ref.ToolInfo;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;

public class TileMiner extends BaseTileEntity {
	protected int totalMineBlocks = -1;
	protected int currentMineBlocks = 0;
	protected boolean invFull = false;
	
	//mining vars
	protected int currentColumn = 0;
	protected int currentYLevel = 0;
	protected Block currentBlock = null;
	protected int miningTime = 0;
	protected int miningWith = 0;

	public TileMiner() {
		super(13);
	}
	
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("MineBlocks", totalMineBlocks);
		nbt.setInteger("MinedBlocks", currentMineBlocks);
		nbt.setBoolean("InvFull", invFull);
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		totalMineBlocks = nbt.getInteger("MineBlocks");
		currentMineBlocks = nbt.getInteger("MinedBlocks");
		invFull = nbt.getBoolean("InvFull");
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (!worldObj.isRemote) {
			checkForChanges();
			checkInventory();
			
			if ( (isBurning()) && (!invFull) ) {
				//mine!
				mine();
			}
		}
	}
	
	public void scanBlocks() {
		totalMineBlocks = currentMineBlocks = 0;
		for (int i = 1; i <= getRange(); i++) {
			Point currentPoint = spiral(i, xCoord, zCoord);
			boolean bedrock = false;
			int newY = this.yCoord - 1;
			while (!bedrock) {
				int result = canMineBlock(currentPoint.getX(), newY, currentPoint.getY());
				if (result >= 1) {
					totalMineBlocks++;
				} else if (result == -1) {
					totalMineBlocks++;
					currentMineBlocks++;
				}
				newY--;
				if (newY<0) bedrock = true;
			}
		}
		ProgressiveAutomation.logger.info("Update Finished: "+currentMineBlocks+"/"+totalMineBlocks);
	}
	
	/* Tests a block to see if it can be mined with the current equipment 
	 * Returns 0 if it can't, -1 if it is cobble
	 * Will return 2 if mined with pick, 3 if shovel, 1 if none */
	public int canMineBlock(int x, int y, int z) {
		Block tryBlock = worldObj.getBlock(x, y, z);
		if (tryBlock != null) {
			if (
				(tryBlock.getBlockHardness(worldObj, x, y, z)>=0) &&
				(tryBlock.getHarvestLevel(0)>=0)
				) {
				boolean mine = false;
				if (tryBlock == Blocks.cobblestone) {
					return -1;
				} if (tryBlock.getHarvestTool(0)=="pickaxe") {
					if (getToolMineLevel(2)>=tryBlock.getHarvestLevel(0)) {
						return 2;
					}
				} else if (tryBlock.getHarvestTool(0)=="shovel") {
					if (getToolMineLevel(3)>=tryBlock.getHarvestLevel(0)) {
						return 3;
					}
				} else {
					return 1;
				}
			}
		}
		return 0;
	}

	public void mine() {
		if (currentBlock!=null) {
			//continue to mine this block
			
		} else {
			if (isDone()) {
				scanBlocks();
				currentColumn = getRange();
			}
			
			if (!isDone()) {
				currentBlock = getNextBlock();
				if (currentBlock != null) {
					Point currentPoint = spiral(currentColumn, xCoord, zCoord);
					if (miningWith!=1) {
						ItemTool tool = (ItemTool)slots[miningWith].getItem();
						ProgressiveAutomation.logger.info("Name: "+currentBlock.getUnlocalizedName());
						ProgressiveAutomation.logger.info("First: "+tool.func_150893_a(slots[miningWith], currentBlock));
						ProgressiveAutomation.logger.info("Second: "+tool.getDigSpeed(slots[miningWith], currentBlock, 0));
					}
				}
			}
		}
	}
	
	public Block getNextBlock() {
		Point currentPoint = spiral(currentColumn, xCoord, zCoord);
		miningWith = canMineBlock(currentPoint.getX(), currentYLevel, currentPoint.getY());
		if (miningWith>0) {
			return worldObj.getBlock(currentPoint.getX(), currentYLevel, currentPoint.getY());
		} else {
			currentYLevel--;
			if (currentYLevel<0) {
				currentYLevel = yCoord - 1;
				currentColumn--;
				if (currentColumn<0) {
					scanBlocks();
					currentColumn = getRange();
				}
			}
		}
		return null;
	}
	
	public int getRange() {
		if (this.getStackInSlot(4)==null) {
			return 1;
		} else {
			return this.getStackInSlot(4).stackSize + 1;
		}
	}
	
	public int getToolMineLevel(int slot) {
		if (getStackInSlot(slot) != null) {
			if (getStackInSlot(slot).getItem() instanceof ItemTool) {
				ItemTool tool = (ItemTool) getStackInSlot(slot).getItem();
				return ToolInfo.getHarvestLevel(tool);
			}
		}
		return -1;
	}
	
	public int getMinedBlocks() {
		return currentMineBlocks;
	}
	
	public void setMinedBlocks(int value) {
		currentMineBlocks = value;
	}
	
	public int getMineBlocks() {
		return totalMineBlocks;
	}
	
	public void setMineBlocks(int value) {
		totalMineBlocks = value;
	}
	
	public boolean isInventoryFull() {
		return invFull;
	}
	
	public boolean isDone() {
		return (totalMineBlocks==currentMineBlocks) && (totalMineBlocks>0);
	}

	
	/* Check for changes to tools and upgrades */
	protected int lastPick = -1;
	protected int lastShovel = -1;
	protected int lastUpgrades = 0;
	
	public void checkForChanges() {
		boolean update = false;
		//check pickaxe
		if ( (slots[2] == null) && (lastPick>=0) ) {
			lastPick = -1;
			update = true;
		} else if (slots[2] != null) {
			if (ToolInfo.getLevel(slots[2].getItem()) != lastPick) {
				lastPick = ToolInfo.getLevel(slots[2].getItem());
				update = true;
			}
		}
		
		//check shovel
		if ( (slots[3] == null) && (lastShovel>=0) ) {
			lastShovel = -1;
			update = true;
		} else if (slots[3] != null) {
			if (ToolInfo.getLevel(slots[3].getItem()) != lastShovel) {
				lastShovel = ToolInfo.getLevel(slots[3].getItem());
				update = true;
			}
		}
		
		//check upgrades
		if (getRange() != lastUpgrades) {
			lastUpgrades = getRange();
			update = true;
		}
		
		//update
		if (update) {
			scanBlocks();
			currentColumn = getRange();
			currentBlock = null;
			miningTime = 0;
			currentYLevel = yCoord - 1;
		}
	}

	public static Point spiral(int n, int x, int y) {
		int dx, dy;
		
		int k = (int)Math.ceil( (Math.sqrt(n)-1)/2);
		int t = 2*k + 1;
		int m = t*t;
		t = t-1;
		
		if (n>=(m-t)) {
			dx = k-(m-n);
			dy = -k;
		} else {
			m = m-t;
			if (n>=(m-t)) {
				dx = -k;
				dy = -k + (m-n);
			} else {
				m = m-t;
				if (n>=(m-t)) {
					dx = -k + (m-n);
					dy = k;
				} else {
					dx = k;
					dy = k - (m-n-t);
				}
			}
		}
		
		return new Point(x + dx, y + dy);
	}

	/* Check if we are ready to go */
	public boolean readyToBurn() {
		if ( (totalMineBlocks>0) && (currentMineBlocks < totalMineBlocks) ) {
			if ( (slots[1]!=null) && (slots[2]!=null) && (slots[3]!=null) ) {
				return true;
			}
		}
		return false;
	}
	
	
	/* Check the inventory, move any useful items to their correct slots */
	public void checkInventory() {
		for (int i = 5; i <= 13; i++) {
			if (slots[i]!=null) {
				int moveTo = -1;
				if (slots[i].isItemEqual(new ItemStack(Blocks.cobblestone))) {
					moveTo = 1;
				} else if (getBurnTime(slots[i])>0) {
					if (slots[0]==null) {
						moveTo = 0;
					} else if (slots[i].isItemEqual(slots[0])) {
						moveTo = 0;
					}
				}
				
				if (moveTo>=0) {
					if (slots[moveTo]==null) {
						slots[moveTo] = slots[i];
						slots[i] = null;
					} else if (slots[moveTo].stackSize < slots[moveTo].getMaxStackSize()) {
						int avail = slots[moveTo].getMaxStackSize() - slots[moveTo].stackSize;
						if (avail >= slots[i].stackSize) {
							slots[moveTo].stackSize += slots[i].stackSize;
							slots[i] = null;
						} else {
							slots[i].stackSize -= avail;
							slots[1].stackSize += avail;
						}
					}
				}
			}
		}
	}

}
