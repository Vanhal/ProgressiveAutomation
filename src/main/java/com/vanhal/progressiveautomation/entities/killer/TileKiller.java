package com.vanhal.progressiveautomation.entities.killer;

import java.util.List;

import com.google.common.collect.Multimap;
import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.entities.UpgradeableTileEntity;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.util.PlayerFake;
import com.vanhal.progressiveautomation.util.Point2I;
import com.vanhal.progressiveautomation.util.Point3I;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.WorldServer;

public class TileKiller extends UpgradeableTileEntity {
	protected int searchBlock = -1;
	
	//time between attacks
	public int waitTime = 40;
	public int currentTime = 0;
	
	protected PlayerFake faker;
	protected AxisAlignedBB boundCheck = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

	public TileKiller() {
		super(11);
		setUpgradeLevel(ToolHelper.LEVEL_WOOD);
		setAllowedUpgrades(UpgradeType.WOODEN, UpgradeType.WITHER, UpgradeType.FILTER_ADULT, UpgradeType.FILTER_ANIMAL, UpgradeType.FILTER_MOB, UpgradeType.FILTER_PLAYER);

		//auto output direction
		setExtDirection(EnumFacing.DOWN);
		
		//slots
		SLOT_SWORD = 1;
		SLOT_UPGRADE = 2;
	}
	
	public void setAttackTime(int time) {
		this.waitTime = time;
	}
	
	@Override
	public void writeCommonNBT(NBTTagCompound nbt) {
		super.writeCommonNBT(nbt);
		//save the current attack time
		nbt.setInteger("currentTime", currentTime);
		nbt.setInteger("currentBlock", searchBlock);
	}

	@Override
	public void readCommonNBT(NBTTagCompound nbt) {
		super.readCommonNBT(nbt);
		//load the current attack time
		if (nbt.hasKey("currentTime")) currentTime = nbt.getInteger("currentTime");
		if (nbt.hasKey("currentBlock")) searchBlock = nbt.getInteger("currentBlock");
	}
	
	@Override
	public void update() {
		super.update();
		if (!worldObj.isRemote) {
			doXpPickup();
			checkInventory();
			
			// Pause if we're full and told to
			if (isFull()) return;

			if (isBurning()) {
				if (currentTime>0) {
					//count down the time between attacks
					currentTime--;
				} else {
					if (searchBlock > -1) {
						EntityLivingBase mob = getMob(searchBlock);
						if (mob==null) {
							searchBlock = -1;
							addPartialUpdate("currentBlock", searchBlock);
						} else {
							if (slots[SLOT_SWORD]!=null) {
								//attack the "mob"
								if (faker == null) {
									faker = new PlayerFake((WorldServer)worldObj, worldObj.getBlockState(pos).getBlock().getLocalizedName());
									faker.setPosition(0, 0, 0);
								}
								
								faker.setItemInHand(slots[SLOT_SWORD].copy());
								Multimap<String, AttributeModifier> attributeModifiers = slots[SLOT_SWORD].getAttributeModifiers(EntityEquipmentSlot.MAINHAND);

								attributeModifiers.get(SharedMonsterAttributes.ATTACK_SPEED.getAttributeUnlocalizedName());
								faker.setItemInUse(faker.getHeldItemMainhand(), 72000);
								faker.onUpdate();
								faker.attackTargetEntityWithCurrentItem(mob);
								

								if (ToolHelper.damageTool(slots[SLOT_SWORD], worldObj, pos.getX(), pos.getY(), pos.getZ())) {
									destroyTool(SLOT_SWORD);
								}
								
								pickupDrops(searchBlock);
								currentTime = waitTime;
								addPartialUpdate("currentTime", currentTime);
							} else {
								searchBlock = -1;
								addPartialUpdate("currentBlock", searchBlock);
							}
						}
					} else {
						doSearch();
					}
				}
			}
		}
	}
	
	public void doXpPickup() {
		for (int i = 0; i < this.getRange(); i++) {
			pickupXP(i);
		}
	}
	
	public boolean doSearch() {
		if (searchBlock>=0) return true;
		//search for any living entities within range
		for (int i = 0; i < this.getRange(); i++) {
			if (getMob(i)!=null) {
				searchBlock = i;
				addPartialUpdate("currentBlock", searchBlock);
				return true;
			}
		}
		return false;
	}
	
	public EntityLivingBase getMob(int n) {
		Point3I point = getPoint(n);
		boundCheck = new AxisAlignedBB(point.getX(), point.getY()-1, point.getZ(), 
				point.getX()+1, point.getY()+2, point.getZ()+1);
		List<EntityLivingBase> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, boundCheck);
		if (!entities.isEmpty()) {
			for (EntityLivingBase mob: entities) {
				if (mob instanceof EntityPlayer) {
					if (PAConfig.allowKillPlayer) {
						if (hasFilterInstalled()) {
							if (hasUpgrade(UpgradeType.FILTER_PLAYER)) return mob;
						} else {
							return mob;
						}
					}
				} else if (hasFilterInstalled()) {
					if (hasUpgrade(UpgradeType.FILTER_ANIMAL)) {
						if (mob instanceof IAnimals) {
							if (hasUpgrade(UpgradeType.FILTER_ADULT)) {
								if (!mob.isChild()) {
									return mob;
								}
							} else {
								return mob;
							}
						}
					}
					if (hasUpgrade(UpgradeType.FILTER_MOB)) {
						if (mob instanceof IMob) {
							if (hasUpgrade(UpgradeType.FILTER_ADULT)) {
								if (!mob.isChild()) {
									return mob;
								}
							} else {
								return mob;
							}
						}
					}
					if ( (!hasUpgrade(UpgradeType.FILTER_MOB)) && (!hasUpgrade(UpgradeType.FILTER_ANIMAL)) ) {
						if (hasUpgrade(UpgradeType.FILTER_ADULT)) {
							if (!mob.isChild()) {
								return mob;
							}
						}
					}
				} else {
					return mob;
				}
			}
		}
		
		return null;
	}
	
	public void pickupDrops(int n) {
		Point3I point = getPoint(n);
		boundCheck = new AxisAlignedBB(point.getX(), point.getY(), point.getZ(), 
				point.getX()+1, point.getY()+2, point.getZ()+1);
		//pick up the drops
		List<EntityItem> entities = worldObj.getEntitiesWithinAABB(EntityItem.class, boundCheck);
		if (!entities.isEmpty()) {
			for (EntityItem item: entities) {
				if (roomInInventory(item.getEntityItem())) {
					if (!worldObj.isRemote) addToInventory(item.getEntityItem());
					worldObj.removeEntity(item);
				}
			}
		}
		
	}
	
	public void pickupXP(int n) {
		Point3I point = getPoint(n);
		boundCheck = new AxisAlignedBB(point.getX(), point.getY(), point.getZ(), 
				point.getX()+1, point.getY()+2, point.getZ()+1);
		//pick up the drops
		List<EntityXPOrb> entities = worldObj.getEntitiesWithinAABB(EntityXPOrb.class, boundCheck);
		if (!entities.isEmpty()) {
			for (EntityXPOrb item: entities) {
				worldObj.removeEntity(item);
			}
		}
		
	}
	
	//check filters
	public boolean hasFilterInstalled() {
		if (hasUpgrade(UpgradeType.FILTER_ADULT)) {
			return true;
		} else if (hasUpgrade(UpgradeType.FILTER_ANIMAL)) {
			return true;
		} else if (hasUpgrade(UpgradeType.FILTER_MOB)) {
			return true;
		} else if (hasUpgrade(UpgradeType.FILTER_PLAYER)) {
			return true;
		}
		return false;
	}
	
	protected Point3I getPoint(int n) {
		Point2I p1 = spiral(n+1, pos.getX(), pos.getZ());
		return new Point3I(p1.getX(), pos.getY() + 1, p1.getY());
	}

	
	@Override
	public boolean readyToBurn() {
		if (slots[SLOT_SWORD]!=null) {
			if (doSearch()) {
				return true;
			}
		}
		return false;
	}

	public boolean isKilling() {
		return (searchBlock>-1);
	}
	
	@Override
	protected Point3I adjustedSpiral(int n) {
		Point3I point = super.adjustedSpiral(n);
		point.setY(point.getY()+1);
		return point;
	}
}
