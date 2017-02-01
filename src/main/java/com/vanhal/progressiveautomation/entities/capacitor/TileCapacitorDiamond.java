package com.vanhal.progressiveautomation.entities.capacitor;

import com.vanhal.progressiveautomation.PAConfig;

public class TileCapacitorDiamond extends TileCapacitor {
	
	public TileCapacitorDiamond() {
		super();
		setEnergyStorage(80000*PAConfig.rfStorageFactor,640);
	}

}
