package com.vanhal.progressiveautomation.blocks;

import com.vanhal.progressiveautomation.entities.crafter.TileCrafter;
import com.vanhal.progressiveautomation.entities.crafter.TileCrafterDiamond;
import com.vanhal.progressiveautomation.entities.crafter.TileCrafterIron;
import com.vanhal.progressiveautomation.entities.crafter.TileCrafterStone;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BlockCrafter extends BaseBlock {

    public static final Block firstTier = Blocks.FURNACE;

    public BlockCrafter(int level) {
        super("Crafter", level);
    }

    public TileEntity createNewTileEntity(World world, int var2) {
        if (blockLevel >= ToolHelper.LEVEL_DIAMOND) return new TileCrafterDiamond();
        else if (blockLevel == ToolHelper.LEVEL_IRON) return new TileCrafterIron();
        else if (blockLevel == ToolHelper.LEVEL_STONE) return new TileCrafterStone();
        else return new TileCrafter();
    }

    @Override
    protected ArrayList<ItemStack> getInsides(World world, BlockPos pos) {
        TileCrafter crafter = (TileCrafter) world.getTileEntity(pos);
        if (crafter != null) {
            crafter.setInventorySlotContents(crafter.CRAFT_RESULT, ItemStack.EMPTY);
        }
        return super.getInsides(world, pos);
    }
}