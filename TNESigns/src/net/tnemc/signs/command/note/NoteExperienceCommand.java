package net.tnemc.signs.command.note;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.currency.Experience;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
public class NoteExperienceCommand extends TNECommand {

  public NoteExperienceCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String name() {
    return "experience";
  }

  @Override
  public String[] aliases() {
    return new String[0];
  }

  @Override
  public String node() {
    return "tne.note.experience";
  }

  @Override
  public boolean console() {
    return false;
  }

  @Override
  public String helpLine() {
    return "/note experience <amount> - Creates a note that will add experience on right click.";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {

    if(arguments.length < 1) {
      help(sender);
      return false;
    }

    Integer amount = 0;

    try {
      amount = Integer.valueOf(arguments[0]);
    } catch(Exception ignore) {
      sender.sendMessage(ChatColor.RED + "Amount must be numerical");
      return false;
    }

    if(amount < 1) {
      sender.sendMessage(ChatColor.RED + "Amount must be greater than or equal to 1.");
      return false;
    }

    Player player = getPlayer(sender);

    int xp = Experience.getExperience(player);

    if(xp < amount) {
      sender.sendMessage(ChatColor.RED + "You don't have enough experience.");
      return false;
    }

    Experience.changeExperience(player, amount, true);

    ItemStack stack = new ItemStack(Material.PAPER, 1);

    ItemMeta meta = stack.getItemMeta();

    List<String> lore = (meta.hasLore())? meta.getLore() : new ArrayList<>();
    lore.add(ChatColor.WHITE + "Amount: " + amount);
    lore.add(ChatColor.GREEN + "Right click to claim experience.");

    meta.setDisplayName("Experience Note");
    meta.setLore(lore);
    stack.setItemMeta(meta);

    Map<Integer, ItemStack> left = player.getInventory().addItem(stack);

    if(left.size() > 0) {
      player.sendMessage(ChatColor.WHITE + "Your experience note has been dropped on the floor since your inventory was full.");
    } else {
      player.sendMessage(ChatColor.WHITE + "Your experience note has been added to your inventory.");
    }
    return true;
  }
}