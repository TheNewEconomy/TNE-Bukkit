package net.tnemc.core.common;

import com.github.tnerevival.core.UUIDManager;
import com.github.tnerevival.core.collection.EventMap;
import net.tnemc.core.TNE;
import net.tnemc.core.listeners.collections.IDListener;

import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 11/28/2017.
 */
public class TNEUUIDManager extends UUIDManager {

  private EventMap<String, UUID> uuids = new EventMap<>();

  public TNEUUIDManager() {
    uuids.setListener(new IDListener());
    TNE.instance().registerEventMap(uuids);
  }


  @Override
  public boolean hasUsername(UUID uuid) {
    return uuids.containsValue(uuid);
  }

  @Override
  public boolean hasID(String username) {
    return uuids.containsKey(username);
  }

  @Override
  public boolean containsUUID(UUID uuid) {
    return uuids.containsValue(uuid);
  }

  @Override
  public void addUUID(String username, UUID uuid) {
    TNE.debug("TNEUUIDManager.addUUID(username: " + username + ", uuid: " + uuid.toString() + ")");
    uuids.put(username, uuid);
  }

  @Override
  public UUID getID(String username) {
    return uuids.get(username);
  }

  @Override
  public String getIdentifier(String username) {
    return uuids.get(username).toString();
  }

  @Override
  public String getUsername(UUID uuid) {
    for(Map.Entry<String, UUID> entry : uuids.entrySet()) {
      if(entry.getValue().toString().equalsIgnoreCase(uuid.toString())) return entry.getKey();
    }
    return null;
  }

  @Override
  public String getUsername(String identifier) {
    for(Map.Entry<String, UUID> entry : uuids.entrySet()) {
      if(entry.getValue().toString().equalsIgnoreCase(identifier)) return entry.getKey();
    }
    return null;
  }

  @Override
  public void remove(String username) {
    uuids.remove(username);
  }

  public EventMap<String, UUID> getUuids() {
    return uuids;
  }

  public void addAll(Map<String, UUID> toAdd) {
    uuids.putAll(toAdd);
  }
}
