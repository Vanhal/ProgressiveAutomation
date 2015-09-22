package com.vanhal.progressiveautomation.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cofh.api.block.IDismantleable;

import com.vanhal.progressiveautomation.ProgressiveAutomation;
import com.vanhal.progressiveautomation.entities.BaseTileEntity;
import com.vanhal.progressiveautomation.entities.IUpgradeable;
import com.vanhal.progressiveautomation.entities.UpgradeableTileEntity;
import com.vanhal.progressiveautomation.entities.miner.TileMiner;
import com.vanhal.progressiveautomation.items.ItemBlockMachine;
import com.vanhal.progressiveautomation.items.PAItems;
import com.vanhal.progressiveautomation.ref.Ref;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeRegistry;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.ref.WrenchModes;

import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BaseBlock extends BlockContainer implements IDismantleable {
	public String name;
	public String machineType;
	public int GUIid = -1;
	
	protected int blockLevel = ToolHelper.LEVEL_WOOD;
	
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
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
		if (blockLevel==ToolHelper.LEVEL_WOOD) {
			return 5;
		} else {
			return 0;
		}
    }
	
	public BaseBlock(String machineType, int level) {
		super(Material.iron);
		
		this.machineType = machineType;
		name = machineType+returnLevelName(level);
		setUnlocalizedName(name);
		
		
		setHardness(1.0f);
		setCreativeTab(ProgressiveAutomation.PATab);
		blockLevel = level;
		
		GUIid = ProgressiveAutomation.proxy.registerGui(machineType);
	}
	
	public String getLevelName() {
		String thisName = returnLevelName(blockLevel);
		if (thisName=="") {
			thisName = "Wooden";
		}
		return thisName;
	}
	
	@Override
	public int getRenderType() {
        return 3;
    }


	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			if (GUIid>=0) {
				FMLNetworkHandler.openGui(player, ProgressiveAutomation.instance, GUIid, world, pos.getX(), pos.getY(), pos.getZ());
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
		BaseTileEntity tileEntity = (BaseTileEntity)world.getTileEntity(pos);

        if (tileEntity != null) {
            ArrayList<ItemStack> items = getInsides(world, pos);
            
            for (ItemStack item: items) {
            	dumpItems(world, pos, item);
            }
            

            //world.func_147453_f(pos, state.getBlock()); //I have no idea what this method did....
        }
        super.breakBlock(world, pos, state);
    }
	
	public void dumpItems(World world, BlockPos pos, ItemStack items) {
		EntityItem entItem = new EntityItem(world, (float)pos.getX() + 0.5f, (float)pos.getY() + 0.5f, (float)pos.getZ() + 0.5f, items);
		float f3 = 0.05F;
		entItem.motionX = (double)((float)world.rand.nextGaussian() * f3);
		entItem.motionY = (double)((float)world.rand.nextGaussian() * f3 + 0.2F);
		entItem.motionZ = (double)((float)world.rand.nextGaussian() * f3);
		
		world.spawnEntityInWorld(entItem);
	}
	
	public void addRecipe(Block previousTier) {
		
	}
	
	public void preInit(Block previousTier) {
		GameRegistry.registerBlock(this, ItemBlockMachine.class, name);
		addRecipe(previousTier);
	}
	
	public void init() {
		
	}
	
	public void postInit() {
		if (ProgressiveAutomation.proxy.isClient()) {
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
				.register(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Ref.MODID + ":" + name, "inventory"));
		}
		if(net.minecraftforge.fml.common.Loader.isModLoaded("poweradvantage")){
			cyano.poweradvantage.api.modsupport.LightWeightPowerRegistry.registerLightWeightPowerAcceptor(this, 
					new cyano.poweradvantage.api.modsupport.ILightWeightPowerAcceptor(){

				public boolean canAcceptEnergyType(cyano.poweradvantage.api.ConduitType powerType) {
					return cyano.poweradvantage.api.ConduitType.areSameType(powerType, "electricity");
				}

				public float getEnergyDemand(TileEntity yourMachine,
						cyano.poweradvantage.api.ConduitType powerType) {
					if(yourMachine instanceof cofh.api.energy.IEnergyReceiver) {
						cofh.api.energy.IEnergyReceiver m = (cofh.api.energy.IEnergyReceiver)yourMachine;
						for(EnumFacing dir : EnumFacing.values()){
							if(m.canConnectEnergy(dir)){
								return m.getMaxEnergyStored(dir) - m.getEnergyStored(dir);
							}
						}
					}
					return 0;
				}

				public float addEnergy(TileEntity yourMachine,
						float amountAdded, cyano.poweradvantage.api.ConduitType powerType) {
					if(yourMachine instanceof cofh.api.energy.IEnergyReceiver) {
						cofh.api.energy.IEnergyReceiver m = (cofh.api.energy.IEnergyReceiver)yourMachine;
						for(EnumFacing dir : EnumFacing.values()){
							if(m.canConnectEnergy(dir)){
								return m.receiveEnergy(dir, (int)amountAdded, false);
							}
						}
					}
					return 0;
				}

			});
		}
	}
	
	protected ArrayList<ItemStack> getInsides(World world, BlockPos pos) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		
		BaseTileEntity tileEntity = (BaseTileEntity)world.getTileEntity(pos);
		if (tileEntity != null) {
        	//get the inventory
            for (int i = 0; i < tileEntity.getSizeInventory(); ++i) {
                ItemStack itemstack = tileEntity.getStackInSlot(i);
                if (itemstack != null) {
                	items.add(itemstack);
                }
            }

			if (tileEntity instanceof UpgradeableTileEntity) {
				UpgradeableTileEntity tileMachine = (UpgradeableTileEntity)tileEntity;
				for (UpgradeType upgradeType : tileMachine.getInstalledUpgradeTypes()) {
					int amount = tileMachine.getUpgradeAmount(upgradeType);
					while (amount > 0) {
						ItemStack upgradeItemStack = new ItemStack(UpgradeRegistry.getUpgradeItem(upgradeType));
						int stackSize = amount > 64 ? 64 : amount;
						upgradeItemStack.stackSize = stackSize;
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
	public ArrayList<ItemStack> dismantleBlock(EntityPlayer player,
			World world, int x, int y, int z, boolean returnDrops) {
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

		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		items.add(block);
		return items;
	}

	@Override
	public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z) {
		return true;
	}
	
	@Override
    public boolean rotateBlock(World worldObj, BlockPos pos, EnumFacing axis){
		BaseTileEntity tileEntity = (BaseTileEntity)worldObj.getTileEntity(pos);
		if (tileEntity.facing == EnumFacing.NORTH) tileEntity.facing = EnumFacing.EAST;
		else if (tileEntity.facing == EnumFacing.EAST) tileEntity.facing = EnumFacing.SOUTH;
		else if (tileEntity.facing == EnumFacing.SOUTH) tileEntity.facing = EnumFacing.WEST;
		else if (tileEntity.facing == EnumFacing.WEST) tileEntity.facing = EnumFacing.NORTH;
		//ProgressiveAutomation.logger.info(chopper.facing.toString());
        return true;
    }
	
}
