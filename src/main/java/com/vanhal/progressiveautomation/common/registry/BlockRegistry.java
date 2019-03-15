package com.vanhal.progressiveautomation.common.registry;

import com.vanhal.progressiveautomation.References;
import com.vanhal.progressiveautomation.common.blocks.BlockCapacitor;
import com.vanhal.progressiveautomation.common.blocks.BlockChopper;
import com.vanhal.progressiveautomation.common.blocks.BlockCrafter;
import com.vanhal.progressiveautomation.common.blocks.BlockFarmer;
import com.vanhal.progressiveautomation.common.blocks.BlockGenerator;
import com.vanhal.progressiveautomation.common.blocks.BlockKiller;
import com.vanhal.progressiveautomation.common.blocks.BlockMiner;
import com.vanhal.progressiveautomation.common.blocks.BlockPlanter;
import com.vanhal.progressiveautomation.common.util.ToolHelper;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = References.MODID)
public class BlockRegistry {

    @SubscribeEvent
    public static void onRegister(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> blocks = event.getRegistry();
        blocks.registerAll(
                new BlockCapacitor(ToolHelper.LEVEL_WOOD),
                new BlockCapacitor(ToolHelper.LEVEL_STONE),
                new BlockCapacitor(ToolHelper.LEVEL_IRON),
                new BlockCapacitor(ToolHelper.LEVEL_DIAMOND),
                new BlockChopper(ToolHelper.LEVEL_WOOD),
                new BlockChopper(ToolHelper.LEVEL_STONE),
                new BlockChopper(ToolHelper.LEVEL_IRON),
                new BlockChopper(ToolHelper.LEVEL_DIAMOND),
                new BlockCrafter(ToolHelper.LEVEL_WOOD),
                new BlockCrafter(ToolHelper.LEVEL_STONE),
                new BlockCrafter(ToolHelper.LEVEL_IRON),
                new BlockCrafter(ToolHelper.LEVEL_DIAMOND),
                new BlockFarmer(ToolHelper.LEVEL_WOOD),
                new BlockFarmer(ToolHelper.LEVEL_STONE),
                new BlockFarmer(ToolHelper.LEVEL_IRON),
                new BlockFarmer(ToolHelper.LEVEL_DIAMOND),
                new BlockGenerator(ToolHelper.LEVEL_WOOD),
                new BlockGenerator(ToolHelper.LEVEL_STONE),
                new BlockGenerator(ToolHelper.LEVEL_IRON),
                new BlockGenerator(ToolHelper.LEVEL_DIAMOND),
                new BlockKiller(ToolHelper.LEVEL_WOOD),
                new BlockKiller(ToolHelper.LEVEL_STONE),
                new BlockKiller(ToolHelper.LEVEL_IRON),
                new BlockKiller(ToolHelper.LEVEL_DIAMOND),
                new BlockMiner(ToolHelper.LEVEL_WOOD),
                new BlockMiner(ToolHelper.LEVEL_STONE),
                new BlockMiner(ToolHelper.LEVEL_IRON),
                new BlockMiner(ToolHelper.LEVEL_DIAMOND),
                new BlockPlanter(ToolHelper.LEVEL_WOOD),
                new BlockPlanter(ToolHelper.LEVEL_STONE),
                new BlockPlanter(ToolHelper.LEVEL_IRON),
                new BlockPlanter(ToolHelper.LEVEL_DIAMOND)
        );
    }
}