package com.vanhal.progressiveautomation.client.gui.container;

import com.vanhal.progressiveautomation.common.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.common.entities.farmer.TileFarmer;
import com.vanhal.progressiveautomation.client.gui.slots.SlotFeed;
import com.vanhal.progressiveautomation.client.gui.slots.SlotItemDisabledUpgrade;
import com.vanhal.progressiveautomation.client.gui.slots.SlotShearsDisabledUpgrade;
import com.vanhal.progressiveautomation.client.gui.slots.SlotUpgrades;
import com.vanhal.progressiveautomation.common.util.ToolHelper;
import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerFarmer extends BaseContainer {

    protected ItemStack updateType;
    protected TileFarmer farmer;

    public ContainerFarmer(InventoryPlayer inv, TileEntity entity) {
        super((BaseTileEntity) entity, 11, 52);
        farmer = (TileFarmer) entity;
        updateType = ToolHelper.getUpgradeType(farmer.getUpgradeLevel());
        addSlotToContainer(new SlotFeed(farmer, farmer.SLOT_FOOD, 11, 16)); //animal food
        addSlotToContainer(new SlotShearsDisabledUpgrade(UpgradeType.SHEARING, farmer, farmer.SLOT_SHEARS, 37, 52)); //shears
        addSlotToContainer(new SlotItemDisabledUpgrade(new ItemStack(Items.BUCKET), UpgradeType.MILKER, farmer, farmer.SLOT_BUCKETS, 63, 52)); //buckets
        addSlotToContainer(new SlotUpgrades(farmer, farmer.SLOT_UPGRADE, 89, 52)); //upgrades
        //output slots
        addInventory(farmer, farmer.SLOT_INVENTORY_START, 112, 16, 3, 3);
        addPlayerInventory(inv);
    }
}