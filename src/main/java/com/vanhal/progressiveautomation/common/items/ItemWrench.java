package com.vanhal.progressiveautomation.common.items;

import com.vanhal.progressiveautomation.common.blocks.BaseBlock;
import com.vanhal.progressiveautomation.common.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.common.util.IDismantleable;
import com.vanhal.progressiveautomation.common.util.WrenchModes;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class ItemWrench extends Item {

    public ItemWrench() {
        this.setMaxStackSize(1);
    }

    private void setMode(ItemStack itemStack, WrenchModes.Mode mode) {
        this.setDamage(itemStack, mode.ordinal());
    }

    private WrenchModes.Mode getMode(ItemStack itemStack) {
        if (WrenchModes.modes.size() > itemStack.getItemDamage()) {
            return WrenchModes.modes.get(itemStack.getItemDamage());
        }
        return WrenchModes.Mode.Rotate;
    }

    @Override
    public void addInformation(final ItemStack stack, final World world, final List<String> tooltip, final ITooltipFlag flag) {
        tooltip.add(TextFormatting.GRAY + "Current Mode: " + TextFormatting.WHITE + getMode(stack));
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing face, float hitX, float hitY, float hitZ, EnumHand hand) {
        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ) {
        ItemStack itemStack = player.getHeldItem(hand);
        if (itemStack.isEmpty() || itemStack.getItem() != this) {
            return EnumActionResult.PASS;
        }

        Block block = world.getBlockState(pos).getBlock();
        if (player.isSneaking()) {
            if (block instanceof IDismantleable) {
                IDismantleable dBlock = (IDismantleable) block;
                if (dBlock.canDismantle(player, world, pos.getX(), pos.getY(), pos.getZ())) {
                    if (!world.isRemote)
                        dBlock.dismantleBlock(player, world, pos.getX(), pos.getY(), pos.getZ(), false);
                    return EnumActionResult.SUCCESS;
                }
            }
        } else {
            if (getMode(itemStack) == WrenchModes.Mode.Rotate) {
                block.rotateBlock(world, pos, face);
                return EnumActionResult.SUCCESS;
            } else {
                if (block instanceof BaseBlock) {
                    BaseTileEntity PABlock = (BaseTileEntity) world.getTileEntity(pos);
                    if (getMode(itemStack) == WrenchModes.Mode.Query) {
                        if (world.isRemote)
                            player.sendMessage(new TextComponentString(face + " side currently set to: " + PABlock.getSide(face)));
                    } else {
                        PABlock.setSide(face, getMode(itemStack));
                        if (world.isRemote)
                            player.sendMessage(new TextComponentString(face + " side set to: " + getMode(itemStack)));
                    }
                    return EnumActionResult.SUCCESS;
                }
            }
        }
        return EnumActionResult.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (player.isSneaking()) {
            int temp = getMode(player.getHeldItem(hand)).ordinal() + 1;
            if (temp >= WrenchModes.modes.size()) {
                temp = 0;
            }
            ((ItemWrench) player.getHeldItem(hand).getItem()).setMode(player.getHeldItem(hand), WrenchModes.modes.get(temp));
            if (world.isRemote) {
                player.sendMessage(new TextComponentString("Mode: " + WrenchModes.modes.get(temp)));
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    public void dumpItems(World world, int x, int y, int z, ItemStack items) {
        EntityItem entItem = new EntityItem(world, x + 0.5f, y + 0.5f, z + 0.5f, items);
        float f3 = 0.05F;
        entItem.motionX = (world.rand.nextGaussian() * f3);
        entItem.motionY = (world.rand.nextGaussian() * f3 + 0.2F);
        entItem.motionZ = (world.rand.nextGaussian() * f3);
        world.spawnEntity(entItem);
    }
}