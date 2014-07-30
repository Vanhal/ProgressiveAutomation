package com.vanhal.progressiveautomation.entities.chopper;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.oredict.OreDictionary;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.UpgradeableTileEntity;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.util.CoordList;
import com.vanhal.progressiveautomation.util.Point2I;
import com.vanhal.progressiveautomation.util.Point3I;


public class TileChopper extends UpgradeableTileEntity {
	
	protected int searchBlock = -1;
	protected boolean plantSapling = false;
	protected CoordList blockList = new CoordList();
	protected Point3I currentBlock = null;
	protected int choppingTime = 0;
	
	public int SLOT_SAPLINGS = 1;
	
	public TileChopper() {
		super(12);
		setUpgradeLevel(ToolHelper.LEVEL_WOOD);
		
		//slots
		SLOT_AXE = 2;
		SLOT_UPGRADE = 3;
	}
	
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		//save the current block
		if (currentBlock!=null) {
			nbt.setTag("CurrentBlock", currentBlock.getNBT());
		} else if (nbt.hasKey("CurrentBlock")) {
			nbt.removeTag("CurrentBlock");
		}
		//save the current chopping time
		nbt.setInteger("choppingTime", choppingTime);
		
		//save the block list
		nbt.setTag("BlockList", blockList.saveToNBT());
		
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		//Load the current Block
		NBTTagCompound point = (NBTTagCompound)nbt.getTag("CurrentBlock");
		if (nbt.hasKey("CurrentBlock")) {
			currentBlock = new Point3I();
			currentBlock.setNBT((NBTTagCompound)nbt.getTag("CurrentBlock"));
		} else {
			currentBlock = null;
		}
		//load the current chopping time
		choppingTime = nbt.getInteger("choppingTime");
		
		//get the current tag list
		NBTTagList contents = nbt.getTagList("BlockList", 10);
		blockList.loadFromNBT(contents);
	}

	public void updateEntity() {
		super.updateEntity();
		if (!worldObj.isRemote) {
			checkForChanges();
			checkInventory();

			if (isBurning()) {
				//do tree stuff
				if (blockList.size()>0) {
					//cut tree
					cutTree();
				} else if (plantSapling) {
					plantSaplings(searchBlock, true);
					plantSapling = false;
				}
				scanBlocks();
			}
		}
	}
	
	protected boolean scanBlocks() {
		//this should scan the available range and return true if we can do something
		//scan for trees, we will always leave the base till last so that it doesn't plant more saplings....
		for (int i = 0; i < this.getRange(); i++) {
			findTree(i);
		}
		if (blockList.size()>0) return true; //if we can harvest some trees lets do that before planting more
		
		//scan for blocks that we can plant on
		for (int i = 0; i < this.getRange(); i++) {
			if (plantSaplings(i, false)) {
				searchBlock = i;
				plantSapling = true;
				return true;
			}
		}
		
		return false;
	}
	
	protected void cutTree() {
		if (slots[SLOT_AXE]==null) return;
		if (currentBlock!=null) {
			if (choppingTime<=0) { //finished chopping
				choppingTime = 0;
				
				if (validBlock(currentBlock)) {
					
					boolean targetTree = isTree(currentBlock);
					
					//I'm fairly sure this doesn't actually do anything, but gonna leave it here anyway
					int fortuneLevel = 0;
					if (targetTree) {
						fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, slots[SLOT_AXE]);
					}
	
					//then break the block
					Block actualBlock = worldObj.getBlock(currentBlock.getX(), currentBlock.getY(), currentBlock.getZ());
					int metaData = worldObj.getBlockMetadata( currentBlock.getX(), currentBlock.getY(), currentBlock.getZ() );
					ArrayList<ItemStack> items = actualBlock.getDrops(worldObj, currentBlock.getX(), currentBlock.getY(), currentBlock.getZ(),
							metaData, fortuneLevel);
					//get the drops
					for (ItemStack item : items) {
						addToInventory(item);
					}
					
					//if we're chopping a tree, then damage the tool, otherwise don't
					if (targetTree) {
						if (ToolHelper.damageTool(slots[SLOT_AXE], worldObj, currentBlock.getX(), currentBlock.getY(), currentBlock.getZ())) {
							slots[SLOT_AXE] = null;
						}
					}
					
					//remove the block and entity if there is one
					worldObj.removeTileEntity( currentBlock.getX(), currentBlock.getY(), currentBlock.getZ() );
					//TODO: animate the leaves breaking
					worldObj.setBlockToAir(currentBlock.getX(), currentBlock.getY(), currentBlock.getZ());
				}
				currentBlock = null;
				
			} else { // chop some more?
				choppingTime--;
			}
		} else { //start cutting something why don't we?
			if (blockList.size()>0) {
				currentBlock = blockList.pop();
				
				if (validBlock(currentBlock)) {
					Block actualBlock = worldObj.getBlock(currentBlock.getX(), currentBlock.getY(), currentBlock.getZ());
					choppingTime = (int)Math.ceil( actualBlock.getBlockHardness(worldObj, currentBlock.getX(), currentBlock.getY(), currentBlock.getZ()) * 1.5 * 20 );
					
					Item tool = (Item)slots[SLOT_AXE].getItem();
					
					float choppingSpeed = 1.0f;
	
					if (isTree(currentBlock)) {
						choppingSpeed = tool.getDigSpeed( slots[SLOT_AXE], actualBlock,
								worldObj.getBlockMetadata( currentBlock.getX(), currentBlock.getY(), currentBlock.getZ() ) );
						//check for efficiency on the tool, only for the wood though!
						int eff = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, slots[SLOT_AXE]);
						if (eff>0) {
							for (int i = 0; i<eff; i++) {
								choppingSpeed = choppingSpeed * 1.3f;
							}
						}
					} else {
						choppingSpeed = (float)ToolHelper.getSpeed(getUpgradeLevel()-1);
					}
	
					choppingTime = (int) Math.ceil(choppingTime / choppingSpeed);
					//ProgressiveAutomation.logger.info("Target: "+actualBlock.getLocalizedName()+" <"+currentBlock.toString()+"> with time of "+choppingTime);
				}
			}
		}
	}
	
	protected void findTree(int n) {
		Point2I p1 = spiral(n + 2, xCoord, zCoord);
		//see if we already have the block first
		Point3I point = new Point3I(p1.getX(), yCoord, p1.getY());
		if (!blockList.inList(point)) {
			if (validBlock(point)) {
				blockList.push(point);
				point.stepUp();
				searchTree(point);
			}
		}
	}
	
	protected void searchTree(Point3I point) {
		//recursively search for the other blocks of a tree
		if (validBlock(point)) {
			//add this block
			blockList.push(point);
			//now we want to search in the 8 directions around this block and 1 above
			for (int i = 0; i < 8; i++) {
				Point3I newPoint = new Point3I(point);
				Point2I spiralPoint = spiral(2 + i, newPoint.getX(), newPoint.getZ());
				newPoint.setX(spiralPoint.getX());
				newPoint.setZ(spiralPoint.getY());
				if (!blockList.inList(newPoint)) {
					searchTree(newPoint);
				}
			}
			Point3I origPoint = new Point3I(point);
			origPoint.stepUp();
			searchTree(origPoint);
			
		}
		
	}
	
	protected boolean validBlock(Point3I point) {
		return validBlock(point.getX(), point.getY(), point.getZ());
	}
	
	protected boolean validBlock(int x, int y, int z) {
		if ( (worldObj.blockExists(x, y, z)) && (!worldObj.isAirBlock(x, y, z)) ) {
			String type = testBlock(x, y, z);
			if (isTree(type)) return true;
			else return isLeaf(type);
		}
		return false;
	}
	
	protected boolean isTree(Point3I point) { return isTree(point.getX(), point.getY(), point.getZ()); }
	
	protected boolean isTree(int x, int y, int z) { return isTree(testBlock(x,y,z)); }
	
	protected boolean isTree(String type) {
		if (type.equalsIgnoreCase("logWood")) {
			return true;
		}
		return false;
	}
	
	protected boolean isLeaf(Point3I point) { return isLeaf(point.getX(), point.getY(), point.getZ()); }
	
	protected boolean isLeaf(int x, int y, int z) { return isLeaf(testBlock(x,y,z)); }
	
	protected boolean isLeaf(String type) {
		if (type.equalsIgnoreCase("treeLeaves")) {
			return true;
		}
		return false;
	}
	
	protected String testBlock(int x, int y, int z) {
		Block _block = worldObj.getBlock(x, y, z);
		int metaData = worldObj.getBlockMetadata(x, y, z);
		ItemStack testItem = new ItemStack(Item.getItemFromBlock(_block), metaData);
		int ordID = OreDictionary.getOreID(testItem);
		if (ordID>=0) {
			return OreDictionary.getOreName(ordID);
		} else {
			return "Unknown";
		}
	}
	
	protected boolean testOre(int OreID, int x, int y, int z) {
		Block _block = worldObj.getBlock(x, y, z);
		int metaData = worldObj.getBlockMetadata(x, y, z);
		ItemStack testItem = new ItemStack(Item.getItemFromBlock(_block), metaData);
		int[] ordIDs = OreDictionary.getOreIDs(testItem);
		return false;
	}
	
	public boolean readyToBurn() {
		if (slots[SLOT_AXE]!=null) {
			if (scanBlocks()) {
				if ( (plantSapling && (slots[SLOT_SAPLINGS]!=null)) || (blockList.size()>0) )
					return true;
			}
		}
		return false;
	}

	
	protected boolean plantSaplings(int n, boolean doAction) {
		//so this method will attempt to plant saplings on anything that they can be planted on
		if (slots[SLOT_SAPLINGS]!=null) {
			if (slots[SLOT_SAPLINGS].stackSize>0) {
				Point2I p1 = spiral(n + 2, xCoord, zCoord);
				//ProgressiveAutomation.logger.debug("Plant: "+p1.getX()+", "+yCoord+", "+p1.getY());
				if (Block.getBlockFromItem(slots[SLOT_SAPLINGS].getItem()) instanceof IPlantable) {
					Block tree = (Block)Block.getBlockFromItem(slots[SLOT_SAPLINGS].getItem());
					if (tree.canPlaceBlockAt(worldObj, p1.getX(), yCoord, p1.getY())) {
						if (doAction) {
							worldObj.setBlock(p1.getX(), yCoord, p1.getY(), tree, slots[SLOT_SAPLINGS].getItem().getDamage(slots[SLOT_SAPLINGS]), 7);
							slots[SLOT_SAPLINGS].stackSize--;
							if (slots[SLOT_SAPLINGS].stackSize==0) {
								slots[SLOT_SAPLINGS] = null;
							}
						}
						
						return true;
					}
					
				}
			}
		}
		return false;
	}

	protected int getCurrentUpgrades() {
		if (SLOT_UPGRADE==-1) return 0;
		if (this.getStackInSlot(SLOT_UPGRADE)==null) {
			return 0;
		} else {
			return this.getStackInSlot(SLOT_UPGRADE).stackSize;
		}
	}
	
	//gui methods
	public boolean isPlanting() {
		return plantSapling;
	}
	
	public boolean isChopping() {
		return (blockList.size()>0);
	}
	
	public boolean planting = false;
	public boolean chopping = false;
	

	protected int lastAxe = -1;
	protected int lastUpgrades = 0;
	
	public void checkForChanges() {
		boolean update = false;

		//check axe
		if ( (slots[SLOT_AXE] == null) && (lastAxe>=0) ) {
			lastAxe = -1;
			update = true;
		} else if (slots[SLOT_AXE] != null) {
			if (ToolHelper.getLevel(slots[SLOT_AXE]) != lastAxe) {
				lastAxe = ToolHelper.getLevel(slots[SLOT_AXE]);
				update = true;
			}
		}

		//check upgrades
		if (getCurrentUpgrades() != lastUpgrades) {
			//remove the upgrade and add it to the upgrades var
			if (slots[SLOT_UPGRADE].isItemEqual(ToolHelper.getUpgradeType(getUpgradeLevel()))) {
				addUpgrades(getCurrentUpgrades());
				slots[SLOT_UPGRADE] = null;
				lastUpgrades = getCurrentUpgrades();
				update = true;
			}
		}

		//update
		if (update) {
			//ProgressiveAutomation.logger.info("Inventory Changed Update");
			scanBlocks();
		}
	}
	
	public int extraSlotCheck(int slot) {
		if (OreDictionary.getOreID(slots[slot]) == OreDictionary.getOreID("treeSapling")) {
			return SLOT_SAPLINGS;
		}
		return -1;
	}


	/* ISided Stuff */
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if ( (slot == SLOT_SAPLINGS) && (OreDictionary.getOreID(stack) == OreDictionary.getOreID("treeSapling")) ) {
    		return true;
    	}
		return super.isItemValidForSlot(slot, stack);
	}

}
