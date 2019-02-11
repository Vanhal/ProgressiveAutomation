package com.vanhal.progressiveautomation.common.entities;

import com.vanhal.progressiveautomation.common.upgrades.UpgradeType;

import java.util.Set;

public interface IUpgradeable {

    @Deprecated
    int getUpgrades();

    @Deprecated
    void setUpgrades(int value);

    @Deprecated
    void addUpgrades(int addValue);

    int getRange();

    int getUpgradeLevel();

    void setUpgradeLevel(int level);

    boolean isAllowedUpgrade(UpgradeType type);

    boolean hasUpgrade(UpgradeType type);

    Integer getUpgradeAmount(UpgradeType type);

    void setUpgradeAmount(UpgradeType type, Integer amount);

    void addUpgrade(UpgradeType type, Integer amount);

    void removeUpgradeCompletely(UpgradeType type);

    Set<UpgradeType> getInstalledUpgradeTypes();
}
