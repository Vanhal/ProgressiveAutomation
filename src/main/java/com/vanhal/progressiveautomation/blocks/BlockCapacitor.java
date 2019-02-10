package com.vanhal.progressiveautomation.blocks;

import com.vanhal.progressiveautomation.entities.capacitor.TileCapacitor;
import com.vanhal.progressiveautomation.entities.capacitor.TileCapacitorDiamond;
import com.vanhal.progressiveautomation.entities.capacitor.TileCapacitorIron;
import com.vanhal.progressiveautomation.entities.capacitor.TileCapacitorStone;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCapacitor extends BaseBlock {

    public static final Block firstTier = Blocks.FURNACE;
    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    public BlockCapacitor(int level) {
        super("Capacitor", level);
        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    public TileEntity createNewTileEntity(World world, int var2) {
        if (blockLevel >= ToolHelper.LEVEL_DIAMOND) return new TileCapacitorDiamond();
        else if (blockLevel == ToolHelper.LEVEL_IRON) return new TileCapacitorIron();
        else if (blockLevel == ToolHelper.LEVEL_STONE) return new TileCapacitorStone();
        else return new TileCapacitor();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(FACING)).getIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.byIndex(meta);
        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING});
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer).getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer).getOpposite()), 2);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote) {
            EnumFacing enumfacing = state.getValue(FACING);
            world.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
        }
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.withProperty(FACING, state.getValue(FACING));
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        IBlockState state = world.getBlockState(pos);
        world.setBlockState(pos, state.cycleProperty(FACING));
        return true;
    }
}