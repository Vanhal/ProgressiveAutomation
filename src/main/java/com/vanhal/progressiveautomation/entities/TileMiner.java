package com.vanhal.progressiveautomation.entities;

import java.util.ArrayList;

import org.lwjgl.util.Point;
import org.lwjgl.util.vector.Vector2f;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.ref.ToolInfo;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;

public class TileMiner extends BaseTileEntity {
	protected int totalMineBlocks = -1;
	protected int currentMineBlocks = 0;
	protected boolean invFull = false;
	protected int numUpgrades = 0;
	protected int mineLevel;
	
	//mining vars
	protected int currentColumn = 0;
	protected int currentYLevel = 0;
	protected Block currentBlock = null;
	protected int miningTime = 0;
	protected int miningWith = 0;
	
	
	public TileMiner() {
		super(13);
		setMiningLevel(ToolInfo.LEVEL_WOOD);
	}
	
	
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("MineBlocks", totalMineBlocks);
		nbt.setInteger("MinedBlocks", currentMineBlocks);
		nbt.setInteger("NumUpgrades", numUpgrades);
		nbt.setBoolean("InvFull", invFull);
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		totalMineBlocks = nbt.getInteger("MineBlocks");
		currentMineBlocks = nbt.getInteger("MinedBlocks");
		numUpgrades = nbt.getInteger("NumUpgrades");
		invFull = nbt.getBoolean("InvFull");
	}
	
	public int getMiningLevel() {
		return mineLevel;
	}
	
	public void setMiningLevel(int level) {
		mineLevel = level;
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (!worldObj.isRemote) {
			checkForChanges();
			checkInventory();
			
			if ( (!isDone()) && (isBurning()) && (!invFull) ) {
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
		//ProgressiveAutomation.logger.info("Update Finished: "+currentMineBlocks+"/"+totalMineBlocks);
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
					if (getToolMineLevel(2)>=tryBlock.getHarvestLevel(worldObj.getBlockMetadata( x, y, z ))) {
						return 2;
					}
				} else if (tryBlock.getHarvestTool(0)=="shovel") {
					if (getToolMineLevel(3)>=tryBlock.getHarvestLevel(worldObj.getBlockMetadata( x, y, z ))) {
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
		if ( (slots[1]==null) || (slots[2]==null) || (slots[3]==null) ) return;
		if (currentBlock!=null) {
			//continue to mine this block
			if (miningTime<=0) {
				miningTime = 0;
				//clock is done, lets mine it
				Point currentPoint = spiral(currentColumn, xCoord, zCoord);
				

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
				
				//remove the block and entity if there is one
				worldObj.removeTileEntity( currentPoint.getX(), currentYLevel, currentPoint.getY() );
				worldObj.setBlock( currentPoint.getX(), currentYLevel, currentPoint.getY(), Blocks.cobblestone);
				slots[1].stackSize--;
				if (slots[1].stackSize == 0) {
					slots[1] = null;
				}
				currentMineBlocks++;
				currentBlock = null;
				if (miningWith!=1) {
					if (slots[miningWith].attemptDamageItem(1, this.RND)) {
						slots[miningWith] = null;
					}
				}
				
			} else {
				miningTime--;
			}
		} else {
			if (!isDone()) {
				currentBlock = getNextBlock();
				if (currentBlock != null) {
					Point currentPoint = spiral(currentColumn, xCoord, zCoord);
					miningTime = (int)Math.ceil( currentBlock.getBlockHardness(worldObj, currentPoint.getX(), currentYLevel, currentPoint.getY()) * 1.5 * 20 ) ;
					if (miningWith!=1) {
						ItemTool tool = (ItemTool)slots[miningWith].getItem();
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
		Point currentPoint = spiral(currentColumn, xCoord, zCoord);
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
	
	public int getRange() {
		return numUpgrades + 1;
		/*if (this.getStackInSlot(4)==null) {
			return 1;
		} else {
			return this.getStackInSlot(4).stackSize + 1;
		}*/
	}
	
	protected int getCurrentUpgrades() {
		if (this.getStackInSlot(4)==null) {
			return 0;
		} else {
			return this.getStackInSlot(4).stackSize;
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
	
	public boolean isSearching() {
		return (currentBlock!=null);
	}

	public int getUpgrades() {
		return numUpgrades;
	}
	
	public void setUpgrades(int value) {
		numUpgrades = value;
	}
	
	public void addUpgrades(int addValue) {
		numUpgrades += addValue;
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
		if (getCurrentUpgrades() != lastUpgrades) {
			//remove the upgrade and add it to the upgrades var
			if (slots[4].isItemEqual(ToolInfo.getUpgradeType(mineLevel))) {
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
							slots[moveTo].stackSize += avail;
						}
					}
				}
			} else {
				invFull = false;
			}
		}
		//then check if there is any inventories on top of this block that we can output to
		if (worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof IInventory) {
			IInventory externalInv = (IInventory) worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
			for (int i = 5; i <= 13; i++) {
				if (slots[i]!=null) {
					addtoExtInventory(externalInv, i);
				}
			}
		}
	}
	
	public boolean addtoExtInventory(IInventory inv, int fromSlot) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i)!=null) {
				if ( (inv.getStackInSlot(i).isItemEqual(slots[fromSlot])) && (inv.getStackInSlot(i).stackSize < inv.getStackInSlot(i).getMaxStackSize()) ) {
					int avail = inv.getStackInSlot(i).getMaxStackSize() - inv.getStackInSlot(i).stackSize;
					if (avail >= slots[fromSlot].stackSize) {
						inv.getStackInSlot(i).stackSize += slots[fromSlot].stackSize;
						slots[fromSlot] = null;
						return true;
					} else {
						slots[fromSlot].stackSize -= avail;
						inv.getStackInSlot(i).stackSize += avail;
					}
				}
			}
		}
		if ( (slots[fromSlot] != null) && (slots[fromSlot].stackSize>0) ) {
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				if (inv.getStackInSlot(i)==null) {
					inv.setInventorySlotContents(i, slots[fromSlot]);
					slots[fromSlot] = null;
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean addToInventory(ItemStack item) {
		for (int i = 5; i <= 13; i++) {
			if (slots[i]!=null) {
				if (item!=null) {
					if ( (slots[i].isItemEqual(item)) && (slots[i].stackSize < slots[i].getMaxStackSize()) ) {
						int avail = slots[i].getMaxStackSize() - slots[i].stackSize;
						if (avail >= item.stackSize) {
							slots[i].stackSize += item.stackSize;
							item = null;
							return true;
						} else {
							item.stackSize -= avail;
							slots[i].stackSize += avail;
						}
					}
				}
			}
		}
		if ( (item != null) && (item.stackSize>0) ) {
			for (int i = 5; i <= 13; i++) {
				if (slots[i]==null) {
					slots[i] = item;
					item = null;
					return true;
				}
			}
		}
		if ( (item != null) && (item.stackSize==0) ) {
			item = null;
		}
		//if we still have an item, drop in on the ground
		if (item!=null) {
			EntityItem entItem = new EntityItem(worldObj, xCoord + 0.5f, yCoord + 1.5f, zCoord + 0.5f, item);
			entItem.delayBeforeCanPickup = 1;
			float f3 = 0.05F;
			entItem.motionX = (double)((float)worldObj.rand.nextGaussian() * f3);
			entItem.motionY = (double)((float)worldObj.rand.nextGaussian() * f3 + 0.2F);
			entItem.motionZ = (double)((float)worldObj.rand.nextGaussian() * f3);
			worldObj.spawnEntityInWorld(entItem);
		}
		
		return false;
	}

	/* ISided Stuff */
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if ( (slot==1) && (stack.isItemEqual(new ItemStack(Blocks.cobblestone))) ) {
    		return true;
    	} else if ( (slot==2) && ( ToolInfo.getType(stack.getItem()) == ToolInfo.TYPE_PICKAXE ) ) {
    		if (ToolInfo.getLevel(stack.getItem()) <= getMiningLevel()) {
    			return true;
    		}
    	} else if ( (slot==3) && ( ToolInfo.getType(stack.getItem()) == ToolInfo.TYPE_SHOVEL ) ) {
    		if (ToolInfo.getLevel(stack.getItem()) <= getMiningLevel()) {
    			return true;
    		}
     	} else if ( (slot==0) && (TileEntityFurnace.getItemBurnTime(stack)>0) && (ToolInfo.getType(stack.getItem())==-1) ) {
     		return true;
    	} else if ( (slot==4) && (stack.isItemEqual(ToolInfo.getUpgradeType(getMiningLevel()))) ) {
    		return true;
     	}
		return false;
	}

	public int[] getAccessibleSlotsFromSide(int var1) {
		int[] output = {0,1,2,3,4,5,6,7,8,9,10,11,12,13};
		return output;
	}
	
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		if (slot>=5) {
			return true;
		}
		return false;
	}
	
}
