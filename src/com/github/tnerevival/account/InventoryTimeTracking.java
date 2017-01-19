/*
 * The New Economy Minecraft Server Plugin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.tnerevival.account;

import org.bukkit.event.inventory.InventoryType;

import java.util.UUID;

/**
 * Created by creatorfromhell on 1/19/2017.
 **/
public class InventoryTimeTracking {

  private UUID player;
  private long opened;
  private long closeTime;
  private boolean closed = false;
  private InventoryType type;

  public InventoryTimeTracking(UUID player) {
    this.player = player;
  }

  public UUID getPlayer() {
    return player;
  }

  public void setPlayer(UUID player) {
    this.player = player;
  }

  public long getOpened() {
    return opened;
  }

  public void setOpened(long opened) {
    this.opened = opened;
  }

  public long getCloseTime() {
    return closeTime;
  }

  public void setCloseTime(long closeTime) {
    this.closeTime = closeTime;
  }

  public boolean isClosed() {
    return closed;
  }

  public void setClosed(boolean closed) {
    this.closed = closed;
  }

  public InventoryType getType() {
    return type;
  }

  public void setType(InventoryType type) {
    this.type = type;
  }
}