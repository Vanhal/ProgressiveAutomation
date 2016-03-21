package com.vanhal.progressiveautomation.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

/* A Point case to allow for an x, y point */
public class Point3I {
	private int x, y, z;
	
	public Point3I() {
		setLocation(0, 0, 0);
	}
	
	public Point3I(Point3I point) {
		setLocation(point.getX(), point.getY(), point.getZ());
	}
	
	public Point3I(int x, int y, int z) {
		setLocation(x, y, z);
	}
	
	public void setLocation(int x1, int y1, int z1) {
		this.x = x1;
		this.y = y1;
		this.z = z1;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setZ(int z) {
		this.z = z;
	}
	
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
	
	public String toString() {
		return ""+x+", "+y+", "+z;
	}
	
	public void stepUp() {
		this.y++;
	}
	
	public void stepDown() {
		this.y--;
	}
	
	public void incrX() {
		this.x++;
	}
	
	public void decrX() {
		this.x--;
	}
	
	public void incrZ() {
		this.z++;
	}
	
	public void decrZ() {
		this.z--;
	}
	
	public NBTTagCompound getNBT() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("x", getX());
		tag.setInteger("y", getY());
		tag.setInteger("z", getZ());
		return tag;
	}
	
	public void setNBT(NBTTagCompound nbt) {
		setX(nbt.getInteger("x"));
		setY(nbt.getInteger("y"));
		setZ(nbt.getInteger("z"));
	}
	
	public BlockPos toPosition() {
		return new BlockPos(getX(), getY(), getZ());
	}
	
	public void fromPosition(BlockPos pos) {
		setX(pos.getX());
		setY(pos.getY());
		setZ(pos.getZ());
	}
}
