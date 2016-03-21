package com.vanhal.progressiveautomation.items;

import java.util.List;

import com.vanhal.progressiveautomation.PAConfig;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCreativeRFEngine extends ItemRFEngine {
	public ItemCreativeRFEngine() {
		super("CreativeRFEngine");
		//setTextureName(Ref.MODID+":CreativeRFEngine");
		setMaxStackSize(1);
		setMaxCharge(PAConfig.rfStored);
	}
	
	public int getCharge(ItemStack itemStack) {
		return PAConfig.rfStored;
	}
	
	protected void addNormalRecipe() { }
	
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par) {
		list.add(TextFormatting.GRAY + "Can power a machine");
		list.add(TextFormatting.DARK_PURPLE + "Creative Only");
       
    }
}
