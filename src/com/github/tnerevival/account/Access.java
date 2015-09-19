package com.github.tnerevival.account;

import java.util.UUID;

import com.github.tnerevival.TNE;

public class Access {
	UUID accessing;
	String world;
	boolean save = false;
	
	public Access(UUID accessing) {
		this(accessing, TNE.instance.defaultWorld, false);
	}
	
	public Access(UUID accessing, String world) {
		this(accessing, world, false);
	}
	
	public Access(UUID accessing, String world, boolean save) {
		this.accessing = accessing;
		this.world = world;
		this.save = save;
	}
	
	public UUID getAccessing() {
		return this.accessing;
	}
	
	public String getWorld() {
		return this.world;
	}
	
	public Boolean getSave() {
		return this.save;
	}
}