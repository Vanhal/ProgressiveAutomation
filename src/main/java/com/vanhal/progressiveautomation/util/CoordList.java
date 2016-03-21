package com.vanhal.progressiveautomation.util;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class CoordList {
	private TMap<String, Point3I> list = new THashMap<String, Point3I>();
	private TMap<Integer, String> map = new THashMap<Integer, String>();
	
	public CoordList() {
		clear();
	}
	
	public boolean inList(Point3I point) {
		if (list.containsKey(hashPoint(point))) {
			return true;
		}
		return false;
	}
	
	public NBTTagList saveToNBT() {
		
		NBTTagList contents = new NBTTagList();
		for (int i = 0; i < list.size(); i++) {
			NBTTagCompound tag = new NBTTagCompound();
			int index = list.size() - i;
			String key = map.get(index);
			tag.setInteger("index", index);
			tag.setString("key", key);
			tag.setTag("coord", list.get(key).getNBT());
			contents.appendTag(tag);
		}
		return contents;
	}
	
	public void loadFromNBT(NBTTagList contents) {
		clear();
		for (int i = 0; i < contents.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) contents.getCompoundTagAt(i);
			int index = tag.getInteger("index");
			String key = tag.getString("key");
			Point3I point = new Point3I();
			point.setNBT(tag.getCompoundTag("coord"));
			map.put(index, key);
			list.put(key, point);
		}
	}
	
	public boolean inList(int x, int y, int z) {
		return inList(new Point3I(x, y, z));
	}
	
	public boolean push(Point3I inPoint) {
		String key = hashPoint(inPoint);
		Point3I point = new Point3I(inPoint);
		if (list.containsKey(key)) {		
			return false;
		}
		list.put(key, point);
		map.put(list.size(), key);
		return true;
	}
	
	public Point3I pop() {
		if (list.size()>0) {
			String key = map.remove(list.size());
			return list.remove(key);
		} else {
			return null;
		}
	}
	
	public Point3I peak() {
		if (list.size()>0) {
			String key = map.get(list.size());
			return list.get(key);
		} else {
			return null;
		}
	}
	
	public void clear() {
		list.clear();
		map.clear();
	}
	
	public int size() {
		return list.size();
	}
	
	protected String hashPoint(Point3I point) {
		String hash = point.getX()+"|"+point.getY()+"|"+point.getZ();
		return hash;
	}
	
}
