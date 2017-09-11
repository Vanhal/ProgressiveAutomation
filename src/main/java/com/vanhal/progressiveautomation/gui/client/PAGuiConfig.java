package com.vanhal.progressiveautomation.gui.client;

import java.util.ArrayList;
import java.util.List;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.ref.Ref;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.CategoryEntry;
import net.minecraftforge.fml.client.config.IConfigElement;

public class PAGuiConfig extends GuiConfig {
	
	public PAGuiConfig(GuiScreen parent) {
        super(parent, getConfigElements(),
               Ref.MODID, false, false, GuiConfig.getAbridgedConfigPath(PAConfig.config.toString()));
    }
	
	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<IConfigElement>();
        list.add(new DummyCategoryElement("general", "gui.general", GeneralEntry.class));
        list.add(new DummyCategoryElement("blocks", "gui.blocks", BlocksEntry.class));
        list.add(new DummyCategoryElement("upgrades", "gui.upgrade", UpgradesEntry.class));
        list.add(new DummyCategoryElement("rfoptions", "gui.rfoptions", RFOptionsEntry.class));
        return list;
	}
	
	public static class GeneralEntry extends CategoryEntry {

		public GeneralEntry(GuiConfig owningScreen,
				GuiConfigEntries owningEntryList, IConfigElement prop) {
			super(owningScreen, owningEntryList, prop);
		}
		
		protected GuiScreen buildChildScreen() {
			 return new GuiConfig(this.owningScreen, new ConfigElement(PAConfig.config.getCategory("general")).getChildElements(),
		                Ref.MODID, false, false, GuiConfig.getAbridgedConfigPath(PAConfig.config.toString()));
		}
		
	}
	
	public static class BlocksEntry extends CategoryEntry {

		public BlocksEntry(GuiConfig owningScreen,
				GuiConfigEntries owningEntryList, IConfigElement prop) {
			super(owningScreen, owningEntryList, prop);
		}
		
		protected GuiScreen buildChildScreen() {
			 return new GuiConfig(this.owningScreen, new ConfigElement(PAConfig.config.getCategory("blocks")).getChildElements(),
		                Ref.MODID, false, true, GuiConfig.getAbridgedConfigPath(PAConfig.config.toString()));
		}
		
	}
	
	public static class UpgradesEntry extends CategoryEntry {

		public UpgradesEntry(GuiConfig owningScreen,
				GuiConfigEntries owningEntryList, IConfigElement prop) {
			super(owningScreen, owningEntryList, prop);
		}
		
		protected GuiScreen buildChildScreen() {
			 return new GuiConfig(this.owningScreen, new ConfigElement(PAConfig.config.getCategory("upgrades")).getChildElements(),
		                Ref.MODID, false, true, GuiConfig.getAbridgedConfigPath(PAConfig.config.toString()));
		}
		
	}
	
	public static class RFOptionsEntry extends CategoryEntry {

		public RFOptionsEntry(GuiConfig owningScreen,
				GuiConfigEntries owningEntryList, IConfigElement prop) {
			super(owningScreen, owningEntryList, prop);
		}
		
		protected GuiScreen buildChildScreen() {
			 return new GuiConfig(this.owningScreen, new ConfigElement(PAConfig.config.getCategory("rfoptions")).getChildElements(),
		                Ref.MODID, false, false, GuiConfig.getAbridgedConfigPath(PAConfig.config.toString()));
		}
		
	}
}
