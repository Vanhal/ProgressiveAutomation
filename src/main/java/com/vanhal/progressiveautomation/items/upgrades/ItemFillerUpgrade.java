package com.vanhal.progressiveautomation.items.upgrades;

import com.vanhal.progressiveautomation.upgrades.UpgradeType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemFillerUpgrade extends ItemUpgrade {

    public ItemFillerUpgrade() {
        super(UpgradeType.FILLER);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(final ItemStack stack, final World world, final List<String> tooltip, final ITooltipFlag flag) {
        tooltip.add(TextFormatting.GRAY + "Will make the miner fill in any air and fluid blocks while mining");
    }
}