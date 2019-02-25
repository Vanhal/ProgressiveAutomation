package com.vanhal.progressiveautomation.common.items;

import com.vanhal.progressiveautomation.PAConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.energy.IEnergyStorage;

import java.text.DecimalFormat;
import java.util.List;

public class ItemRFEngine extends BaseItem implements IEnergyStorage {

    protected int maxCharge = 100000;
    private static DecimalFormat rfDecimalFormat = new DecimalFormat("###,###,###,###,###");
    private EnergyStats myCap;
    
    public ItemRFEngine() {
        this.myCap = new EnergyStats(this.maxCharge);
        setMaxStackSize(1);
        setMaxCharge(PAConfig.rfStored);
    }

    public void setMaxCharge(int amount) {
        maxCharge = amount;
        this.myCap.setMaxCharge(amount);
    }

    public int getMaxCharge() {
        return this.myCap.getMaxCharge();
    }

    public int getCharge() {
    	return this.myCap.getEnergyStored();
    }

    public void setCharge(int charge) {
    	this.myCap.setCharge(charge);
    }

    public int addCharge(int amount) {
        int amountUsed = amount;
        int current = this.getCharge();
        if ((current + amount) > maxCharge) amountUsed = (maxCharge - current);
        if ((current + amount) < 0) amountUsed = (current);
        current += amount;
        if (current >= maxCharge) current = maxCharge;
        if (current < 0) current = 0;
        setCharge(current);
        return amountUsed;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean par) {
        if (!itemStack.isEmpty()) {
            int charge = this.getCharge();
            list.add(TextFormatting.RED + "" +
                    String.format("%s", rfDecimalFormat.format(charge)) + "/" +
                    String.format("%s", rfDecimalFormat.format(maxCharge)) + " FE");
        } else {
            list.add(TextFormatting.GRAY + "Add to the fuel slot to");
            list.add(TextFormatting.GRAY + "power a machine with FE");
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean showDurabilityBar() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public double getDurabilityForDisplay() {
        return 1.0 - (double) this.getCharge() / (double) maxCharge;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (simulate) return maxReceive;
        return addCharge(maxReceive);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (simulate) return maxExtract;
        return addCharge(maxExtract * -1);
    }

    @Override
    public int getEnergyStored() {
        return getCharge();
    }

    @Override
    public int getMaxEnergyStored() {
        return getMaxCharge();
    }

	@Override
	public boolean canExtract() {
		return this.getCharge() > 0;
	}

	@Override
	public boolean canReceive() {
		return this.getCharge() < this.maxCharge;
	}

	private class EnergyStats {
		private int maxCharge;
		private int curCharge;
		
		public EnergyStats(int maxCharge) {
			this.maxCharge = maxCharge;
		}
		
		public int getEnergyStored() {
			return this.curCharge;
		}
		
		public void setMaxCharge(int newMax) {
			this.maxCharge = newMax;
		}
		
		public int getMaxCharge() {
			return this.maxCharge;
		}
		
		public void setCharge(int newCharge) {
			if(newCharge < maxCharge) this.curCharge = newCharge;
		}
	}
}