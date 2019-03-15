package com.vanhal.progressiveautomation.common.entities;


import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.client.events.EventRenderWorld;
import com.vanhal.progressiveautomation.common.items.upgrades.ItemUpgrade;
import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.common.util.Point2I;
import com.vanhal.progressiveautomation.common.util.Point3I;
import com.vanhal.progressiveautomation.common.util.ToolHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public class UpgradeableTileEntity extends BaseTileEntity implements IUpgradeable {

    protected int toolLevel = ToolHelper.LEVEL_WOOD;
    private Map<UpgradeType, Integer> installedUpgrades;
    private Set<UpgradeType> allowedUpgrades;
    protected int advancedToolFlash = 0;
    protected boolean showRange = false;
    protected int displayRangeCount = 0;
    protected static final int displayTicksPerBlock = 4;

    public UpgradeableTileEntity(int numSlots) {
        super(numSlots);
        installedUpgrades = new EnumMap<>(UpgradeType.class);
        allowedUpgrades = Collections.emptySet();
        if (ProgressiveAutomation.proxy.isClient()) {
            EventRenderWorld.addMachine(this);
        }
    }

    public void writeCommonNBT(NBTTagCompound nbt) {
        super.writeCommonNBT(nbt);
        // Save upgrades
        NBTTagCompound tag = new NBTTagCompound();
        for (Map.Entry<UpgradeType, Integer> mapEntry : installedUpgrades.entrySet()) {
            tag.setInteger(mapEntry.getKey().name(), mapEntry.getValue());
        }

        nbt.setTag("installedUpgrades", tag);
        nbt.setInteger("advancedToolFlash", advancedToolFlash);
        nbt.setBoolean("showRange", showRange);
    }

    public void readCommonNBT(NBTTagCompound nbt) {
        super.readCommonNBT(nbt);
        // Load upgrades
        NBTTagCompound tag = nbt.getCompoundTag("installedUpgrades");
        if (tag != null) {
            // func_150296_c() returns a set of all tag names inside the NBTTagCompound
            // This is called getKeySet now?
            for (Object key : tag.getKeySet()) {
                String upgradeName = (String) key;
                installedUpgrades.put(UpgradeType.valueOf(upgradeName), tag.getInteger(upgradeName));
            }
        }

        if (nbt.hasKey("advancedToolFlash")) {
            advancedToolFlash = nbt.getInteger("advancedToolFlash");
        }

        if (nbt.hasKey("showRange")) {
            showRange = nbt.getBoolean("showRange");
        }
    }

    public void setInvalidTool() {
        advancedToolFlash = 30;
    }

    public boolean isInvalidTool() {
        return (advancedToolFlash > 0);
    }

    /* IUpgradeable methods */
    @Override
    public boolean hasUpgrade(UpgradeType type) {
        return installedUpgrades.get(type) != null;
    }

    @Override
    public Integer getUpgradeAmount(UpgradeType type) {
        if (!installedUpgrades.containsKey(type)) {
            return 0;
        }

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
        notifyUpdate();
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
        int range = (getUpgrades() * PAConfig.upgradeRange) + PAConfig.initialRange;
        if (hasUpgrade(UpgradeType.WITHER)) {
            range = range * PAConfig.witherMultiplier;
        }
        return range;
    }

    public int getUpgradeLevel() {
        return toolLevel;
    }

    public void setUpgradeLevel(int level) {
        toolLevel = level;
    }

    public boolean isAllowedUpgrade(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemUpgrade) {
            ItemUpgrade upgradeItem = (ItemUpgrade) itemStack.getItem();
            UpgradeType type = upgradeItem.getType();
            return allowedUpgrades.contains(type);
        }
        return false;
    }

    @Override
    public boolean isAllowedUpgrade(UpgradeType type) {
        return allowedUpgrades.contains(type);
    }


    public void update() {
        super.update();
        if (!world.isRemote) {
            ItemStack upgrade = (SLOT_UPGRADE != -1) ? getStackInSlot(SLOT_UPGRADE) : ItemStack.EMPTY;
            // Something inside the upgrade slot
            if ((!upgrade.isEmpty()) && (upgrade.getCount() > 0)) {
                if (upgrade.getItem() instanceof ItemUpgrade) {
                    ItemUpgrade upgradeItem = (ItemUpgrade) upgrade.getItem();
                    UpgradeType type = upgradeItem.getType();
                    int installedAmount = getUpgradeAmount(type);
                    if (allowedUpgrades.contains(upgradeItem.getType()) && installedAmount < upgradeItem.allowedAmount()) {
                        int newTotal = installedAmount + upgrade.getCount();
                        upgrade.setCount((newTotal <= upgradeItem.allowedAmount()) ? 0 : newTotal - upgradeItem.allowedAmount());
                        newTotal = newTotal - upgrade.getCount();
                        setUpgradeAmount(type, newTotal);
                    }

                    // We've eaten all items in this stack, it should be disposed of
                    if (upgrade.getCount() <= 0) {
                        slots[SLOT_UPGRADE] = ItemStack.EMPTY;
                    }
                } else {
                    ProgressiveAutomation.logger.warn("A non upgrade found it's way into the upgrade slot somehow. Deleting.");
                    slots[SLOT_UPGRADE] = ItemStack.EMPTY;
                }
            } else if (!upgrade.isEmpty()) {
                // Malformed itemstack? Better delete it
                ProgressiveAutomation.logger.warn("Inserted ItemStack with stacksize <= 0. Deleting");
                slots[SLOT_UPGRADE] = ItemStack.EMPTY;
            }
        }

        if (advancedToolFlash > 0) {
            advancedToolFlash--;
            if (advancedToolFlash < 0) {
                advancedToolFlash = 0;
            }
        }

        //code for showing the range of a machine
        if (world.isRemote) {
            displayRangeCount++;
            if (Math.floor(displayRangeCount / displayTicksPerBlock) >= getRange()) {
                displayRangeCount = 0;
            }
        }
    }

    //this will return the current block to render to show the range of a machine
    public Point3I getRangeBlock() {
        int block = (int) Math.floor(displayRangeCount / displayTicksPerBlock);
        return adjustedSpiral(block);
    }

    //override isided stuff
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if ((slot == SLOT_UPGRADE) && (isAllowedUpgrade(stack))) {
            return true;
        } else if (slot == SLOT_UPGRADE) {
            return false;
        }

        if ((slot == SLOT_PICKAXE) && (ToolHelper.getType(stack) == ToolHelper.TYPE_PICKAXE)) {
            if ((ToolHelper.getLevel(stack) <= PAConfig.getToolConfigLevel(getUpgradeLevel())) && (!ToolHelper.isBroken(stack))) {
                return true;
            }
        } else if ((slot == SLOT_SHOVEL) && (ToolHelper.getType(stack) == ToolHelper.TYPE_SHOVEL)) {
            if ((ToolHelper.getLevel(stack) <= PAConfig.getToolConfigLevel(getUpgradeLevel())) && (!ToolHelper.isBroken(stack))) {
                return true;
            }
        } else if ((slot == SLOT_AXE) && (ToolHelper.getType(stack) == ToolHelper.TYPE_AXE)) {
            if ((ToolHelper.getLevel(stack) <= PAConfig.getToolConfigLevel(getUpgradeLevel())) && (!ToolHelper.isBroken(stack))) {
                return true;
            }
        } else if ((slot == SLOT_SWORD) && (ToolHelper.getType(stack) == ToolHelper.TYPE_SWORD)) {
            if ((ToolHelper.getLevel(stack) <= PAConfig.getToolConfigLevel(getUpgradeLevel())) && (!ToolHelper.isBroken(stack))) {
                return true;
            }
        } else if ((slot == SLOT_HOE) && (ToolHelper.getType(stack) == ToolHelper.TYPE_HOE)) {
            if ((ToolHelper.getLevel(stack) <= PAConfig.getToolConfigLevel(getUpgradeLevel())) && (!ToolHelper.isBroken(stack))) {
                return true;
            }
        } else if ((slot == SLOT_UPGRADE) && (stack.isItemEqual(ToolHelper.getUpgradeType(getUpgradeLevel())))) {
            return true;
        }
        return super.isItemValidForSlot(slot, stack);
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

    protected void addAllowedUpgrade(UpgradeType upgrade) {
        if (allowedUpgrades != null) {
            this.allowedUpgrades.add(upgrade);
        }
    }

    protected Point3I adjustedSpiral(int n) {
        Point2I spiralPoint = this.spiral(n + 1, pos.getX(), pos.getZ());
        return new Point3I(spiralPoint.getX(), pos.getY(), spiralPoint.getY());
    }

    public boolean displayRange() {
        return this.showRange;
    }

    public void toggleRange() {
        this.showRange = !showRange;
    }
}