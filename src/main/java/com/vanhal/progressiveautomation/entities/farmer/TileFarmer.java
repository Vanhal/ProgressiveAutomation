package com.vanhal.progressiveautomation.entities.farmer;

import com.vanhal.progressiveautomation.entities.UpgradeableTileEntity;
import com.vanhal.progressiveautomation.ref.ToolHelper;
import com.vanhal.progressiveautomation.upgrades.UpgradeType;
import com.vanhal.progressiveautomation.util.PlayerFake;
import com.vanhal.progressiveautomation.util.Point2I;
import com.vanhal.progressiveautomation.util.Point3I;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IShearable;

import java.util.List;

public class TileFarmer extends UpgradeableTileEntity {

    public int SLOT_FOOD;
    public int SLOT_SHEARS;
    public int SLOT_BUCKETS;
    //time between actions
    public int waitTime = 80;
    public int currentTime = 0;
    protected int searchBlock = -1;
    protected PlayerFake faker;
    protected int currentAction = 0;
    protected AxisAlignedBB boundCheck = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

    public TileFarmer() {
        super(13);
        setUpgradeLevel(ToolHelper.LEVEL_WOOD);
        setAllowedUpgrades(UpgradeType.WOODEN, UpgradeType.WITHER, UpgradeType.MILKER, UpgradeType.SHEARING);
        //slots
        SLOT_UPGRADE = 1;
        SLOT_FOOD = 2;
        SLOT_SHEARS = 3;
        SLOT_BUCKETS = 4;
    }

    public void setWaitTime(int time) {
        this.waitTime = time;
    }

    public int getCurrentAction() {
        return currentAction;
    }

    @Override
    protected void writeSyncOnlyNBT(NBTTagCompound nbt) {
        super.writeSyncOnlyNBT(nbt);
        nbt.setInteger("currentAction", currentAction);
    }

    @Override
    public void readSyncOnlyNBT(NBTTagCompound nbt) {
        super.readSyncOnlyNBT(nbt);
        if (nbt.hasKey("currentAction")) {
            currentAction = nbt.getInteger("currentAction");
        }
    }

    @Override
    public void writeCommonNBT(NBTTagCompound nbt) {
        super.writeCommonNBT(nbt);
        //save the current action time
        nbt.setInteger("currentTime", currentTime);
        nbt.setInteger("currentBlock", searchBlock);
    }

    @Override
    public void readCommonNBT(NBTTagCompound nbt) {
        super.readCommonNBT(nbt);
        //load the current action time
        if (nbt.hasKey("currentTime")) {
            currentTime = nbt.getInteger("currentTime");
        }

        if (nbt.hasKey("currentBlock")) {
            searchBlock = nbt.getInteger("currentBlock");
        }
    }

    public boolean doSearch() {
        if (searchBlock >= 0) {
            return true;
        }

        //search for any animals that can be fed
        if (!slots[SLOT_FOOD].isEmpty()) {
            for (int i = 0; i < this.getRange(); i++) {
                if (findAnimalToFeed(i) != null) {
                    searchBlock = i;
                    addPartialUpdate("currentBlock", searchBlock);
                    currentAction = 1;
                    addPartialUpdate("currentAction", currentAction);
                    return true;
                }
            }
        }

        //if we as able to shear then search for animal to shear
        if (hasUpgrade(UpgradeType.SHEARING)) {
            if (!slots[SLOT_SHEARS].isEmpty()) {
                for (int i = 0; i < this.getRange(); i++) {
                    if (findAnimalToShear(i) != null) {
                        searchBlock = i;
                        addPartialUpdate("currentBlock", searchBlock);
                        currentAction = 2;
                        addPartialUpdate("currentAction", currentAction);
                        return true;
                    }
                }
            }
        }

        //if we are able to milk, and have empty containers then see if there is an animal to milk
        if (hasUpgrade(UpgradeType.MILKER)) {
            if ((!slots[SLOT_BUCKETS].isEmpty()) && (slots[SLOT_BUCKETS].getCount() > 0)) {
                for (int i = 0; i < this.getRange(); i++) {
                    if (findAnimalToMilk(i) != null) {
                        searchBlock = i;
                        addPartialUpdate("currentBlock", searchBlock);
                        currentAction = 3;
                        addPartialUpdate("currentAction", currentAction);
                        return true;
                    }
                }
            }
        }

        currentAction = 0;
        addPartialUpdate("currentAction", currentAction);
        return false;
    }

    protected EntityAnimal findAnimalToFeed(int n) {
        Point3I point = getPoint(n);
        boundCheck = new AxisAlignedBB(point.getX(), point.getY() - 1, point.getZ(),
                point.getX() + 1, point.getY() + 2, point.getZ() + 1);
        List<EntityAnimal> entities = world.getEntitiesWithinAABB(EntityAnimal.class, boundCheck);
        if (!entities.isEmpty()) {
            for (EntityAnimal animal : entities) {
                if ((!slots[SLOT_FOOD].isEmpty()) && (animal.isBreedingItem(slots[SLOT_FOOD]))) {
                    if (animal.getGrowingAge() == 0 && !animal.isInLove()) {
                        return animal;
                    }
                }
            }
        }
        return null;
    }

    protected EntityAnimal findAnimalToShear(int n) {
        if (!hasUpgrade(UpgradeType.SHEARING)) {
            return null;
        }

        Point3I point = getPoint(n);
        boundCheck = new AxisAlignedBB(point.getX(), point.getY() - 1, point.getZ(),
                point.getX() + 1, point.getY() + 2, point.getZ() + 1);
        List<EntityAnimal> entities = world.getEntitiesWithinAABB(EntityAnimal.class, boundCheck);
        if (!entities.isEmpty()) {
            for (EntityAnimal animal : entities) {
                if (!slots[SLOT_SHEARS].isEmpty()) {
                    if (animal instanceof IShearable) {
                        if (((IShearable) animal).isShearable(slots[SLOT_SHEARS], world, point.toPosition())) {
                            return animal;
                        }
                    }
                }
            }
        }
        return null;
    }

    protected EntityAnimal findAnimalToMilk(int n) {
        if (!hasUpgrade(UpgradeType.MILKER)) {
            return null;
        }

        Point3I point = getPoint(n);
        boundCheck = new AxisAlignedBB(point.getX(), point.getY() - 1, point.getZ(),
                point.getX() + 1, point.getY() + 2, point.getZ() + 1);
        List<EntityAnimal> entities = world.getEntitiesWithinAABB(EntityAnimal.class, boundCheck);
        if (!entities.isEmpty()) {
            for (EntityAnimal animal : entities) {
                if ((!slots[SLOT_BUCKETS].isEmpty()) && (slots[SLOT_BUCKETS].getCount() > 0)) {
                    if (animal instanceof EntityCow) {
                        NBTTagCompound cowTag = new NBTTagCompound();
                        animal.writeEntityToNBT(cowTag);
                        if (cowTag.hasKey("CurrentUseCooldown")) {
                            int coolDown = cowTag.getInteger("CurrentUseCooldown");
                            if (coolDown <= 0) {
                                return animal;
                            }
                        } else {
                            if (cowTag.hasKey("NextUseCooldown")) {
                                int coolDown = cowTag.getInteger("NextUseCooldown");
                                if (coolDown <= 0) {
                                    return animal;
                                }
                            } else {
                                initFaker();
                                faker.setItemInHand(slots[SLOT_BUCKETS].copy());
                                if (animal.processInteract(faker, EnumHand.MAIN_HAND)) {
                                    return animal;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public void pickup(int n) {
        Point3I point = getPoint(n);
        boundCheck = new AxisAlignedBB(point.getX(), point.getY(), point.getZ(),
                point.getX() + 1, point.getY() + 2, point.getZ() + 1);
        //pick up the drops
        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, boundCheck);
        if (!entities.isEmpty()) {
            for (Entity item : entities) {
                if (item instanceof EntityXPOrb) {
                    world.removeEntity(item);
                } else {
                    if (item instanceof EntityItem) {
                        if (((EntityItem) item).getItem().getItem() == Items.EGG) {
                            if (roomInInventory(((EntityItem) item).getItem())) {
                                if (!world.isRemote) {
                                    addToInventory(((EntityItem) item).getItem());
                                }
                                world.removeEntity(item);
                            }
                        }
                    }
                }
            }
        }
    }

    public void doPickup() {
        for (int i = 0; i < this.getRange(); i++) {
            pickup(i);
        }
    }

    protected Point3I getPoint(int n) {
        Point2I p1 = spiral(n + 2, pos.getX(), pos.getZ());
        return new Point3I(p1.getX(), pos.getY(), p1.getY());
    }

    @Override
    public boolean readyToBurn() {
        if (doSearch()) {
            return true;
        }
        return false;
    }

    protected void initFaker() {
        if (faker == null) {
            faker = new PlayerFake((WorldServer) world, world.getBlockState(pos).getBlock().getLocalizedName());
            faker.setPosition(0, 0, 0);
        }
        faker.inventory.clear();
    }

    @Override
    public void update() {
        super.update();
        if (!world.isRemote) {
            doPickup();
            checkInventory();
            // Pause if we're full and told to
            if (isFull()) {
                return;
            }

            if (isBurning()) {
                if (currentTime > 0) {
                    currentTime--;
                } else {
                    if (searchBlock > -1) {
                        initFaker();
                        EntityAnimal animal = findAnimalToFeed(searchBlock);
                        if (animal != null) {
                            feedAnimal(animal);
                        } else {
                            animal = findAnimalToShear(searchBlock);
                            if (animal != null) {
                                shearAnimal(animal);
                            }
                            else {
                                animal = findAnimalToMilk(searchBlock);
                                if (animal != null){
                                    milkAnimal(animal);
                                } else {
                                    searchBlock = -1;
                                    addPartialUpdate("currentBlock", searchBlock);
                                }
                            }
                        }
                    } else {
                        doSearch();
                    }
                }
            }
        }
    }

    protected void feedAnimal(EntityAnimal animal) {
        if (!slots[SLOT_FOOD].isEmpty()) {
            animal.setInLove(faker);
            slots[SLOT_FOOD].shrink(1);
            if (slots[SLOT_FOOD].getCount() == 0) {
                slots[SLOT_FOOD] = ItemStack.EMPTY;
            }

            currentTime = waitTime;
            addPartialUpdate("currentTime", currentTime);
        }
    }

    protected void shearAnimal(EntityAnimal animal) {
        if ((!slots[SLOT_SHEARS].isEmpty()) && (hasUpgrade(UpgradeType.SHEARING))) {
            if (animal instanceof IShearable) {
                int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByLocation("fortune"), slots[SLOT_SHEARS]);
                List<ItemStack> items = ((IShearable) animal).onSheared(slots[SLOT_SHEARS], world, pos, fortuneLevel);
                //get the drops
                for (ItemStack item : items) {
                    addToInventory(item);
                }

                if (ToolHelper.damageTool(slots[SLOT_SHEARS], world, pos.getX(), pos.getY(), pos.getZ())) {
                    destroyTool(SLOT_SHEARS);
                }

                currentTime = waitTime;
                addPartialUpdate("currentTime", currentTime);
            }
        }
    }

    protected void milkAnimal(EntityAnimal animal) {
        if ((!slots[SLOT_BUCKETS].isEmpty()) && (hasUpgrade(UpgradeType.MILKER))) {
            initFaker();
            ItemStack item = slots[SLOT_BUCKETS].copy();
            item.setCount(1);
            faker.setItemInHand(item);
            if (animal.processInteract(faker, EnumHand.MAIN_HAND)) {
                IInventory inv = faker.inventory;
                for (int i = 0; i < inv.getSizeInventory(); i++) {
                    if (!inv.getStackInSlot(i).isEmpty()) {
                        addToInventory(inv.getStackInSlot(i));
                        inv.setInventorySlotContents(i, ItemStack.EMPTY);
                    }
                }

                slots[SLOT_BUCKETS].shrink(1);
                if (slots[SLOT_BUCKETS].getCount() == 0) {
                    slots[SLOT_BUCKETS] = ItemStack.EMPTY;
                }

                currentTime = waitTime;
                addPartialUpdate("currentTime", currentTime);
            }
        }
    }

    public static boolean isFeed(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return false;
        }

        if (itemStack.getItem() == null) {
            return false;
        }

        if (itemStack.getItem() == Items.WHEAT) {
            return true;
        }

        if (itemStack.getItem() == Items.WHEAT_SEEDS) {
            return true;
        }

        if (itemStack.getItem() == Items.CARROT) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        if ((slot == this.SLOT_FOOD) && (TileFarmer.isFeed(stack))) {
            return true;
        }

        if ((slot == this.SLOT_SHEARS) && (stack.getItem() == Items.SHEARS) && (hasUpgrade(UpgradeType.SHEARING))) {
            return true;
        } else {
            if ((slot == this.SLOT_BUCKETS) && (stack.getItem() == Items.BUCKET) && (hasUpgrade(UpgradeType.MILKER))) {
                return true;
            }
        }
        return super.isItemValidForSlot(slot, stack);
    }

    @Override
    protected Point3I adjustedSpiral(int n) {
        Point3I point = super.adjustedSpiral(n + 1);
        return point;
    }
}