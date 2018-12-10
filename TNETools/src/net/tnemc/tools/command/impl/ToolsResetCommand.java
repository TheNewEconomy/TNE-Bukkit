package net.tnemc.tools.command.impl;

import com.github.tnerevival.core.db.SQLDatabase;
import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import net.tnemc.core.common.api.IDFinder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 12/5/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ToolsResetCommand extends TNECommand {
  public ToolsResetCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "remove";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.command.tools.remove";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String[] getHelpLines() {
    return new String[] {
      "/tnet remove <name> - Removes all accounts that share the name specified."
    };
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    if(arguments.length <= 0) {
      help(sender);
      return false;
    }
    if(!TNE.manager().exists(IDFinder.getID(arguments[0]))) {
      sender.sendMessage(ChatColor.RED + "Invalid account name specified.");
      return false;
    }

    Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
      try {
        final SQLDatabase connector = ((SQLDatabase)TNE.saveManager().getTNEManager().getTNEProvider().connector());
        List<UUID> ids = new ArrayList<>();

        try(Connection connection = connector.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT uuid FROM " + TNE.saveManager().getTNEManager().getPrefix() + "_USERS WHERE display_name = ?");
            ResultSet results = connector.executePreparedQuery(statement, new Object[] { arguments[0] })) {

          while(results.next()) {
            ids.add(UUID.fromString(results.getString("uuid")));
          }
        }

            connector.executePreparedUpdate(
            "DELETE FROM " + TNE.saveManager().getTNEManager().getPrefix() + "_USERS WHERE display_name = ?", new Object[] { arguments[0] }
            );
            connector.executePreparedUpdate(
            "DELETE FROM " + TNE.saveManager().getTNEManager().getPrefix() + "_ECOIDS WHERE username = ?", new Object[] { arguments[0] }
            );

            for(UUID id : ids) {
              connector.executePreparedUpdate(
                  "DELETE FROM " + TNE.saveManager().getTNEManager().getPrefix() + "_BALANCES WHERE uuid = ?", new Object[] { id.toString() }
              );
            }
            TNE.manager().createAccount(IDFinder.getID(arguments[0]), arguments[0]);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
    return true;
  }
}