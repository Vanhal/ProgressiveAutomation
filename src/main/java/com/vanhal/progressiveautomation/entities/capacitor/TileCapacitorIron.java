package com.vanhal.progressiveautomation.entities.capacitor;

import com.vanhal.progressiveautomation.PAConfig;

public class TileCapacitorIron extends TileCapacitor {
	
	public TileCapacitorIron() {
		super();
		setEnergyStorage(40000*PAConfig.rfStorageFactor, 320);
	}

}
