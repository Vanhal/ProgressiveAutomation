package com.vanhal.progressiveautomation.common.items;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.common.util.PAEnergyStorage;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.text.DecimalFormat;
import java.util.List;

public class ItemRFEngine extends Item {

    protected int maxCharge = 100000;
    private static DecimalFormat rfDecimalFormat = new DecimalFormat("###,###,###,###,###");

    public ItemRFEngine() {
        setMaxStackSize(1);
        setMaxCharge(PAConfig.rfStored);
    }

    protected void setMaxCharge(int amount) {
        maxCharge = amount;
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    public int getMaxCharge() {
        return maxCharge;
    }

    public int getCharge(ItemStack itemStack) {
        if (itemStack.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.UP)) {
            return itemStack.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP).getEnergyStored();
        } else return 0;
    }

    public int addCharge(ItemStack itemStack, int amount) {
        int amountUsed = 0;
        IEnergyStorage cap = itemStack.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.UP) ? itemStack.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP) : null;
        if (cap == null) return 0;
        if (cap.canReceive()) {
            amountUsed = cap.receiveEnergy(amount, false);
        }
        return amountUsed;
    }

    @Override
    public void addInformation(ItemStack itemStack, World player, List<String> list, ITooltipFlag advanced) {
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

    @Override
    public boolean showDurabilityBar(ItemStack itemStack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack itemStack) {
        if (itemStack.hasCapability(CapabilityEnergy.ENERGY, null)) {
            IEnergyStorage storage = itemStack.getCapability(CapabilityEnergy.ENERGY, null);
            if (storage != null) {
                double maxAmount = storage.getMaxEnergyStored();
                double energyDif = maxAmount - storage.getEnergyStored();
                return energyDif / maxAmount;
            }
        }
        return super.getDurabilityForDisplay(itemStack);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new ICapabilityProvider() {
            private PAEnergyStorage storage = new PAEnergyStorage(((ItemRFEngine) stack.getItem()).getMaxCharge(), 640) {
                @Override
                public int getEnergyStored() {
                    if (!stack.hasTagCompound()) initStack();
                    this.energy = stack.getTagCompound().getInteger("Energy");

                    return this.energy;
                }

                @Override
                public void setEnergyStored(int energy) {
                    if (!stack.hasTagCompound()) initStack();

                    stack.getTagCompound().setInteger("Energy", energy);
                    this.energy = energy;
                }

                protected void initStack() {
                    stack.setTagCompound(new NBTTagCompound());
                    stack.getTagCompound().setInteger("Energy", this.energy);
                }

                @Override
                public int receiveEnergy(int amount, boolean simulate) {
                    if (!stack.hasTagCompound()) initStack();
                    int rcv = (amount > this.maxReceive ? this.maxReceive : amount);
                    int next = this.energy + rcv;
                    int rv = 0;

                    if (next > this.capacity) rv = this.capacity - next;
                    else rv = rcv;

                    this.energy += rv;
                    setEnergyStored(this.energy);
                    return rv;
                }

                @Override
                public boolean canReceive() {
                    return ((this.energy < this.capacity) && (this.maxReceive > 0));
                }

                @Override
                public boolean canExtract() {
                    return ((this.energy > 0) && (this.maxExtract > 0));
                }
            };

            @Override
            public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
                if (capability == CapabilityEnergy.ENERGY) return true;
                return false;
            }

            @Override
            public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
                if (capability == CapabilityEnergy.ENERGY) return CapabilityEnergy.ENERGY.cast(storage);
                else return null;
            }
        };
    }

}