package com.github.tnerevival.core.shops;

import java.util.UUID;

/**
 * Created by creatorfromhell on 3/15/2017.
 * All rights reserved.
 **/
public class ShopPermission {
  private UUID id;
  private boolean blacklisted = false;
  private boolean whitelisted = false;

  public ShopPermission(UUID id) {
    this.id = id;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public boolean isBlacklisted() {
    return blacklisted;
  }

  public void setBlacklisted(boolean blacklisted) {
    this.blacklisted = blacklisted;
  }

  public boolean isWhitelisted() {
    return whitelisted;
  }

  public void setWhitelisted(boolean whitelisted) {
    this.whitelisted = whitelisted;
  }
}