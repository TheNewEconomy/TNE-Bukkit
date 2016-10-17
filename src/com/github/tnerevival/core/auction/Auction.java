package com.github.tnerevival.core.auction;

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.transaction.TransactionCost;
import com.github.tnerevival.serializable.SerializableItemStack;

import java.util.UUID;

/**
 * Created by Daniel on 10/17/2016.
 */
public class Auction {

  private Integer lotNumber;
  private long added = System.nanoTime();
  private UUID player;
  private String world = TNE.instance.defaultWorld;
  private Boolean silent;
  private SerializableItemStack item;
  private TransactionCost cost = new TransactionCost(50.00);
  private Bid highestBid;
  private Double start = 50.00;
  private Double increment = 10.00;
  private Boolean admin = false;
  private Boolean global = true;
  private Integer time = 30;
  private String node = "";

  public Auction(Integer lotNumber) {
    this.lotNumber = lotNumber;
  }

  public Auction(UUID player, String world) {
    this.player = player;
    this.world = world;
  }

  public Integer getLotNumber() {
    return lotNumber;
  }

  public void setLotNumber(Integer lotNumber) {
    this.lotNumber = lotNumber;
  }

  public long getAdded() {
    return added;
  }

  public void setAdded(long added) {
    this.added = added;
  }

  public UUID getPlayer() {
    return player;
  }

  public void setPlayer(UUID player) {
    this.player = player;
  }

  public String getWorld() {
    return world;
  }

  public void setWorld(String world) {
    this.world = world;
  }

  public Boolean getSilent() {
    return silent;
  }

  public void setSilent(Boolean silent) {
    this.silent = silent;
  }

  public SerializableItemStack getItem() {
    return item;
  }

  public void setItem(SerializableItemStack item) {
    this.item = item;
  }

  public TransactionCost getCost() {
    return cost;
  }

  public void setCost(TransactionCost cost) {
    this.cost = cost;
  }

  public Bid getHighestBid() {
    return highestBid;
  }

  public void setHighestBid(Bid highestBid) {
    this.highestBid = highestBid;
  }

  public Double getStart() {
    return start;
  }

  public void setStart(Double start) {
    this.start = start;
  }

  public Double getIncrement() {
    return increment;
  }

  public void setIncrement(Double increment) {
    this.increment = increment;
  }

  public Boolean getAdmin() {
    return admin;
  }

  public void setAdmin(Boolean admin) {
    this.admin = admin;
  }

  public Boolean getGlobal() {
    return global;
  }

  public void setGlobal(Boolean global) {
    this.global = global;
  }

  public Integer getTime() {
    return time;
  }

  public void setTime(Integer time) {
    this.time = time;
  }

  public String getNode() {
    return node;
  }

  public void setNode(String node) {
    this.node = node;
  }
}