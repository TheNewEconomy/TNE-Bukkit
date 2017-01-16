package com.github.tnerevival.core.api;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.utils.MISCUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

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

  private static JSONObject send(String url) {
    return (JSONObject) JSONValue.parse(MISCUtils.sendGetRequest(url));
  }
}