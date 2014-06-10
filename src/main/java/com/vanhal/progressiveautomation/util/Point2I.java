package com.vanhal.progressiveautomation.util;

/* A Point case to allow for an x, y point */
public class Point2I {
	private int x, y;
	
	public Point2I() {
		
	}
	
	public Point2I(int x, int y) {
		setLocation(x, y);
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public String toString() {
		return ""+x+", "+y;
	}
}
