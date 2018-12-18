package com.vanhal.progressiveautomation.items.tools;

import java.util.List;

import com.vanhal.progressiveautomation.items.BaseItem;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWitherIron extends BaseItem {
	public ItemWitherIron() {
		super("WitherIron");
		//setTextureName("Minecraft:iron_ingot");
	}
	
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemStack) {
        return true;
    }

	@SideOnly(Side.CLIENT)
	@Override
    public void addInformation(final ItemStack stack, final World worldIn,
    		final List<String> tooltip, final ITooltipFlag flagIn) {
    	tooltip.add(TextFormatting.GRAY + "Can be used to craft tools");
    }
}
