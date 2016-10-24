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
package com.github.tnerevival.commands.eco;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by creatorfromhell on 10/24/2016.
 **/
public class EcoCleanCommand extends TNECommand {

  public EcoCleanCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "clean";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.eco.clean";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Player player = getPlayer(sender);
    player.sendMessage(ChatColor.WHITE + "Cleaning items from your inventory.");
    ItemStack[] contents = player.getInventory().getContents().clone();
    for(int i = 0; i < contents.length; i++) {
      if(contents[i] != null) {
        ItemStack cloneStack = contents[i].clone();
        ItemMeta meta = cloneStack.getItemMeta();
        List<String> newLore = new ArrayList<>();
        if(meta.getLore() != null) {
          for (String s : meta.getLore()) {
            if (!s.contains("Crafting Cost")) {
              newLore.add(s);
            }
          }
        }
        meta.setLore(newLore);
        cloneStack.setItemMeta(meta);
        contents[i] = cloneStack;
      }
    }
    player.getInventory().setContents(contents);
    return true;
  }

  @Override
  public String getHelp() {
    return "/eco clean - Fixes all items in the inventory that contain Crafting Cost in the lore.";
  }
}