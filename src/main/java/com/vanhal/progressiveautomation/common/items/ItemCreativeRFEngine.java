package com.vanhal.progressiveautomation.common.items;

import com.vanhal.progressiveautomation.PAConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemCreativeRFEngine extends ItemRFEngine {

    public ItemCreativeRFEngine() {
        setMaxStackSize(1);
        setMaxCharge(PAConfig.rfStored);
    }

    public int getCharge(ItemStack itemStack) {
        return PAConfig.rfStored;
    }

    protected void addNormalRecipe() {
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean par) {
        list.add(TextFormatting.GRAY + "Can power a machine");
        list.add(TextFormatting.DARK_PURPLE + "Creative Only");
    }
    
    @Override
    public double getDurabilityForDisplay(ItemStack itemStack) {
    	return 0.00;
    }
}