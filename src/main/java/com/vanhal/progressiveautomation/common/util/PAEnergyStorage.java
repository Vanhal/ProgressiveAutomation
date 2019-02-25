/**
 * 
 */
package com.vanhal.progressiveautomation.common.util;

import net.minecraftforge.energy.EnergyStorage;

/**
 * @author madman
 *
 */
public class PAEnergyStorage extends EnergyStorage {

	public PAEnergyStorage(int capacity, int maxTransfer) {
		super(capacity, maxTransfer);
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
		this.maxExtract = this.maxReceive = transfer;
	}
	
	public void resetStats(int capacity, int transfer) {
		this.setCapacity(capacity);
		this.setMaxTransfer(transfer);
	}
	
	public int getMaxTransfer() {
		return this.maxExtract;
	}	
}
