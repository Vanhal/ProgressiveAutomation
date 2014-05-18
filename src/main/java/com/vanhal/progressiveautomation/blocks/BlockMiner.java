package com.vanhal.progressiveautomation.blocks;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.TileMiner;
import com.vanhal.progressiveautomation.ref.ToolInfo;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMiner extends BaseBlock {

	public BlockMiner() {
		this("Miner");
	}
	
	public BlockMiner(String name) {
		super(name);
		GUIid = ProgressiveAutomation.guiHandler.MinerGUI;
	}
	
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileMiner();
	}
	
	public void breakBlock(World world, int x, int y, int z, Block p_149749_5_, int p_149749_6_) {
		//check if it's a miner, if it is then we need to dump out the upgrades as well!
	    if (world.getTileEntity(x, y, z) instanceof TileMiner) {
	    	TileMiner tileMiner = (TileMiner)world.getTileEntity(x, y, z);
	    	int numUpgrades = tileMiner.getUpgrades();
	    	while (numUpgrades>0) {
	    		ItemStack upgrades = ToolInfo.getUpgradeType(tileMiner.getMiningLevel());
	    		if (numUpgrades<=64) {
	    			upgrades.stackSize = numUpgrades;
	    			numUpgrades = 0;
	    		} else {
	    			upgrades.stackSize = 64;
	    			numUpgrades -= 64;
	    		}
	    		EntityItem entItem = new EntityItem(world, x + 0.5f, y + 0.5f, z + 0.5f, upgrades);
				float f3 = 0.05F;
				entItem.motionX = (double)((float)world.rand.nextGaussian() * f3);
				entItem.motionY = (double)((float)world.rand.nextGaussian() * f3 + 0.2F);
				entItem.motionZ = (double)((float)world.rand.nextGaussian() * f3);
				world.spawnEntityInWorld(entItem);
	    	}
	    }
	    super.breakBlock(world, x, y, z, p_149749_5_, p_149749_6_);
	}
}
