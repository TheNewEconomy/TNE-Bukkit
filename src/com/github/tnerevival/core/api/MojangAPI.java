package com.github.tnerevival.core.api;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.utils.MISCUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.*;

public class MojangAPI {

  /*public static UUID getPlayerUUID(String name) {
    if(TNE.uuidCache.containsKey(name)) {
      return TNE.uuidCache.get(name);
    }
    JSONObject object = send("users/profiles/minecraft/" + name);
    UUID id = (object != null && object.containsKey("id")) ? UUID.fromString(MISCUtils.dashUUID((String) object.get("id"))) : null;

    if(id != null) {
      TNE.uuidCache.put(name, id);
    }

    return id;
  }*/

  public static UUID getPlayerUUID3rd(String name) {
    if(TNE.uuidCache.containsKey(name)) {
      return TNE.uuidCache.get(name);
    }
    JSONObject object = send("user/" + name);
    UUID id = (object != null && object.containsKey("id")) ? UUID.fromString(object.get("uuid_formatted").toString()) : IDFinder.ecoID(name, true);

    if(id != null) {
      TNE.uuidCache.put(name, id);
    }
    return id;
  }

  public static Map<String, UUID> convertIDS(List<String> usernames) {
    Map<String, UUID> ids = new HashMap<>();
    int limit = 600;
    List<String> query = new ArrayList<>();
    for(int i = 0; i < usernames.size(); i++) {
      query.add(usernames.get(i));
      if(limit == 1) {
        ids.putAll(MojangAPI.bulkUUID(query));
        query = new ArrayList<>();
        limit = 601;
      }
      limit--;
    }

    for(Map.Entry<String, UUID> entry : ids.entrySet()) {
      if(entry.getValue() == null) {
        UUID id = IDFinder.ecoID(entry.getKey(), true);
        ids.put(entry.getKey(), id);
        TNE.uuidCache.put(entry.getKey(), id);
      }
    }
    return ids;
  }

  public static Map<String, UUID> bulkUUID(List<String> usernames) {
    Map<String, UUID> toReturn = new HashMap<>();
    for(String s : usernames) {
      toReturn.put(s, null);
    }

    JSONArray jsonArray = new JSONArray();
    jsonArray.addAll(usernames);
    JSONObject[] array = MISCUtils.sendPostRequest("https://api.mojang.com/profiles/minecraft", jsonArray.toJSONString());

    for(JSONObject object : array) {
      if(object != null) {
        String name = object.get("name").toString();
        String id = MISCUtils.dashUUID(object.get("id").toString());
        if(toReturn.containsKey(name)) {
          toReturn.put(name, UUID.fromString(id));
        }

        TNE.uuidCache.put(name, UUID.fromString(id));
      }
    }
    return toReturn;
  }

  private static JSONObject send(String url) {
    return (JSONObject) JSONValue.parse(MISCUtils.sendGetRequest("http://mcapi.de/api/" + url));
  }
}