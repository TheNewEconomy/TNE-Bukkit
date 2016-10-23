package com.github.tnerevival.core.signs;

import java.util.UUID;

import org.bukkit.entity.Player;

public class BuySign extends TNESign {

  public BuySign(UUID owner) {
    super(owner);
    setType(SignType.BUY);
  }

  @Override
  public boolean onClick(Player player) {
    return false;
  }

  @Override
  public boolean onRightClick(Player player) {
    return false;
  }
}