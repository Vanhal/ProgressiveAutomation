package com.vanhal.progressiveautomation.entities.capacitor;

import com.vanhal.progressiveautomation.PAConfig;

public class TileCapacitorStone extends TileCapacitor {
	
	public TileCapacitorStone() {
		super();
		setEnergyStorage(10000*PAConfig.rfStorageFactor, 160);
	}

}
