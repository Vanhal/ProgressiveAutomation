package com.vanhal.progressiveautomation.entities;

import com.google.common.base.Enums;
import com.vanhal.progressiveautomation.items.upgrades.ItemUpgrade;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import java.util.*;

public class UpgradeableTileEntity extends BaseTileEntity implements IUpgradeable {
	protected int toolLevel = ToolHelper.LEVEL_WOOD;

	private Map<UpgradeType, Integer> installedUpgrades;
	private Set<UpgradeType> allowedUpgrades;

	public UpgradeableTileEntity(int numSlots) {
		super(numSlots);
		installedUpgrades = new EnumMap<UpgradeType, Integer>(UpgradeType.class);
		allowedUpgrades = Collections.emptySet();
	}
	
	public void writeCommonNBT(NBTTagCompound nbt) {
		super.writeCommonNBT(nbt);

		// Save upgrades
		NBTTagCompound tag = new NBTTagCompound();
		for (Map.Entry<UpgradeType, Integer> mapEntry : installedUpgrades.entrySet()) {
			tag.setInteger(mapEntry.getKey().name(), mapEntry.getValue());
		}
		nbt.setTag("installedUpgrades", tag);
	}

	public void readCommonNBT(NBTTagCompound nbt) {
		super.readCommonNBT(nbt);
		readLegacyNBT(nbt);

		// Load upgrades
		NBTTagCompound tag = nbt.getCompoundTag("installedUpgrades");
		if (tag != null) {
			// func_150296_c() returns a set of all tag names inside the NBTTagCompound
			for (Object key : tag.func_150296_c()) {
				String upgradeName = (String) key;
				installedUpgrades.put(UpgradeType.valueOf(upgradeName), tag.getInteger(upgradeName));
			}
		}
	}

	/**
	 * Method kept for compatibility reasons. Needs to be removed at a later date.
	 * Minecraft must load chunks with placed machines (and afterwards, save the world)
	 * for legacy NBT to convert into the new map.
	 * @param nbt NBTTagCompound
	 */
	@Deprecated
	private void readLegacyNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("NumUpgrades") && nbt.getInteger("NumUpgrades") > 0)
			installedUpgrades.put(UpgradeType.getRangeUpgrade(toolLevel), nbt.getInteger("NumUpgrades"));
		if (nbt.hasKey("hasWitherUpgrade") && nbt.getInteger("hasWitherUpgrade") > 0)
			installedUpgrades.put(UpgradeType.WITHER, nbt.getInteger("hasWitherUpgrade"));
		if (nbt.hasKey("hasCobbleUpgrade") && nbt.getInteger("hasCobbleUpgrade") > 0)
			installedUpgrades.put(UpgradeType.COBBLE_GEN, nbt.getInteger("hasCobbleUpgrade"));
		if (nbt.hasKey("hasFillerUpgrade") && nbt.getInteger("hasFillerUpgrade") > 0)
			installedUpgrades.put(UpgradeType.FILLER, nbt.getInteger("hasFillerUpgrade"));
	}
	
	/* IUpgradeable methods */
	@Override
	public boolean hasUpgrade(UpgradeType type) {
		return installedUpgrades.get(type) != null;
	}

	@Override
	public Integer getUpgradeAmount(UpgradeType type) {
		Integer upgradeAmount = installedUpgrades.get(type);
		return upgradeAmount != null ? upgradeAmount : 0;
	}

	@Override
	public void addUpgrade(UpgradeType type, Integer amount) {
		setUpgradeAmount(type, getUpgradeAmount(type) + amount);
	}

	@Override
	public void setUpgradeAmount(UpgradeType type, Integer amount) {
		installedUpgrades.put(type, amount);

		NBTTagCompound tag = getCompoundTagFromPartialUpdate("installedUpgrades");
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		tag.setInteger(type.name(), amount);
		addPartialUpdate("installedUpgrades", tag);
	}

	@Override
	public void removeUpgradeCompletely(UpgradeType type) {
		installedUpgrades.remove(type);

		NBTTagCompound tag = getCompoundTagFromPartialUpdate("installedUpgrades");
		if (tag == null) {
			tag = new NBTTagCompound();
		}
		tag.setInteger(type.name(), 0);
		addPartialUpdate("upgrades", tag);
	}

	@Override
	public Set<UpgradeType> getInstalledUpgradeTypes() {
		return installedUpgrades.keySet();
	}

	public int getUpgrades() {
		return getUpgradeAmount(UpgradeType.getRangeUpgrade(toolLevel));
	}

	public void setUpgrades(int value) {
		UpgradeType type = UpgradeType.getRangeUpgrade(toolLevel);
		setUpgradeAmount(type, value);
	}

	public void addUpgrades(int addValue) {
		addUpgrade(UpgradeType.getRangeUpgrade(toolLevel), addValue);
	}

	public int getRange() {
		int range = (getUpgrades() * PAConfig.upgradeRange) + 1;
		if (hasUpgrade(UpgradeType.WITHER)) range = range * PAConfig.witherMultiplier;
		return range;
	}

	public int getUpgradeLevel() {
		return toolLevel;
	}

	public void setUpgradeLevel(int level) {
		toolLevel = level;
	}

	@Override
	public boolean isAllowedUpgrade(UpgradeType type) {
		return allowedUpgrades.contains(type);
	}


	public void updateEntity() {
		super.updateEntity();
		if (!worldObj.isRemote) {
			ItemStack upgrade = SLOT_UPGRADE != -1 ? getStackInSlot(SLOT_UPGRADE) : null;
			
			// Something inside the upgrade slot
			if (upgrade != null && upgrade.stackSize > 0) {
				ItemUpgrade upgradeItem = (ItemUpgrade) upgrade.getItem();
				UpgradeType type = upgradeItem.getType();
				int installedAmount = getUpgradeAmount(type);

				if (allowedUpgrades.contains(upgradeItem.getType()) && installedAmount < upgradeItem.allowedAmount()) {
					int newTotal = installedAmount + upgrade.stackSize;
					upgrade.stackSize = newTotal <= upgradeItem.allowedAmount() ? 0 : newTotal - upgradeItem.allowedAmount();
					newTotal = newTotal - upgrade.stackSize;
					setUpgradeAmount(type, newTotal);
				}
				
				// We've eaten all items in this stack, it should be disposed of
				if (upgrade.stackSize <= 0) {
					slots[SLOT_UPGRADE] = null;
				}
			}
			else if (upgrade != null) {
				// Malformed itemstack? Better delete it
				ProgressiveAutomation.logger.warn("Inserted ItemStack with stacksize <= 0. Deleting");
				slots[SLOT_UPGRADE] = null;
			}
		}
	}
	
	//override isided stuff
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if ( (slot==SLOT_PICKAXE) && ( ToolHelper.getType(stack.getItem()) == ToolHelper.TYPE_PICKAXE ) ) {
    		if (ToolHelper.getLevel(stack) <= getUpgradeLevel()) {
    			return true;
    		}
    	} else if ( (slot==SLOT_SHOVEL) && ( ToolHelper.getType(stack.getItem()) == ToolHelper.TYPE_SHOVEL ) ) {
    		if (ToolHelper.getLevel(stack) <= getUpgradeLevel()) {
    			return true;
    		}
     	} else if ( (slot==SLOT_AXE) && ( ToolHelper.getType(stack.getItem()) == ToolHelper.TYPE_AXE ) ) {
    		if (ToolHelper.getLevel(stack) <= getUpgradeLevel()) {
    			return true;
    		}
     	} else if ( (slot==SLOT_SWORD) && ( ToolHelper.getType(stack.getItem()) == ToolHelper.TYPE_SWORD ) ) {
    		if (ToolHelper.getLevel(stack) <= getUpgradeLevel()) {
    			return true;
    		}
     	} else if ( (slot==SLOT_HOE) && ( ToolHelper.getType(stack.getItem()) == ToolHelper.TYPE_HOE ) ) {
    		if (ToolHelper.getLevel(stack) <= getUpgradeLevel()) {
    			return true;
    		}
     	} else if ( (slot==SLOT_FUEL) && (TileEntityFurnace.getItemBurnTime(stack)>0) && (ToolHelper.getType(stack.getItem())==-1) ) {
     		return true;
    	} else if ( (slot==SLOT_UPGRADE) && (stack.isItemEqual(ToolHelper.getUpgradeType(getUpgradeLevel()))) ) {
    		return true;
     	}
		return false;
	}

	protected Set<UpgradeType> getAllowedUpgrades() {
		return allowedUpgrades;
	}

	protected void setAllowedUpgrades(Set<UpgradeType> allowedUpgrades) {
		this.allowedUpgrades = allowedUpgrades;
	}

	protected void setAllowedUpgrades(UpgradeType first, UpgradeType... rest) {
		this.allowedUpgrades = EnumSet.of(first, rest);
	}
}
