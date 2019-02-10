package com.vanhal.progressiveautomation.ref;

import com.mojang.authlib.GameProfile;
import com.vanhal.progressiveautomation.api.PAItems;
import com.vanhal.progressiveautomation.util.PlayerFake;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class ToolHelper {

    public static final String USERNAME = "FAKE_PLAYER_PROGRESSIVE_AUTOMATION";
    public static GameProfile PROFILE = new GameProfile(UUID.nameUUIDFromBytes(USERNAME.getBytes()), USERNAME);

    //tools
    public static int TYPE_PICKAXE = 0;
    public static int TYPE_SHOVEL = 1;
    public static int TYPE_AXE = 2;
    public static int TYPE_SWORD = 3;
    public static int TYPE_HOE = 4;

    //levels
    public static final int LEVEL_WOOD = 0;
    public static final int LEVEL_STONE = 1;
    public static final int LEVEL_IRON = 2;
    public static final int LEVEL_GOLD = 0;
    public static final int LEVEL_DIAMOND = 3;
    public static final int LEVEL_MAX = 100;

    //speeds for levels
    public static int SPEED_WOOD = 2;
    public static int SPEED_STONE = 4;
    public static int SPEED_IRON = 6;
    public static int SPEED_DIAMOND = 8;
    public static int SPEED_GOLD = 12;

    //random
    protected static Random RND = new Random();

    public static int getType(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (item instanceof ItemPickaxe) {
            return TYPE_PICKAXE;
        } else if (item instanceof ItemAxe) {
            return TYPE_AXE;
        } else if (item instanceof ItemSpade) {
            return TYPE_SHOVEL;
        } else if (item instanceof ItemSword) {
            return TYPE_SWORD;
        } else if (item instanceof ItemHoe) {
            return TYPE_HOE;
        } else if (item instanceof ItemTool) {
            Set<String> toolClasses = ((ItemTool) item).getToolClasses(itemStack);
            if (toolClasses.contains("pickaxe")) return TYPE_PICKAXE;
            else if (toolClasses.contains("axe")) return TYPE_AXE;
            else if (toolClasses.contains("shovel")) return TYPE_SHOVEL;
            else if (toolClasses.contains("hoe")) return TYPE_HOE;
            else return -1;
        } else if (tinkersType(item) >= 0) { //see if it's a tinkers type
            return tinkersType(item);
        } else {
            return -1;
        }
    }

    public static int getLevel(ItemStack itemStack) {
        //Vanilla Tools
        if (itemStack.getItem() instanceof ItemTool) {
            String toolClass = (itemStack.getItem()).getToolClasses(itemStack).iterator().next();
            return (itemStack.getItem()).getHarvestLevel(itemStack, toolClass, null, null);
        } else if ((itemStack.getItem() instanceof ItemSword) ||
                (itemStack.getItem() instanceof ItemHoe)) {
            String material = "";
            if (itemStack.getItem() instanceof ItemSword)
                material = ((ItemSword) itemStack.getItem()).getToolMaterialName();
            else if (itemStack.getItem() instanceof ItemHoe)
                material = ((ItemHoe) itemStack.getItem()).getMaterialName();
            if (material.equals("WOOD")) return LEVEL_WOOD;
            else if (material.equals("STONE")) return LEVEL_STONE;
            else if (material.equals("IRON")) return LEVEL_IRON;
            else if (material.equals("GOLD")) return LEVEL_GOLD;
            else if (material.equals("EMERALD")) return LEVEL_DIAMOND;
            else if (material.equals("DIAMOND")) return LEVEL_DIAMOND;
        } else {
            //Tinkers Tools
            if (itemStack.hasTagCompound()) {
                return tinkersLevel(itemStack);
            }
        }

        return -1;
    }

    //check for the tinkers tag
    public static int tinkersLevel(ItemStack item) {
        if (item.getTagCompound().hasKey("Stats")) {
            NBTTagCompound tags = item.getTagCompound().getCompoundTag("Stats");
            int toolLevel = tags.getInteger("HarvestLevel");
            return toolLevel;
        }
        return -1;
    }

    //Get the type of tinker tool
    public static int tinkersType(Item item) {
        String name = item.getTranslationKey();
        if (name.length() >= 16) {
            if (name.substring(5, 15).equalsIgnoreCase("tconstruct")) {
                if (name.substring(16).equalsIgnoreCase("pickaxe")) {
                    return TYPE_PICKAXE;
                } else if ((name.substring(16).equalsIgnoreCase("axe")) || (name.substring(16).equalsIgnoreCase("hatchet"))) {
                    return TYPE_AXE;
                } else if (name.substring(16).equalsIgnoreCase("shovel")) {
                    return TYPE_SHOVEL;
                } else if ((name.substring(16).equalsIgnoreCase("hoe")) || (name.substring(16).equalsIgnoreCase("Mattock"))) {
                    return TYPE_HOE;
                } else if ((name.substring(16).equalsIgnoreCase("Broadsword")) ||
                        (name.substring(16).equalsIgnoreCase("Rapier")) ||
                        (name.substring(16).equalsIgnoreCase("Cutlass")) ||
                        (name.substring(16).equalsIgnoreCase("Cleaver"))) {
                    return TYPE_SWORD;
                }
            }
        }
        return -1;
    }

    public static boolean isBroken(ItemStack item) {
        if (item.isEmpty()) return false;
        boolean broken = tinkersIsBroken(item);
        if (!broken) {
            if (item.getItemDamage() >= item.getMaxDamage()) {
                return true;
            }
        }
        return broken;
    }

    public static boolean tinkersIsBroken(ItemStack item) {
        if (item.isEmpty()) return false;
        if ((item.hasTagCompound()) && (item.getTagCompound().hasKey("Stats"))) {
            NBTTagCompound tags = item.getTagCompound().getCompoundTag("Stats");
            return tags.getBoolean("Broken");
        }
        return false;
    }

    public static ItemStack getUpgradeType(int level) {
        if (level >= ToolHelper.LEVEL_DIAMOND) {
            return new ItemStack(PAItems.DIAMOND_UPGRADE);
        } else if (level == ToolHelper.LEVEL_IRON) {
            return new ItemStack(PAItems.IRON_UPGRADE);
        } else if (level == ToolHelper.LEVEL_STONE) {
            return new ItemStack(PAItems.STONE_UPGRADE);
        } else {
            return new ItemStack(PAItems.WOOD_UPGRADE);
        }
    }

    public static int getHarvestLevel(ItemStack item) {
        int value = getLevel(item);
        return value;
    }

    public static int getSpeed(int level) {
        if (level == LEVEL_GOLD) {
            return SPEED_GOLD;
        } else if (level >= LEVEL_DIAMOND) {
            return SPEED_DIAMOND;
        } else if (level == LEVEL_IRON) {
            return SPEED_IRON;
        } else if (level == LEVEL_STONE) {
            return SPEED_STONE;
        } else if (level == LEVEL_WOOD) {
            return SPEED_WOOD;
        } else {
            return 1;
        }
    }

    public static float getDigSpeed(ItemStack itemStack, IBlockState state) {
        if ((!itemStack.isEmpty())) {
            return itemStack.getDestroySpeed(state);
        }
        return 1.0f;
    }

    public static boolean damageTool(ItemStack tool, World world, int x, int y, int z) {
        if ((tool.getItem() instanceof ItemShears) || (tool.getItem() instanceof ItemTool) ||
                (tool.getItem() instanceof ItemHoe) || (tool.getItem() instanceof ItemSword)) {
            if (tool.attemptDamageItem(1, RND, null)) {
                return true;
            }
        } else {
            Block mineBlock = world.getBlockState(new BlockPos(x, y, z)).getBlock();
            PlayerFake fakePlayer = new PlayerFake((WorldServer) world);
            if (tinkersType(tool.getItem()) == TYPE_HOE) {
                tool.attemptDamageItem(1, RND, null);
            } else {
                tool.getItem().onBlockDestroyed(tool, world, mineBlock.getDefaultState(), new BlockPos(x, y, z), fakePlayer);
            }
            if (tinkersIsBroken(tool)) return true;
        }
        return false;
    }
}