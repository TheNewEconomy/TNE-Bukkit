package com.github.tnerevival.core.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class MojangAPI {
	
	private static Pattern dashUUID = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");
	
	public static UUID getPlayerUUID(String name) {
		JSONObject object = send("users/profiles/minecraft/" + name);
		return (object.containsKey("id")) ? UUID.fromString(dashUUID.matcher(((String) object.get("id"))).replaceAll("$1-$2-$3-$4-$5")) : null;
	}
	
	private static JSONObject send(String url) {
		String URL = "https://api.mojang.com/" + url;
		StringBuilder builder = new StringBuilder();
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(URL).openConnection();
			connection.setRequestMethod("GET");
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String response;
			while((response = reader.readLine()) != null) {
				builder.append(response);
			}
			reader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (JSONObject) JSONValue.parse(builder.toString());
	}
}