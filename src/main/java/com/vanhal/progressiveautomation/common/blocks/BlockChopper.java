package com.vanhal.progressiveautomation.common.blocks;

import com.vanhal.progressiveautomation.common.entities.chopper.TileChopper;
import com.vanhal.progressiveautomation.common.entities.chopper.TileChopperDiamond;
import com.vanhal.progressiveautomation.common.entities.chopper.TileChopperIron;
import com.vanhal.progressiveautomation.common.entities.chopper.TileChopperStone;
import com.vanhal.progressiveautomation.common.util.ToolHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockChopper extends BaseBlock {

    public static final Block firstTier = Blocks.FURNACE;

    public BlockChopper(int level) {
        super("Chopper", level);
        this.rangeCount = 1;
    }

    public TileEntity createNewTileEntity(World world, int var2) {
        if (blockLevel >= ToolHelper.LEVEL_DIAMOND) return new TileChopperDiamond();
        else if (blockLevel == ToolHelper.LEVEL_IRON) return new TileChopperIron();
        else if (blockLevel == ToolHelper.LEVEL_STONE) return new TileChopperStone();
        else return new TileChopper();
    }
}