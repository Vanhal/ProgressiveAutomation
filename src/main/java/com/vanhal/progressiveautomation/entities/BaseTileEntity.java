package com.vanhal.progressiveautomation.entities;

import cofh.api.energy.IEnergyHandler;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.blocks.network.PartialTileNBTUpdateMessage;
import com.vanhal.progressiveautomation.items.ItemRFEngine;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.ref.WrenchModes;
import com.vanhal.progressiveautomation.util.BlockHelper;
import com.vanhal.progressiveautomation.util.Point2I;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BaseTileEntity extends TileEntity implements ISidedInventory, IEnergyHandler {
	protected ItemStack[] slots;
	protected int progress = 0;
	protected int burnLevel = 0;
	
	protected boolean RedstonePowered = false;
	
	//first time looking in this machine, used for displaying help
	protected boolean firstLook = false;
	
	/**
	 * Direction to auto output items to
	 */
	public ForgeDirection extDirection = ForgeDirection.UP;
	public ForgeDirection facing = ForgeDirection.EAST;
	
	public  WrenchModes.Mode sides[] = new WrenchModes.Mode[6];
	
	/**
	 * Sygnalises whether the TileEntity needs to be synced with clients
	 */
	private boolean dirty;
	/**
	 * Tag holding partial updates that will be sent to players upon synchronisation
	 */
	private NBTTagCompound partialUpdateTag = new NBTTagCompound();
	
	protected Random RND = new Random();
	
	//inventory slots variables
	public int SLOT_FUEL = 0;
	public int SLOT_PICKAXE = -1;
	public int SLOT_SHOVEL = -1;
	public int SLOT_AXE = -1;
	public int SLOT_SWORD = -1;
	public int SLOT_HOE = -1;
	public int SLOT_UPGRADE = -1;
	public int SLOT_INVENTORY_START = -1;
	public int SLOT_INVENTORY_END = -1;
	
	public BaseTileEntity(int numSlots) {
		slots = new ItemStack[numSlots+1];
		if (numSlots > 9) {
			SLOT_INVENTORY_START = numSlots - 8;
			SLOT_INVENTORY_END = numSlots;
			//ProgressiveAutomation.logger.info("Start: "+SLOT_INVENTORY_START+" End: "+SLOT_INVENTORY_END);
		} else {
			SLOT_INVENTORY_START = SLOT_INVENTORY_END = numSlots;
		}
		for (int i =0; i < 6; i++) {
			sides[i] = WrenchModes.Mode.Normal;
			if (i==extDirection.ordinal()) sides[i] = WrenchModes.Mode.Output;
		}
	}
	
	protected void setExtDirection(ForgeDirection dir) {
		sides[extDirection.ordinal()] = WrenchModes.Mode.Normal;
		extDirection = dir;
		sides[extDirection.ordinal()] = WrenchModes.Mode.Output;
	}
	
	/**
	 * Do not extend this method, use writeSyncOnlyNBT, writeCommonNBT or writeNonSyncableNBT as needed.
	 */
	@Override
	public final void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		
		writeCommonNBT(nbt);
		writeNonSyncableNBT(nbt);
	}
	
	/**
	 * Do not extend this method, use readSyncOnlyNBT, readCommonNBT or readNonSyncableNBT as needed.
	 */
	@Override
	public final void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		
		readCommonNBT(nbt);
		readNonSyncableNBT(nbt);
	}

	public void readFromItemStack(ItemStack itemStack) {
		if (itemStack == null || itemStack.stackTagCompound == null) {
			return;
		}
		readCommonNBT(itemStack.stackTagCompound);
		readNonSyncableNBT(itemStack.stackTagCompound);
	}

	public void writeToItemStack(ItemStack itemStack) {
		if (itemStack == null ) {
			return;
		}
		if (itemStack.stackTagCompound == null) {
			itemStack.stackTagCompound = new NBTTagCompound();
		}
		writeCommonNBT(itemStack.stackTagCompound);
		writeNonSyncableNBT(itemStack.stackTagCompound);
	}
	
	/**
	 * This overridable method is intended for writing to NBT all data that is used only at runtime
	 * and never saved to hdd.
	 * @param nbt
	 */
	protected void writeSyncOnlyNBT(NBTTagCompound nbt) {
	}
	
	/**
	 * This overridable method is intended for writing to NBT all data that is both saved to hdd
	 * and can be sent to the client TileEntity through S35PacketUpdateTileEntity.
	 * 
	 * WARNING: Do not include slot contents here. 
	 * They are automagically synced when a GUI is opened using S30PacketWindowItems 
	 * @param nbt
	 */
	public void writeCommonNBT(NBTTagCompound nbt) {
		nbt.setInteger("Progress", progress);
		nbt.setInteger("BurnLevel", burnLevel);
		nbt.setBoolean("firstLook", firstLook);
		nbt.setInteger("facing", facing.ordinal());
		int ary[] = new int[6];
		for (int i = 0; i < 6; i++) {
			ary[i] = sides[i].ordinal();
		}
		nbt.setIntArray("sides", ary);
		ary = null;
	}
	
	/**
	 * This overridable method is intended for writing to NBT all data that will 
	 * NOT be synced using S35PacketUpdateTileEntity packets.
	 * 
	 * This includes, but is not limited to, slot contents. 
	 * See writeSyncableNBT for more info.
	 * @param nbt
	 */
	public void writeNonSyncableNBT(NBTTagCompound nbt) {
		
		NBTTagList contents = new NBTTagList();
		for (int i = 0; i < slots.length; i++) {
			if (slots[i] != null) {
				ItemStack stack = slots[i];
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte)i);
				stack.writeToNBT(tag);
				contents.appendTag(tag);
			}
		}
		nbt.setTag("Contents", contents);
	}
	
	/**
	 * This overridable method is intended for reading from NBT all data that is used only at runtime
	 * and never saved to hdd.
	 * @param nbt
	 */
	public void readSyncOnlyNBT(NBTTagCompound nbt) {
	}
	
	/**
	 * This overridable method is intended for reading from NBT all data that is both saved to hdd
	 * and can be sent to the client TileEntity through S35PacketUpdateTileEntity and PartialTileNBTUpdateMessage.
	 * 
	 * WARNING: Please check if the tag exists before reading it. Due to the nature of 
	 * PartialTileNBTUpdateMessage properties that didn't change since the last message
	 * will not be included.
	 * 
	 * WARNING: Do not include slot contents here. 
	 * They are automagically synced when a GUI is opened using S30PacketWindowItems 
	 * @param nbt
	 */
	public void readCommonNBT(NBTTagCompound nbt) {
		
		if (nbt.hasKey("Progress")) progress = nbt.getInteger("Progress");
		if (nbt.hasKey("BurnLevel")) burnLevel = nbt.getInteger("BurnLevel");
		if (nbt.hasKey("firstLook")) firstLook = nbt.getBoolean("firstLook");
		if (nbt.hasKey("facing")) facing = ForgeDirection.getOrientation(nbt.getInteger("facing"));
		if (nbt.hasKey("sides")) {
			int ary[] = nbt.getIntArray("sides");
			for (int i = 0; i<6; i++) {
				sides[i] = WrenchModes.modes.get(ary[i]);
			}
		}
	}
	
	/**
	 * This overridable method is intended for reading from NBT all data that is only saved to hdd 
	 * and never synced between client and server, or is synced using a different method (e.g. inventory 
	 * contents and S30PacketWindowItems)
	 * @param nbt
	 */
	protected void readNonSyncableNBT(NBTTagCompound nbt) {
		
		NBTTagList contents = nbt.getTagList("Contents", 10);
		for (int i = 0; i < contents.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) contents.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot < slots.length) {
				slots[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
	}
	
    /**
     * This method is used to sync data when a GUI is opened. the packet will contain
     * all syncable data.
     */
		@Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeCommonNBT(nbttagcompound);
        this.writeSyncOnlyNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, -1, nbttagcompound);
    }
	
	/**
	 * This method is used to load syncable data when a GUI is opened.
	 */
		@Override    
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
    	this.readCommonNBT(pkt.func_148857_g());
    	this.readSyncOnlyNBT(pkt.func_148857_g());
    }
	
	/**
	 * This method will generate a message with partial updates for this TileEntity.
	 * 
	 * WARNING: Using this method resets the dirty flag and clears all pending updates up to this point.
	 * @return
	 */
	public PartialTileNBTUpdateMessage getPartialUpdateMessage() {
		
		PartialTileNBTUpdateMessage message = new PartialTileNBTUpdateMessage(this.xCoord, this.yCoord, this.zCoord, partialUpdateTag);
		dirty = false;
		partialUpdateTag = new NBTTagCompound();
		
		return message;
	}
	
	/**
	 * Utility method, so you don't have to remember to set dirty to true
	 */
	protected void addPartialUpdate(String fieldName, Integer value) {
		partialUpdateTag.setInteger(fieldName, value);
		dirty = true;
	}
	/**
	 * Utility method, so you don't have to remember to set dirty to true
	 */	
	protected void addPartialUpdate(String fieldName, String value) {
		partialUpdateTag.setString(fieldName, value);
		dirty = true;
	}
	/**
	 * Utility method, so you don't have to remember to set dirty to true
	 */	
	protected void addPartialUpdate(String fieldName, NBTBase value) {
		partialUpdateTag.setTag(fieldName, value);
		dirty = true;
	}
	/**
	 * Utility method, so you don't have to remember to set dirty to true
	 */	
	protected void addPartialUpdate(String fieldName, Boolean value) {
		partialUpdateTag.setBoolean(fieldName, value);
		dirty = true;
	}

	protected void addPartialUpdate(String fieldName, NBTTagCompound value) {
		partialUpdateTag.setTag(fieldName, value);
		dirty = true;
	}

	protected NBTTagCompound getCompoundTagFromPartialUpdate(String fieldName) {
		return partialUpdateTag.getCompoundTag(fieldName);
	}
	
	/**
	 * Whether the TileEntity needs syncing.
	 * @return
	 */
	public boolean isDirty() {
		return dirty;
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (!worldObj.isRemote) {
			if (!isBurning()) {
				RedstonePowered = isIndirectlyPowered();
				if (!RedstonePowered) {
					if (readyToBurn()) {
						if (slots[SLOT_FUEL]!=null) {
							if (isFuel()) {
								burnLevel = progress = getBurnTime();
								addPartialUpdate("Progress", progress);
								addPartialUpdate("BurnLevel", burnLevel);
								
								if (slots[SLOT_FUEL].getItem().hasContainerItem(slots[SLOT_FUEL])) {
									
									slots[SLOT_FUEL] = slots[SLOT_FUEL].getItem().getContainerItem(slots[SLOT_FUEL]);
								} else {
									slots[SLOT_FUEL].stackSize--;
									if (slots[SLOT_FUEL].stackSize==0) {
										slots[SLOT_FUEL] = null;
									}
								}
							} else if (hasEngine()) {
								if (useEnergy(PAConfig.rfCost, false) > 0) {
									if (burnLevel != 1 || progress != 1) {
										//consumed a tick worth of energy
										burnLevel = progress = 1;
										addPartialUpdate("Progress", progress);
										addPartialUpdate("BurnLevel", burnLevel);
									}
								}
							}
						}
					}
				}
			} else {
				//ProgressiveAutomation.logger.info("Ticks Left for fuel: "+progress);
				progress--;
				if (progress<=0) {
					burnLevel = progress = 0;
					addPartialUpdate("BurnLevel", burnLevel);
					if ( (readyToBurn()) && (hasEngine()) ) {
						if (useEnergy(PAConfig.rfCost, false) > 0) {
							//consumed a tick worth of energy
							burnLevel = progress = 1;
						}
					}
				}
				addPartialUpdate("Progress", progress);
			}
			checkForPowerChange();
		}
	}
	
	public int getProgress() {
		return progress;
	}
	
	public void setProgress(int value) {
		progress = value;
	}
	
	public int getBurnLevel() {
		return burnLevel;
	}
	
	public void setBurnLevel(int value) {
		burnLevel = value;
	}
	

	
	/* Inventory methods */
	public int getSizeInventory() {
		return slots.length;
	}

	public ItemStack getStackInSlot(int slot) {
		return slots[slot];
	}

	public ItemStack decrStackSize(int slot, int amt) {
		if (slots[slot] != null) {
			ItemStack newStack;
			if (slots[slot].stackSize <= amt) {
				newStack = slots[slot];
				slots[slot] = null;
			} else {
				newStack = slots[slot].splitStack(amt);
				if (slots[slot].stackSize == 0) {
					slots[slot] = null;
				}
			}
			return newStack;
		}
		return null;
	}

	public ItemStack getStackInSlotOnClosing(int slot) {
		if (slots[slot]!=null) {
			ItemStack stack = slots[slot];
			slots[slot] = null;
			return stack;
		}
		return null;
	}

	public void setInventorySlotContents(int slot, ItemStack stack) {
		slots[slot] = stack;
		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}
	}

	public String getInventoryName() {
		return null;
	}

	public boolean hasCustomInventoryName() {
		return false;
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this &&
		 player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
	}

	public void openInventory() { }

	public void closeInventory() { }

	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if ( (slot==SLOT_FUEL) && (getItemBurnTime(stack)>0) && (ToolHelper.getType(stack)==-1) ) {
     		return true;
    	}
		return false;
	}
	
	public void destroyTool(int slot) {
		if ((slot==-1)||(slots[slot]==null)) return;
		if (ToolHelper.tinkersType(slots[slot].getItem())>=0) {
			addToInventory(slots[slot]);
		} else {
			if (!PAConfig.destroyTools) {
				addToInventory(slots[slot]);
			}
		}
		slots[slot] = null;
	}
	
	//sided things
	public WrenchModes.Mode getSide(ForgeDirection side) {
		return sides[side.ordinal()];
	}
	
	public void setSide(ForgeDirection side, WrenchModes.Mode type) {
		sides[side.ordinal()] = type;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		int[] output = new int[slots.length];
		for (int i=0; i<slots.length; i++) {
			output[i] = i;
		}
		return output;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		if ( (slot<0) || (slot>SLOT_INVENTORY_END) ) return false;
		if (sides[side] == WrenchModes.Mode.Disabled) return false;
		if (sides[side] == WrenchModes.Mode.Output) return false;
		if ( (sides[side] == WrenchModes.Mode.FuelInput) && (slot != SLOT_FUEL) ) return false;
		if ( (sides[side] == WrenchModes.Mode.Input) && (slot == SLOT_FUEL) ) return false;
		
		if ( (slots[slot] != null) 
				&& (slots[slot].isItemEqual(stack))
				&& (ItemStack.areItemStackTagsEqual(stack, slots[slot])) ) {
			int availSpace = this.getInventoryStackLimit() - slots[slot].stackSize;
			if (availSpace>0) {
				return true;
			}
		} else if (isItemValidForSlot(slot, stack)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
		if (sides[side] == WrenchModes.Mode.Disabled) return false;
		if ( (slot>=SLOT_INVENTORY_START) && (slot<=SLOT_INVENTORY_END) ) {
			if ( (sides[side] == WrenchModes.Mode.Normal) || (sides[side] == WrenchModes.Mode.Output) ) return true;
		}
		return false;
	}
	
	public boolean readyToBurn() {
		return true;
	}
	
	public boolean isBurning() {
		return (progress>0);
	}
	
	public float getPercentDone() {
		if ( (isBurning()) && (burnLevel>0) ) {
			float done = (float)progress;//(burnLevel - progress);
			done = done/(float)burnLevel;
			return done;
		} else {
			return 0;
		}
	}
	
	public int getScaledDone(int scale) {
		return (int) Math.floor(scale * getPercentDone());
	}
	
	public int getBurnTime() {
		return getBurnTime(slots[0]);
	}
	
	public int getBurnTime(ItemStack item) {
		return getItemBurnTime(item) / PAConfig.fuelCost;
	}
	
	public static int getItemBurnTime(ItemStack item) {
		if (PAConfig.allowPotatos) {
			if (item.getItem() == Items.potato) return 40;
			else if (item.getItem() == Items.baked_potato) return 80;
		}
		if (item==null) return 0;
		return TileEntityFurnace.getItemBurnTime(item);
	}
	
	public boolean isFuel() {
		return (getBurnTime()>0);
	}
	
	public boolean hasFuel() {
		if (slots[SLOT_FUEL]!=null) {
			if (hasEngine()) {
				if (useEnergy(PAConfig.rfCost, true) > 0) {
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}
	
	/* do some checks for block specific items, must return -1 on failure */
	public int extraSlotCheck(ItemStack item) {
		int targetSlot = -1;
		if (getBurnTime(item)>0) {
			if (slots[0]==null) {
				targetSlot = 0;
			} else if ( (item.isItemEqual(slots[0])) && (ItemStack.areItemStackTagsEqual(item, slots[0])) ) {
				targetSlot = 0;
			}
		}
		return targetSlot;
	}
	
	/* Check the inventory, move any useful items to their correct slots */
	public void checkInventory() {
		if ( (SLOT_INVENTORY_START==-1) || (SLOT_INVENTORY_END==-1) ) return;
		for (int i = SLOT_INVENTORY_START; i <= SLOT_INVENTORY_END; i++) {
			if (slots[i]!=null) {
				int moveTo = extraSlotCheck(slots[i]);
				if (moveTo>=0) {
					slots[i] = moveItemToSlot(slots[i], moveTo);
				}
			}
		}
		//then check if there is any inventories on any of the output sides that we can output to
		for (int x = 0; x < 6; x++) {
			if (sides[x] == WrenchModes.Mode.Output) {
				ForgeDirection testSide = ForgeDirection.getOrientation(x);
				if (BlockHelper.getAdjacentTileEntity(this, testSide) instanceof ISidedInventory) {
					ISidedInventory externalInv = (ISidedInventory) BlockHelper.getAdjacentTileEntity(this, testSide);
					for (int i = SLOT_INVENTORY_START; i <= SLOT_INVENTORY_END; i++) {
						if (slots[i]!=null) {
							addtoSidedExtInventory(externalInv, i);
						}
					}
				} else if (BlockHelper.getAdjacentTileEntity(this, testSide) instanceof IInventory) {
					IInventory externalInv = (IInventory) BlockHelper.getAdjacentTileEntity(this, testSide);
					for (int i = SLOT_INVENTORY_START; i <= SLOT_INVENTORY_END; i++) {
						if (slots[i]!=null) {
							addtoExtInventory(externalInv, i);
						}
					}
				}
			}
		}
	}

	protected ItemStack moveItemToSlot(ItemStack item, int targetSlot) {
		if (slots[targetSlot]==null) {
			slots[targetSlot] = item;
			item = null;
		} else if ( (slots[targetSlot].stackSize < slots[targetSlot].getMaxStackSize())
					&& (slots[targetSlot].isItemEqual(item)) 
					&& (ItemStack.areItemStackTagsEqual(item, slots[targetSlot])) ) {
			int avail = slots[targetSlot].getMaxStackSize() - slots[targetSlot].stackSize;
			if (avail >= item.stackSize) {
				slots[targetSlot].stackSize += item.stackSize;
				item = null;
			} else {
				item.stackSize -= avail;
				slots[targetSlot].stackSize += avail;
			}
		}
		return item;
	}
	
	public boolean addtoExtInventory(IInventory inv, int fromSlot) {
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i)!=null) {
				if ( (inv.getStackInSlot(i).isItemEqual(slots[fromSlot]))
						&& (inv.getStackInSlot(i).stackSize < inv.getStackInSlot(i).getMaxStackSize())
						&& (ItemStack.areItemStackTagsEqual(inv.getStackInSlot(i), slots[fromSlot])) ) {
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
				if ( (inv.getStackInSlot(i)==null) && (inv.isItemValidForSlot(i, slots[fromSlot])) ) {
					inv.setInventorySlotContents(i, slots[fromSlot]);
					slots[fromSlot] = null;
					return true;
				}
			}
		}
		return false;
	}

	public boolean addtoSidedExtInventory(ISidedInventory inv, int fromSlot) {
		int side = extDirection.getOpposite().ordinal();
		int[] trySlots = inv.getAccessibleSlotsFromSide(side);
		int i = 0;
		
		for (int j = 0; j < trySlots.length; j++) {
			i = trySlots[j];
			if (inv.getStackInSlot(i)!=null) {
				if ( (inv.getStackInSlot(i).isItemEqual(slots[fromSlot])) 
						&& (inv.getStackInSlot(i).stackSize < inv.getStackInSlot(i).getMaxStackSize()) 
						&& (ItemStack.areItemStackTagsEqual(inv.getStackInSlot(i), slots[fromSlot])) ) {
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
			for (int j = 0; j < trySlots.length; j++) {
				i = trySlots[j];
				if (inv.canInsertItem(i, slots[fromSlot], side)) {
					if ( (inv.getStackInSlot(i)==null) && (inv.isItemValidForSlot(i, slots[fromSlot])) ) {
						inv.setInventorySlotContents(i, slots[fromSlot]);
						slots[fromSlot] = null;
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean roomInInventory(ItemStack item) {
		if ( (SLOT_INVENTORY_START==-1) || (SLOT_INVENTORY_END==-1) ) return false;
		if (item == null) return false;
		int stackSize = item.stackSize;
		for (int i = SLOT_INVENTORY_START; i <= SLOT_INVENTORY_END; i++) {
			if (slots[i]!=null) {
				if ( (slots[i].isItemEqual(item))
						&& (slots[i].stackSize < slots[i].getMaxStackSize())
						&& (ItemStack.areItemStackTagsEqual(item, slots[i]))) {
					int avail = slots[i].getMaxStackSize() - slots[i].stackSize;
					if (avail >= stackSize) {
						return true;
					} else {
						stackSize -= avail;
					}
				}
			}
		}
		if ( (item != null) && (stackSize>0) ) {
			for (int i = SLOT_INVENTORY_START; i <= SLOT_INVENTORY_END; i++) {
				if (slots[i]==null) {
					return true;
				}
			}
		}
		if (stackSize==0) {
			return true;
		}
		return false;
	}

	public boolean addToInventory(ItemStack item) {
		if ( (SLOT_INVENTORY_START==-1) || (SLOT_INVENTORY_END==-1) ) return false;
		//check to see if this item is something that's used
		int extraSlot = extraSlotCheck(item);
		if (extraSlot>=0) {
			item = moveItemToSlot(item, extraSlot);
		}
		//add it to the main inventory
		for (int i = SLOT_INVENTORY_START; i <= SLOT_INVENTORY_END; i++) {
			if (slots[i]!=null) {
				if (item!=null) {
					if ( (slots[i].isItemEqual(item)) 
							&& (slots[i].stackSize < slots[i].getMaxStackSize())
							&& (ItemStack.areItemStackTagsEqual(item, slots[i])) ) {
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
			for (int i = SLOT_INVENTORY_START; i <= SLOT_INVENTORY_END; i++) {
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
	
	
	//the following are for handling energy
	public boolean hasEngine() {
		if (slots[SLOT_FUEL]==null) return false;
		if (slots[SLOT_FUEL].getItem() instanceof ItemRFEngine) {
			return true;
		}
		return false;
	}
	
	protected ItemRFEngine getEngine() {
		if (hasEngine()) {
			return (ItemRFEngine) slots[SLOT_FUEL].getItem();
		}
		return null;
	}
	
	public boolean canConnectEnergy(ForgeDirection from) {
		if (worldObj.isRemote) return false;
		if (getEngine()==null) return false;
		else return true;
	}

	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		if (worldObj.isRemote) return 0;
		return addEnergy(maxReceive, simulate);
	}

	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return 0;
	}
	
	protected int useEnergy(int amount, boolean simulate) {
		ItemRFEngine engine = getEngine();
		if (getEngine()==null) return 0;
		
		int energyExtracted = Math.min(engine.getCharge(slots[SLOT_FUEL]), amount);

		if (energyExtracted!=amount) return 0;
		
		if (!simulate) {
			engine.addCharge(slots[SLOT_FUEL], (energyExtracted * -1));
		}
		return energyExtracted;
	}
	
	protected int addEnergy(int amount, boolean simulate) {
		ItemRFEngine engine = getEngine();
		if (getEngine()==null) return 0;
		
		int energyReceived = Math.min(engine.getMaxCharge() - engine.getCharge(slots[SLOT_FUEL]), Math.min(amount, PAConfig.rfRate));

		if (!simulate) {
			engine.addCharge(slots[SLOT_FUEL], energyReceived);
		}
		return energyReceived;
	}

	public int getEnergyStored(ForgeDirection from) {
		if (slots[SLOT_FUEL].getItem() instanceof ItemRFEngine) {
			return getEngine().getCharge(slots[SLOT_FUEL]);
		} else {
			return 0;
		}
	}

	public int getMaxEnergyStored(ForgeDirection from) {
		if (slots[SLOT_FUEL].getItem() instanceof ItemRFEngine) {
			return getEngine().getMaxCharge();
		} else {
			return 0;
		}
	}
	
	//this will just update the blocks around if the rfengine is added to a machine
	protected boolean lastEngine = false;
	protected void checkForPowerChange() {
		if ( ( (!lastEngine) && (hasEngine()) ) ||
				( (lastEngine) && (!hasEngine()) ) ) {
			lastEngine = hasEngine();
			notifyUpdate();
		}
	}
	
	protected void notifyUpdate() {
		Block minerBlock = worldObj.getBlock(xCoord, yCoord, zCoord);
		worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, minerBlock);
	}
	
	public void setLooked() {
		if (!firstLook) {
			firstLook = true;
			addPartialUpdate("firstLook", firstLook);
		}
	}
	
	public boolean isLooked() {
		return firstLook;
	}
	
	public Point2I spiral(int n, int x, int y) {
		return spiral(n, x, y, facing);
	}
	
	//my function to get a point on a spiral around the block
	public static Point2I spiral(int n, int x, int y, ForgeDirection direction) {
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

		if (direction == ForgeDirection.NORTH)
			return new Point2I(x + dy, y - dx);
		else if (direction == ForgeDirection.SOUTH)
			return new Point2I(x + dy, y + dx);
		else if (direction == ForgeDirection.EAST)
			return new Point2I(x + dx, y + dy);
		else
			return new Point2I(x - dx, y + dy);
	}
	
	//check if machine is powered
	protected boolean isIndirectlyPowered() {
        return worldObj.getIndirectPowerOutput(xCoord, yCoord - 1, zCoord, 0) ? true : 
        	(worldObj.getIndirectPowerOutput(xCoord, yCoord + 1, zCoord, 1) ? true : 
        	(worldObj.getIndirectPowerOutput(xCoord, yCoord, zCoord - 1, 2) ? true : 
        	(worldObj.getIndirectPowerOutput(xCoord, yCoord, zCoord + 1, 3) ? true : 
        	(worldObj.getIndirectPowerOutput(xCoord + 1, yCoord, zCoord, 5) ? true : 
        	(worldObj.getIndirectPowerOutput(xCoord - 1, yCoord, zCoord, 4) ? true : 
        	(worldObj.getIndirectPowerOutput(xCoord, yCoord + 2, zCoord, 1) ? true : 
        	(worldObj.getIndirectPowerOutput(xCoord, yCoord + 1, zCoord - 1, 2) ? true : 
        	(worldObj.getIndirectPowerOutput(xCoord, yCoord + 1, zCoord + 1, 3) ? true : 
        	(worldObj.getIndirectPowerOutput(xCoord - 1, yCoord + 1, zCoord, 4) ? true : 
        	worldObj.getIndirectPowerOutput(xCoord + 1, yCoord + 1, zCoord, 5))))))))));
    }
}
