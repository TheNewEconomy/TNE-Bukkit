
package net.tnemc.core.common.api;

import com.github.tnerevival.TNELib;
import com.github.tnerevival.core.api.MojangAPI;
import net.tnemc.core.TNE;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by creatorfromhell on 11/6/2016.
 **/
public class IDFinder {

  public static UUID ecoID(String username) {
    return ecoID(username, false);
  }

  public static UUID ecoID(String username, boolean skip) {
    if(TNE.uuidManager().hasID(username)) {
      TNELib.debug("TNE.uuidManager().hasID(username)");
      return TNE.uuidManager().getID(username);
    }
    UUID eco = (skip)? genUUID() : genUUID(username);
    TNELib.debug("Eco: " + eco.toString());
    //TNELib.instance().getUuidManager().addUUID(username, eco);
    return eco;
  }

  public static String getUsername(String identifier) {
    if(isUUID(identifier)) {
      if(IDFinder.getID(TNE.instance().consoleName) != null && identifier.equalsIgnoreCase(IDFinder.getID(TNE.instance().consoleName).toString())) {
        return TNE.instance().consoleName;
      }
      final UUID id = UUID.fromString(identifier);
      final OfflinePlayer player = Bukkit.getOfflinePlayer(id);
      if(player == null) {
        return MojangAPI.getPlayerUsername(id);
      }
      return player.getName();
    }
    return identifier;
  }

  public static UUID genUUID(String name) {
    OfflinePlayer player = Bukkit.getOfflinePlayer(name);
    if(player.hasPlayedBefore()) {
      TNELib.debug("genUUID: OfflinePlayer");
      final UUID id = player.getUniqueId();
      if(id != null) return id;
    }

    UUID id = MojangAPI.getPlayerUUID(name);
    if(id != null) {
      TNELib.debug("genUUID: Mojang");
      return id;
    }

    TNELib.debug("genUUID: gen");
    return genUUID();
  }

  public static UUID genUUID() {
    UUID id = UUID.randomUUID();
    while(TNE.uuidManager().containsUUID(id)) {
      //This should never happen, but we'll play it safe
      id = UUID.randomUUID();
    }
    return id;
  }

  public static String ecoToUsername(UUID id) {
    return (TNELib.instance().getUuidManager().containsUUID(id))? TNE.uuidManager().getUsername(id) : getUsername(id.toString());
  }

  public static UUID getID(CommandSender sender) {
    if(!(sender instanceof Player)) {
      return getID(TNELib.instance().consoleName);
    }
    return getID((Player)sender);
  }

  public static UUID getID(Player player) {
    return getID(player.getName());
  }

  public static UUID getID(OfflinePlayer player) {
    if(!TNELib.instance().useUUID) {
      return ecoID(player.getName());
    }
    return player.getUniqueId();
  }

  public static Player getPlayer(String identifier) {
    final UUID id = getID(identifier);
    if(!TNELib.instance().useUUID) {
      return Bukkit.getPlayer(IDFinder.ecoToUsername(id));
    }
    if(!Bukkit.getServer().getOnlineMode()) {
      return Bukkit.getPlayer(IDFinder.ecoToUsername(id));
    }
    return Bukkit.getPlayer(id);
  }

  public static OfflinePlayer getOffline(String identifier, boolean username) {
    if(username) return Bukkit.getOfflinePlayer(identifier);
    UUID id = getID(identifier);

    return Bukkit.getOfflinePlayer(id);
  }

  public static OfflinePlayer getOffline(UUID id) {
    return Bukkit.getOfflinePlayer(id);
  }

  public static UUID getID(String identifier) {
    identifier = ChatColor.stripColor(identifier.replaceAll("\\[.*?\\] ?", "")).trim();
    TNELib.debug("GETID: " + identifier);
    if(isUUID(identifier)) {
      return UUID.fromString(identifier);
    }

    if(identifier.contains("discord-")) {
      TNELib.debug("Discord Economy");
      UUID id = ecoID(identifier);
      checkSpecial(id);
      return id;
    }

    if(identifier.contains(TNELib.instance().factionPrefix)) {
      TNELib.debug("Faction");
      UUID id = ecoID(identifier);
      checkSpecial(id);
      return id;
    }

    if(identifier.contains("towny-war-chest")) {
      TNELib.debug("Towny War Chest");
      UUID id = ecoID(identifier);
      checkSpecial(id);
      return id;
    }

    if(identifier.contains(TNELib.instance().townPrefix)) {
      TNELib.debug("Towny Town");
      UUID id = ecoID(identifier);
      checkSpecial(id);
      return id;
    }

    if(identifier.contains(TNELib.instance().nationPrefix)) {
      TNELib.debug("Towny Nation");
      UUID id = ecoID(identifier);
      checkSpecial(id);
      return id;
    }

    if(identifier.contains("kingdom-")) {
      TNELib.debug("Kingdom");
      UUID id = ecoID(identifier);
      checkSpecial(id);
      return id;
    }

    if(identifier.contains("village-")) {
      TNELib.debug("Village");
      UUID id = ecoID(identifier);
      checkSpecial(id);
      return id;
    }

    if(!TNELib.instance().useUUID) {
      TNELib.debug("ECO ID RETURNED");
      return ecoID(identifier);
    }

    TNELib.debug("MOJANG API TIME");
    UUID mojangID = (identifier.equalsIgnoreCase(TNELib.instance().consoleName))? null : Bukkit.getOfflinePlayer(identifier).getUniqueId();
    if(mojangID == null) {
      TNELib.debug("MOJANG API RETURNED NULL VALUE");
      return ecoID(identifier);
    }
    //TNELib.instance().getUuidManager().addUUID(identifier, mojangID);
    return mojangID;
  }

  private static void checkSpecial(UUID id) {
    if(!TNELib.instance().special.contains(id)) {
      TNELib.instance().special.add(id);
    }
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