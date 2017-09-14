package com.vanhal.progressiveautomation.items;

import java.util.List;

import com.google.common.base.CaseFormat;
import com.vanhal.progressiveautomation.blocks.BaseBlock;
import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.ref.WrenchModes;
import com.vanhal.progressiveautomation.util.IDismantleable;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemWrench extends BaseItem {
	public ItemWrench() {
		super("Wrench");
		this.setMaxStackSize(1);
		
	}
	
	private void setMode(ItemStack itemStack, WrenchModes.Mode mode) {
		this.setDamage(itemStack, mode.ordinal());
	}
	
	private WrenchModes.Mode getMode(ItemStack itemStack) {
		if (WrenchModes.modes.size()>itemStack.getItemDamage()) {
			return WrenchModes.modes.get(itemStack.getItemDamage());
		}
		return WrenchModes.Mode.Rotate;
	}
	
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean bool) {
		list.add(TextFormatting.GRAY + "Current Mode: "+TextFormatting.WHITE+getMode(itemStack));
	}
	
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing face, float hitX, float hitY, float hitZ, EnumHand hand) {
		return EnumActionResult.PASS;
	}
	
	@Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ) {
		ItemStack itemStack = player.getHeldItem(hand);
		
		if (itemStack.isEmpty() || itemStack.getItem() != this) return EnumActionResult.PASS;
		
		Block block = world.getBlockState(pos).getBlock();
		if (player.isSneaking()) {
			if (block instanceof IDismantleable) {
				IDismantleable dBlock = (IDismantleable)block;
				if (dBlock.canDismantle(player, world, pos.getX(), pos.getY(), pos.getZ())) {
					if (!world.isRemote) dBlock.dismantleBlock(player, world, pos.getX(), pos.getY(), pos.getZ(), false);
					return EnumActionResult.SUCCESS;
				}
			}
		} else {
			if (getMode(itemStack)==WrenchModes.Mode.Rotate) {
				block.rotateBlock(world, pos, face);
				return EnumActionResult.SUCCESS;
			} else {
				if (block instanceof BaseBlock) {
					BaseTileEntity PABlock = (BaseTileEntity)world.getTileEntity(pos);
					if (getMode(itemStack)==WrenchModes.Mode.Query) {
						if (world.isRemote) player.sendMessage(new TextComponentString(face+" side currently set to: "+PABlock.getSide(face)));
					} else {
						PABlock.setSide(face, getMode(itemStack));
						if (world.isRemote) player.sendMessage(new TextComponentString(face+" side set to: "+getMode(itemStack)));
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
			if (temp>=WrenchModes.modes.size()) temp = 0;
			((ItemWrench)player.getHeldItem(hand).getItem()).setMode(player.getHeldItem(hand), WrenchModes.modes.get(temp));
			if (world.isRemote) player.sendMessage(new TextComponentString("Mode: "+WrenchModes.modes.get(temp)));
		}
        return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
	}
	
	
	public void dumpItems(World world, int x, int y, int z, ItemStack items) {
		EntityItem entItem = new EntityItem(world, (float)x + 0.5f, (float)y + 0.5f, (float)z + 0.5f, items);
		float f3 = 0.05F;
		entItem.motionX = (double)((float)world.rand.nextGaussian() * f3);
		entItem.motionY = (double)((float)world.rand.nextGaussian() * f3 + 0.2F);
		entItem.motionZ = (double)((float)world.rand.nextGaussian() * f3);
		
		world.spawnEntity(entItem);
	}
	
	@Override
	protected void addNormalRecipe() {
		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
			"s s", " i ", " s ", 'i', Items.IRON_INGOT, 's', Items.STICK});
		GameRegistry.addRecipe(recipe);
	}
	
	@Override
	protected void addUpgradeRecipe() {
		addNormalRecipe();
	}
	
	@Override
	public void init() {
		//1.11 ResourceLocation changed! This should fix it ~~Psycho Ray
		String item_name = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, itemName);
		
		for (int i = 0; i < WrenchModes.Mode.values().length; i++) {
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
				.register(this, i, new ModelResourceLocation(Ref.MODID + ":" + item_name, "inventory"));
		}
	}
}
