package com.vanhal.progressiveautomation.common.items.upgrades;

import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemCobbleGenUpgrade extends ItemUpgrade {

    public ItemCobbleGenUpgrade() {
        super(UpgradeType.COBBLE_GEN);
    }

    protected void addUpgradeRecipe() {
        this.addNormalRecipe();
    }

    protected void addTieredRecipe(Item previousTier) {
        this.addNormalRecipe();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack stack, final World world, final List<String> tooltip, final ITooltipFlag flag) {
        tooltip.add(TextFormatting.GRAY + "Uses extra pick durability to mine cobble from an internal cobble gen");
    }
}