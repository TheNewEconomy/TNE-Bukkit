package com.github.tnerevival.core;

import com.github.tnerevival.TNE;
import com.github.tnerevival.utils.MISCUtils;

public class Statistics {
	public static void send() {
		int playerCount = TNE.instance.getServer().getOnlinePlayers().size();
		String version = TNE.instance.getDescription().getVersion();
		String URL = "https://creatorfromhell.com/tne/statistics.php?a=send&players=" + playerCount + "&lite=false" + "&version=" + version;
		MISCUtils.sendGetRequest(URL);
	}
}