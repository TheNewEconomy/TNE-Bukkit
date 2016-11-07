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

import com.github.tnerevival.TNE;
import com.github.tnerevival.core.api.MojangAPI;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by creatorfromhell on 11/6/2016.
 **/
public class IDFinder {



  public static UUID ecoID(String username) {
    if(TNE.instance.manager.ecoIDs.containsKey(username)) {
      return TNE.instance.manager.ecoIDs.get(username);
    }
    UUID eco = genUUID(username);
    TNE.instance.manager.ecoIDs.put(username, eco);
    return eco;
  }

  public static UUID genUUID(String name) {
    UUID id = MojangAPI.getPlayerUUID(name);
    if(id != null) return id;

    return genUUID();
  }

  public static UUID genUUID() {
    UUID id = UUID.randomUUID();
    while(TNE.instance.manager.accounts.containsKey(id) || TNE.instance.manager.ecoIDs.containsValue(id)) {
      //This should never happen, but we'll play it safe
      id = UUID.randomUUID();
    }
    return id;
  }

  public static String ecoToUsername(UUID id) {
    return (String) MISCUtils.getKey(TNE.instance.manager.ecoIDs, id);
  }

  public static UUID getID(Player player) {
    return getID(player.getDisplayName());
  }

  public static UUID getID(OfflinePlayer player) {
    if(!TNE.instance.api.getBoolean("Core.UUID")) {
      return ecoID(player.getName());
    }
    return player.getUniqueId();
  }

  public static Player getPlayer(String identifier) {
    UUID id = (getID(identifier));
    if(!TNE.instance.api.getBoolean("Core.UUID")) {
      return Bukkit.getPlayer(IDFinder.ecoToUsername(id));
    }
    return Bukkit.getPlayer(id);
  }

  public static UUID getID(String identifier) {
    identifier = ChatColor.stripColor(identifier);
    MISCUtils.debug("ID IDENTIFIER: " + identifier);
    if(isUUID(identifier)) {
      MISCUtils.debug("ID RETURNED");
      return UUID.fromString(identifier);
    }

    if(identifier.contains("town-")) {
      return ecoID(identifier);
    }

    if(identifier.contains("nation-")) {
      return ecoID(identifier);
    }

    if(!TNE.instance.api.getBoolean("Core.UUID")) {
      MISCUtils.debug("ECO ID RETURNED");
      return ecoID(identifier);
    }
    Player p = Bukkit.getPlayer(identifier);
    if(p != null) {
      MISCUtils.debug("Player ID RETURNED");
      return p.getUniqueId();
    }

    UUID mojangID = MojangAPI.getPlayerUUID(identifier);
    if(mojangID == null) {
      MISCUtils.debug("ECO2 ID RETURNED");
      return ecoID(identifier);
    }
    MISCUtils.debug("Mojang ID RETURNED");
    return mojangID;
  }

  public static boolean isUUID(String lookup) {
    try {
      UUID.fromString(lookup);
      return true;
    } catch (Exception ex) {
      return false;
    }
  }
}