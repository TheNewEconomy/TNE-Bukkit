package net.tnemc.signs;

import net.tnemc.signs.impl.TNESign;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 2/26/2018.
 */
public class SignAccount {
  private Map<Location, TNESign> signs = new HashMap<>();

  private UUID identifier;

  public SignAccount(UUID identifier) {
    this.identifier = identifier;
  }

  public Map<Location, TNESign> getSigns() {
    return signs;
  }

  public void setSigns(Map<Location, TNESign> signs) {
    this.signs = signs;
  }

  public UUID getIdentifier() {
    return identifier;
  }

  public void setIdentifier(UUID identifier) {
    this.identifier = identifier;
  }
}