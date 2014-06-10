package com.vanhal.progressiveautomation.entities;

import com.vanhal.progressiveautomation.PAConfig;

public interface IUpgradeable {
	
	public int getUpgrades();

	public void setUpgrades(int value);

	public void addUpgrades(int addValue);
	
	public int getRange();
	
	public int getUpgradeLevel();
	
	public void setUpgradeLevel(int level);
}
