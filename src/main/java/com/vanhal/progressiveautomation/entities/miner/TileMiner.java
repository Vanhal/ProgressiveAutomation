package com.vanhal.progressiveautomation.entities.miner;

import java.util.List;

import com.vanhal.progressiveautomation.entities.UpgradeableTileEntity;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.util.Point2I;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.ForgeHooks;

public class TileMiner extends UpgradeableTileEntity {
	protected int totalMineBlocks = -1;
	protected int currentMineBlocks = 0;


	//mining vars
	protected int currentColumn = 0;
	protected int currentYLevel = 0;
	protected Block currentBlock = null;
	protected int miningWith = 0;
	
	protected int blockMineDuration = 0;
	protected int elapsedDuration = 0;


	public TileMiner() {
		super(13);
		setUpgradeLevel(ToolHelper.LEVEL_WOOD);
		setAllowedUpgrades(UpgradeType.WOODEN, UpgradeType.WITHER, UpgradeType.COBBLE_GEN, UpgradeType.FILLER);
		
		//set the slots
		SLOT_PICKAXE = 2;
		SLOT_SHOVEL = 3;
		SLOT_UPGRADE = 4;
	}


	public void writeCommonNBT(NBTTagCompound nbt) {
		super.writeCommonNBT(nbt);
		nbt.setInteger("MineBlocks", totalMineBlocks);
		nbt.setInteger("MinedBlocks", currentMineBlocks);
	}

	public void readCommonNBT(NBTTagCompound nbt) {
		super.readCommonNBT(nbt);
		if (nbt.hasKey("MineBlocks")) totalMineBlocks = nbt.getInteger("MineBlocks");
		if (nbt.hasKey("MinedBlocks")) currentMineBlocks = nbt.getInteger("MinedBlocks");
	}



	public void update() {
		super.update();
		if (!worldObj.isRemote) {
			checkForChanges();
			checkInventory();
			
			// If we're full, don't do anything else
			if (isFull()) return;

			if (isBurning()) {
				useCobbleGen();
				if (!isDone()) {
					//mine!
					mine();
				}
			}
		}
	}

	public void scanBlocks() {
		totalMineBlocks = currentMineBlocks = 0;	
		for (int i = 1; i <= getRange(); i++) {
			Point2I currentPoint = spiral(i, pos.getX(), pos.getZ());
			boolean bedrock = false;
			int newY = this.pos.getY() - 1;
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
		addPartialUpdate("MineBlocks", totalMineBlocks);
		addPartialUpdate("MinedBlocks", currentMineBlocks);
		notifyUpdate();
		//ProgressiveAutomation.logger.info("Update Finished: "+currentMineBlocks+"/"+totalMineBlocks);
	}

	/* Tests a block to see if it can be mined with the current equipment
	 * Returns 0 if it can't, -1 if it is cobble
	 * Will return 2 if mined with pick, 3 if shovel, 1 if none
	 * return 4 if just need to fill using the filler upgrade  */
	public int canMineBlock(int x, int y, int z) {
		BlockPos minePos = new BlockPos(x, y, z);
		IBlockState tryState = worldObj.getBlockState(minePos);
		Block tryBlock = tryState.getBlock();
		
		if (tryBlock != null) {
			int meta = tryBlock.getMetaFromState(tryState);
			if (
				(tryBlock.getBlockHardness(tryState, worldObj, minePos)>=0) &&
				(!tryBlock.isAir(tryState, worldObj, minePos)) 
			) {
				boolean mine = false;
				//ProgressiveAutomation.logger.info("Tool: "+tryBlock.getHarvestTool(meta)+", Level: "+tryBlock.getHarvestLevel(meta)+", Can use Pick: "+tryBlock.isToolEffective("pickaxe", meta));
				//ProgressiveAutomation.logger.info("Harvestable: "+ForgeHooks.canToolHarvestBlock(tryBlock, meta, getStackInSlot(2)));
				if (tryBlock == Blocks.COBBLESTONE) {
					return -1;
				}
				if (tryBlock.getHarvestTool(tryState)=="chisel") { //this is compatibility for chisel 1
					return 2;
				} else if (tryBlock.getHarvestTool(tryState)=="pickaxe") {
					if (ForgeHooks.canToolHarvestBlock(worldObj, minePos, getStackInSlot(2))) {
						//ProgressiveAutomation.logger.info("Tool can harvest");
						return 2;
					}
				} else if (tryBlock.getHarvestTool(tryState)=="shovel") {
					if (ForgeHooks.canToolHarvestBlock(worldObj, minePos, getStackInSlot(3))) {
						return 3;
					}
				} else {
					if (!tryBlock.getMaterial(tryState).isLiquid()) {
						return 1;
					}
				}
			}
			
			//see if the filler upgrade is active, if it is then the block will need to be filled.
			if (hasUpgrade(UpgradeType.FILLER)) {
				if ( (tryBlock.isAir(tryState, worldObj, minePos)) || (tryBlock.getMaterial(tryState).isLiquid()) ) {
					return 4;
				}
			}
		}
		return 0;
	}

	public void mine() {
		if ( (slots[1]==null) || (slots[2]==null) || (slots[3]==null) ) return;
		if (currentBlock!=null) {
			//continue to mine this block
			if (elapsedDuration >= blockMineDuration) { 
				//clock is done, lets mine it
				Point2I currentPoint = spiral(currentColumn, pos.getX(), pos.getZ());
				BlockPos currentPosition = new BlockPos(currentPoint.getX(), currentYLevel, currentPoint.getY());
				//ProgressiveAutomation.logger.info("Point: "+miningWith+" "+currentPoint.getX()+","+currentYLevel+","+currentPoint.getY());

				// Verify the block is what we expect, fixes race condition and item dupe effect
				Block testBlock = worldObj.getBlockState(currentPosition).getBlock();
				if (testBlock != currentBlock) {
					//ProgressiveAutomation.logger.info("Possible race condition found, expected "+currentBlock.getUnlocalizedName()+" actually had "+testBlock.getUnlocalizedName()+".");
					currentBlock = testBlock;
					miningWith = canMineBlock( currentPosition.getX(), currentPosition.getY(), currentPosition.getZ() );

					// Intentionally leaving cobble requirement and item durability reduction as penalty for multiple miners in same area
					if (miningWith == -1) {
						miningWith = 2;
					}
					
					blockMineDuration = miningDuration( currentPosition, miningWith );
					// If we haven't waited enough, wait some more!
					if (elapsedDuration < blockMineDuration) {
						elapsedDuration++;
						return;
					}
					
				}

				//don't harvest anything if the block is air or liquid
				if (miningWith!=4) {
					//get the inventory of anything under it
					if (worldObj.getTileEntity(currentPosition) instanceof IInventory) {
						IInventory inv = (IInventory) worldObj.getTileEntity(currentPosition);
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
						silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, slots[miningWith]);
					}
	
					if (silkTouch>0) {
						int i = 0;
				        Item item = Item.getItemFromBlock(currentBlock);
				        if (item != null && item.getHasSubtypes()) i = currentBlock.getMetaFromState(worldObj.getBlockState(currentPosition));

						ItemStack addItem = new ItemStack(currentBlock, 1, i);
						addToInventory(addItem);
	
					} else {
						//mine the block
						int fortuneLevel = 0;
						if (miningWith!=1) {
							fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, slots[miningWith]);
						}
	
						//then break the block
						List<ItemStack> items = currentBlock.getDrops(worldObj, currentPosition,
								worldObj.getBlockState(currentPosition), fortuneLevel);
						//get the drops
						for (ItemStack item : items) {
							addToInventory(item);
						}
					}
					
				
					if (miningWith!=1) {
						if (ToolHelper.damageTool(slots[miningWith], worldObj, currentPoint.getX(), currentYLevel, currentPoint.getY())) {
							destroyTool(miningWith);
						}
					}
				}

				//remove the block and entity if there is one
				worldObj.removeTileEntity( currentPosition );
				worldObj.setBlockState( currentPosition, Blocks.COBBLESTONE.getDefaultState());
				slots[1].stackSize--;
				if (slots[1].stackSize == 0) {
					slots[1] = null;
				}
				currentMineBlocks++;
				addPartialUpdate("MinedBlocks", currentMineBlocks);
				currentBlock = null;
				
				elapsedDuration = 0;

			} else {
				elapsedDuration++;
			}
		} else {
			if (!isDone()) {
				currentBlock = getNextBlock();
				if (currentBlock != null) {
					Point2I currentPoint = spiral(currentColumn, pos.getX(), pos.getZ());
					BlockPos currentPosition = new BlockPos(currentPoint.getX(), currentYLevel, currentPoint.getY());
					
					blockMineDuration = miningDuration( currentPosition, miningWith );
					//ProgressiveAutomation.logger.info("Mining: "+currentBlock.getUnlocalizedName()+" in "+blockMineDuration+" ticks");
				}
			}
		}

		if (isDone()) {
			//ProgressiveAutomation.logger.info("Done Update");
			scanBlocks();
			currentColumn = getRange();
		}
	}
	
	// Determine how long it will take to mine the block at pos with the tool specified.
	private int miningDuration(BlockPos pos, int tool) {
		int duration = 0;
		
		IBlockState state = worldObj.getBlockState(pos);
		Block block = state.getBlock();
		int normal = (int)Math.ceil( block.getBlockHardness(state, worldObj, pos) * 1.5 * 20 ) ;
		
		switch (tool) {
			case 1: // Hands
				duration = normal;
				break;
			case 2:  // Pickaxe
			case 3:  // Shovel
				float miningSpeed = ToolHelper.getDigSpeed( slots[tool], state );

				// If tool doesn't have efficiency, we're done.
				if (miningSpeed <= 1) {
					duration = normal;
					break;
				}

				int eff = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, slots[tool]);
				if (eff>0) {
					for (int i = 0; i<eff; i++) {
						miningSpeed = miningSpeed * 1.3f;
					}
				}

				duration = (int) Math.ceil(normal / miningSpeed);
				break;
			case 4: // Liquid
				duration = 1;
				break;
			default:
				duration = 0;
				break;
		}
		
		return duration;
	}

	public Block getNextBlock() {
		Point2I currentPoint = spiral(currentColumn, pos.getX(), pos.getZ());
		miningWith = canMineBlock(currentPoint.getX(), currentYLevel, currentPoint.getY());
		while ( (miningWith<=0) && (currentYLevel>=0) ) {
			if (miningWith>0) {
				return worldObj.getBlockState(new BlockPos(currentPoint.getX(), currentYLevel, currentPoint.getY())).getBlock();
			} else {
				currentYLevel--;
				if (currentYLevel>=0)
					miningWith = canMineBlock(currentPoint.getX(), currentYLevel, currentPoint.getY());
			}
		}
		if (miningWith>0) {
			return worldObj.getBlockState(new BlockPos(currentPoint.getX(), currentYLevel, currentPoint.getY())).getBlock();
		}
		if (currentYLevel<0) {
			currentYLevel = pos.getY() - 1;
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
		return (totalMineBlocks==currentMineBlocks) && (totalMineBlocks>0) && (slots[SLOT_PICKAXE]!=null) && (slots[SLOT_SHOVEL]!=null);
	}
	
	//if we have a cobblegen upgrade then this function will deal with adding cobble that is generated
	public void useCobbleGen() {
		if (hasUpgrade(UpgradeType.COBBLE_GEN)) {
			if ( (slots[1] == null) || (slots[1].stackSize==0) ) {
				if (slots[SLOT_PICKAXE]!=null) {
					if (ToolHelper.damageTool(slots[SLOT_PICKAXE], worldObj, this.pos.getX(), this.pos.getY(), this.pos.getZ())) {
						destroyTool(SLOT_PICKAXE);
					}
					slots[1] = new ItemStack(Blocks.COBBLESTONE);
				}
			}
		}
	}

	/* Check for changes to tools and upgrades */
	protected int lastPick = -1;
	protected int lastShovel = -1;
	
	private int previousUpgrades;

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
		if (previousUpgrades != getUpgrades()) {
			previousUpgrades = getUpgrades();
			update = true;
		}

		//update
		if (update) {
			//ProgressiveAutomation.logger.info("INventory Changed Update");
			scanBlocks();
			currentColumn = getRange();
			currentBlock = null;
			elapsedDuration = 0;
			blockMineDuration = 0;
			currentYLevel = pos.getY() - 1;
		}
	}


	/* Check if we are ready to go */
	public boolean readyToBurn() {
		if ( (totalMineBlocks>0) && (currentMineBlocks < totalMineBlocks) ) {
			if ( ((slots[1]!=null)||(hasUpgrade(UpgradeType.COBBLE_GEN))) && (slots[2]!=null) && (slots[3]!=null) ) {
				return true;
			}
		}
		if (((slots[1] == null) || (slots[1].stackSize==0)) && (hasUpgrade(UpgradeType.COBBLE_GEN)) && (slots[2]!=null) && (slots[3]!=null) ) {
			return true;
		}
		return false;
	}

	public int extraSlotCheck(ItemStack item) {
		if (item.isItemEqual(new ItemStack(Blocks.COBBLESTONE))) {
			return 1;
		}
		return super.extraSlotCheck(item);
	}


	/* ISided Stuff */
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if ( (slot==1) && (stack.isItemEqual(new ItemStack(Blocks.COBBLESTONE))) ) {
    		return true;
    	}
		return super.isItemValidForSlot(slot, stack);
	}

}
