package com.vanhal.progressiveautomation.compat.mods;

import java.util.ArrayList;
import java.util.List;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.util.PlayerFake;
import com.vanhal.progressiveautomation.util.Point3I;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class RightClick extends Vanilla {

	public RightClick() {
		this.modID = "rightclickplants";
	}
	
	@Override
	public boolean shouldLoad() {
		if (PAConfig.config.getBoolean(modID, "ModCompatibility", true, "Enable support for "+modID)) {
			ProgressiveAutomation.logger.info(modID+" Loaded");
			return true;
		} else {
			ProgressiveAutomation.logger.info(modID+" Found, but compatibility has been disabled in the configs");
			return false;
		}
	}
	
	@Override
	public List<ItemStack> harvestPlant(Point3I point, Block plantBlock, IBlockState state, World worldObj) {
		PlayerFake faker = new PlayerFake((WorldServer)worldObj);
		plantBlock.onBlockActivated(worldObj, point.toPosition(), state, faker, null, EnumFacing.DOWN, 0, 0, 0);
		
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		
		IInventory inv = faker.inventory;
		for (int i = 0; i < inv.getSizeInventory(); i++){
			if (inv.getStackInSlot(i)!=null) {
				items.add(inv.getStackInSlot(i).copy());
			}
		}

		AxisAlignedBB block = new AxisAlignedBB(point.getX(), point.getY(), point.getZ(), 
														point.getX()+1, point.getY()+1, point.getZ()+1);
		List<EntityItem> entities = worldObj.getEntitiesWithinAABB(EntityItem.class, block);
		if (entities.isEmpty()) {
			return null;
		}
		
		for (EntityItem item: entities) {
			items.add(item.getItem());
			worldObj.removeEntity(item);
		}
		
		return items;
	}
}
