package com.vanhal.progressiveautomation.entities.chopper;

import java.util.List;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.compat.ModHelper;
import com.vanhal.progressiveautomation.entities.UpgradeableTileEntity;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.util.CoordList;
import com.vanhal.progressiveautomation.util.OreHelper;
import com.vanhal.progressiveautomation.util.Point2I;
import com.vanhal.progressiveautomation.util.Point3I;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.IPlantable;


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
	public int SLOT_SHEARS = 4;
	
	public TileChopper() {
		super(13);
		setUpgradeLevel(ToolHelper.LEVEL_WOOD);
		setAllowedUpgrades(UpgradeType.WOODEN, UpgradeType.WITHER);
		allowSheer();
		
		forceRecalculate = true;
		
		//slots
		SLOT_AXE = 2;
		SLOT_UPGRADE = 3;
	}
	
	protected boolean removeShears = false;
	protected void allowSheer() {
		if ( (PAConfig.shearTrees) && (PAConfig.allowShearingUpgrade) ) {
			if (!isAllowedUpgrade(UpgradeType.SHEARING)) {
				addAllowedUpgrade(UpgradeType.SHEARING);
			}
		} else {
			removeShears = true;
		}
	}
	
	@Override
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
		if (removeShears) {
			if (slots[SLOT_SHEARS]!=null) slots[SLOT_SHEARS] = null;
			if (hasUpgrade(UpgradeType.SHEARING)) removeUpgradeCompletely(UpgradeType.SHEARING);
			removeShears = false;
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
			
			// Pause if we're full and told to
			if (isFull()) return;

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
				
				BlockPos currentPosition = currentBlock.toPosition();
				if (validBlock(currentPosition)) {
					
					boolean targetTree = isTree(currentPosition);
					
					//I'm fairly sure this doesn't actually do anything, but gonna leave it here anyway
					int fortuneLevel = 0;
					if (targetTree) {
						fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, slots[SLOT_AXE]);

					}
	
					
					//then break the block
					IBlockState actualBlockState = worldObj.getBlockState(currentPosition);
					Block actualBlock = actualBlockState.getBlock();
					int metaData = actualBlock.getMetaFromState(actualBlockState);
					List<ItemStack> items = actualBlock.getDrops(worldObj, currentPosition, actualBlockState, fortuneLevel);

					
					if ( (!targetTree) && (slots[SLOT_SHEARS]!=null) && (hasUpgrade(UpgradeType.SHEARING)) ) {
						int i = 0;
				        Item item = Item.getItemFromBlock(actualBlock);
				        if (item != null && item.getHasSubtypes()) i = metaData;
						items.add(new ItemStack(item, 1, i));
						if (ToolHelper.damageTool(slots[SLOT_SHEARS], worldObj, currentBlock.getX(), currentBlock.getY(), currentBlock.getZ())) {
							destroyTool(SLOT_SHEARS);
						}
					} else {
						items = actualBlock.getDrops(worldObj, currentBlock.toPosition(), actualBlockState, fortuneLevel);
					}
					
					//get the drops
					for (ItemStack item : items) {
						addToInventory(item);
					}
					
					//if we're chopping a tree, then damage the tool, otherwise don't
					if (targetTree) {
						if (ToolHelper.damageTool(slots[SLOT_AXE], worldObj, currentBlock.getX(), currentBlock.getY(), currentBlock.getZ())) {
							destroyTool(SLOT_AXE);
						}
					}
					
					//remove the block and entity if there is one
					worldObj.removeTileEntity( currentPosition );
					worldObj.setBlockToAir(currentPosition);
				}
				currentBlock = null;
				
			} else { // chop some more?
				choppingTime--;
			}
		} else { //start cutting something why don't we?
			if (blockList.size()>0) {
				currentBlock = blockList.pop();
				
				BlockPos currentPosition = currentBlock.toPosition();
				
				if (validBlock(currentPosition)) {
					IBlockState actualBlockState = worldObj.getBlockState(currentPosition);
					Block actualBlock = actualBlockState.getBlock();
					int metaData = actualBlock.getMetaFromState(actualBlockState);
					
					choppingTime = (int)Math.ceil( actualBlock.getBlockHardness(actualBlockState, worldObj, currentPosition) * 1.5 * 20 );
					
					Item tool = (Item)slots[SLOT_AXE].getItem();
					
					float choppingSpeed = 1.0f;
	
					if (isTree(currentPosition)) {
						choppingSpeed = ToolHelper.getDigSpeed( slots[SLOT_AXE], actualBlockState );
						//check for efficiency on the tool, only for the wood though!
						int eff = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, slots[SLOT_AXE]);
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
		Point2I p1 = spiral(n + 2, getPos().getX(), getPos().getZ());
		//see if we already have the block first
		Point3I point = new Point3I(p1.getX(), getPos().getY(), p1.getY());
		if (!blockList.inList(point)) {
			if (validBlock(point.toPosition())) {
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
		if (validBlock(point.toPosition())) {
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
	
	protected boolean validBlock(BlockPos point) {
		if (!worldObj.isAirBlock(point)) {
			if (isTree(point)) return true;
			else return isLeaf(point);
		}
		return false;
	}
	
	protected boolean validBlock(int x, int y, int z) {
		return validBlock(new BlockPos(x, y, z));
	}
	
	protected boolean isTree(BlockPos point) { 
		return (OreHelper.testOreBlock("logWood", point, worldObj)) || 
				(OreHelper.testOreBlock("woodRubber", point, worldObj)) ||
				isTree(testBlock(point));
	}
	
	protected boolean isTree(int x, int y, int z) { return isTree(new BlockPos(x,y,z)); }
	
	protected boolean isTree(String type) {
		if ( (type.equalsIgnoreCase("logWood")) || (type.equalsIgnoreCase("woodRubber")) ) {
			return true;
		}
		return false;
	}
	
	protected boolean isLeaf(BlockPos point) { 
		return (OreHelper.testOreBlock("treeLeaves", point, worldObj)) || 
				(OreHelper.testOreBlock("leavesRubber", point, worldObj)) ||
				isLeaf(testBlock(point));
	}
	
	
	protected boolean isLeaf(int x, int y, int z) { return isLeaf(new BlockPos(x,y,z)); }
	
	protected boolean isLeaf(String type) {
		if ( (type.equalsIgnoreCase("treeLeaves")) || (type.equalsIgnoreCase("leavesRubber")) ) {
			return true;
		} 
		return false;
	}
	
	protected String testBlock(BlockPos pos) {
		IBlockState _blockState = worldObj.getBlockState(pos);
		Block _block = _blockState.getBlock();
		int metaData = _block.getMetaFromState(_blockState);
		ItemStack testItem = new ItemStack(_block, 1, metaData);
		if (ModHelper.isLeaf(testItem)) return "treeLeaves";
		if (ModHelper.isLog(testItem)) return "logWood";
		return "Unknown";
	}
	
	@Override
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
				Point2I p1 = spiral(n + 2, getPos().getX(), getPos().getZ());
				//ProgressiveAutomation.logger.debug("Plant: "+p1.getX()+", "+getPos().getY()+", "+p1.getY());
				if (Block.getBlockFromItem(slots[SLOT_SAPLINGS].getItem()) instanceof IPlantable) {
					Block tree = (Block)Block.getBlockFromItem(slots[SLOT_SAPLINGS].getItem());
					BlockPos plantPos = new BlockPos(p1.getX(), getPos().getY(), p1.getY());
					if ( (tree.canPlaceBlockAt(worldObj, plantPos)) && 
							(worldObj.getBlockState(plantPos).getBlock().canReplace(worldObj, plantPos, EnumFacing.DOWN, slots[SLOT_SAPLINGS])) &&
							(worldObj.getBlockState(plantPos).getBlock() != tree)
							) {
						if (doAction) {
							worldObj.setBlockState(plantPos, 
									tree.getStateFromMeta(slots[SLOT_SAPLINGS].getItem().getDamage(slots[SLOT_SAPLINGS])), 7);
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

	public boolean isPlanting() {
		return plantSapling;
	}
	
	public boolean isChopping() {
		return chopping;
	}

	protected int lastAxe = -1;
	
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
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if ( (slot == this.SLOT_SHEARS) && (stack.getItem() == Items.SHEARS) && (hasUpgrade(UpgradeType.SHEARING)) ) {
			return true;
		} else if ( (slot == SLOT_SAPLINGS) && (checkSapling(stack)) ) {
    		return true;
    	}
		return super.isItemValidForSlot(slot, stack);
	}
	
	public static boolean checkSapling(ItemStack stack) {
		if (ModHelper.checkSapling(stack)) {
			return true;
		} else {
			return false;
		}
	}
	
	private void recalculateChoppingRange() {
		int cuttingSideSize = CUTTING_EXTRA_RANGE +  (int)Math.ceil( (Math.sqrt(getUpgrades() + 1)-1)/2);
		maxCuttingX = this.getPos().getX() + cuttingSideSize;
		minCuttingX = this.getPos().getX() - cuttingSideSize;
		maxCuttingZ = this.getPos().getZ() + cuttingSideSize;
		minCuttingZ = this.getPos().getZ() - cuttingSideSize;
	}
	
	private boolean isWithinCuttingRange(int x, int z) {
		if (x >= minCuttingX && x <= maxCuttingX && z >= minCuttingZ && z <= maxCuttingZ) 
			return true;
		return false;
	}
	
	@Override
	protected Point3I adjustedSpiral(int n) {
		Point3I point = super.adjustedSpiral(n + 1);
		return point;
	}

}
