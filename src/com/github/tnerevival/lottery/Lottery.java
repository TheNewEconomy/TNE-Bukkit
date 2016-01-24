package com.github.tnerevival.lottery;

import java.util.HashMap;
import java.util.UUID;

import com.github.tnerevival.TNE;

public class Lottery {
	
	private HashMap<UUID, Integer> entries = new HashMap<UUID, Integer>();
	
	private String name;
	private String world;
	private Boolean global;
	private Boolean auto;
	private Integer maxEntries;
	private Long start;
	private Integer message;
	//Time in minutes
	private Integer length;
	private LotteryData cost;
	private LotteryData reward;
	
	public Lottery(String name) {
		this.name = name;
		this.world = TNE.instance.defaultWorld;
		this.global = true;
		this.auto = true;
		this.cost = new LotteryData();
		this.maxEntries = 5;
		this.start = System.nanoTime();
		this.message = 180;
		this.length = 600;
		this.reward = new LotteryData();
	}
	
	public void chooseWinner() {
		System.out.println("winner chosen");
	}
	
	public void reminder() {
		System.out.println("reminder");
	}

	public HashMap<UUID, Integer> getEntries() {
		return entries;
	}

	public void setEntries(HashMap<UUID, Integer> entries) {
		this.entries = entries;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Boolean getAuto() {
		return auto;
	}

	public void setAuto(Boolean auto) {
		this.auto = auto;
	}

	public Integer getMaxEntries() {
		return maxEntries;
	}

	public void setMaxEntries(Integer maxEntries) {
		this.maxEntries = maxEntries;
	}

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Integer getMessage() {
		return message;
	}

	public void setMessage(Integer message) {
		this.message = message;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public LotteryData getCost() {
		return cost;
	}

	public void setCost(LotteryData cost) {
		this.cost = cost;
	}

	public LotteryData getReward() {
		return reward;
	}

	public void setReward(LotteryData reward) {
		this.reward = reward;
	}
}