package com.vanhal.progressiveautomation.common.upgrades;

import com.vanhal.progressiveautomation.common.items.upgrades.ItemUpgrade;

import java.util.EnumMap;
import java.util.Map;

public class UpgradeRegistry {

    private static Map<UpgradeType, ItemUpgrade> upgradesMap = new EnumMap<>(UpgradeType.class);

    /**
     * Register an upgrade item for the purposes of dropping machine contents on block destruction
     *
     * @param type type of upgrade
     * @param item the item corresponding to the upgrade
     */
    public static void registerUpgradeItem(UpgradeType type, ItemUpgrade item) {
        upgradesMap.put(type, item);
    }

    /**
     * @param type type of upgrade
     * @return Item corresponding to the given type. If the upgrade is tiered, it will return
     * the version appropriate to the given level.
     */
    public static ItemUpgrade getUpgradeItem(UpgradeType type) {
        return upgradesMap.get(type);
    }
}