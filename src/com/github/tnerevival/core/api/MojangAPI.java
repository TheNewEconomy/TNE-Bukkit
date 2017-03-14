package com.github.tnerevival.core.api;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.utils.MISCUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.Map;
import java.util.UUID;

public class MojangAPI {

  public static UUID getPlayerUUID(String name) {
    if(TNE.uuidCache.containsKey(name)) {
      return TNE.uuidCache.get(name);
    }
    JSONObject object = send("https://api.mojang.com/users/profiles/minecraft/" + name);
    UUID id = (object != null && object.containsKey("id")) ? UUID.fromString(MISCUtils.dashUUID(object.get("id").toString())) : IDFinder.ecoID(name, true);

    if(id != null) {
      TNE.uuidCache.put(name, id);
    }

    return id;
  }

  public static String getPlayerUsername(UUID id) {
    if(TNE.uuidCache.containsValue(id)) {
      for(Map.Entry<String, UUID> entry : TNE.uuidCache.entrySet()) {
        if(entry.getValue().equals(id)) {
          return entry.getKey();
        }
      }
    }
    JSONObject object = send("https://sessionserver.mojang.com/session/minecraft/profile/" + id.toString().replace("-", ""));
    String name = object.get("name").toString();

    if(name != null) {
      TNE.uuidCache.put(name, id);
    }
    return name;
  }

  private static JSONObject send(String url) {
    return (JSONObject) JSONValue.parse(MISCUtils.sendGetRequest(url));
  }
}