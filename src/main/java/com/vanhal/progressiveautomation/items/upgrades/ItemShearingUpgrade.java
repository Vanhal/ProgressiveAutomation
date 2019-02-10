package com.vanhal.progressiveautomation.items.upgrades;

import com.vanhal.progressiveautomation.upgrades.UpgradeType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemShearingUpgrade extends ItemUpgrade {

    public ItemShearingUpgrade() {
        super(UpgradeType.SHEARING);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(final ItemStack stack, final World worldIn, final List<String> tooltip, final ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + "Allows the Farmer shear animals");
    }
}