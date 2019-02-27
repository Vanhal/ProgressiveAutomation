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
	
    public void addEnergyRaw(int energy){
    	this.energy = Math.min(this.energy + energy, this.capacity);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate){
        if(!this.canReceive()){
            return 0;
        }
        int energy = this.getEnergyStored();

        int energyReceived = Math.min(this.capacity-energy, Math.min(this.maxReceive, maxReceive));
        if(!simulate){
            this.setEnergyStored(energy+energyReceived);
        }

        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate){
        if(!this.canExtract()){
            return 0;
        }
        int energy = this.getEnergyStored();

        int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
        if(!simulate){
            this.setEnergyStored(energy-energyExtracted);
        }
        return energyExtracted;
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

