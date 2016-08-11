package com.github.tnerevival.core.signs;

import com.github.tnerevival.core.event.sign.SignEventAction;
import com.github.tnerevival.core.event.sign.TNESignEvent;
import com.github.tnerevival.serializable.SerializableLocation;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public abstract class TNESign {
	protected UUID owner;
	protected SignType type;
	protected SerializableLocation location;
	protected Inventory inventory = null;
	protected String permission = null;
	
	public TNESign(UUID owner) {
		this.owner = owner;
	}

	/**
	 * Called when this sign is clicked on
	 * @param player
	 * @return Whether or not the action was performed successfully
	 */
	public boolean onClick(Player player) {
		TNESignEvent event = new TNESignEvent(MISCUtils.getID(player), this, SignEventAction.LEFT_CLICKED);
		Bukkit.getServer().getPluginManager().callEvent(event);
		return (!event.isCancelled());
	}
	
	/**
	 * Called when this sign is right clicked on
	 * @param player
	 * @return Whether or not the action was performed successfully
	 */
	public boolean onRightClick(Player player) {
		TNESignEvent event = new TNESignEvent(MISCUtils.getID(player), this, SignEventAction.RIGHT_CLICKED);
		Bukkit.getServer().getPluginManager().callEvent(event);
		return (!event.isCancelled());
	}

	/**
	 * Called when the inventory(if any) attached to this sign is opened.
	 * @param player
	 * @return Whether or not the action was performed successfully
	 */
	public boolean onOpen(Player player) {
		TNESignEvent event = new TNESignEvent(MISCUtils.getID(player), this, SignEventAction.INVENTORY_OPENED);
		Bukkit.getServer().getPluginManager().callEvent(event);
		return (!event.isCancelled());
	}
	
	/**
	 * Called when the inventory(if any) attached to this sign is closed.
	 * @param player
	 * @return Whether or not the action was performed successfully
	 */
	public boolean onClose(Player player) {
		TNESignEvent event = new TNESignEvent(MISCUtils.getID(player), this, SignEventAction.INVENTORY_CLOSED);
		Bukkit.getServer().getPluginManager().callEvent(event);
		return (!event.isCancelled());
	}
 
	public UUID getOwner() {
		return owner;
	}

	public void setOwner(UUID owner) {
		this.owner = owner;
	}

	public SignType getType() {
		return type;
	}

	public void setType(SignType type) {
		this.type = type;
	}

	public SerializableLocation getLocation() {
		return location;
	}

	public void setLocation(SerializableLocation location) {
		this.location = location;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}
}