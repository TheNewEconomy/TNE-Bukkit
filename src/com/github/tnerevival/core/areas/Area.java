package com.github.tnerevival.core.areas;

import java.io.Serializable;

public class Area implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * The name of the Player that owns this Area.
	 */
	String owner;
	
	/**
	 * The x coordinate of the chunk that this Area is located in.
	 */
	int x;
	
	/**
	 * The z coordinate of the chunk that this Area is located in.
	 */
	int z;
	
	/**
	 * The name of the type of area that this area is.
	 */
	String type;
	
	/**
	 * Creates a new Area with the specified owner, chunk x and z coordinates, and sets the Area Type to personal.
	 * @param owner
	 * @param x
	 * @param z
	 */
	public Area(String owner, int x, int z) {
		this(owner, x, z, "Personal");
	}
	
	/**
	 * Creates a new Area with the specified owner, chunk x and z coordinates, and Area Type.
	 * @param owner
	 * @param x
	 * @param z
	 * @param type
	 */
	public Area(String owner, int x, int z, String type) {
		this.owner = owner;
		this.x = x;
		this.z = z;
		this.type = type;
	}
}