package com.github.tnerevival.utils;

import org.bukkit.block.Sign;

import com.github.tnerevival.core.signs.SignType;

public class SignUtils {
	
	public Boolean validSign(Sign sign) {
		return sign.getLine(0).equalsIgnoreCase("[tne]");
	}
	
	public SignType getType(Sign sign) {
		return SignType.fromLine(sign.getLine(1));
	}
}