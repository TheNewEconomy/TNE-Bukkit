package net.tnemc.signs;

import net.tnemc.signs.impl.TNESign;
import org.bukkit.Location;

import java.util.*;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 2/13/2018.
 */
public class SignsManager {

  private Map<UUID, SignAccount> signProfiles = new HashMap<>();

  private Map<UUID, String> signStep = new HashMap<>();

  private static List<String> validTypes = new ArrayList<>();

  static {
    validTypes.add("item");
    validTypes.add("safe");
  }

  public SignsManager() {

  }

  public TNESign getSign(Location location) {

    Iterator<Map.Entry<UUID, SignAccount>> it = signProfiles.entrySet().iterator();
    while(it.hasNext()) {
      Map.Entry<UUID, SignAccount> entry = it.next();

      if(entry.getValue().getSigns().containsKey(location)) {
        return entry.getValue().getSigns().get(location);
      }
    }
    return null;
  }

  public static String getSignType(String identifier) {
    if (identifier.toLowerCase().contains("[tne:") && identifier.contains("]")) {
      String stripped = identifier.substring(identifier.indexOf("[") + 1, identifier.indexOf("]"));
      String[] match = stripped.split(":");
      if(validTypes.contains(match[1].toLowerCase())) return match[1].toLowerCase();
    }
    return "invalid";
  }

  public void addSignStep(UUID player, String stepID) {
    signStep.put(player, stepID);
  }

  public void removeSignStep(UUID player) {
    signStep.remove(player);
  }

  public String getStep(UUID player) {
    return signStep.get(player);
  }

  public boolean isAwaitingStep(UUID player) {
    return signStep.containsKey(player);
  }

  public Map<UUID, String> getSignCreation() {
    return signStep;
  }

  public void setSignCreation(Map<UUID, String> signStep) {
    this.signStep = signStep;
  }

  public static List<String> getValidTypes() {
    return validTypes;
  }

  public static void setValidTypes(List<String> validTypes) {
    SignsManager.validTypes = validTypes;
  }
}