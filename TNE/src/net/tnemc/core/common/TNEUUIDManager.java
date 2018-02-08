package net.tnemc.core.common;

import com.github.tnerevival.core.UUIDManager;
import com.github.tnerevival.core.collection.EventMap;
import com.github.tnerevival.core.utils.Utilities;
import net.tnemc.core.TNE;
import net.tnemc.core.listeners.collections.IDListener;

import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
    return (String)Utilities.getKey(uuids, uuid);
  }

  @Override
  public String getUsername(String identifier) {
    return (String)Utilities.getKey(uuids, UUID.fromString(identifier));
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
