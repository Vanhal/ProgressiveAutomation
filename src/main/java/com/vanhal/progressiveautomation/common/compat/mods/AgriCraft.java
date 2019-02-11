package com.vanhal.progressiveautomation.common.compat.mods;

import com.vanhal.progressiveautomation.common.util.PlayerFake;
import com.vanhal.progressiveautomation.common.util.Point3I;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IPlantable;

import java.util.ArrayList;
import java.util.List;


public class AgriCraft extends Vanilla {

    public AgriCraft() {
        this.modID = "agricraft";
    }

    @Override
    public boolean shouldLoad() {
        return checkModLoad();
    }

    @Override
    public boolean isPlantible(ItemStack item) {
        if (item.getItem() instanceof IPlantable) {
            return true;
        }

        if (!item.isEmpty()) {
            if (item.getItem() != null) {
                if (item.getItem().toString().contains("ItemAgriSeed")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isPlant(Block plantBlock, IBlockState state) {
        if (super.isPlant(plantBlock, state)) {
            if (Block.REGISTRY.getNameForObject(plantBlock).getNamespace().equals(modID)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected IBlockState getPlantBlock(World worldObj, ItemStack itemStack, Point3I point) {
        if (this.isPlantible(itemStack)) {
            return Blocks.AIR.getDefaultState();
        }
        return null;
    }

    @Override
    public boolean validBlock(World world, ItemStack itemStack, Point3I point) {
        IBlockState plant = getPlantBlock(world, itemStack, point);
        if (plant != null) {
            TileEntity tile = world.getTileEntity(point.toPosition());
            if (tile != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tile.writeToNBT(tag);
                if (tag.hasKey("agri_cross_crop")) {
                    if ((!tag.hasKey("agri_seed")) && (!tag.getBoolean("agri_cross_crop"))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean placeSeed(World world, ItemStack itemStack, Point3I point, boolean doAction) {
        IBlockState block = world.getBlockState(point.toPosition());
        Block actualBlock = block.getBlock();
        if (actualBlock != null) {
            if (doAction) {
                PlayerFake faker = new PlayerFake((WorldServer) world);
                faker.inventory.setInventorySlotContents(1, itemStack.copy());
                faker.setItemInHand(1);
                faker.setPosition(point.getX(), point.getY(), point.getZ());
                actualBlock.onBlockActivated(world, point.toPosition(), block, faker, EnumHand.MAIN_HAND, EnumFacing.UP, 0, 0, 0);
                if ((!faker.inventory.getStackInSlot(1).isEmpty()) && (faker.inventory.getStackInSlot(1).getCount() == itemStack.getCount())) {
                    faker = null;
                    return false;
                }
                faker = null;
            }
            return true;
        }
        return false;
    }

    @Override
    public List<ItemStack> harvestPlant(Point3I plantPoint, Block plantBlock, IBlockState state, World world) {
        PlayerFake faker = new PlayerFake((WorldServer) world);
        plantBlock.onBlockActivated(world, plantPoint.toPosition(), state, faker, EnumHand.MAIN_HAND, EnumFacing.UP, 0, 0, 0);
        AxisAlignedBB block = new AxisAlignedBB(plantPoint.getX(), plantPoint.getY(), plantPoint.getZ(),
                plantPoint.getX() + 1, plantPoint.getY() + 1, plantPoint.getZ() + 1);
        List<EntityItem> entities = world.getEntitiesWithinAABB(EntityItem.class, block);
        if (entities.isEmpty()) {
            return null;
        }

        List<ItemStack> items = new ArrayList<>();
        for (EntityItem item : entities) {
            items.add(item.getItem());
            world.removeEntity(item);
        }
        return items;
    }
}