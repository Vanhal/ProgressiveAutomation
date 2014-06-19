package com.vanhal.progressiveautomation.entities.miner;

import java.util.ArrayList;

import com.vanhal.progressiveautomation.util.Point2I;
import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.blocks.PABlocks;
import com.vanhal.progressiveautomation.entities.UpgradeableTileEntity;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;

public class TileMiner extends UpgradeableTileEntity {
	protected int totalMineBlocks = -1;
	protected int currentMineBlocks = 0;


	//mining vars
	protected int currentColumn = 0;
	protected int currentYLevel = 0;
	protected Block currentBlock = null;
	protected int miningTime = 0;
	protected int miningWith = 0;


	public TileMiner() {
		super(13);
		setUpgradeLevel(ToolHelper.LEVEL_WOOD);
		
		//set the slots
		SLOT_PICKAXE = 2;
		SLOT_SHOVEL = 3;
		SLOT_UPGRADE = 4;
	}


	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("MineBlocks", totalMineBlocks);
		nbt.setInteger("MinedBlocks", currentMineBlocks);
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		totalMineBlocks = nbt.getInteger("MineBlocks");
		currentMineBlocks = nbt.getInteger("MinedBlocks");
	}



	public void updateEntity() {
		super.updateEntity();
		if (!worldObj.isRemote) {
			checkForChanges();
			checkInventory();

			if ( (!isDone()) && (isBurning()) ) {
				//mine!
				mine();
			}
		}
	}

	public void scanBlocks() {
		totalMineBlocks = currentMineBlocks = 0;
		for (int i = 1; i <= getRange(); i++) {
			Point2I currentPoint = spiral(i, xCoord, zCoord);
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
		//ProgressiveAutomation.logger.info("Update Finished: "+currentMineBlocks+"/"+totalMineBlocks);
	}

	/* Tests a block to see if it can be mined with the current equipment
	 * Returns 0 if it can't, -1 if it is cobble
	 * Will return 2 if mined with pick, 3 if shovel, 1 if none */
	public int canMineBlock(int x, int y, int z) {
		Block tryBlock = worldObj.getBlock(x, y, z);
		if (tryBlock != null) {
			int meta = worldObj.getBlockMetadata(x, y, z);
			if (
				(tryBlock.getBlockHardness(worldObj, x, y, z)>=0) &&
				(!tryBlock.isAir(worldObj, x, y, z)) 
			) {
				boolean mine = false;
				//ProgressiveAutomation.logger.info("Tool: "+tryBlock.getHarvestTool(meta)+", Level: "+tryBlock.getHarvestLevel(meta)+", Can use Pick: "+tryBlock.isToolEffective("pickaxe", meta));
				if (tryBlock == Blocks.cobblestone) {
					return -1;
				} if (tryBlock.getHarvestTool(meta)=="pickaxe") {
					if (getToolMineLevel(2)>=tryBlock.getHarvestLevel(meta)) {
						return 2;
					}
				} else if (tryBlock.getHarvestTool(meta)=="shovel") {
					if (getToolMineLevel(3)>=tryBlock.getHarvestLevel(meta)) {
						return 3;
					}
				} else {
					if (!tryBlock.getMaterial().isLiquid()) {
						return 1;
					}
				}
			}
		}
		return 0;
	}

	public void mine() {
		if ( (slots[1]==null) || (slots[2]==null) || (slots[3]==null) ) return;
		if (currentBlock!=null) {
			//continue to mine this block
			if (miningTime<=0) {
				miningTime = 0;
				//clock is done, lets mine it
				Point2I currentPoint = spiral(currentColumn, xCoord, zCoord);


				//get the inventory of anything under it
				if (worldObj.getTileEntity(currentPoint.getX(), currentYLevel, currentPoint.getY()) instanceof IInventory) {
					IInventory inv = (IInventory) worldObj.getTileEntity(currentPoint.getX(), currentYLevel, currentPoint.getY());
					for (int i = 0; i < inv.getSizeInventory(); i++) {
						if (inv.getStackInSlot(i)!=null) {
							addToInventory(inv.getStackInSlot(i));
							inv.setInventorySlotContents(i, null);
						}
					}
				}

				//silk touch the block if we have it
				int silkTouch = 0;
				if (miningWith!=1) {
					silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, slots[miningWith]);
				}

				if (silkTouch>0) {
					ItemStack item = new ItemStack(currentBlock);
					addToInventory(item);

				} else {
					//mine the block
					int fortuneLevel = 0;
					if (miningWith!=1) {
						fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, slots[miningWith]);
					}

					//then break the block
					ArrayList<ItemStack> items = currentBlock.getDrops(worldObj, currentPoint.getX(), currentYLevel, currentPoint.getY(),
							worldObj.getBlockMetadata( currentPoint.getX(), currentYLevel, currentPoint.getY() ), fortuneLevel);
					//get the drops
					for (ItemStack item : items) {
						addToInventory(item);
					}
				}
				
			
				if (miningWith!=1) {
					if (ToolHelper.damageTool(slots[miningWith], worldObj, currentPoint.getX(), currentYLevel, currentPoint.getY())) {
						slots[miningWith] = null;
					}
				}

				//remove the block and entity if there is one
				worldObj.removeTileEntity( currentPoint.getX(), currentYLevel, currentPoint.getY() );
				worldObj.setBlock( currentPoint.getX(), currentYLevel, currentPoint.getY(), Blocks.cobblestone);
				slots[1].stackSize--;
				if (slots[1].stackSize == 0) {
					slots[1] = null;
				}
				currentMineBlocks++;
				currentBlock = null;
				

			} else {
				miningTime--;
			}
		} else {
			if (!isDone()) {
				currentBlock = getNextBlock();
				if (currentBlock != null) {
					Point2I currentPoint = spiral(currentColumn, xCoord, zCoord);
					miningTime = (int)Math.ceil( currentBlock.getBlockHardness(worldObj, currentPoint.getX(), currentYLevel, currentPoint.getY()) * 1.5 * 20 ) ;
					
					Item tool = (Item)slots[miningWith].getItem();
					
					if (miningWith!=1) {
						/*Item tool = (Item)slots[miningWith].getItem();*/
						float miningSpeed = tool.getDigSpeed( slots[miningWith], currentBlock,
								worldObj.getBlockMetadata( currentPoint.getX(), currentYLevel, currentPoint.getY() ) );

						//check for efficiency on the tool
						int eff = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, slots[miningWith]);
						if (eff>0) {
							for (int i = 0; i<eff; i++) {
								miningSpeed = miningSpeed * 1.3f;
							}
						}

						miningTime = (int) Math.ceil(miningTime / miningSpeed);
					}

					//ProgressiveAutomation.logger.info("Mining: "+currentBlock.getUnlocalizedName()+" in "+miningTime+" ticks");
					
				}
			}
		}

		if (isDone()) {
			//ProgressiveAutomation.logger.info("Done Update");
			scanBlocks();
			currentColumn = getRange();
		}
	}

	public Block getNextBlock() {
		Point2I currentPoint = spiral(currentColumn, xCoord, zCoord);
		miningWith = canMineBlock(currentPoint.getX(), currentYLevel, currentPoint.getY());
		while ( (miningWith<=0) && (currentYLevel>=0) ) {
			if (miningWith>0) {
				return worldObj.getBlock(currentPoint.getX(), currentYLevel, currentPoint.getY());
			} else {
				currentYLevel--;
				miningWith = canMineBlock(currentPoint.getX(), currentYLevel, currentPoint.getY());
			}
		}
		if (miningWith>0) {
			return worldObj.getBlock(currentPoint.getX(), currentYLevel, currentPoint.getY());
		}
		if (currentYLevel<0) {
			currentYLevel = yCoord - 1;
			currentColumn--;
			if (currentColumn<0) {
				//ProgressiveAutomation.logger.info("Last Column done Update");
				scanBlocks();
				currentColumn = getRange();
			} else {
				return getNextBlock();
			}
		}
		return null;
	}

	protected int getCurrentUpgrades() {
		if (SLOT_UPGRADE==-1) return 0;
		if (this.getStackInSlot(SLOT_UPGRADE)==null) {
			return 0;
		} else {
			return this.getStackInSlot(SLOT_UPGRADE).stackSize;
		}
	}

	public int getToolMineLevel(int slot) {
		if (getStackInSlot(slot) != null) {
			return ToolHelper.getHarvestLevel(getStackInSlot(slot));
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
			if (ToolHelper.getLevel(slots[2]) != lastPick) {
				lastPick = ToolHelper.getLevel(slots[2]);
				update = true;
			}
		}

		//check shovel
		if ( (slots[3] == null) && (lastShovel>=0) ) {
			lastShovel = -1;
			update = true;
		} else if (slots[3] != null) {
			if (ToolHelper.getLevel(slots[3]) != lastShovel) {
				lastShovel = ToolHelper.getLevel(slots[3]);
				update = true;
			}
		}

		//check upgrades
		if (getCurrentUpgrades() != lastUpgrades) {
			//remove the upgrade and add it to the upgrades var
			if (slots[4].isItemEqual(ToolHelper.getUpgradeType(getUpgradeLevel()))) {
				addUpgrades(getCurrentUpgrades());
				slots[4] = null;
				lastUpgrades = getCurrentUpgrades();
				update = true;
			}
		}

		//update
		if (update) {
			//ProgressiveAutomation.logger.info("INventory Changed Update");
			scanBlocks();
			currentColumn = getRange();
			currentBlock = null;
			miningTime = 0;
			currentYLevel = yCoord - 1;
		}
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

	public int extraSlotCheck(int slot) {
		if (slots[slot].isItemEqual(new ItemStack(Blocks.cobblestone))) {
			return 1;
		}
		return -1;
	}


	/* ISided Stuff */
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if ( (slot==1) && (stack.isItemEqual(new ItemStack(Blocks.cobblestone))) ) {
    		return true;
    	}
		return super.isItemValidForSlot(slot, stack);
	}

}
