package com.github.tnerevival.auction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.tnerevival.utils.MISCUtils;

public class LotMessage {
	
	private List<UUID> recipients = new ArrayList<UUID>();
	private List<UUID> skip = new ArrayList<UUID>();
	private String viewingNode;
	private String message;
	
	public LotMessage(String viewingNode, String message) {
		this.viewingNode = viewingNode;
		this.message = message;
	}
	
	public void send() {
		for(UUID id : recipients) {
			if(!skip.contains(id) && MISCUtils.getPlayer(id).hasPermission(this.viewingNode)) {
				MISCUtils.getPlayer(id).sendMessage(this.message);
			}
		}
	}
	
	public void broadcast() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(!skip.contains(MISCUtils.getID(p)) && p.hasPermission(this.viewingNode)) {
				p.sendMessage(this.message);
			}
		}
	}

	public List<UUID> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<UUID> recipients) {
		this.recipients = recipients;
	}

	public List<UUID> getSkip() {
		return skip;
	}

	public void setSkip(List<UUID> skip) {
		this.skip = skip;
	}

	public String getViewingNode() {
		return viewingNode;
	}

	public void setViewingNode(String viewingNode) {
		this.viewingNode = viewingNode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}