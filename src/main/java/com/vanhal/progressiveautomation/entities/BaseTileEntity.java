package com.vanhal.progressiveautomation.entities;

import java.util.Random;

import javax.annotation.Nonnull;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.blocks.network.PartialTileNBTUpdateMessage;
import com.vanhal.progressiveautomation.items.ItemRFEngine;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.ref.WrenchModes;
import com.vanhal.progressiveautomation.util.BlockHelper;
import com.vanhal.progressiveautomation.util.Point2I;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class BaseTileEntity extends TileEntity implements ISidedInventory, IEnergyStorage, IEnergyProvider, IEnergyReceiver, ITickable {
	protected ItemStack[] slots;
	protected int progress = 0;
	protected int burnLevel = 0;
	protected boolean inventoryFull = false;
	
	protected boolean RedstonePowered = false;
	
	//first time looking in this machine, used for displaying help
	protected boolean firstLook = false;
	
	/**
	 * Direction to auto output items to
	 */
	public EnumFacing extDirection = EnumFacing.UP;
	public EnumFacing facing = EnumFacing.EAST;
	
	public  WrenchModes.Mode sides[] = new WrenchModes.Mode[6];
	
	/**
	 * Signalises whether the TileEntity needs to be synced with clients
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
	
	protected void setExtDirection(EnumFacing dir) {
		sides[extDirection.ordinal()] = WrenchModes.Mode.Normal;
		extDirection = dir;
		sides[extDirection.ordinal()] = WrenchModes.Mode.Output;
	}
	
	/**
	 * Do not extend this method, use writeSyncOnlyNBT, writeCommonNBT or writeNonSyncableNBT as needed.
	 * @return 
	 */
	@Override
	public final NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt = super.writeToNBT(nbt);
		
		writeCommonNBT(nbt);
		writeNonSyncableNBT(nbt);
		return nbt;
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
		if (itemStack == null || itemStack.getTagCompound() == null) {
			return;
		}
		readCommonNBT(itemStack.getTagCompound());
		readNonSyncableNBT(itemStack.getTagCompound());
	}

	public void writeToItemStack(ItemStack itemStack) {
		if (itemStack == null ) {
			return;
		}
		if (itemStack.getTagCompound() == null) {
			itemStack.setTagCompound(new NBTTagCompound());
		}
		writeCommonNBT(itemStack.getTagCompound());
		writeNonSyncableNBT(itemStack.getTagCompound());
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
		nbt.setBoolean("inventoryFull", inventoryFull);
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
		if (nbt.hasKey("inventoryFull")) inventoryFull = nbt.getBoolean("inventoryFull");
		if (nbt.hasKey("firstLook")) firstLook = nbt.getBoolean("firstLook");
		if (nbt.hasKey("facing")) facing = EnumFacing.getFront(nbt.getInteger("facing"));
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
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeCommonNBT(nbttagcompound);
        this.writeSyncOnlyNBT(nbttagcompound);
        return new SPacketUpdateTileEntity(new BlockPos(getPos().getX(), getPos().getY(), getPos().getZ()), -1, nbttagcompound);
    }
	
	/**
	 * This method is used to load syncable data when a GUI is opened.
	 */
	@Override    
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readCommonNBT(pkt.getNbtCompound());
    	this.readSyncOnlyNBT(pkt.getNbtCompound());
    }
	
	/**
	 * This method will generate a message with partial updates for this TileEntity.
	 * 
	 * WARNING: Using this method resets the dirty flag and clears all pending updates up to this point.
	 * @return
	 */
	public PartialTileNBTUpdateMessage getPartialUpdateMessage() {
		
		PartialTileNBTUpdateMessage message = new PartialTileNBTUpdateMessage(getPos().getX(), getPos().getY(), getPos().getZ(), partialUpdateTag);
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
	
	@Override
	public void update() {
		if (!worldObj.isRemote) {
			if (isFull()) return;
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
									//if the container is no longer fuel then pump it into the internal inventory
									if (!isFuel()) {
										moveToInventoryOrDrop(SLOT_FUEL);
									}
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
	
	private void setInventoryFull(boolean state) {
		if (SLOT_INVENTORY_START == SLOT_INVENTORY_END)
			return;
		
		if (inventoryFull != state) {
			inventoryFull = state;
			addPartialUpdate("inventoryFull", inventoryFull);
		}
	}

	
	/* Inventory methods */
	@Override
	public int getSizeInventory() {
		return slots.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return slots[slot];
	}

	@Override
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

	@Override
	public ItemStack removeStackFromSlot(int slot) {
		if (slots[slot]!=null) {
			ItemStack stack = slots[slot];
			slots[slot] = null;
			return stack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		slots[slot] = stack;
		if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
			stack.stackSize = this.getInventoryStackLimit();
		}
	}

	// Do we have available inventory spaces and do we care?
	public boolean isFull() {
        return PAConfig.pauseOnFullInventory && SLOT_INVENTORY_START != SLOT_INVENTORY_END && inventoryFull;
	}
	
	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(getPos()) == this &&
		 player.getDistanceSq(getPos().getX() + 0.5, getPos().getY() + 0.5, getPos().getZ() + 0.5) < 64;
	}

	@Override
	public void openInventory(EntityPlayer playerIn) {}

	@Override
	public void closeInventory(EntityPlayer playerIn) {}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return this.isItemValidForSlot(slot, stack, false);
	}
	
	public boolean isItemValidForSlot(int slot, ItemStack stack, boolean internalStorage) {
		if ( (slot==SLOT_FUEL) && (getItemBurnTime(stack)>0) && (ToolHelper.getType(stack)==-1) ) {
     		return true;
    	}
		if ( ( (slot >= SLOT_INVENTORY_START) && (slot <= SLOT_INVENTORY_END) ) && 
				(SLOT_INVENTORY_START!=SLOT_INVENTORY_END) && (internalStorage) ) {
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
	public WrenchModes.Mode getSide(EnumFacing side) {
		return sides[side.ordinal()];
	}
	
	public void setSide(EnumFacing side, WrenchModes.Mode type) {
		sides[side.ordinal()] = type;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		int[] output = new int[slots.length];
		for (int i=0; i<slots.length; i++) {
			output[i] = i;
		}
		return output;
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing face) {
		if ( (slot<0) || (slot>SLOT_INVENTORY_END) ) return false;
		if (face!=null) {
			if (sides[face.ordinal()] == WrenchModes.Mode.Disabled) return false;
			if (sides[face.ordinal()] == WrenchModes.Mode.Output) return false;
			if ( (sides[face.ordinal()] == WrenchModes.Mode.FuelInput) && (slot != SLOT_FUEL) ) return false;
			if ( (sides[face.ordinal()] == WrenchModes.Mode.Input) && (slot == SLOT_FUEL) ) return false;
		}
		
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
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing face) {
		if (face!=null)
			if (sides[face.ordinal()] == WrenchModes.Mode.Disabled) return false;
		if ( (slot>=SLOT_INVENTORY_START) && (slot<=SLOT_INVENTORY_END) ) {
			if ( (sides[face.ordinal()] == WrenchModes.Mode.Normal) || (sides[face.ordinal()] == WrenchModes.Mode.Output) ) return true;
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
		if (PAConfig.fuelCost == 0) return 0;
		return getItemBurnTime(item) / PAConfig.fuelCost;
	}
	
	public static int getItemBurnTime(ItemStack item) {
		if (PAConfig.allowPotatos) {
			if (item.getItem() == Items.POTATO) return 40;
			else if (item.getItem() == Items.BAKED_POTATO) return 80;
		}
		if ( (item == null) || (item.getItem() == null) ) return 0;
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
		
		boolean openSlots = false;
		for (int i = SLOT_INVENTORY_START; i <= SLOT_INVENTORY_END; i++) {
			if (slots[i]!=null) {
				int moveTo = extraSlotCheck(slots[i]);
				if (moveTo>=0) {
					slots[i] = moveItemToSlot(slots[i], moveTo);
				}
			}
			if (slots[i]==null && !openSlots) {
				openSlots = true;
			}
		}
		//then check if there is any inventories on any of the output sides that we can output to
		for(EnumFacing facing : EnumFacing.values()) {
			if (sides[facing.ordinal()] == WrenchModes.Mode.Output) {
				TileEntity tile = worldObj.getTileEntity(pos.offset(facing));
				if (tile != null) {
					if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite())) {
						IItemHandler inv = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
						for (int i = SLOT_INVENTORY_START; i <= SLOT_INVENTORY_END; i++) {
							if (slots[i]!=null && addtoExtInventory(inv, i)) {
								openSlots = true;
							}
						}
					} else if (tile instanceof ISidedInventory) {
						ISidedInventory externalInv = (ISidedInventory) tile;
						for (int i = SLOT_INVENTORY_START; i <= SLOT_INVENTORY_END; i++) {
							if (slots[i]!=null && addtoSidedExtInventory(externalInv, i)) {
								openSlots = true;
							}
						}
					} else if (tile instanceof IInventory) {
						IInventory externalInv = (IInventory) tile;
						for (int i = SLOT_INVENTORY_START; i <= SLOT_INVENTORY_END; i++) {
							if (slots[i]!=null && addtoExtInventory(externalInv, i)) {
								openSlots = true;
							}
						}
					}
				}
			}
		}
		setInventoryFull( !openSlots );
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
	
	public boolean addtoExtInventory(IItemHandler inv, int fromSlot) {
		if (ItemHandlerHelper.insertItemStacked(inv, slots[fromSlot], true) != slots[fromSlot]) {
			slots[fromSlot] = ItemHandlerHelper.insertItemStacked(inv, slots[fromSlot], false);
		}
		return false;
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
		int[] trySlots = inv.getSlotsForFace(extDirection.getOpposite());
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
				if (inv.canInsertItem(i, slots[fromSlot], extDirection.getOpposite())) {
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
		boolean hasRoom = false;

		for (int i = SLOT_INVENTORY_START; i <= SLOT_INVENTORY_END; i++) {
			if (slots[i]==null) {
				hasRoom = true;
				setInventoryFull( false );
			}
			else if (stackSize > 0 
					&& slots[i].isItemEqual(item) 
					&& (ItemStack.areItemStackTagsEqual(item, slots[i]))) {
				stackSize = Math.max( stackSize - (slots[i].getMaxStackSize() - slots[i].stackSize), 0 );
			}

			if (hasRoom || stackSize == 0) return true;
		}
		
		setInventoryFull( true );
		return false;
	}

	public boolean addToInventory(ItemStack item) {
		if (item == null) return false;
		if ( (SLOT_INVENTORY_START==-1) || (SLOT_INVENTORY_END==-1) ) return false;
		//check to see if this item is something that's used		
		int extraSlot = extraSlotCheck(item);
		if (extraSlot>=0) {
			item = moveItemToSlot(item, extraSlot);
			if (item == null) return true;
		}
		int emptySlot = -1;
		
		//add it to the main inventory
		for (int i = SLOT_INVENTORY_START; i <= SLOT_INVENTORY_END; i++) {
			if (slots[i]==null) {
				// Remember this empty slot in case there aren't enough partial slots
				if (emptySlot == -1 
					&& this.isItemValidForSlot(i, item, true)) {
					emptySlot = i;
					setInventoryFull( false );
				}
				continue;
			}
			if (item == null || item.stackSize == 0) {
				continue;
			}

			if (slots[i].isItemEqual(item) 
				&& slots[i].stackSize < slots[i].getMaxStackSize()
				&& ItemStack.areItemStackTagsEqual(item, slots[i]) ) {

				int avail = slots[i].getMaxStackSize() - slots[i].stackSize;
				if (avail >= item.stackSize) {
					slots[i].stackSize += item.stackSize;
					item.stackSize = 0;
					item = null;
				} else {
					item.stackSize -= avail;
					slots[i].stackSize += avail;
				}
			}
		}

		if (emptySlot == -1)
			setInventoryFull( true );
		
		if (item == null || item.stackSize == 0) {
			item = null;
			return true;
		}
		
		// If we've got an empty slot available, drop it in there
		if (emptySlot != -1) {
			slots[emptySlot] = item;
			item = null;
			return true;
		}
		
		// If we still have an item, drop in on the ground, unless the config says to delete it.
		if (item!=null) {
			if (PAConfig.allowInventoryOverflow)
				dropItem(item);
			item = null;
		}

		return false;
	}
	
	public void moveToInventoryOrDrop(int slot) {
		if (!worldObj.isRemote) {
			//move directly to an output side first if possible
			for (int x = 0; x < 6; x++) {
				if (sides[x] == WrenchModes.Mode.Output) {
					EnumFacing testSide = EnumFacing.getFront(x);
					if (BlockHelper.getAdjacentTileEntity(this, testSide) instanceof ISidedInventory) {
						ISidedInventory externalInv = (ISidedInventory) BlockHelper.getAdjacentTileEntity(this, testSide);
						if (slots[slot]!=null) {
							addtoSidedExtInventory(externalInv, slot);
						}
					} else if (BlockHelper.getAdjacentTileEntity(this, testSide) instanceof IInventory) {
						IInventory externalInv = (IInventory) BlockHelper.getAdjacentTileEntity(this, testSide);
						if (slots[slot]!=null) {
							addtoExtInventory(externalInv, slot);
						}
					}
				}
			}
			//try the internal inventory
			//We'll need to make a copy of it so that it doesn't duplicate at this point
			if (slots[slot]!=null) {
				ItemStack item = slots[slot].copy();
				slots[slot] = null;
				
				if (item!=null) {
					addToInventory(item);
				}
				
				//finally if it hasn't already been dropped, drop it
				if ( (item!=null) && ( (SLOT_INVENTORY_START==-1) || (SLOT_INVENTORY_END==-1) ) ) {
					dropItem(item);
				}
			}
			
		}
	}	
	public void dropItem(ItemStack item) {
		if (item!=null) {
			EntityItem entItem = new EntityItem(worldObj, pos.getX() + 0.5f, pos.getY() + 1.5f, pos.getZ() + 0.5f, item);
			float f3 = 0.05F;
			entItem.motionX = (double)((float)worldObj.rand.nextGaussian() * f3);
			entItem.motionY = (double)((float)worldObj.rand.nextGaussian() * f3 + 0.2F);
			entItem.motionZ = (double)((float)worldObj.rand.nextGaussian() * f3);
			worldObj.spawnEntityInWorld(entItem);
		}
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
	
	@Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nonnull EnumFacing facing) {
       	if ( capability == CapabilityEnergy.ENERGY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY )
       		return true;
       	return super.hasCapability(capability, facing);
    }
	
	@Override
    @Nonnull
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nonnull EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) 
        	return CapabilityEnergy.ENERGY.cast(this);
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        	return (T) new SidedInvWrapper(this, facing);
        return super.getCapability(capability, facing);
    }
	

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		return this.receiveEnergy(EnumFacing.UP, maxReceive, simulate);
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		return this.extractEnergy(EnumFacing.UP, maxExtract, simulate);
	}

	@Override
	public int getEnergyStored() {
		return this.getEnergyStored(EnumFacing.UP);
	}

	@Override
	public int getMaxEnergyStored() {
		return this.getMaxEnergyStored(EnumFacing.UP);
	}

	@Override
	public boolean canExtract() {
		return this.extractEnergy(1, true)>0;
	}

	@Override
	public boolean canReceive() {
		return this.receiveEnergy(1, true)>0;
	}
	
	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		if (getEngine()==null) return false;
		else return true;
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		if (worldObj.isRemote) return 0;
		return addEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
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

	public int getEnergyStored(EnumFacing from) {
		if ( hasEngine() ) {
			return getEngine().getCharge(slots[SLOT_FUEL]);
		} else {
			return 0;
		}
	}

	public int getMaxEnergyStored(EnumFacing from) {
		if ( hasEngine() ) {
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
		Block minerBlock = worldObj.getBlockState(pos).getBlock();
		worldObj.notifyNeighborsOfStateChange(getPos(), minerBlock);
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
	public static Point2I spiral(int n, int x, int y, EnumFacing direction) {
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

		if (direction == EnumFacing.NORTH)
			return new Point2I(x + dy, y - dx);
		else if (direction == EnumFacing.SOUTH)
			return new Point2I(x + dy, y + dx);
		else if (direction == EnumFacing.EAST)
			return new Point2I(x + dx, y + dy);
		else
			return new Point2I(x - dx, y - dy);
	}


	//clear the internal inventory (I have a feeling this is used for pooling but I can't be sure)
	@Override
	public void clear() {
		for (int i = 0; i < this.slots.length; ++i) {
            this.slots[i] = null;
        }
		setInventoryFull( false );
	}

	//WHAT THE HELL ARE THESE??????
	//They look like they're possibly used for GUI stuff?
	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	
	//check if machine is powered
	protected boolean isIndirectlyPowered() {
		EnumFacing[] aenumfacing = EnumFacing.values();
        int i = aenumfacing.length;
        int j;

        for (j = 0; j < i; ++j) {
            EnumFacing enumfacing1 = aenumfacing[j];

            if (worldObj.isSidePowered(pos.offset(enumfacing1), enumfacing1)) {
                return true;
            }
        }
        return false;
    }
	
	public int getX() {
		return pos.getX();
	}
	
	public int getY() {
		return pos.getY();
	}
	
	public int getZ() {
		return pos.getZ();
	}
	
	public World getWorldObj() {
		return this.worldObj;
	}

	
}
