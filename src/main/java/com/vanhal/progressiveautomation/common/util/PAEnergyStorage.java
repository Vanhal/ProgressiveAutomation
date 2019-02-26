/**
 * 
 */
package com.vanhal.progressiveautomation.common.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.EnergyStorage;

/**
 * @author madman
 *
 */
public class PAEnergyStorage extends EnergyStorage {

	public PAEnergyStorage(int capacity, int maxTransfer) {
		super(capacity, maxTransfer);
	}

	public PAEnergyStorage(int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
	}
	
	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public void consumePower(int energy) {
		this.energy -= energy;
		if (this.energy < 0) {
			this.energy = 0;
		}
	}

	public void generatePower(int energy) {
		this.energy += energy;
		if (this.energy > capacity) {
			this.energy = capacity;
		}
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public void setMaxTransfer(int transfer) {
		if(this.maxReceive != 0) this.maxExtract = this.maxReceive = transfer;
		else this.maxExtract = transfer;
	}
	
	public void resetStats(int capacity, int transfer) {
		this.setCapacity(capacity);
		this.setMaxTransfer(transfer);
	}
	
	public int getMaxTransfer() {
		return this.maxExtract;
	
	}
	
	@Override
	public int receiveEnergy(int amount, boolean simulate) {
		int rv = super.receiveEnergy(amount, simulate);
		return rv;
	}
	
	@Override
	public int extractEnergy(int amount, boolean simulate) {
		int rv = super.extractEnergy(amount, simulate);
		return rv;
	}
	
    public void readFromNBT(NBTTagCompound compound){
        this.setEnergyStored(compound.getInteger("Energy"));
    }

    public void writeToNBT(NBTTagCompound compound){
        compound.setInteger("Energy", this.getEnergyStored());
    }

    public void setEnergyStored(int energy){
        this.energy = energy;
    }
}
