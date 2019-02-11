package com.vanhal.progressiveautomation.common.blocks;

import com.vanhal.progressiveautomation.common.entities.farmer.TileFarmer;
import com.vanhal.progressiveautomation.common.entities.farmer.TileFarmerDiamond;
import com.vanhal.progressiveautomation.common.entities.farmer.TileFarmerIron;
import com.vanhal.progressiveautomation.common.entities.farmer.TileFarmerStone;
import com.vanhal.progressiveautomation.common.util.ToolHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFarmer extends BaseBlock {

    public static final Block firstTier = Blocks.FURNACE;

    public BlockFarmer(int level) {
        super("Farmer", level);
        this.rangeCount = 1;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int var2) {
        if (blockLevel >= ToolHelper.LEVEL_DIAMOND) return new TileFarmerDiamond();
        else if (blockLevel == ToolHelper.LEVEL_IRON) return new TileFarmerIron();
        else if (blockLevel == ToolHelper.LEVEL_STONE) return new TileFarmerStone();
        else return new TileFarmer();
    }
}