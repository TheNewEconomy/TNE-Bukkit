package com.github.tnerevival.utils;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.signs.*;
import com.github.tnerevival.serializable.SerializableLocation;
import org.bukkit.Location;

import java.util.Iterator;
import java.util.Map;
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

  public static void removeSign(SerializableLocation location) {
    Iterator<Map.Entry<SerializableLocation, TNESign>> i = TNE.instance.manager.signs.entrySet().iterator();

    while(i.hasNext()) {
      Map.Entry<SerializableLocation, TNESign> e = i.next();

      if(e.getKey().equals(location)) {
        i.remove();
      }
    }
  }

  public static TNESign getSign(SerializableLocation location) {
    Iterator<Map.Entry<SerializableLocation, TNESign>> i = TNE.instance.manager.signs.entrySet().iterator();

    while(i.hasNext()) {
      Map.Entry<SerializableLocation, TNESign> e = i.next();

      if(e.getKey().equals(location)) {
        return e.getValue();
      }
    }
    return null;
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
        MISCUtils.debug("defaulting...");
        return new BankSign(owner);
    }
  }
}