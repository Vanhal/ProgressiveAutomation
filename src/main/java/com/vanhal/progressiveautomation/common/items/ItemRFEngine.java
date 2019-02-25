package com.vanhal.progressiveautomation.common.items;

import cofh.redstoneflux.api.IEnergyContainerItem;
import com.vanhal.progressiveautomation.PAConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.text.DecimalFormat;
import java.util.List;

public class ItemRFEngine extends BaseItem implements IEnergyContainerItem {

    protected int maxCharge = 100000;
    private static DecimalFormat rfDecimalFormat = new DecimalFormat("###,###,###,###,###");

    public ItemRFEngine() {
        setMaxStackSize(1);
        setMaxCharge(PAConfig.rfStored);
    }

    public void setMaxCharge(int amount) {
        maxCharge = amount;
    }

    public int getMaxCharge() {
        return maxCharge;
    }

    public int getCharge(ItemStack itemStack) {
        initNBT(itemStack);
        return itemStack.getTagCompound().getInteger("charge");
    }

    public void setCharge(ItemStack itemStack, int charge) {
        initNBT(itemStack);
        itemStack.getTagCompound().setInteger("charge", charge);
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

    protected void initNBT(ItemStack itemStack) {
        if (itemStack.getTagCompound() == null) {
            itemStack.setTagCompound(new NBTTagCompound());
            itemStack.getTagCompound().setInteger("charge", 0);
        }
    }

    protected boolean isInit(ItemStack itemStack) {
        return (itemStack.getTagCompound() != null);
    }


    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean par) {
        if ((!itemStack.isEmpty()) && (isInit(itemStack))) {
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
        return isInit(itemStack);
    }

    @SideOnly(Side.CLIENT)
    public double getDurabilityForDisplay(ItemStack itemStack) {
        return 1.0 - (double) getCharge(itemStack) / (double) maxCharge;
    }

    @Override
    public int receiveEnergy(ItemStack itemStack, int maxReceive, boolean simulate) {
        if (simulate) return maxReceive;
        return addCharge(itemStack, maxReceive);
    }

    @Override
    public int extractEnergy(ItemStack itemStack, int maxExtract, boolean simulate) {
        if (simulate) return maxExtract;
        return addCharge(itemStack, maxExtract * -1);
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        return getCharge(container);
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return getMaxCharge();
    }
}