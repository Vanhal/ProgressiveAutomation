package com.vanhal.progressiveautomation.core;

public class ClientProxy extends Proxy {
	
	@Override
	public boolean isClient() {
		return true;
	}
	
	@Override
	public boolean isServer() {
		return false;
	}
}
