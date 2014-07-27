package com.vanhal.progressiveautomation.blocks;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.planter.TilePlanter;
import com.vanhal.progressiveautomation.entities.planter.TilePlanterDiamond;
import com.vanhal.progressiveautomation.entities.planter.TilePlanterIron;
import com.vanhal.progressiveautomation.entities.planter.TilePlanterStone;
import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockPlanter extends BaseBlock {
	public BlockPlanter(int level) {
		super("Planter", level);
		GUIid = ProgressiveAutomation.guiHandler.PlanterGUI;
	}

	public TileEntity createNewTileEntity(World world, int var2) {
		if (blockLevel >= ToolHelper.LEVEL_DIAMOND) return new TilePlanterDiamond();
		else if (blockLevel == ToolHelper.LEVEL_IRON) return new TilePlanterIron();
		else if (blockLevel == ToolHelper.LEVEL_STONE) return new TilePlanterStone();
		else return new TilePlanter();
	}
	
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
		blockIcons[0] = register.registerIcon(Ref.MODID+":Planter/"+getLevelName()+"_Bottom");
		blockIcons[1] = register.registerIcon(Ref.MODID+":Planter/"+getLevelName()+"_Top");
		blockIcons[2]= register.registerIcon(Ref.MODID+":Planter/"+getLevelName()+"_Side");
		blockIcons[3]= register.registerIcon(Ref.MODID+":Planter/"+getLevelName()+"_Side");
		blockIcons[4]= register.registerIcon(Ref.MODID+":Planter/"+getLevelName()+"_Side");
		blockIcons[5]= register.registerIcon(Ref.MODID+":Planter/"+getLevelName()+"_Side");
    }
	
	public void addRecipe() {
		ShapedOreRecipe recipe = null;
		
		if (blockLevel == ToolHelper.LEVEL_STONE) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sss", "scs", "sps", 's', Blocks.stone, 'c', PABlocks.planter[ToolHelper.LEVEL_WOOD], 'p', Items.stone_hoe});
		} else if (blockLevel == ToolHelper.LEVEL_IRON) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sbs", "scs", "sps", 's', Items.iron_ingot, 'c', PABlocks.planter[ToolHelper.LEVEL_STONE], 'p', Items.iron_hoe, 'b', Blocks.iron_block});
		} else if (blockLevel == ToolHelper.LEVEL_DIAMOND) {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"sss", "scs", "sps", 's', Items.diamond, 'c', PABlocks.planter[ToolHelper.LEVEL_IRON], 'p', Items.diamond_hoe});
		} else {
			recipe = new ShapedOreRecipe(new ItemStack(this), new Object[]{
				"scs", "srs", "sps", 's', Blocks.log, 'r', Blocks.furnace, 'c', Blocks.chest, 'p', Items.wooden_hoe});
		}
		
		
		GameRegistry.addRecipe(recipe);
	}
}
