package com.github.tnerevival.utils;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.signs.*;
import com.github.tnerevival.serializable.SerializableLocation;
import org.bukkit.Location;
import org.bukkit.block.Sign;

import java.util.UUID;

public class SignUtils {

  public static Boolean validSign(Location location) {
    SerializableLocation cerealLoc = new SerializableLocation(location);
    for(SerializableLocation loc : TNE.instance.manager.signs.keySet()) {
      if(loc.equals(cerealLoc)) {
        return true;
      }
    }
    return false;
  }

	public static Boolean validSign(Sign sign) {
		return sign.getLine(0).equalsIgnoreCase("[tne]");
	}
	
	public static SignType getType(Sign sign) {
		return SignType.fromLine(sign.getLine(1));
	}

	public static TNESign instance(String type, UUID owner) {
	  switch(type.toLowerCase()) {
      case "bank":
        return new BankSign(owner);
      case "shop":
        return new ShopSign(owner);
      case "sell":
        return new SellSign(owner);
      case "buy":
        return new BuySign(owner);
      default:
        return new BankSign(owner);
    }
  }
}