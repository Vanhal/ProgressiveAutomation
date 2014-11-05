package com.vanhal.progressiveautomation.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.oredict.OreDictionary;

import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.entities.chopper.TileChopper;
import com.vanhal.progressiveautomation.entities.generator.TileGenerator;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerGenerator extends BaseContainer {
	
	TileGenerator generator;

	public ContainerGenerator(InventoryPlayer inv, TileEntity entity) {
		super((BaseTileEntity)entity, 23, 24, false);
		generator = (TileGenerator)entity;
		
		addPlayerInventory(inv, 53);
	}
	
	
	//send updates
	int lastEnergy = -1;
	
	public void sendUpdates(ICrafting i) {
		if (lastEnergy != generator.getEnergyStored()) {
			lastEnergy = generator.getEnergyStored();
			i.sendProgressBarUpdate(this, 4, lastEnergy);
		}
	}
	
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int i, int value) {
		super.updateProgressBar(i, value);
		if (i==4) {
			generator.setEnergyStored(value);
		}
	}

}
