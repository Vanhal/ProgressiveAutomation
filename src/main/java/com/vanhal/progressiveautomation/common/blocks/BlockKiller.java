package com.vanhal.progressiveautomation.common.blocks;

import com.vanhal.progressiveautomation.common.entities.killer.TileKiller;
import com.vanhal.progressiveautomation.common.entities.killer.TileKillerDiamond;
import com.vanhal.progressiveautomation.common.entities.killer.TileKillerIron;
import com.vanhal.progressiveautomation.common.entities.killer.TileKillerStone;
import com.vanhal.progressiveautomation.common.util.ToolHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockKiller extends BaseBlock {

    public static final Block firstTier = Blocks.FURNACE;

    public BlockKiller(int level) {
        super("Killer", level);
        this.rangeCount = 0;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int var2) {
        if (blockLevel >= ToolHelper.LEVEL_DIAMOND) return new TileKillerDiamond();
        else if (blockLevel == ToolHelper.LEVEL_IRON) return new TileKillerIron();
        else if (blockLevel == ToolHelper.LEVEL_STONE) return new TileKillerStone();
        else return new TileKiller();
    }
}