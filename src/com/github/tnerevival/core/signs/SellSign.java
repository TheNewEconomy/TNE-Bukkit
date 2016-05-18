package com.github.tnerevival.core.signs;

import java.util.UUID;

import org.bukkit.entity.Player;

public class SellSign extends TNESign {

	public SellSign(UUID owner) {
		super(owner);
		setType(SignType.SELL);
	}

	@Override
	public boolean onClick(Player player) {
		return false;
	}

	@Override
	public boolean onRightClick(Player player) {
		return false;
	}
}