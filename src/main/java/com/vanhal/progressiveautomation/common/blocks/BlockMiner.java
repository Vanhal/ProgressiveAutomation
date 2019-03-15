package com.vanhal.progressiveautomation.common.blocks;

import com.vanhal.progressiveautomation.common.entities.miner.TileMiner;
import com.vanhal.progressiveautomation.common.entities.miner.TileMinerDiamond;
import com.vanhal.progressiveautomation.common.entities.miner.TileMinerIron;
import com.vanhal.progressiveautomation.common.entities.miner.TileMinerStone;
import com.vanhal.progressiveautomation.common.util.ToolHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMiner extends BaseBlock {

    public BlockMiner(int level) {
        super("Miner", level);
    }

    public TileEntity createNewTileEntity(World world, int var2) {
        if (blockLevel >= ToolHelper.LEVEL_DIAMOND) return new TileMinerDiamond();
        else if (blockLevel == ToolHelper.LEVEL_IRON) return new TileMinerIron();
        else if (blockLevel == ToolHelper.LEVEL_STONE) return new TileMinerStone();
        else return new TileMiner();
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess world, BlockPos pos, EnumFacing side) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileMiner) {
            return (((TileMiner) tile).isDone()) ? 15 : 0;
        }
        return 0;
    }

    @Override
    public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.DOWN ? this.getWeakPower(state, world, pos, side) : 0;
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }
}
