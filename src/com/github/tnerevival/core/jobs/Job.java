package com.github.tnerevival.core.jobs;

import org.bukkit.entity.Player;

public abstract class Job {
	public abstract String name();
	public abstract int pay();
	public abstract int totalPromotions();
	public abstract Player jobLeader();
}
