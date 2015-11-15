package com.github.tnerevival.core.api;

import java.util.UUID;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.github.tnerevival.TNE;
import com.github.tnerevival.utils.MISCUtils;

public class MojangAPI {
	
	private static Pattern dashUUID = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");
	
	public static UUID getPlayerUUID(String name) {
		if(TNE.uuidCache.containsKey(name)) {
			return TNE.uuidCache.get(name);
		}
		JSONObject object = send("users/profiles/minecraft/" + name);
		UUID id = (object.containsKey("id")) ? UUID.fromString(dashUUID.matcher(((String) object.get("id"))).replaceAll("$1-$2-$3-$4-$5")) : null;
		
		if(id != null) {
			TNE.uuidCache.put(name, id);
		}
		
		return id;
	}
	
	private static JSONObject send(String url) {
		return (JSONObject) JSONValue.parse(MISCUtils.sendGetRequest("https://api.mojang.com/" + url));
	}
}