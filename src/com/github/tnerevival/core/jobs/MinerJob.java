package com.github.tnerevival.core.jobs;

import java.awt.Color;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MinerJob extends Job {
	@Override
	public String name() {
		return "Miner";
	}

	@Override
	public int pay() {
		return 1000;
	}

	@Override
	public int totalPromotions() {
		return 7;
	}
	
	public MinerRank starterRank() {
		return MinerRank.JUNIOR_ASSOCIATE;
	}
	
	public MinerRank leaderRank() {
		return MinerRank.PRESIDENT;
	}

	@Override
	public Player jobLeader() {
		return null;
	}
	
	//todo: lists of each rank
	
	public void OnJobLeaderFired(Player exLeader, Player newLeader) {
		exLeader.sendMessage(ChatColor.RED + "[" + ChatColor.GREEN + "Jobs" + ChatColor.RED + "] " +
				"You've been replaced by " + ChatColor.DARK_PURPLE + newLeader.getDisplayName() + "!");
	}
}

enum MinerRank {
	JUNIOR_ASSOCIATE, 
	ASSOCIATE,
	SENIOR_ASSOCIATE,
	MANAGER,
	SUPERVISOR,
	VICE_PRESIDENT,
	PRESIDENT
}
