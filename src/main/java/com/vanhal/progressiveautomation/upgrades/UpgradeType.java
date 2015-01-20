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
	FILLER;

	/**
	 * Helper method for retrieving the proper range upgrade given the machineLevel
	 * @param machineLevel
	 * @return
	 */
	public static UpgradeType getRangeUpgrade(int machineLevel) {
		switch (machineLevel) {
			case ToolHelper.LEVEL_WOOD : return WOODEN;
			case ToolHelper.LEVEL_STONE : return STONE;
			case ToolHelper.LEVEL_IRON : return IRON;
			case ToolHelper.LEVEL_DIAMOND : return DIAMOND;
			default : return WOODEN;
		}
	}
}
