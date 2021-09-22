package net.tnemc.core.listeners.player;

import net.tnemc.core.TNE;
import net.tnemc.core.menu.ResponseData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 8/8/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class PlayerChatListener implements Listener {

  TNE plugin;

  public PlayerChatListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onChat(AsyncPlayerChatEvent event) {
    final UUID id = event.getPlayer().getUniqueId();
    if(TNE.menuManager().response.containsKey(id)) {
      final ResponseData responseData = TNE.menuManager().response.get(id);
      TNE.menuManager().setViewerData(id, responseData.getChat(), event.getMessage());
      TNE.menuManager().open(responseData.getMenu(), event.getPlayer());
      TNE.menuManager().response.remove(id);
      event.setCancelled(true);
      return;
    }

    if(event.getMessage().length() > 0) {
      List<String> triggers = new ArrayList<>(Arrays.asList(TNE.instance().api().getString("Core.Commands.Triggers", event.getPlayer().getWorld().getName(), event.getPlayer().getUniqueId()).split(",")));

      if (triggers.contains(event.getMessage().charAt(0) + "")) {
        String[] parsed = event.getMessage().split(" ");
        String[] arguments = (parsed.length > 1) ? Arrays.copyOfRange(parsed, 1, parsed.length) : new String[0];
        TNE.instance().customCommand(event.getPlayer(), parsed[0].substring(1), arguments);
        event.setCancelled(true);
      }
    }
  }
}