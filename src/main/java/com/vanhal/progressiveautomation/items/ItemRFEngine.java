package com.vanhal.progressiveautomation.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cofh.api.energy.IEnergyContainerItem;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.ref.Ref;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.text.DecimalFormat;

public class ItemRFEngine extends BaseItem implements IEnergyContainerItem {
	protected int maxCharge = 100000;
	
	private static DecimalFormat rfDecimalFormat = new DecimalFormat("###,###,###,###,###");
	
	public ItemRFEngine() {
		super("RFEngine");
		setTextureName(Ref.MODID+":RFEngine");
		setMaxStackSize(1);
		setMaxCharge(PAConfig.rfStored);
	}
	
	public ItemRFEngine(String name) {
		super(name);
	}
	
	public void setMaxCharge(int amount) {
		maxCharge = amount;
	}
	

	public int getMaxCharge() {
		return maxCharge;
	}
	
	public int getCharge(ItemStack itemStack) {
		initNBT(itemStack);
		return itemStack.stackTagCompound.getInteger("charge");
	}
	
	public void setCharge(ItemStack itemStack, int charge) {
		initNBT(itemStack);
		itemStack.stackTagCompound.setInteger("charge", charge);
	}
	
	public int addCharge(ItemStack itemStack, int amount) {
		int amountUsed = amount;
		int current = getCharge(itemStack);
		if ((current + amount)>maxCharge) amountUsed = (maxCharge-current);
		if ((current + amount)<0) amountUsed = (current);
		current += amount;
		if (current>=maxCharge) current = maxCharge;
		if (current<0) current = 0;
		setCharge(itemStack, current);
		return amountUsed;
	}
	
	protected void initNBT(ItemStack itemStack) {
		if (itemStack.stackTagCompound == null) {
			itemStack.stackTagCompound = new NBTTagCompound();
			itemStack.stackTagCompound.setInteger("charge", 0);
		}
	}
	
	protected boolean isInit(ItemStack itemStack) {
		return (itemStack.stackTagCompound != null);
	}
	
	
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par) {
    	if ( (itemStack!=null) && (isInit(itemStack)) ) {
    		int charge = getCharge(itemStack);
    		list.add(EnumChatFormatting.RED + "" + 
    				String.format("%s", rfDecimalFormat.format(charge)) + "/" +
    				String.format("%s", rfDecimalFormat.format(maxCharge)) + " RF");
    	} else {
    		list.add(EnumChatFormatting.GRAY + "Add to the fuel slot to");
        	list.add(EnumChatFormatting.GRAY + "power a machine with RF");
    	}
    }
    
    @SideOnly(Side.CLIENT)
    public boolean showDurabilityBar(ItemStack itemStack) {
        return isInit(itemStack);
    }
    
    @SideOnly(Side.CLIENT)
    public double getDurabilityForDisplay(ItemStack itemStack) {
        return 1.0 - (double)getCharge(itemStack) / (double)maxCharge;
    }
    
    
    protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"iii", "grg", "iii", 'i', Items.iron_ingot, 'r', Blocks.redstone_block, 'g', Items.gold_ingot});
		GameRegistry.addRecipe(recipe);
	}
	
	protected void addUpgradeRecipe() {
		addNormalRecipe();
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
