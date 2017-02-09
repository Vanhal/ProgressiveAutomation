package com.vanhal.progressiveautomation.entities.capacitor;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.ref.WrenchModes;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;


public class TileCapacitor extends BaseTileEntity {

	protected int transferRate = 10;
	protected int maxStorage = 10000;
	protected int currentStorage = 0;
	
	public int SLOT_CHARGER = 1;

	public TileCapacitor() {
		super(1);
		setEnergyStorage(5000*PAConfig.rfStorageFactor, 80);
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

	public void setEnergyStorage(int size, int rate) {
		maxStorage = size;
		transferRate = rate;
	}

	@Override
	public void update() {
		super.update();
		if (!worldObj.isRemote) {
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
			
			//output only of we don't get a redstone signal
			if(worldObj.getRedstonePower(pos, EnumFacing.UP)==0 ||
				worldObj.getRedstonePower(pos, EnumFacing.DOWN)==0 ||
				worldObj.getRedstonePower(pos, EnumFacing.NORTH)==0 ||
				worldObj.getRedstonePower(pos, EnumFacing.SOUTH)==0 ||
				worldObj.getRedstonePower(pos, EnumFacing.WEST)==0 ||
				worldObj.getRedstonePower(pos, EnumFacing.EAST)==0){
				//output the energy to connected devices....
				outputEnergy();
			}
		}
	}
	
	public int getTransferRate(){
		return transferRate;
	}

	//Energy stuff
	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		int energyReceived = 0;
		if(from.getOpposite().getIndex() == getBlockMetadata()){
			if(currentStorage+maxReceive<=maxStorage || currentStorage+transferRate<=maxStorage){
				if(!simulate){
					if(maxReceive>transferRate){
						energyReceived = transferRate;
					}else{
						energyReceived = maxReceive;
					}
					changeCharge(energyReceived);
				}
			}
		}
		return energyReceived;
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		int energyExtracted = Math.min(currentStorage, maxExtract);
		if(from.getOpposite().getIndex() != getBlockMetadata()-1){
			if (!simulate) {
				changeCharge((energyExtracted * -1));
			}
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
