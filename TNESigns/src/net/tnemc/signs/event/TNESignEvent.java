package net.tnemc.signs.event;

import net.tnemc.signs.signs.TNESign;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 5/29/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
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