package com.github.tnerevival.core.event.sign;

import com.github.tnerevival.core.signs.TNESign;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class TNESignEvent extends Event implements Cancellable {
  private static final HandlerList handlers = new HandlerList();
    
  private UUID playerID;
  private TNESign sign;
  private SignEventAction action;
  private Boolean cancelled = false;
 
  public TNESignEvent(UUID playerID, TNESign sign, SignEventAction action) {
    this.playerID = playerID;
    this.sign = sign;
    this.setAction(action);
  }
 
  public UUID getPlayerID() {
    return playerID;
  }

  public void setPlayerID(UUID playerID) {
    this.playerID = playerID;
  }

  public TNESign getSign() {
    return sign;
  }

  public void setSign(TNESign sign) {
    this.sign = sign;
  }

  public SignEventAction getAction() {
    return action;
  }

  public void setAction(SignEventAction action) {
    this.action = action;
  }

  public HandlerList getHandlers() {
        return handlers;
    }
 
  public static HandlerList getHandlerList() {
        return handlers;
    }

  public boolean isCancelled() {
        return cancelled;
    }
 
  public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}