package com.vanhal.progressiveautomation.items.upgrades;

import java.util.List;

import com.vanhal.progressiveautomation.upgrades.UpgradeType;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFillerUpgrade extends ItemUpgrade {
	public ItemFillerUpgrade() {
		super("FillerUpgrade", UpgradeType.FILLER);
	}

	@SideOnly(Side.CLIENT)
	@Override
    public void addInformation(final ItemStack stack, final World worldIn,
    		final List<String> tooltip, final ITooltipFlag flagIn) {
		tooltip.add(TextFormatting.GRAY + "Will make the miner fill in any air and fluid blocks while mining");
       
    }
}
