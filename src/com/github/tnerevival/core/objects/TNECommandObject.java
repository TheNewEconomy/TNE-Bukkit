package com.github.tnerevival.core.objects;

import java.math.BigDecimal;
import java.util.HashMap;

public class TNECommandObject {

  private HashMap<String, TNECommandObject> subCommands = new HashMap<String, TNECommandObject>();

  private String name;
  private BigDecimal cost;

  public TNECommandObject(String name, BigDecimal cost) {
    this.name = name;
    this.cost = cost;
  }

  public String getIdentifier() {
    return name;
  }

  public BigDecimal getCost() {
    return cost;
  }

  public TNECommandObject findSub(String name) {
    return subCommands.get(name);
  }

  public BigDecimal subCost(String name) {
    if(findSub(name) != null) {
      return subCommands.get(name).getCost();
    }
    return BigDecimal.ZERO;
  }

  public void addSub(TNECommandObject sub) {
    subCommands.put(sub.getIdentifier(), sub);
  }
}