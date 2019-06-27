package net.tnemc.signs.signs.impl;

import net.tnemc.signs.SignsData;
import net.tnemc.signs.signs.SignStep;
import net.tnemc.signs.signs.SignType;
import net.tnemc.signs.signs.impl.command.CommandSelectionStep;
import net.tnemc.signs.signs.impl.command.CostSelectionStep;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.event.block.SignChangeEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/18/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class CommandSign implements SignType {
  /**
   * @return The name of this sign type.
   */
  @Override
  public String name() {
    return "command";
  }

  /**
   * @return The permission node required to use this sign.
   */
  @Override
  public String usePermission() {
    return "tne.sign.command.use";
  }

  /**
   * @return The permission node required to create this sign.
   */
  @Override
  public String createPermission() {
    return "tne.sign.command.create";
  }

  @Override
  public Map<Integer, SignStep> steps() {
    Map<Integer, SignStep> steps = new HashMap<>();
    steps.put(1, new CommandSelectionStep());
    steps.put(2, new CostSelectionStep());
    return steps;
  }

  @Override
  public Map<String, List<String>> tables() {
    Map<String, List<String>> tables = new HashMap<>();

    tables.put("mysql", Collections.singletonList(
        "CREATE TABLE IF NOT EXISTS " + SignsData.prefix + "_SIGNS_COMMANDS (" +
            "`sign_location` VARCHAR(255) NOT NULL UNIQUE," +
            "`command_value` TEXT NOT NULL," +
            "`command_currency` BOOLEAN NOT NULL DEFAULT 1," +
            "`command_cost` DECIMAL(49,4) DEFAULT 10," +
            "`command_offer` TEXT NOT NULL," +
            "`command_trade` TEXT NOT NULL DEFAULT ''" +
            ") ENGINE = INNODB DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;"
    ));

    tables.put("h2", Collections.singletonList(
        "CREATE TABLE IF NOT EXISTS " + SignsData.prefix + "_SIGNS_COMMANDS (" +
            "`sign_location` VARCHAR(255) NOT NULL UNIQUE," +
            "`command_value` TEXT NOT NULL," +
            "`command_currency` BOOLEAN NOT NULL DEFAULT 1," +
            "`command_cost` DECIMAL(49,4) DEFAULT 10," +
            "`command_offer` TEXT NOT NULL," +
            "`command_trade` TEXT NOT NULL DEFAULT ''" +
            ") ENGINE = INNODB;"
    ));
    return tables;
  }

  @Override
  public boolean onSignCreate(SignChangeEvent event, Block attached, UUID player) {
    Bukkit.getPlayer(player).sendMessage(ChatColor.WHITE + "Now type your command in chat, without the slash(/) character.");
    return true;
  }
}