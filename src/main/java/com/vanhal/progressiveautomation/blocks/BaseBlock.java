package com.vanhal.progressiveautomation.blocks;

import java.util.Random;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.entities.IUpgradeable;
import com.vanhal.progressiveautomation.entities.miner.TileMiner;
import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.ref.ToolHelper;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BaseBlock extends BlockContainer {
	public String name;
	public int GUIid = -1;
	
	protected int blockLevel = ToolHelper.LEVEL_WOOD;
	protected IIcon[] blockIcons = new IIcon[6];
	
	public static String returnLevelName(int level) {
		if (level==ToolHelper.LEVEL_STONE) {
			return "Stone";
		} else if (level==ToolHelper.LEVEL_IRON) {
			return "Iron";
		} else if (level==ToolHelper.LEVEL_DIAMOND) {
			return "Diamond";
		}
		return "";
	}
	
	public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
		if (blockLevel==ToolHelper.LEVEL_WOOD) {
			return 5;
		} else {
			return 0;
		}
    }
	
	public BaseBlock(String baseName, int level) {
		this(baseName+returnLevelName(level));
		blockLevel = level;
	}
	
	public String getLevelName() {
		String thisName = returnLevelName(blockLevel);
		if (thisName=="") {
			thisName = "Wooden";
		}
		return thisName;
	}
	
	public BaseBlock(String blockName) {
		super(Material.iron);
		name = blockName;
		setBlockName(name);
		setHardness(1.0f);
		setCreativeTab(ProgressiveAutomation.PATab);
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			if (GUIid>=0) {
				FMLNetworkHandler.openGui(player, ProgressiveAutomation.instance, GUIid, world, x, y, z);
			}
		}
		return true;
	}

	public TileEntity createNewTileEntity(World world, int var2) {
		return null;
	}
	
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
		blockIcons[0] = register.registerIcon(Ref.MODID+":"+name+"_Bottom");
		blockIcons[1] = register.registerIcon(Ref.MODID+":"+name+"_Top");
		blockIcons[2]= register.registerIcon(Ref.MODID+":"+name+"_Side");
		blockIcons[3]= register.registerIcon(Ref.MODID+":"+name+"_Side");
		blockIcons[4]= register.registerIcon(Ref.MODID+":"+name+"_Side");
		blockIcons[5]= register.registerIcon(Ref.MODID+":"+name+"_Side");
    }
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
        return blockIcons[side];
    }
	
	public void breakBlock(World world, int x, int y, int z, Block block, int p_149749_6_) {
		BaseTileEntity tileEntity = (BaseTileEntity)world.getTileEntity(x, y, z);

        if (tileEntity != null) {
        	//dump out the inventory
            for (int i = 0; i < tileEntity.getSizeInventory(); ++i) {
                ItemStack itemstack = tileEntity.getStackInSlot(i);

                if (itemstack != null) {
                	dumpItems(world, x, y, z, itemstack);
                }
            }
            
            //if entity is IUpgradeable, drop the upgrades
            if (world.getTileEntity(x, y, z) instanceof IUpgradeable) {
            	IUpgradeable tileMachine = (IUpgradeable)world.getTileEntity(x, y, z);
    	    	int numUpgrades = tileMachine.getUpgrades();
    	    	while (numUpgrades>0) {
    	    		ItemStack upgrades = ToolHelper.getUpgradeType(tileMachine.getUpgradeLevel());
    	    		if (numUpgrades<=64) {
    	    			upgrades.stackSize = numUpgrades;
    	    			numUpgrades = 0;
    	    		} else {
    	    			upgrades.stackSize = 64;
    	    			numUpgrades -= 64;
    	    		}
    	    		dumpItems(world, x, y, z, upgrades);
    	    	}
    	    }

            world.func_147453_f(x, y, z, block);
        }
        super.breakBlock(world, x, y, z, block, p_149749_6_);
    }
	
	public void dumpItems(World world, int x, int y, int z, ItemStack items) {
		EntityItem entItem = new EntityItem(world, (float)x + 0.5f, (float)y + 0.5f, (float)z + 0.5f, items);
		float f3 = 0.05F;
		entItem.motionX = (double)((float)world.rand.nextGaussian() * f3);
		entItem.motionY = (double)((float)world.rand.nextGaussian() * f3 + 0.2F);
		entItem.motionZ = (double)((float)world.rand.nextGaussian() * f3);
		
		if (items.hasTagCompound()) {
			entItem.getEntityItem().setTagCompound((NBTTagCompound)items.getTagCompound().copy());
        }
		
		world.spawnEntityInWorld(entItem);
	}
	
	public void addRecipe() {
		
	}
	
	public void preInit() {
		GameRegistry.registerBlock(this, name);
		addRecipe();
	}
	
	public void init() {
		
	}
	
	public void postInit() {
		
	}
}
