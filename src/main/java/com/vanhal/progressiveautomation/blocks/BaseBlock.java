package com.vanhal.progressiveautomation.blocks;

import java.util.Random;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.entities.TileMiner;
import com.vanhal.progressiveautomation.ref.Ref;

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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BaseBlock extends BlockContainer {
	public String name;
	public int GUIid = -1;
	protected IIcon[] blockIcons = new IIcon[6];
	
	
	
	public BaseBlock(String blockName) {
		super(Material.iron);
		name = blockName;
		setBlockName(name);
		setHardness(1.0f);
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
	
	public void breakBlock(World world, int x, int y, int z, Block p_149749_5_, int p_149749_6_) {
		BaseTileEntity tileEntity = (BaseTileEntity)world.getTileEntity(x, y, z);

        if (tileEntity != null) {
            for (int i = 0; i < tileEntity.getSizeInventory(); ++i) {
                ItemStack itemstack = tileEntity.getStackInSlot(i);

                if (itemstack != null) {
                    float f = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem;

                    for (float f2 = world.rand.nextFloat() * 0.8F + 0.1F; itemstack.stackSize > 0; world.spawnEntityInWorld(entityitem)) {
                        int j1 = world.rand.nextInt(21) + 10;

                        if (j1 > itemstack.stackSize) {
                            j1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= j1;
                        entityitem = new EntityItem(world, (double)((float)x + f), (double)((float)y + f1), (double)((float)z + f2), new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));
                        float f3 = 0.05F;
                        entityitem.motionX = (double)((float)world.rand.nextGaussian() * f3);
                        entityitem.motionY = (double)((float)world.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double)((float)world.rand.nextGaussian() * f3);

                        if (itemstack.hasTagCompound()) {
                            entityitem.getEntityItem().setTagCompound((NBTTagCompound)itemstack.getTagCompound().copy());
                        }
                    }
                }
            }

            world.func_147453_f(x, y, z, p_149749_5_);
        }
        super.breakBlock(world, x, y, z, p_149749_5_, p_149749_6_);
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
