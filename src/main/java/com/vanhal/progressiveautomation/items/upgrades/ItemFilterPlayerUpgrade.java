package com.vanhal.progressiveautomation.items.upgrades;

import com.vanhal.progressiveautomation.upgrades.UpgradeType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemFilterPlayerUpgrade extends ItemUpgrade {

    public ItemFilterPlayerUpgrade() {
        super(UpgradeType.FILTER_PLAYER);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final World worldIn, final List<String> tooltip, final ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + "Will make the Killer to kill players only");
    }
}