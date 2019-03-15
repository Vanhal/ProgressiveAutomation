package com.vanhal.progressiveautomation.common.util;


import com.mojang.authlib.GameProfile;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

import java.util.UUID;

/**
 * This class was pulled from COFH Core
 * <p>
 * {@link} https://github.com/CoFH/CoFHCore/blob/master/src/main/java/cofh/core/entity/CoFHFakePlayer.java
 *
 * @author COFH
 */

public class PlayerFake extends FakePlayer {

    private static GameProfile NAME = new GameProfile(UUID.fromString("08B9E87C-A9F9-5161-AEC6-B671C8F4FCB9"), "[VANHAL]");
    //private static GameProfile NAME = new GameProfile("08B9E87C-A9F9-5161-AEC6-B671C8F4FCB9", "[VANHAL]");

    public boolean isSneaking = false;
    public ItemStack previousItem = ItemStack.EMPTY;
    public String myName = "[VANHAL]";

    public PlayerFake(WorldServer world, String FakeName) {
        this(world, new GameProfile(UUID.randomUUID(), FakeName));
        myName = FakeName;
    }

    public PlayerFake(WorldServer world, GameProfile FakeName) {
        super(world, FakeName);
        this.addedToChunk = false;
        this.onGround = true;
    }

    public PlayerFake(WorldServer world) {
        this(world, NAME);
    }

	/*
	public static boolean isBlockBreakable(PlayerFake myFakePlayer, World worldObj, int x, int y, int z) {
		Block block = worldObj.getBlock(x, y, z);
		if (myFakePlayer == null) {
			return block.getBlockHardness(worldObj, x, y, z) > -1;
		} else {
			return block.getPlayerRelativeBlockHardness(myFakePlayer, worldObj, x, y, z) > -1;
		}
	}
	*/

    @Override
    public boolean canUseCommand(int var1, String var2) {
        return false;
    }

    @Override
    public boolean isSprinting() {
        return false;
    }

	/*
	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		return null;
	}
	*/

    public void setItemInHand(ItemStack m_item) {
        this.inventory.currentItem = 0;
        this.inventory.setInventorySlotContents(0, m_item);
    }

    public void setItemInHand(int slot) {
        this.inventory.currentItem = slot;
    }

    @Override
    public double getDistanceSq(double x, double y, double z) {
        return 0F;
    }

    @Override
    public double getDistance(double x, double y, double z) {
        return 0F;
    }

    @Override
    public boolean isSneaking() {
        return isSneaking;
    }

    @Override
    public void onUpdate() {
        ItemStack itemstack = previousItem;
        ItemStack itemstack1 = getHeldItemMainhand();
        if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
            if (!itemstack.isEmpty()) {
                getAttributeMap().removeAttributeModifiers(itemstack.getAttributeModifiers(null));
            }
            if (!itemstack1.isEmpty()) {
                getAttributeMap().applyAttributeModifiers(itemstack1.getAttributeModifiers(null));
            }
            myName = "[VANHAL]" + (!itemstack1.isEmpty() ? " using " + itemstack1.getDisplayName() : "");
        }
        previousItem = (itemstack1.isEmpty()) ? ItemStack.EMPTY : itemstack1.copy();
        //theItemInWorldManager.updateBlockRemoving();
    }

	/*
	@Override
	protected void updateItemUse(ItemStack par1ItemStack, int par2) {
		if (par1ItemStack.getItemUseAction() == EnumAction.drink) {
			this.playSound("random.drink", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
		}

		if (par1ItemStack.getItemUseAction() == EnumAction.eat) {
			this.playSound("random.eat", 0.5F + 0.5F * this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
		}
	}

	@Override
	public String getDisplayName() {
		return getCommandSenderName();
	}
	*/

    @Override
    public float getEyeHeight() {
        return 1.1F;
    }

	/*
	@Override
	public ItemStack getCurrentArmor(int par1) {
		return new ItemStack(Items.diamond_chestplate);
	}
	*/

    @Override
    public void sendMessage(ITextComponent chatmessagecomponent) {
    }

    @Override
    public void sendStatusMessage(ITextComponent chatmessagecomponent, boolean actionBar) {
    }

    @Override
    public void addStat(StatBase par1StatBase, int par2) {
    }

    @Override
    public void openGui(Object mod, int modGuiId, World world, int x, int y, int z) {
    }

	/*
	@Override
	public boolean isEntityInvulnerable() {
		return true;
	}
	*/

    @Override
    public void onDeath(DamageSource source) {
        return;
    }

	/*
	@Override
	public void travelToDimension(int dim) {
		return;
	}
	*/

	/*
	@Override
	public void func_147100_a(C15PacketClientSettings pkt) {
		return;
	}
	*/

    @Override
    public void addPotionEffect(PotionEffect effect) {
    }

    public void setItemInUse(ItemStack heldItemMainhand, int i) {
        this.ticksSinceLastSwing = (int) getCooldownPeriod() + 1;
    }
}