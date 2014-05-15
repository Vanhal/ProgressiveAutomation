package com.vanhal.progressiveautomation.blocks;

import com.vanhal.progressiveautomation.Ref;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BaseBlock extends BlockContainer {
	public String name;
	
	
	public BaseBlock(String blockName) {
		super(Material.iron);
		name = blockName;
		setBlockName(name);
		setHardness(1.0f);
		setBlockTextureName(Ref.MODID+":"+name);
	}

	public TileEntity createNewTileEntity(World world, int var2) {
		return null;
	}
	
	public void preInit() {
		GameRegistry.registerBlock(this, name);
	}
	
	public void init() {
		
	}
	
	public void postInit() {
		
	}
}
