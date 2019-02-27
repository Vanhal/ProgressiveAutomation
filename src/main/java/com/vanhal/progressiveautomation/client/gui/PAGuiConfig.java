package com.vanhal.progressiveautomation.client.gui;

import com.vanhal.progressiveautomation.PAConfig;
import com.vanhal.progressiveautomation.References;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.CategoryEntry;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

public class PAGuiConfig extends GuiConfig {

    public PAGuiConfig(GuiScreen parent) {
        super(parent, getConfigElements(), References.MODID, false, false, GuiConfig.getAbridgedConfigPath(PAConfig.config.toString()));
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<>();
        list.add(new DummyCategoryElement("general", "gui.general", GeneralEntry.class));
        list.add(new DummyCategoryElement("blocks", "gui.blocks", BlocksEntry.class));
        list.add(new DummyCategoryElement("upgrades", "gui.upgrade", UpgradesEntry.class));
        list.add(new DummyCategoryElement("rfoptions", "gui.rfoptions", RFOptionsEntry.class));
        return list;
    }

    public static class GeneralEntry extends CategoryEntry {

        public GeneralEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
            super(owningScreen, owningEntryList, prop);
        }

        protected GuiScreen buildChildScreen() {
            return new GuiConfig(this.owningScreen, new ConfigElement(PAConfig.config.getCategory("general")).getChildElements(),
                    References.MODID, false, false, GuiConfig.getAbridgedConfigPath(PAConfig.config.toString()));
        }
    }

    public static class BlocksEntry extends CategoryEntry {

        public BlocksEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
            super(owningScreen, owningEntryList, prop);
        }

        protected GuiScreen buildChildScreen() {
            return new GuiConfig(this.owningScreen, new ConfigElement(PAConfig.config.getCategory("blocks")).getChildElements(),
                    References.MODID, false, true, GuiConfig.getAbridgedConfigPath(PAConfig.config.toString()));
        }
    }

    public static class UpgradesEntry extends CategoryEntry {

        public UpgradesEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
            super(owningScreen, owningEntryList, prop);
        }

        protected GuiScreen buildChildScreen() {
            return new GuiConfig(this.owningScreen, new ConfigElement(PAConfig.config.getCategory("upgrades")).getChildElements(),
                    References.MODID, false, true, GuiConfig.getAbridgedConfigPath(PAConfig.config.toString()));
        }
    }

    public static class RFOptionsEntry extends CategoryEntry {

        public RFOptionsEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
            super(owningScreen, owningEntryList, prop);
        }

        protected GuiScreen buildChildScreen() {
            return new GuiConfig(this.owningScreen, new ConfigElement(PAConfig.config.getCategory("rfoptions")).getChildElements(),
                    References.MODID, false, false, GuiConfig.getAbridgedConfigPath(PAConfig.config.toString()));
        }
    }
}