package com.vanhal.progressiveautomation.client.gui.container;

import com.vanhal.progressiveautomation.client.gui.slots.SlotPlantable;
import com.vanhal.progressiveautomation.client.gui.slots.SlotTool;
import com.vanhal.progressiveautomation.client.gui.slots.SlotUpgrades;
import com.vanhal.progressiveautomation.common.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.common.entities.planter.TilePlanter;
import com.vanhal.progressiveautomation.common.util.ToolHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerPlanter extends BaseContainer {

    TilePlanter planter;
    protected ItemStack updateType;

    public ContainerPlanter(InventoryPlayer inv, TileEntity entity) {
        super((BaseTileEntity) entity, 11, 52);
        planter = (TilePlanter) entity;
        updateType = ToolHelper.getUpgradeType(planter.getUpgradeLevel());
        //add slots
        addSlotToContainer(new SlotPlantable(planter, planter.SLOT_SEEDS, 11, 16)); //seeds
        addSlotToContainer(new SlotTool(ToolHelper.TYPE_HOE, planter.getUpgradeLevel(), planter, planter.SLOT_HOE, 49, 52)); //hoe
        addSlotToContainer(new SlotUpgrades(planter, planter.SLOT_UPGRADE, 76, 52)); //upgrades
        //output slots
        addInventory(planter, planter.SLOT_INVENTORY_START, 112, 16, 3, 3);
        addPlayerInventory(inv);
    }
}