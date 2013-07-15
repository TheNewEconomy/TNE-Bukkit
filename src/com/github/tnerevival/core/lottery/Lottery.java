package com.github.tnerevival.core.lottery;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class Lottery {
	List<String> entries = new ArrayList<String>();
	
	String name;
	long startTime;
	ItemStack[] itemReward;
	Double moneyReward;
	
	public Lottery(String name, long startTime, ItemStack[] reward) {
		this(name, startTime, reward, 0.0);
	}
	
	public Lottery(String name, long startTime, Double reward) {
		this(name, startTime, null, reward);
	}
	
	public Lottery(String name, long startTime, ItemStack[] iReward, Double mReward) {
		this.name = name;
		this.startTime = startTime;
		this.itemReward = iReward;
		this.moneyReward = mReward;
	}
}