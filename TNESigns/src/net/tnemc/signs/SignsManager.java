package net.tnemc.signs;

import com.github.tnerevival.core.collection.EventMap;
import com.github.tnerevival.serializable.SerializableLocation;
import net.tnemc.signs.signs.TNESign;

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

  public SignsManager() {
    signs.setListener(new SignsListener());
  }

  public EventMap<SerializableLocation, TNESign> getSigns() {
    return signs;
  }

  public void setSigns(EventMap<SerializableLocation, TNESign> signs) {
    this.signs = signs;
  }

  public void saveSign(TNESign sign) {

  }
}