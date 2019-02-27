package com.vanhal.progressiveautomation.client.gui.container;

import com.vanhal.progressiveautomation.client.gui.slots.SlotTool;
import com.vanhal.progressiveautomation.client.gui.slots.SlotUpgrades;
import com.vanhal.progressiveautomation.common.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.common.entities.killer.TileKiller;
import com.vanhal.progressiveautomation.common.util.ToolHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerKiller extends BaseContainer {

    protected TileKiller killer;
    protected ItemStack updateType;

    public ContainerKiller(InventoryPlayer inv, TileEntity entity) {
        super((BaseTileEntity) entity, 11, 40);
        killer = (TileKiller) entity;
        updateType = ToolHelper.getUpgradeType(killer.getUpgradeLevel());
        addSlotToContainer(new SlotTool(ToolHelper.TYPE_SWORD, killer.getUpgradeLevel(), killer, killer.SLOT_SWORD, 49, 52)); //sword
        addSlotToContainer(new SlotUpgrades(killer, killer.SLOT_UPGRADE, 76, 52)); //upgrades
        //output slots
        addInventory(killer, killer.SLOT_INVENTORY_START, 112, 16, 3, 3);
        addPlayerInventory(inv);
    }
}