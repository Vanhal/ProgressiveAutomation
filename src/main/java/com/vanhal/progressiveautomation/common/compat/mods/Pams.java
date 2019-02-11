package com.vanhal.progressiveautomation.common.compat.mods;

import com.vanhal.progressiveautomation.common.util.Point3I;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class Pams extends Vanilla {

    public Pams() {
        modID = "harvestcraft";
    }

    @Override
    public boolean shouldLoad() {
        return checkModLoad();
    }

    @Override
    public boolean isPlantible(ItemStack item) {
        if (item.getItem() instanceof IPlantable) {
            if (Item.REGISTRY.getNameForObject(item.getItem()).getNamespace().equals(modID)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldHoe(ItemStack item) {
        return true;
    }

    @Override
    public boolean isSapling(ItemStack stack) {
        return OreDictionary.containsMatch(true, OreDictionary.getOres("treeSapling"), stack);
    }

    @Override
    public boolean isPlant(Block plantBlock, IBlockState state) {
        if (super.isPlant(plantBlock, state)) {
            if (plantBlock.getClass().getName().startsWith("com.pam.harvestcraft")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean validBlock(World worldObj, ItemStack itemStack, Point3I point) {
        if (Item.REGISTRY.getNameForObject(itemStack.getItem()).getNamespace().equals(modID)) {
            IBlockState plantState = getPlantBlock(worldObj, itemStack, point);
            Block plant = plantState.getBlock();
            if (plant != null) {
                Point3I dirtPoint = new Point3I(point.getX(), point.getY() - 1, point.getZ());
                IBlockState dirtBlockState = worldObj.getBlockState(dirtPoint.toPosition());
                Block dirtBlock = dirtBlockState.getBlock();
                if (dirtBlock == Blocks.FARMLAND) {
                    return (plant.canPlaceBlockAt(worldObj, point.toPosition())) && (worldObj.getBlockState(point.toPosition()).getBlock() != plant);
                }
            }
        }
        return false;
    }

    @Override
    public List<ItemStack> harvestPlant(Point3I plantPoint, Block plantBlock, IBlockState state, World worldObj) {
        List<ItemStack> items = plantBlock.getDrops(worldObj, plantPoint.toPosition(), state, 0);
        worldObj.setBlockState(plantPoint.toPosition(), plantBlock.getDefaultState(), 2);
        return items;
    }
}