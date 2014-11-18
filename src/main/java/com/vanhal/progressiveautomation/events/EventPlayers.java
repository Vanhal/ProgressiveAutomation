package com.vanhal.progressiveautomation.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vanhal.progressiveautomation.ProgressiveAutomation;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class EventPlayers {
	
	private List<String> names = Arrays.asList();
	private final String url = "https://raw.githubusercontent.com/Vanhal/PAData/master/names.txt";
	
	public EventPlayers() {
		updateList();
	}
	
	private void updateList() {
		try {
			URL listFile = new URL(url);
			BufferedReader reader = new BufferedReader(new InputStreamReader(listFile.openStream()));
			String temp = reader.readLine();
			while (temp!=null) {
				names.add(temp);
				temp = reader.readLine();
			}
		} catch (Exception e) {
			
		}
	}

	@SubscribeEvent
	public void onJoin(EntityJoinWorldEvent event) {
		if (!event.world.isRemote) {
			if (event.entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) event.entity;
				if (names.contains(player.getDisplayName())) {
					ItemStack potato = new ItemStack(Items.poisonous_potato);
					potato.addEnchantment(Enchantment.unbreaking, 1);
					potato.setStackDisplayName("Death Potato");
					if (!player.inventory.hasItemStack(potato)) {
						player.inventory.addItemStackToInventory(potato);
					}
				}
			}
		}
	}
}
