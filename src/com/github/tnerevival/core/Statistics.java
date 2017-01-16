package com.github.tnerevival.core;

import com.github.tnerevival.TNE;
import com.github.tnerevival.utils.MISCUtils;

public class Statistics {
  public static void send() {
    String ip = TNE.instance.getServer().getIp();
    int playerCount = TNE.instance.getServer().getOnlinePlayers().size();
    String version = TNE.instance.getDescription().getVersion();
    String URL = "https://creatorfromhell.com/tne/statistics.php?a=send&ip=" + ip + "&players=" + playerCount + "&lite=false" + "&version=" + version;
    MISCUtils.sendGetRequest(URL);
  }

  public static void kill() {
    String ip = TNE.instance.getServer().getIp();
    String URL = "http://creatorfromhell.com/tne/statistics.php?a=kill&ip=" + ip;
  }
}