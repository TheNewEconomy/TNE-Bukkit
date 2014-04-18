package com.github.tnerevival.lottery;

import java.util.HashMap;

public class Lottery {
	
	HashMap<String, Integer> entries = new HashMap<String, Integer>();
	
	String name;
	LotteryCost cost;
	Integer maxEntries;
	Integer passed;
	Integer length;
	LotteryReward reward;
	
	public Lottery(String name) {
		this.name = name;
		this.cost = new LotteryCost();
		this.maxEntries = 5;
		this.passed = 0;
		this.length = 600;
		this.reward = new LotteryReward();
	}
}