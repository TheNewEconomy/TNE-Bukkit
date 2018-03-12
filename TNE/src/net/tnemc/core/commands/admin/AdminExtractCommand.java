package net.tnemc.core.commands.admin;

import com.github.tnerevival.commands.TNECommand;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 2/8/2018.
 */
public class AdminExtractCommand extends TNECommand {

  public AdminExtractCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "extract";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.admin.extract";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Admin.Extract";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    File file = new File(TNE.instance().getDataFolder(), "extracted.yml");
    if(!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
    Map<String, UUID> ids = TNE.saveManager().getTNEManager().getTNEProvider().loadEconomyIDS();
    TNE.debug("Accounts Length: " + ids.size());

    ids.forEach((username, id)->{
      TNEAccount account = TNE.manager().getAccount(id);
      TNE.debug("Extracting Account: " + username);
      TNE.debug("Account null? " + (account == null));
      TNE.debug("WorldHoldings null? " + (account.getWorldHoldings()));
      account.getWorldHoldings().forEach((world, holdings)->{
        holdings.getHoldings().forEach((currency, amount)->{
          configuration.set("Accounts." + username + ".Balances." + world + "." + currency, amount.toPlainString());
        });
      });
    });
    try {
      configuration.save(file);
    } catch (IOException e) {
      TNE.debug(e);
    }

    LocalDateTime now = LocalDateTime.now();
    int year = now.getYear();
    int month = now.getMonthValue();
    int day = now.getDayOfMonth();

    StringBuilder content = new StringBuilder();

    String name = TNE.instance().getServerName() + "-" + year + "-" + month + "-" + day + "-extracted.yml";
    try (BufferedReader br = new BufferedReader(new FileReader(new File(TNE.instance().getDataFolder(),"extracted.yml")))){
      String line;
      while ((line = br.readLine()) != null) {
        content.append(line + System.getProperty("line.separator"));
      }
    } catch (Exception e) {
      TNE.debug(e);
    }
    String pasteURL = MISCUtils.pastebinUpload(name, content);

    String result = "Successfully extracted player balance data." + ((pasteURL.contains("pastebin.com"))? " Uploaded backup to " + pasteURL : "");
    sender.sendMessage(result);
    return true;
  }
}