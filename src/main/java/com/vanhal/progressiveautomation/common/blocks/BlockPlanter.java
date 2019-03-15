package com.vanhal.progressiveautomation.common.blocks;

import com.vanhal.progressiveautomation.common.entities.planter.TilePlanter;
import com.vanhal.progressiveautomation.common.entities.planter.TilePlanterDiamond;
import com.vanhal.progressiveautomation.common.entities.planter.TilePlanterIron;
import com.vanhal.progressiveautomation.common.entities.planter.TilePlanterStone;
import com.vanhal.progressiveautomation.common.util.ToolHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockPlanter extends BaseBlock {

    public static final Block firstTier = Blocks.FURNACE;

    public BlockPlanter(int level) {
        super("Planter", level);
    }

    public TileEntity createNewTileEntity(World world, int var2) {
        if (blockLevel >= ToolHelper.LEVEL_DIAMOND) return new TilePlanterDiamond();
        else if (blockLevel == ToolHelper.LEVEL_IRON) return new TilePlanterIron();
        else if (blockLevel == ToolHelper.LEVEL_STONE) return new TilePlanterStone();
        else return new TilePlanter();
    }
}