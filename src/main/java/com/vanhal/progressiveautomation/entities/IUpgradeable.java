package com.vanhal.progressiveautomation.entities;

import java.util.Set;

import com.vanhal.progressiveautomation.upgrades.UpgradeType;

public interface IUpgradeable {

	@Deprecated
	public int getUpgrades();

	@Deprecated
	public void setUpgrades(int value);

	@Deprecated
	public void addUpgrades(int addValue);
	
	public int getRange();
	
	public int getUpgradeLevel();
	
	public void setUpgradeLevel(int level);

	public boolean isAllowedUpgrade(UpgradeType type);

	public boolean hasUpgrade(UpgradeType type);

	public Integer getUpgradeAmount(UpgradeType type);

	public void setUpgradeAmount(UpgradeType type, Integer amount);

	public void addUpgrade(UpgradeType type, Integer amount);

	public void removeUpgradeCompletely(UpgradeType type);

	public Set<UpgradeType> getInstalledUpgradeTypes();
}
