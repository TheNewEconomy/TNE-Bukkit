package net.tnemc.signs;

import com.github.tnerevival.core.collection.EventMap;
import com.github.tnerevival.serializable.SerializableLocation;
import net.tnemc.signs.signs.SignType;
import net.tnemc.signs.signs.TNESign;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 5/28/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class SignsManager {
  private EventMap<SerializableLocation, TNESign> signs = new EventMap<>();
  private Map<String, SignType> signTypes = new HashMap<>();

  public SignsManager() {
    signs.setListener(new SignsListener());
  }

  public EventMap<SerializableLocation, TNESign> getSigns() {
    return signs;
  }

  public void setSigns(EventMap<SerializableLocation, TNESign> signs) {
    this.signs = signs;
  }

  public static boolean blockAttachedSign(final Block block) {

    return false;
  }

  public static boolean validSign(final Sign sign) {
    return false;
  }

  public void saveSign(TNESign sign) {

  }

  public TNESign loadSign(Location location) {
    return null;
  }

  public Collection<TNESign> loadSigns() {
    return null;
  }

  public void deleteSign(Location location) {

  }

  public Map<String, SignType> getSignTypes() {
    return signTypes;
  }

  public void setSignTypes(Map<String, SignType> signTypes) {
    this.signTypes = signTypes;
  }
}