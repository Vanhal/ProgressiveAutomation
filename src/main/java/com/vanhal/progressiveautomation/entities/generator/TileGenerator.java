package com.vanhal.progressiveautomation.entities.generator;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.ref.WrenchModes;
import com.vanhal.progressiveautomation.util.Point2I;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileGenerator extends BaseTileEntity {
	protected float fireRisk = 0.02f;
	protected int maxStorage = 10000;
	protected int currentStorage = 0;
	protected int generationRate = 10;
	protected int consumeRate = 1;

	protected boolean burnUpdate = false;
	
	public int SLOT_CHARGER = 1;

	public TileGenerator() {
		super(1);
		setEnergyStorage(20000, 0.5f);
		sides[extDirection.ordinal()] = WrenchModes.Mode.Normal;
	}

	public void writeCommonNBT(NBTTagCompound nbt) {
		super.writeCommonNBT(nbt);
		//save the current energy stored
		nbt.setInteger("energy", currentStorage);
		sides[extDirection.ordinal()] = WrenchModes.Mode.Normal;

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
			
			//Charge items in charge slot
			if (slots[SLOT_CHARGER]!=null) {
				if (currentStorage>0) {
					if (slots[SLOT_CHARGER].getItem() instanceof IEnergyContainerItem) {
						IEnergyContainerItem container = (IEnergyContainerItem)slots[SLOT_CHARGER].getItem();
						if (container.getEnergyStored(slots[SLOT_CHARGER]) < container.getMaxEnergyStored(slots[SLOT_CHARGER])) {
							int giveAmount = container.receiveEnergy(slots[SLOT_CHARGER], currentStorage, false);
							if (giveAmount>0) changeCharge(giveAmount * -1);
						}
					}
				}
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
			worldObj.markBlockRangeForRenderUpdate(pos, pos);
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
			IBlockState fireState = worldObj.getBlockState(firePos);
			Block fireBlock = fireState.getBlock();
			if ( ((fireBlock.isAir(fireState, worldObj, firePos)) 
				&& (supportBlock.isFlammable(worldObj, supportPos, EnumFacing.UP))) ){
				worldObj.setBlockState(firePos, Blocks.FIRE.getDefaultState());
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
			return getItemBurnTime(item) / consumeRate;
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
		for(EnumFacing facing : EnumFacing.values()) {
			//Do we have any energy up for grabs?
			if (currentStorage>0) {
				TileEntity entity = worldObj.getTileEntity(pos.offset(facing));
				if (entity!=null) {
					if (entity.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite())) {
						IEnergyStorage energy = entity.getCapability(CapabilityEnergy.ENERGY, facing);
						if (energy.canReceive()) {
							int giveAmount = energy.receiveEnergy(currentStorage, false);
							if (giveAmount>0) {
								changeCharge(giveAmount * -1);
							}
						}
					} else if (entity instanceof IEnergyReceiver) {
						IEnergyReceiver energy = (IEnergyReceiver) entity;
						if (energy.canConnectEnergy(facing.getOpposite())) {
							int giveAmount = energy.receiveEnergy(facing.getOpposite(), currentStorage, false);
							if (giveAmount>0) {
								changeCharge(giveAmount * -1);
							}
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
