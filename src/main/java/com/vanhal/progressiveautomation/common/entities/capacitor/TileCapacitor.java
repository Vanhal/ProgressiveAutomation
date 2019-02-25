package com.vanhal.progressiveautomation.common.entities.capacitor;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.common.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.common.util.PAEnergyStorage;
import com.vanhal.progressiveautomation.common.util.WrenchModes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;


public class TileCapacitor extends BaseTileEntity {

    public int SLOT_CHARGER = 1;
    protected PAEnergyStorage energyStorage;
    private int maxTransfer;
    
    public TileCapacitor() {
        super(1);
        setEnergyStorage(5000 * PAConfig.rfStorageFactor, 80);
        sides[extDirection.ordinal()] = WrenchModes.Mode.Normal;
    }

    public void writeCommonNBT(NBTTagCompound nbt) {
        super.writeCommonNBT(nbt);
        //save the current energy stored
        nbt.setInteger("energy", this.energyStorage.getEnergyStored());
        sides[extDirection.ordinal()] = WrenchModes.Mode.Normal;
    }

    public void readCommonNBT(NBTTagCompound nbt) {
        super.readCommonNBT(nbt);
        //load the current energy stored
        if (nbt.hasKey("energy")) {
        	this.energyStorage.setEnergy(nbt.getInteger("energy"));
        }
    }

    public void setEnergyStorage(int size, int rate) {
    	if(this.energyStorage == null) this.energyStorage = new PAEnergyStorage(size, rate);
    	else this.energyStorage.resetStats(size, rate);
    	
    	this.maxTransfer = rate;
    }

    public int getTransferRate() {
    	return this.maxTransfer;
    }
    
    @Override
    public void update() {
        super.update();
        if (!world.isRemote) {
            //Charge items in charge slot
            if (!slots[SLOT_CHARGER].isEmpty()) {
                if (this.energyStorage.canExtract()) {
                    if (slots[SLOT_CHARGER].hasCapability(CapabilityEnergy.ENERGY, EnumFacing.UP)) {
                        IEnergyStorage container = (IEnergyStorage) slots[SLOT_CHARGER].getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP);
                        if (container.canReceive()) {
                          int giveAmount = container.receiveEnergy(this.energyStorage.getEnergyStored(), false);
                            if (giveAmount > 0) {
                            	energyStorage.extractEnergy(giveAmount, false);
                            }
                        }
                    }
                }
            }

            //output only of we don't get a redstone signal
            if (world.getRedstonePower(pos, EnumFacing.UP) == 0 ||
                    world.getRedstonePower(pos, EnumFacing.DOWN) == 0 ||
                    world.getRedstonePower(pos, EnumFacing.NORTH) == 0 ||
                    world.getRedstonePower(pos, EnumFacing.SOUTH) == 0 ||
                    world.getRedstonePower(pos, EnumFacing.WEST) == 0 ||
                    world.getRedstonePower(pos, EnumFacing.EAST) == 0) {
                //output the energy to connected devices....
                outputEnergy();
            }
        }
    }

    public void outputEnergy() {
        //Lets go around the world and try and give it to someone!
        for (EnumFacing facing : EnumFacing.values()) {
            //Do we have any energy up for grabs?
            if (this.energyStorage.canExtract()) {
                TileEntity entity = world.getTileEntity(pos.offset(facing));
                if (entity != null) {
                    if (entity.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite())) {
                        IEnergyStorage energy = entity.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
                        if (energy.canReceive()) {
                            int giveAmount = energy.receiveEnergy(this.energyStorage.getEnergyStored(), false);
                            if (giveAmount > 0) {
                            	energyStorage.extractEnergy(giveAmount, false);
                            }
                        }
                    }
                }
            }
        }
    }
    
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(capability == CapabilityEnergy.ENERGY) return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(capability == CapabilityEnergy.ENERGY) return CapabilityEnergy.ENERGY.cast(energyStorage);
		else return super.hasCapability(capability, facing)?super.getCapability(capability, facing):null;
	}

    /* ISided Stuff */
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return super.isItemValidForSlot(slot, stack);
    }
}