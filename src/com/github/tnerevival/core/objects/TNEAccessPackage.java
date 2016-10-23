package com.github.tnerevival.core.objects;

public class TNEAccessPackage {

  private String name;
  private long time;
  private double cost;

  public TNEAccessPackage(String name, long time, double cost) {
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

  public double getCost() {
    return cost;
  }
}