package com.vanhal.progressiveautomation.common.blocks;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.References;
import com.vanhal.progressiveautomation.api.PAItems;
import com.vanhal.progressiveautomation.common.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.common.entities.UpgradeableTileEntity;
import com.vanhal.progressiveautomation.common.upgrades.UpgradeRegistry;
import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.common.util.IDismantleable;
import com.vanhal.progressiveautomation.common.util.ToolHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

import java.util.ArrayList;


public class BaseBlock extends BlockContainer implements IDismantleable {

    public int GUIid = -1;
    protected int rangeCount = -1;
    protected int blockLevel = ToolHelper.LEVEL_WOOD;

    public BaseBlock(String machine, int level) {
        super(Material.IRON);
        String blockName = machine.toLowerCase() + "_" + returnLevelName(level);
        setHardness(1.0f);
        setRegistryName(blockName);
        setTranslationKey(References.MODID + ":" + blockName);
        setCreativeTab(ProgressiveAutomation.PATab);

        this.blockLevel = level;

        GUIid = ProgressiveAutomation.proxy.registerGui(machine);
    }

    public static String returnLevelName(int level) {
        if (level == ToolHelper.LEVEL_STONE) {
            return "stone";
        } else if (level == ToolHelper.LEVEL_IRON) {
            return "iron";
        } else if (level == ToolHelper.LEVEL_DIAMOND) {
            return "diamond";
        }
        return "wooden";
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        if (blockLevel == ToolHelper.LEVEL_WOOD) {
            return 5;
        } else {
            return 0;
        }
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        if ((!heldItem.isEmpty()) && (heldItem.getItem() != null) && (heldItem.getItem() == PAItems.WRENCH)) {
            return false;
        } else if (!world.isRemote) {
            if (GUIid >= 0) {
                if (!(player instanceof FakePlayer)) {
                    FMLNetworkHandler.openGui(player, ProgressiveAutomation.instance, GUIid, world, pos.getX(), pos.getY(), pos.getZ());
                }
            }
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int var2) {
        return null;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        BaseTileEntity tileEntity = (BaseTileEntity) world.getTileEntity(pos);
        if (tileEntity != null) {
            ArrayList<ItemStack> items = getInsides(world, pos);
            for (ItemStack item : items) {
                dumpItems(world, pos, item);
            }
            //world.func_147453_f(pos, state.getBlock()); //I have no idea what this method did....
        }
        super.breakBlock(world, pos, state);
    }

    public void dumpItems(World world, BlockPos pos, ItemStack items) {
        EntityItem entItem = new EntityItem(world, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, items);
        float f3 = 0.05F;
        entItem.motionX = (world.rand.nextGaussian() * f3);
        entItem.motionY = (world.rand.nextGaussian() * f3 + 0.2F);
        entItem.motionZ = (world.rand.nextGaussian() * f3);
        world.spawnEntity(entItem);
    }

    protected ArrayList<ItemStack> getInsides(World world, BlockPos pos) {
        ArrayList<ItemStack> items = new ArrayList<>();
        BaseTileEntity tileEntity = (BaseTileEntity) world.getTileEntity(pos);
        if (tileEntity != null) {
            //get the inventory
            for (int i = 0; i < tileEntity.getSizeInventory(); ++i) {
                ItemStack itemstack = tileEntity.getStackInSlot(i);
                if (!itemstack.isEmpty()) {
                    items.add(itemstack);
                }
            }

            if (tileEntity instanceof UpgradeableTileEntity) {
                UpgradeableTileEntity tileMachine = (UpgradeableTileEntity) tileEntity;
                for (UpgradeType upgradeType : tileMachine.getInstalledUpgradeTypes()) {
                    int amount = tileMachine.getUpgradeAmount(upgradeType);
                    while (amount > 0) {
                        ItemStack upgradeItemStack = new ItemStack(UpgradeRegistry.getUpgradeItem(upgradeType));
                        int stackSize = amount > 64 ? 64 : amount;
                        upgradeItemStack.setCount(stackSize);
                        amount -= stackSize;
                        items.add(upgradeItemStack);
                    }
                }
            }
        }
        return items;
    }

    //IDismantleable stuff
    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer player, World world, int x, int y, int z, boolean returnDrops) {
        BlockPos pos = new BlockPos(x, y, z);
        Block targetBlock = world.getBlockState(pos).getBlock();
        ItemStack block = new ItemStack(targetBlock);
        // Get the NBT tag contents
        if (world.getTileEntity(pos) instanceof BaseTileEntity) {
            BaseTileEntity tileEntity = ((BaseTileEntity) world.getTileEntity(pos));
            tileEntity.writeToItemStack(block);
        }

        if (!returnDrops) {
            dumpItems(world, pos, block);
            // Remove the tile entity first, so inventory/upgrades doesn't get dumped
            world.removeTileEntity(pos);
            world.setBlockToAir(pos);
        }

        ArrayList<ItemStack> items = new ArrayList<>();
        items.add(block);
        return items;
    }

    @Override
    public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z) {
        return true;
    }

    @Override
    public boolean rotateBlock(World worldObj, BlockPos pos, EnumFacing axis) {
        BaseTileEntity tileEntity = (BaseTileEntity) worldObj.getTileEntity(pos);
        if (tileEntity.facing == EnumFacing.NORTH) tileEntity.facing = EnumFacing.EAST;
        else if (tileEntity.facing == EnumFacing.EAST) tileEntity.facing = EnumFacing.SOUTH;
        else if (tileEntity.facing == EnumFacing.SOUTH) tileEntity.facing = EnumFacing.WEST;
        else if (tileEntity.facing == EnumFacing.WEST) tileEntity.facing = EnumFacing.NORTH;
        //ProgressiveAutomation.logger.info(chopper.facing.toString());
        worldObj.markBlockRangeForRenderUpdate(pos, pos);
        return true;
    }

    protected EnumFacing nextFace(EnumFacing dir) {
        if (dir == EnumFacing.NORTH) return EnumFacing.EAST;
        else if (dir == EnumFacing.EAST) return EnumFacing.SOUTH;
        else if (dir == EnumFacing.SOUTH) return EnumFacing.WEST;
        else if (dir == EnumFacing.WEST) return EnumFacing.NORTH;
        return dir;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (!worldIn.isRemote) {
            if (worldIn.getTileEntity(pos) instanceof BaseTileEntity) {
                BaseTileEntity tileEntity = (BaseTileEntity) worldIn.getTileEntity(pos);
                tileEntity.readFromItemStack(stack);
            }
        }
    }
}