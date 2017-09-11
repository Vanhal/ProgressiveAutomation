package com.vanhal.progressiveautomation.upgrades;

import com.vanhal.progressiveautomation.ref.ToolHelper;

/**
 * This enum represents available upgrade types. Do NOT change the upgrade names without thought,
 * it may cause players to lose installed upgrades in their machines
 */
public enum UpgradeType {

	WOODEN,
	STONE,
	IRON,
	DIAMOND,
	WITHER,
	COBBLE_GEN,
	FILLER,
	FILTER_MOB,
	FILTER_ANIMAL,
	FILTER_ADULT,
	FILTER_PLAYER,
	MILKER,
	SHEARING;

	/**
	 * Helper method for retrieving the proper range upgrade given the machineLevel
	 * @param machineLevel
	 * @return
	 */
	public static UpgradeType getRangeUpgrade(int machineLevel) {
		if (machineLevel >= ToolHelper.LEVEL_DIAMOND) {
			 return DIAMOND;
		} else if (machineLevel == ToolHelper.LEVEL_IRON) {
			return IRON;
		} else if (machineLevel == ToolHelper.LEVEL_STONE) {
			return STONE;
		} else {
			return WOODEN;
		}
	}
}
