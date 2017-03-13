package com.github.tnerevival.core.signs;

import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * Created by creatorfromhell on 3/6/2017.
 * All rights reserved.
 **/
public class SignChest {

  private Location location = null;
  private boolean ender = false;

  public SignChest(Block block) {
    location = block.getLocation();
    ender = MISCUtils.isOneEight() && block.getState() instanceof org.bukkit.block.EnderChest;
  }

  public SignChest() {
    ender = true;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public boolean isEnder() {
    return ender;
  }

  public void setEnder(boolean ender) {
    this.ender = ender;
  }
}