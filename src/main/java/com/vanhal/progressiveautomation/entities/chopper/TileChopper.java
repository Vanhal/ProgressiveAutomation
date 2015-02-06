package com.vanhal.progressiveautomation.entities.chopper;

import java.util.ArrayList;

import com.vanhal.progressiveautomation.upgrades.UpgradeType;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.oredict.OreDictionary;

import com.vanhal.progressiveautomation.entities.UpgradeableTileEntity;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.util.CoordList;
import com.vanhal.progressiveautomation.util.Point2I;
import com.vanhal.progressiveautomation.util.Point3I;


public class TileChopper extends UpgradeableTileEntity {
	
	/**
	 * This is the extra range above planting range used during tree cutting
	 */
	protected final int CUTTING_EXTRA_RANGE = 6;
	
	private boolean forceRecalculate;
	protected int maxCuttingX;
	protected int minCuttingX;
	protected int maxCuttingZ;
	protected int minCuttingZ;
	
	
	protected int searchBlock = -1;
	
	protected boolean plantSapling;
	protected boolean chopping;
	
	protected CoordList blockList = new CoordList();
	protected Point3I currentBlock = null;
	protected int choppingTime = 0;
	
	public int SLOT_SAPLINGS = 1;
	
	public TileChopper() {
		super(12);
		setUpgradeLevel(ToolHelper.LEVEL_WOOD);
		setAllowedUpgrades(UpgradeType.WOODEN, UpgradeType.WITHER);
		forceRecalculate = true;
		
		//slots
		SLOT_AXE = 2;
		SLOT_UPGRADE = 3;
	}
	
/*	@Override
	public void writeNonSyncableNBT(NBTTagCompound nbt) {
		super.writeNonSyncableNBT(nbt);
		
		//save the current block
		if (currentBlock!=null) {
			nbt.setTag("CurrentBlock", currentBlock.getNBT());
		} else if (nbt.hasKey("CurrentBlock")) {
			nbt.removeTag("CurrentBlock");
		}
		
		//save the block list
		nbt.setTag("BlockList", blockList.saveToNBT());
	}
	
	@Override
	public void writeCommonNBT(NBTTagCompound nbt) {
		super.writeCommonNBT(nbt);
		
		//save the current chopping time
		nbt.setInteger("choppingTime", choppingTime);
	}
	
	@Override
	public void writeSyncOnlyNBT(NBTTagCompound nbt) {
		super.writeSyncOnlyNBT(nbt);
		
		nbt.setBoolean("chopping", chopping);
		nbt.setBoolean("planting", plantSapling);
	}
	
	@Override
	public void readNonSyncableNBT(NBTTagCompound nbt) {
		super.readNonSyncableNBT(nbt);
		
		//Load the current Block
		if (nbt.hasKey("CurrentBlock")) {
			currentBlock = new Point3I();
			currentBlock.setNBT((NBTTagCompound)nbt.getTag("CurrentBlock"));
		} else {
			currentBlock = null;
		}
		
		//get the current tag list
		NBTTagList contents = nbt.getTagList("BlockList", 10);
		blockList.loadFromNBT(contents);
		
		forceRecalculate = true;
		
		if (blockList.size() > 0) {
			chopping = true;
		}
	}
	
	@Override
	public void readCommonNBT(NBTTagCompound nbt) {
		super.readCommonNBT(nbt);
		
		//load the current chopping time
		choppingTime = nbt.getInteger("choppingTime");		
	}
	
	@Override
	public void readSyncOnlyNBT(NBTTagCompound nbt) {
		super.readSyncOnlyNBT(nbt);
		
		if (nbt.hasKey("chopping")) chopping = nbt.getBoolean("chopping");
		if (nbt.hasKey("planting")) plantSapling = nbt.getBoolean("planting");
	}
	
	@Override
	public void update() {
		super.update();
		if (!worldObj.isRemote) {
			checkForChanges();
			checkInventory();

			if (isBurning()) {
				if (chopping && blockList.size() == 0) {
					chopping = false;
					addPartialUpdate("chopping", false);
				}
				//do tree stuff
				if (blockList.size()>0) {
					//cut tree
					cutTree();
				} else if (plantSapling) {
					plantSaplings(searchBlock, true);
					plantSapling = false;
					addPartialUpdate("planting", false);
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
				addPartialUpdate("planting", true);
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
				if (!chopping) {
					chopping = true;
					addPartialUpdate("chopping", true);
				}
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
				if (isWithinCuttingRange(spiralPoint.getX(), spiralPoint.getY()) && !blockList.inList(newPoint)) {
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
		if ( (type.equalsIgnoreCase("logWood")) || (type.equalsIgnoreCase("woodRubber")) ) {
			return true;
		}
		return false;
	}
	
	protected boolean isLeaf(Point3I point) { return isLeaf(point.getX(), point.getY(), point.getZ()); }
	
	protected boolean isLeaf(int x, int y, int z) { return isLeaf(testBlock(x,y,z)); }
	
	protected boolean isLeaf(String type) {
		if ( (type.equalsIgnoreCase("treeLeaves")) || (type.equalsIgnoreCase("leavesRubber")) ) {
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
	}*/

	public boolean isPlanting() {
		return plantSapling;
	}
	
	public boolean isChopping() {
		return chopping;
	}

	/*protected int lastAxe = -1;
	
	private int previousUpgrades;
	
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
		if (forceRecalculate || previousUpgrades != getUpgrades()) {
			forceRecalculate = false;
			previousUpgrades = getUpgrades();
			recalculateChoppingRange();
			update = true;
		}
		
		//update
		if (update) {
			//ProgressiveAutomation.logger.info("Inventory Changed Update");
			scanBlocks();
		}
	}
	
	public int extraSlotCheck(ItemStack item) {
		if (checkSapling(item)) {
			return SLOT_SAPLINGS;
		}
		return super.extraSlotCheck(item);
	}


	/* ISided Stuff */
	/*public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if ( (slot == SLOT_SAPLINGS) && (checkSapling(stack)) ) {
    		return true;
    	}
		return super.isItemValidForSlot(slot, stack);
	}*/
	
	public static boolean checkSapling(ItemStack stack) {
		if (
			//(OreDictionary.getOreID(stack) == OreDictionary.getOreID("treeSapling")) ||
			(stack.getUnlocalizedName().compareToIgnoreCase("tile.mfr.rubberwood.sapling")==0)
		) {
			return true;
		} else {
			return false;
		}
	}
	
	/*private void recalculateChoppingRange() {
		int cuttingSideSize = CUTTING_EXTRA_RANGE +  (int)Math.ceil( (Math.sqrt(getUpgrades() + 1)-1)/2);
		maxCuttingX = this.xCoord + cuttingSideSize;
		minCuttingX = this.xCoord - cuttingSideSize;
		maxCuttingZ = this.zCoord + cuttingSideSize;
		minCuttingZ = this.zCoord - cuttingSideSize;
	}
	
	private boolean isWithinCuttingRange(int x, int z) {
		if (x >= minCuttingX && x <= maxCuttingX && z >= minCuttingZ && z <= maxCuttingZ) 
			return true;
		return false;
	}*/

}
