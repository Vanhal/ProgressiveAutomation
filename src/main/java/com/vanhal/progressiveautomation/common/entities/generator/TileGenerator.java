package com.vanhal.progressiveautomation.common.entities.generator;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.common.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.common.util.WrenchModes;
import com.vanhal.progressiveautomation.common.util.Point2I;
import com.vanhal.progressiveautomation.common.util.PAEnergyStorage;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileGenerator extends BaseTileEntity {

    protected float fireRisk = 0.02f;
    protected int generationRate = 10;
    protected int consumeRate = 1;
    protected boolean burnUpdate = false;
    public int SLOT_CHARGER = 1;
    private PAEnergyStorage energyStorage;
    protected int maxExtract;
    
    public TileGenerator() {
        super(1);
        setEnergyStorage(20000, 0.5f);
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
        	this.energyStorage.setEnergyStored(nbt.getInteger("energy"));
        }
    }

    public void setFireChance(float chance) {
        fireRisk = chance;
    }

    public void setEnergyStorage(int size, float rate) {
    	if(this.energyStorage==null) this.energyStorage = new PAEnergyStorage(size, 0, (int)Math.ceil(rate*640)) {
			@Override
			public int receiveEnergy(int amount, boolean simulate) {
				int curCharge = this.energy;
				int newCharge = super.receiveEnergy(amount, simulate);
				if(curCharge != newCharge && !simulate) addPartialUpdate("energy", this.energy);
				return newCharge;
			}
			
			@Override
			public int extractEnergy(int amount, boolean simulate) {
				int curCharge = this.energy;
				int newCharge = super.extractEnergy(amount, simulate);
				if(curCharge != newCharge && !simulate) addPartialUpdate("energy", this.energy);
				return newCharge;
			}
			
			@Override
			public void addEnergyRaw(int amount) {
				super.addEnergyRaw(amount);
				addPartialUpdate("energy", this.energy);
			}
		};
        generationRate = (int) Math.ceil(((float) PAConfig.rfCost * rate));
        consumeRate = (int) Math.ceil(((float) PAConfig.fuelCost * rate));
        this.maxExtract = (int)(Math.ceil(640 * rate));
    }

    @Override
    public void update() {
        super.update();
        if (!world.isRemote) {
        	// generate!
            if (isBurning()) {
            	this.energyStorage.addEnergyRaw(generationRate);
                checkForFire();
            }
            
            //Charge items in charge slot
            if (!slots[SLOT_CHARGER].isEmpty()) {
            	doEnergyInteraction(this, slots[SLOT_CHARGER].getCapability(CapabilityEnergy.ENERGY, null), this.maxExtract);
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
            world.getBlockState(pos).neighborChanged(world, pos, world.getBlockState(pos).getBlock(), pos);
            world.markBlockRangeForRenderUpdate(pos, pos);
        }
    }

    protected void checkForFire() {
        if (fireRisk > world.rand.nextFloat()) {
            //start a fire on a block nearby
            int n = (int) Math.floor(8 * world.rand.nextFloat()) + 1;
            Point2I p2 = spiral(n, pos.getX(), pos.getZ());
            BlockPos supportPos = new BlockPos(p2.getX(), pos.getY() - 1, p2.getY());
            BlockPos firePos = new BlockPos(p2.getX(), pos.getY(), p2.getY());
            Block supportBlock = world.getBlockState(supportPos).getBlock();
            IBlockState fireState = world.getBlockState(firePos);
            Block fireBlock = fireState.getBlock();
            if (((fireBlock.isAir(fireState, world, firePos))
                    && (supportBlock.isFlammable(world, supportPos, EnumFacing.UP)))) {
                world.setBlockState(firePos, Blocks.FIRE.getDefaultState());
            }
        }
    }

    @Override
    public boolean readyToBurn() {
        if (this.energyStorage.getEnergyStored() < this.energyStorage.getMaxEnergyStored()) {
            return true;
        }
        return false;
    }

    public int getProduceRate() {
        return generationRate;
    }

    @Override
    public int getBurnTime(ItemStack item) {
    	// Thought: should we burn faster/slower as the tiers progress ?
        return getItemBurnTime(item) / consumeRate;
    }

    public void outputEnergy() {
    	//Lets go around the world and try and give it to someone!
    	if (this.energyStorage.canExtract()) {
    		for (EnumFacing facing : EnumFacing.values()) {
    			doEnergyInteraction(this, world.getTileEntity(pos.offset(facing)), facing, this.maxExtract);
    	    	addPartialUpdate("energy", this.energyStorage.getEnergyStored());
    		}
    	}
    }

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(capability == CapabilityEnergy.ENERGY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
		else return super.hasCapability(capability, facing);
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