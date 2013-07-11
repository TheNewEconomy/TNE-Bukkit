package com.github.tnerevival.core.areas;

import java.io.Serializable;

public class Area implements Serializable {
	private static final long serialVersionUID = 1L;
	
	String owner;
	int x;
	int z;
	String type;
	
	public Area(String owner, int x, int z) {
		this.owner = owner;
		this.x = x;
		this.z = z;
		this.type = "Personal";
	}
	
	public Area(String owner, int x, int z, String type) {
		this.owner = owner;
		this.x = x;
		this.z = z;
		this.type = type;
	}
	
}