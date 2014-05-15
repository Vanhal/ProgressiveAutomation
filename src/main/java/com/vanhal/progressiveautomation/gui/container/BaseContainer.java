package com.vanhal.progressiveautomation.gui.container;

import com.vanhal.progressiveautomation.entities.BaseTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class BaseContainer extends Container {
	protected BaseTileEntity entity;
	
	public BaseContainer(BaseTileEntity inEntity) {
		entity = inEntity;
	}
	
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

}
