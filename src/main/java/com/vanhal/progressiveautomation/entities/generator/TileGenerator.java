package com.vanhal.progressiveautomation.entities.generator;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.blocks.BlockGenerator;
import com.vanhal.progressiveautomation.blocks.PABlocks;
import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.entities.UpgradeableTileEntity;
import com.vanhal.progressiveautomation.items.ItemRFEngine;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.util.BlockHelper;
import com.vanhal.progressiveautomation.util.Point2I;
import com.vanhal.progressiveautomation.util.Point3I;

public class TileGenerator extends BaseTileEntity {
	protected float fireRisk = 0.02f;
	protected int maxStorage = 10000;
	protected int currentStorage = 0;
	protected int generationRate = 10;
	protected int consumeRate = 1;

	protected boolean burnUpdate = false;

	public TileGenerator() {
		super(0);
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

	@Override
	public void update() {
		super.update();
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
			
			worldObj.notifyBlockOfStateChange(pos, worldObj.getBlockState(pos).getBlock());
			worldObj.markBlockForUpdate(this.pos);
		}
	}

	protected void checkForFire() {
		if (fireRisk > worldObj.rand.nextFloat()) {
			//start a fire on a block nearby
			int n = (int)Math.floor(8*worldObj.rand.nextFloat()) + 1;
			Point2I p2 = spiral(n, pos.getX(), pos.getZ());
			
			BlockPos supportPos = new BlockPos(p2.getX(), pos.getY() - 1, p2.getY());
			BlockPos firePos = new BlockPos(p2.getX(), pos.getY(), p2.getY());
			
			Block supportBlock = worldObj.getBlockState(supportPos).getBlock();
			Block fireBlock = worldObj.getBlockState(firePos).getBlock();
			if ( ((fireBlock.isAir(worldObj, firePos)) 
				&& (supportBlock.isFlammable(worldObj, supportPos, EnumFacing.UP))) ){
				worldObj.setBlockState(firePos, Blocks.fire.getDefaultState());
			}
		}
	}

	@Override
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
	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		return 0;
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
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

	@Override
	public int getEnergyStored(EnumFacing facing) {
		return getEnergyStored();
	}
	
	public int getEnergyStored() {
		return currentStorage;
	}

	@Override
	public int getMaxEnergyStored(EnumFacing facing) {
		return getMaxEnergyStored();
	}
	
	public int getMaxEnergyStored() {
		return maxStorage;
	}

	public void outputEnergy() {
		//Lets go around the world and try and give it to someone!
		for (int i = 0; i<6; i++) {
			//Do we have any energy up for grabs?
			if (currentStorage>0) {
				TileEntity entity = BlockHelper.getAdjacentTileEntity(worldObj, getPos().getX(), getPos().getY(), getPos().getZ(), i);
				if (entity instanceof IEnergyReceiver) {
					IEnergyReceiver energy = (IEnergyReceiver) entity;
					EnumFacing fromDirection = EnumFacing.getFront(i);
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
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return super.isItemValidForSlot(slot, stack);
	}
}
