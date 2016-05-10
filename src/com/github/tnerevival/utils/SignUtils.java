package com.github.tnerevival.utils;

import org.bukkit.block.Sign;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.signs.BankSign;
import com.github.tnerevival.core.signs.SignType;
import com.github.tnerevival.serializable.SerializableLocation;

public class SignUtils {
	
	public Boolean validSign(Sign sign) {
		return sign.getLine(0).equalsIgnoreCase("[tne]");
	}
	
	public SignType getType(Sign sign) {
		return SignType.fromLine(sign.getLine(1));
	}
	
	public BankSign getBankSign(SerializableLocation location) {
		return (BankSign) TNE.instance.manager.signs.get(location);
	}
}