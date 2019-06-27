package net.tnemc.signs.command;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/27/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class NoteCommandCommand extends TNECommand {

  public NoteCommandCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "command";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.note.command";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String getHelp() {
    return "/note command <command> - Creates a note that will run a specific command on right click. Use $player to include player usernames in the command.";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {

    if(arguments.length < 1) {
      help(sender);
      return false;
    }

    ItemStack stack = new ItemStack(Material.PAPER, 1);

    ItemMeta meta = stack.getItemMeta();

    List<String> lore = (meta.hasLore())? meta.getLore() : new ArrayList<>();
    lore.add(ChatColor.WHITE + "Command: " + String.join(" ", arguments));
    lore.add(ChatColor.GREEN + "Right click to run command.");

    meta.setDisplayName("Command Note");
    meta.setLore(lore);
    stack.setItemMeta(meta);

    Map<Integer, ItemStack> left = getPlayer(sender).getInventory().addItem(stack);

    if(left.size() > 0) {
      getPlayer(sender).sendMessage(ChatColor.WHITE + "Your command note has been dropped on the floor since your inventory was full.");
    } else {
      getPlayer(sender).sendMessage(ChatColor.WHITE + "Your command note has been added to your inventory.");
    }
    return true;
  }
}