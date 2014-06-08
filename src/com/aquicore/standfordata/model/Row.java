package com.aquicore.standfordata.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Row {

	private int id;
	private Map<Integer, String> cell = new HashMap<Integer, String>();
	private int indexCounter;
	
	public Row() {
		clear();
	}
	
	public void add(String value) {
		cell.put(indexCounter, value);
		indexCounter++;
	}
	
	public String get(int index) {
		return cell.get(index);
	}
	
	public void clear() {
		cell.clear();
		indexCounter = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		String buff = "" + id;
		for (Entry<Integer, String> entry : cell.entrySet()) {
			buff += " " + entry.getValue();
		}
		
		return buff;
	}

}
