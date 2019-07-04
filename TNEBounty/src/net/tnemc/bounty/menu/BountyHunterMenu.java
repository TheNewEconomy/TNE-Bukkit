package net.tnemc.bounty.menu;

import net.tnemc.bounty.BountyData;
import net.tnemc.bounty.BountyModule;
import net.tnemc.bounty.model.BountyHunter;
import net.tnemc.bounty.model.HunterLevel;
import net.tnemc.core.TNE;
import net.tnemc.core.menu.Menu;
import net.tnemc.core.menu.MenuHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/4/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class BountyHunterMenu extends Menu {
  public BountyHunterMenu() {
    super("bounty_hunter_menu", "[TNE]Hunter View", 3);
  }

  @Override
  public Inventory buildInventory(Player player) {
    final UUID hunterID = UUID.fromString((String) TNE.menuManager().getViewerData(player.getUniqueId(), "hunter_id"));

    BountyHunter hunter = BountyData.getHunter(hunterID);
    final HunterLevel level = BountyModule.instance().getHunterManager().getObject(hunter.getLevel());

    ItemStack info = new ItemStack(Material.PAINTING);
    ItemMeta infoMeta = info.getItemMeta();
    infoMeta.setDisplayName(ChatColor.YELLOW + "Information");
    List<String> infoLore = new ArrayList<>();
    infoLore.add(ChatColor.YELLOW + "Level: " + ChatColor.WHITE + hunter.getLevel());
    infoLore.add(ChatColor.YELLOW + "Bounties: " + ChatColor.WHITE + hunter.getBounties());
    info.setItemMeta(infoMeta);

    ItemStack experience = new ItemStack(Material.EXPERIENCE_BOTTLE);
    ItemMeta xpMeta = experience.getItemMeta();
    xpMeta.setDisplayName(ChatColor.YELLOW + "Experience");
    List<String> xpLore = new ArrayList<>();
    xpLore.add(ChatColor.YELLOW + "Current XP: " + ChatColor.WHITE + hunter.getExperience());
    xpLore.add(ChatColor.YELLOW + "Next Level: " + ChatColor.WHITE + BountyModule.instance().getHunterManager().experience(hunter.getLevel() + 1));
    xpLore.add(ChatColor.YELLOW + "XP Per Bounty: " + ChatColor.WHITE + level.getExperienceGain());
    experience.setItemMeta(xpMeta);

    ItemStack head = TNE.item().build("PLAYER_HEAD");
    ItemMeta headMeta = head.getItemMeta();
    headMeta.setDisplayName(ChatColor.YELLOW + "Head Drop");
    List<String> headLore = new ArrayList<>();
    headLore.add(ChatColor.YELLOW + "Head Drops: " + ChatColor.WHITE + level.canHead());
    headLore.add(ChatColor.YELLOW + "Head Chance: " + ChatColor.WHITE + level.getHeadChance());
    head.setItemMeta(headMeta);

    ItemStack message = new ItemStack(Material.NAME_TAG);
    ItemMeta messageMeta = message.getItemMeta();
    messageMeta.setDisplayName(ChatColor.YELLOW + "Kill Message");
    List<String> messageLore = new ArrayList<>();
    messageLore.add(ChatColor.WHITE + hunter.getMessage());
    message.setItemMeta(messageMeta);


    ItemStack skull = TNE.item().build("PLAYER_HEAD");
    SkullMeta meta = (SkullMeta) skull.getItemMeta();
    meta.setOwningPlayer(Bukkit.getOfflinePlayer(hunterID));
    skull.setItemMeta(meta);

    Inventory inventory = Bukkit.createInventory(new MenuHolder(getName()), rows * 9, getTitle());
    inventory.setItem(2, skull);
    inventory.setItem(4, info);
    inventory.setItem(6, skull);
    inventory.setItem(20, experience);
    inventory.setItem(22, head);
    inventory.setItem(24, message);
    return inventory;
  }
}