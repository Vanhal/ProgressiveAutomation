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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileGenerator extends BaseTileEntity {

    protected float fireRisk = 0.02f;
    protected int generationRate = 10;
    protected int consumeRate = 1;
    protected boolean burnUpdate = false;
    public int SLOT_CHARGER = 1;
    private PAEnergyStorage energyStorage;
    
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
        	this.energyStorage.setEnergy(nbt.getInteger("energy"));
        }
    }

    public void setFireChance(float chance) {
        fireRisk = chance;
    }

    public void setEnergyStorage(int size, float rate) {
    	if(this.energyStorage==null) this.energyStorage = new PAEnergyStorage(size, 0, (int)Math.ceil(rate));
    	
        generationRate = (int) Math.ceil(((float) PAConfig.rfCost * rate));
        consumeRate = (int) Math.ceil(((float) PAConfig.fuelCost * rate));
    }

    @Override
    public void update() {
        super.update();
        if (!world.isRemote) {
            if (isBurning()) {
                this.energyStorage.generatePower(generationRate);
                checkForFire();
            }

            //Charge items in charge slot
            if (!slots[SLOT_CHARGER].isEmpty()) {
                if (this.energyStorage.canExtract()) {
                    if (slots[SLOT_CHARGER].hasCapability(CapabilityEnergy.ENERGY, EnumFacing.UP)) {
                        IEnergyStorage container = slots[SLOT_CHARGER].getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP);
                        if (container.canReceive()) {
                        	int trans = this.energyStorage.getMaxTransfer() >= this.energyStorage.getEnergyStored()?this.energyStorage.getEnergyStored():this.energyStorage.getMaxTransfer();
                        	int giveAmount = container.receiveEnergy(trans, false);
                            if (giveAmount > 0) {
                            	energyStorage.extractEnergy(giveAmount, false);
                            }
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
        if (this.energyStorage.canReceive()) {
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

    public void outputEnergy() {
    	//Lets go around the world and try and give it to someone!
    	if (this.energyStorage.canExtract()) {
    		for (EnumFacing facing : EnumFacing.values()) {
    			//Do we have any energy up for grabs?
    			TileEntity entity = world.getTileEntity(pos.offset(facing));
    			if (entity != null) {
    				if (entity.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite())) {
    					IEnergyStorage energy = entity.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
    					if (energy.canReceive()) {
    						int giveAmount = energy.receiveEnergy(this.energyStorage.getEnergyStored(), false);
    						if (giveAmount > 0) {
    							this.energyStorage.extractEnergy(giveAmount, false);
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