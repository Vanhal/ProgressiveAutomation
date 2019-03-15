package com.vanhal.progressiveautomation.common.util;

import java.util.ArrayList;

public class WrenchModes {

    public static ArrayList<Mode> modes = new ArrayList<>();

    public enum Mode {
        Rotate, Query, Output, FuelInput, Input, Normal, Disabled;

        Mode() {
            modes.add(this);
        }
    }
}