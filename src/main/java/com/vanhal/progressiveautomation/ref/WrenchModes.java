package com.vanhal.progressiveautomation.ref;

import java.util.ArrayList;

public class WrenchModes {
	public static ArrayList<Mode> modes = new ArrayList<Mode>();
	
	public static enum Mode {
		Rotate, Query, Output, FuelInput, Input, Normal, Disabled;
		Mode() {modes.add(this);}
	}
}
