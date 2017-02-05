package com.github.tnerevival.core.objects;

import java.math.BigDecimal;

public class TNEAccessPackage {

  private String name;
  private long time;
  private BigDecimal cost;

  public TNEAccessPackage(String name, long time, BigDecimal cost) {
    this.name = name;
    this.time = time;
    this.cost = cost;
  }

  public String getName() {
    return name;
  }

  public long getTime() {
    return time;
  }

  public BigDecimal getCost() {
    return cost;
  }
}