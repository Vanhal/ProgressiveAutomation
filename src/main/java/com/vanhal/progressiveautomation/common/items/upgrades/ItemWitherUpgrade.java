package com.vanhal.progressiveautomation.common.items.upgrades;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemWitherUpgrade extends ItemUpgrade {

    public ItemWitherUpgrade() {
        super(UpgradeType.WITHER);
    }

    protected void addUpgradeRecipe() {
        this.addNormalRecipe();
    }

    protected void addTieredRecipe(Item previousTier) {
        this.addNormalRecipe();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(final ItemStack stack, final World worldIn, final List<String> tooltip, final ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + "Multiplies the range of a machine by " + PAConfig.witherMultiplier);
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack itemStack) {
        return true;
    }
}