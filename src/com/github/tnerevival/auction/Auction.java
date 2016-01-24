package com.github.tnerevival.auction;

import java.util.UUID;

import com.github.tnerevival.TNE;
import com.github.tnerevival.serializable.SerializableItemStack;

public class Auction {
	
	private UUID id;
	private UUID owner;
	private SerializableItemStack item;
	private String world;
	private Boolean global;
	private Long start;
	private Long length;
	private Double increment;
	private Double startingPrice;
	
	public Auction() {
		id = UUID.randomUUID();
		this.world = TNE.instance.defaultWorld;
		this.global = true;
		this.start = System.nanoTime();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getOwner() {
		return owner;
	}

	public void setOwner(UUID owner) {
		this.owner = owner;
	}

	public SerializableItemStack getItem() {
		return item;
	}

	public void setItem(SerializableItemStack item) {
		this.item = item;
	}

	public String getWorld() {
		return world;
	}

	public void setWorld(String world) {
		this.world = world;
	}

	public Boolean getGlobal() {
		return global;
	}

	public void setGlobal(Boolean global) {
		this.global = global;
	}

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getLength() {
		return length;
	}

	public void setLength(Long length) {
		this.length = length;
	}

	public Double getIncrement() {
		return increment;
	}

	public void setIncrement(Double increment) {
		this.increment = increment;
	}

	public Double getStartingPrice() {
		return startingPrice;
	}

	public void setStartingPrice(Double startingPrice) {
		this.startingPrice = startingPrice;
	}
}