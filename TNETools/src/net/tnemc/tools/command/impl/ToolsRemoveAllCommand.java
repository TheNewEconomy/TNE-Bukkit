package net.tnemc.tools.command.impl;

import com.github.tnerevival.core.db.SQLDatabase;
import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
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
public class ToolsRemoveAllCommand extends TNECommand {
  public ToolsRemoveAllCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "rmvall";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.command.tools.rmvall";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String[] getHelpLines() {
    return new String[] {
        "/tnet removeall - Removes all accounts that share the same name."
    };
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
      try {
        final SQLDatabase connector = ((SQLDatabase)TNE.saveManager().getTNEManager().getTNEProvider().connector());
        List<UUID> ids = new ArrayList<>();

        try(Connection connection = connector.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT display_name, COUNT(*) user_count FROM " + TNE.saveManager().getTNEManager().getPrefix() + "_USERS GROUP BY display_name HAVING user_count > 2");
            ResultSet results = connector.executePreparedQuery(statement, new Object[] {})) {

          while(results.next()) {
            final String name = results.getString("display_name");
            sender.sendMessage(ChatColor.YELLOW + "REMOVE: " + name);

            /*try(PreparedStatement stmt = connection.prepareStatement("SELECT uuid FROM " + TNE.saveManager().getTNEManager().getPrefix() + "_USERS WHERE display_name = ?");
                ResultSet idResults = connector.executePreparedQuery(stmt, new Object[] { name })) {

              while(idResults.next()) {
                ids.add(UUID.fromString(idResults.getString("uuid")));
              }
            }

            connector.executePreparedUpdate(
                "DELETE FROM " + TNE.saveManager().getTNEManager().getPrefix() + "_USERS WHERE display_name = ?", new Object[] { name }
            );
            connector.executePreparedUpdate(
                "DELETE FROM " + TNE.saveManager().getTNEManager().getPrefix() + "_ECOIDS WHERE username = ?", new Object[] { name }
            );

            for(UUID id : ids) {
              connector.executePreparedUpdate(
                  "DELETE FROM " + TNE.saveManager().getTNEManager().getPrefix() + "_BALANCES WHERE uuid = ?", new Object[] { id.toString() }
              );
            }*/
          }
        }
        //TNE.manager().createAccount(IDFinder.getID(arguments[0]), arguments[0]);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
    return true;
  }
}