package com.vanhal.progressiveautomation.events;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vanhal.progressiveautomation.ProgressiveAutomation;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class EventPlayers {
	
	private ArrayList<String> names = new ArrayList<String>();
	protected static final TMap textLines = new THashMap();
	private final String url = "https://raw.githubusercontent.com/Vanhal/PAData/master/namesv2.txt";
	
	public EventPlayers() {
		updateList();
	}
	
	private void updateList() {
		try {
			URL listFile = new URL(url);
			BufferedReader reader = new BufferedReader(new InputStreamReader(listFile.openStream()));
			String temp = reader.readLine();
			while (temp!=null) {
				String[] rows = temp.split(",");
				if (rows.length==4) {
					String name = rows[0].trim();
					if (rows[1].trim().equals("true")) {
						names.add(name);
					}
					textLines.put(name, rows[2]+","+rows[3]);
				}
				temp = reader.readLine();
			}
		} catch (Exception e) {
			
		}
	}
	
	public static String getPlayerLine(String player, int line) {
		return getPlayerLine(player, line, null);
	}
	
	public static String getPlayerLine(String player, int line, String replace) {
		if (textLines.containsKey(player)) {
			String[] lines = ((String)textLines.get(player)).split(",");
			if (lines[line-1]!=null) {
				return lines[line-1];
			}
		}
		return replace;
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
