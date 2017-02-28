package com.github.tnerevival.core;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class Message {

  public static final Map<String, String> colours;
  static {
    colours = new HashMap<>();
    //Colour Characters
    colours.put("<aqua>", ChatColor.AQUA.toString());
    colours.put("<black>", ChatColor.BLACK.toString());
    colours.put("<blue>", ChatColor.BLUE.toString());
    colours.put("<dark_aqua>", ChatColor.DARK_AQUA.toString());
    colours.put("<dark_blue>", ChatColor.DARK_BLUE.toString());
    colours.put("<dark_gray>", ChatColor.DARK_GRAY.toString());
    colours.put("<dark_grey>", ChatColor.DARK_GRAY.toString());
    colours.put("<dark_green>", ChatColor.DARK_GREEN.toString());
    colours.put("<dark_purple>", ChatColor.DARK_PURPLE.toString());
    colours.put("<dark_red>", ChatColor.DARK_RED.toString());
    colours.put("<gold>", ChatColor.GOLD.toString());
    colours.put("<gray>", ChatColor.GRAY.toString());
    colours.put("<grey>", ChatColor.GRAY.toString());
    colours.put("<green>", ChatColor.GREEN.toString());
    colours.put("<purple>", ChatColor.LIGHT_PURPLE.toString());
    colours.put("<red>", ChatColor.RED.toString());
    colours.put("<white>", ChatColor.WHITE.toString());
    colours.put("<yellow>", ChatColor.YELLOW.toString());

    //Special Characters
    colours.put("<magic>", ChatColor.MAGIC.toString());
    colours.put("<bold>", ChatColor.BOLD.toString());
    colours.put("<strike>", ChatColor.STRIKETHROUGH.toString());
    colours.put("<underline>", ChatColor.UNDERLINE.toString());
    colours.put("<italic>", ChatColor.ITALIC.toString());
    colours.put("<reset>", ChatColor.RESET.toString());
  }

  private HashMap<String, String> variables = new HashMap<String, String>();
  private String node;

  public Message(String node) {
    MISCUtils.debug(node);
    this.node = node;
  }

  public void addVariable(String variable, String replacement) {
    variables.put(variable, replacement);
  }

  private String replaceColours(String message, boolean strip) {
    Iterator<java.util.Map.Entry<String, String>> it = colours.entrySet().iterator();

    while(it.hasNext()) {
      java.util.Map.Entry<String, String> entry = it.next();
      String replacement = (strip)? "" : entry.getValue();
      message = message.replace(entry.getKey(), replacement);
    }
    if(strip) {
      return ChatColor.stripColor(message);
    }
    return ChatColor.translateAlternateColorCodes('&', message);
  }

  public void translate(String world, UUID id) {
    translate(world, IDFinder.getPlayer(id.toString()));
  }

  public String grab(String world, CommandSender sender) {
    String id = (sender instanceof Player)? IDFinder.getID((Player)sender).toString() : "";
    String found = TNE.instance().api().getString(this.node, world, id);

    String message = (found == null)? this.node : found;
    Iterator<java.util.Map.Entry<String, String>> it = variables.entrySet().iterator();

    while (it.hasNext()) {
      java.util.Map.Entry<String, String> entry = it.next();
      message = message.replace(entry.getKey(), entry.getValue());
    }
    return message;
  }

  public void translate(String world, CommandSender sender) {
    if(sender == null) return;
    String id = (sender instanceof Player)? IDFinder.getID((Player)sender).toString() : "";
    String found = TNE.instance().api().getString(this.node, world, id);

    String[] message = (found == null)? new String[] { this.node } : found.split("<newline>");
    for(String s : message) {
      String send = s;
            MISCUtils.debug(send);
      if (!send.equals(this.node)) {
        Iterator<java.util.Map.Entry<String, String>> it = variables.entrySet().iterator();

        while (it.hasNext()) {
          java.util.Map.Entry<String, String> entry = it.next();
          send = send.replace(entry.getKey(), entry.getValue());
        }
      }
      Boolean strip = !(sender instanceof Player);
            sender.sendMessage(replaceColours(send, strip));
    }
  }
}