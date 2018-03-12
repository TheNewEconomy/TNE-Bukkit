package net.tnemc.signs.event;

import net.tnemc.signs.impl.TNESign;
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
    this.action = action;
  }
 
  public UUID getPlayerID() {
    return playerID;
  }

  public TNESign getSign() {
    return sign;
  }

  public SignEventAction getAction() {
    return action;
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