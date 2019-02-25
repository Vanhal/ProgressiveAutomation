package com.vanhal.progressiveautomation.common.items;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.common.util.PAEnergyStorage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.text.DecimalFormat;
import java.util.List;

public class ItemRFEngine extends BaseItem {

    protected int maxCharge = 100000;
    private static DecimalFormat rfDecimalFormat = new DecimalFormat("###,###,###,###,###");
    private PAEnergyStorage storage;
    
    public ItemRFEngine() {
        setMaxStackSize(1);
        setMaxCharge(PAConfig.rfStored);
    }

    public void setMaxCharge(int amount) {
        maxCharge = amount;
        if(this.storage == null) this.storage = new PAEnergyStorage(this.maxCharge, 640);
    }

    public int getMaxCharge() {
        return maxCharge;
    }

    public int getCharge(ItemStack itemStack) {
    	if(itemStack.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.UP)) {
    		return itemStack.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP).getEnergyStored();
    	} else return 0;
    }

    public void setCharge(ItemStack itemStack, int charge) {
    	if(itemStack.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.UP)) {
    		IEnergyStorage cap = itemStack.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP);
    		int curCharge = cap.getEnergyStored();
    		int diff = charge - curCharge;
    		if(diff < 0) {
    			diff *= -1;
    			cap.extractEnergy(diff, false);
    		} else {
    			cap.receiveEnergy(diff, false);
    		}
    	}
    }

    public int addCharge(ItemStack itemStack, int amount) {
        int amountUsed = amount;
        int current = getCharge(itemStack);
        if ((current + amount) > maxCharge) amountUsed = (maxCharge - current);
        if ((current + amount) < 0) amountUsed = (current);
        current += amount;
        if (current >= maxCharge) current = maxCharge;
        if (current < 0) current = 0;
        setCharge(itemStack, current);
        return amountUsed;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean par) {
        if ((!itemStack.isEmpty()) && (itemStack.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.UP))) {
            int charge = getCharge(itemStack);
            list.add(TextFormatting.RED + "" +
                    String.format("%s", rfDecimalFormat.format(charge)) + "/" +
                    String.format("%s", rfDecimalFormat.format(maxCharge)) + " RF");
        } else {
            list.add(TextFormatting.GRAY + "Add to the fuel slot to");
            list.add(TextFormatting.GRAY + "power a machine with RF");
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean showDurabilityBar(ItemStack itemStack) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public double getDurabilityForDisplay(ItemStack itemStack) {
        return 1.0 - (double) getCharge(itemStack) / (double) maxCharge;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new ICapabilityProvider() {
        	private PAEnergyStorage storage = new PAEnergyStorage(10000, 640);
        	
        	@Override
        	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        		if(capability == CapabilityEnergy.ENERGY) return true;
        		return false;
        	}
        	
        	@Override
            public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        		if(capability == CapabilityEnergy.ENERGY) return CapabilityEnergy.ENERGY.cast(storage);
        		else return null;
        	}
        };
    }

}