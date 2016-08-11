package com.github.tnerevival.account;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Daniel on 8/10/2016.
 */
public class CreditsEntry {
  private Map<String, Long> credits = new HashMap<>();

  public CreditsEntry(Map<String, Long> credits) {
    this.credits = credits;
  }

  public Long getRemaining(String world) {
    if(!credits.containsKey(world)) {
      return 0L;
    }
    return credits.get(world);
  }

  public void addCredits(String world, Long amount) {
    credits.put(world, amount);
  }

  public void removeCredits(String world, Long amount) {
    credits.put(world, (credits.get(world) - amount));
  }

  public boolean hasCredits(String world) {
    return credits.containsKey(world) && credits.get(world) > 0L;
  }

  public Map<String, Long> getCredits() {
    return credits;
  }

  public void setCredits(Map<String, Long> credits) {
    this.credits = credits;
  }

  @Override
  public String toString() {
    Iterator<Map.Entry<String, Long>> worldIterator = credits.entrySet().iterator();
    StringBuilder builder = new StringBuilder();

    while(worldIterator.hasNext()) {
      if(builder.length() > 0) {
        builder.append(";");
      }
      Map.Entry<String, Long> worldEntry = worldIterator.next();
      builder.append(worldEntry.getKey() + ":" + worldEntry.getValue());
    }
    return builder.toString();
  }

  public static CreditsEntry fromString(String parse) {
    Map<String, Long> parsed = new HashMap<>();
    String[] values = parse.split(";");

    for(String s : values) {
      String[] data = s.split(":");
      parsed.put(data[0], Long.valueOf(data[1]));
    }
    return new CreditsEntry(parsed);
  }
}