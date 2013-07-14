package com.github.tnerevival.core.jobs;

import java.awt.Color;
import java.awt.List;
import java.util.ArrayList;

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
	
	public Player vicePresident() {
		return null;
	}
	
	public ArrayList<String> juniorAssociates = new ArrayList<String>();
	public ArrayList<String> associates = new ArrayList<String>();
	public ArrayList<String> seniorAssociates = new ArrayList<String>();
	public ArrayList<String> managers = new ArrayList<String>();
	public ArrayList<String> supervisor = new ArrayList<String>();
	//public ArrayList<String> vicePresident = new ArrayList<String>(); not needed
	//ArrayList<String> president = new ArrayList<String>(); also not needed
	
	public void OnJobLeaderFired(Player exLeader, Player newLeader) {
		if (newLeader == vicePresident()) {
			newLeader = jobLeader();
			exLeader = vicePresident();
		exLeader.sendMessage(ChatColor.RED + "[" + ChatColor.GREEN + "Jobs" + ChatColor.RED + "] " +
				"You've been replaced by " + ChatColor.DARK_PURPLE + newLeader.getDisplayName() + "!");
		newLeader.sendMessage(ChatColor.RED + "[" + ChatColor.GREEN + "Jobs" + ChatColor.RED + "] " +
				"You've replaced " + ChatColor.DARK_PURPLE + exLeader.getDisplayName() + ChatColor.RED +
				" as the President of Miners! Congrats!");
		} else {
			//not going to happen
		}
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
