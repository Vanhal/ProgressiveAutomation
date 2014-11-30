package com.vanhal.progressiveautomation.entities.generator;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.UpgradeableTileEntity;
import com.vanhal.progressiveautomation.items.ItemRFEngine;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.util.BlockHelper;
import com.vanhal.progressiveautomation.util.Point2I;
import com.vanhal.progressiveautomation.util.Point3I;

public class TileGenerator extends UpgradeableTileEntity {
	protected float fireRisk = 0.02f;
	protected int maxStorage = 10000;
	protected int currentStorage = 0;
	protected int generationRate = 10;
	protected int consumeRate = 1;

	protected boolean burnUpdate = false;

	public TileGenerator() {
		super(0);
		setUpgradeLevel(ToolHelper.LEVEL_WOOD);
		setEnergyStorage(20000, 0.5f);
	}

	public void writeCommonNBT(NBTTagCompound nbt) {
		super.writeCommonNBT(nbt);
		//save the current energy stored
		nbt.setInteger("energy", currentStorage);

	}

	public void readCommonNBT(NBTTagCompound nbt) {
		super.readCommonNBT(nbt);
		//load the current energy stored
		if (nbt.hasKey("energy")) currentStorage = nbt.getInteger("energy");
	}

	public void setFireChance(float chance) {
		fireRisk = chance;
	}

	public void setEnergyStorage(int size, float rate) {
		maxStorage = size;
		generationRate = (int) ((float)PAConfig.rfCost * rate);
		consumeRate = (int) ((float)PAConfig.fuelCost * rate);
	}

	public void updateEntity() {
		super.updateEntity();
		if (!worldObj.isRemote) {
			if (isBurning()) {
				changeCharge(generationRate);
				checkForFire();
			}

			//output the energy to connected devices....
			outputEnergy();
		} else {
			checkUpdate();
		}
	}

	protected void checkUpdate() {
		if (isBurning() != burnUpdate) {
			burnUpdate = isBurning();
			worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		}
	}

	protected void checkForFire() {
		if (fireRisk > worldObj.rand.nextFloat()) {
			//start a fire on a block nearby
			int n = (int)Math.floor(8*worldObj.rand.nextFloat()) + 1;
			Point2I p2 = spiral(n, xCoord, zCoord);
			Block supportBlock = worldObj.getBlock(p2.getX(), yCoord - 1, p2.getY());
			Block fireBlock = worldObj.getBlock(p2.getX(), yCoord, p2.getY());
			if ( ((fireBlock.isAir(worldObj, p2.getX(), yCoord, p2.getY())) 
				&& (supportBlock.isFlammable(worldObj, p2.getX(), yCoord -1, p2.getY(), ForgeDirection.UP))) ){
				worldObj.setBlock(p2.getX(), yCoord, p2.getY(), Blocks.fire);
			}
		}
	}

	public boolean readyToBurn() {
		if (currentStorage < maxStorage) {
			return true;
		}
		return false;
	}

	public int getProduceRate() {
		return generationRate;
	}

	@Override
	public int getBurnTime(ItemStack item) {
			return TileEntityFurnace.getItemBurnTime(item) / consumeRate;
	}

	//Energy stuff
	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}

	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return 0;
	}

	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		int energyExtracted = Math.min(currentStorage, maxExtract);
		if (!simulate) {
			changeCharge((energyExtracted * -1));
		}
		return energyExtracted;
	}

	public void changeCharge(int amount) {
		int prevAmount = currentStorage;
		
		currentStorage += amount;
		if (currentStorage >= maxStorage) currentStorage = maxStorage;
		if (currentStorage < 0) currentStorage = 0;
		
		if (currentStorage != prevAmount) addPartialUpdate("energy", currentStorage);
	}

	public int getEnergyStored(ForgeDirection from) {
		return currentStorage;
	}

	public int getEnergyStored() {
		return currentStorage;
	}

	public int getMaxEnergyStored(ForgeDirection from) {
		return maxStorage;
	}

	public int getMaxEnergyStored() {
		return maxStorage;
	}

	public void outputEnergy() {
		//Lets go around the world and try and give it to someone!
		for (int i = 0; i<6; i++) {
			//Do we have any energy up for grabs?
			if (currentStorage>0) {
				TileEntity entity = BlockHelper.getAdjacentTileEntity(worldObj, xCoord, yCoord, zCoord, i);
				if (entity instanceof IEnergyHandler) {
					IEnergyHandler energy = (IEnergyHandler) entity;
					ForgeDirection fromDirection = ForgeDirection.values()[ForgeDirection.OPPOSITES[i]];
					if (energy.canConnectEnergy(fromDirection)) {
						int giveAmount = energy.receiveEnergy(fromDirection, currentStorage, false);
						if (giveAmount>0) {
							changeCharge(giveAmount * -1);
						}
					}
				}
			}
		}
	}
	
	/* ISided Stuff */
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return super.isItemValidForSlot(slot, stack);
	}
}
