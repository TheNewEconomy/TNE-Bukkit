package com.github.tnerevival.core.inventory;

import org.bukkit.event.inventory.InventoryType;

public class View {
	
	private long opened;
	private InventoryType type;
	private boolean close = false;
	
	public View(long l, InventoryType type) {
		this.opened = l;
		this.type = type;
	}

	public long getOpened() {
		return opened;
	}

	public void setOpened(long opened) {
		this.opened = opened;
	}

	public InventoryType getType() {
		return type;
	}

	public void setType(InventoryType type) {
		this.type = type;
	}

	public boolean close() {
		return close;
	}

	public void setClose(boolean close) {
		this.close = close;
	}
}