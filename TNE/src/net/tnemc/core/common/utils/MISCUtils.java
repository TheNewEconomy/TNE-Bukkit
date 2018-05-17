package net.tnemc.core.common.utils;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.configuration.ConfigurationManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import static com.google.common.net.HttpHeaders.USER_AGENT;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class MISCUtils {

  //Minecraft Version Utils

  /**
   * @return Whether the bukkit in use is for MC >= 1.8
   */
  public static boolean isOneEight() {
    return Bukkit.getVersion().contains("1.8") || isOneNine() || isOneTen() || isOneEleven() || isOneTwelve();
  }

  /**
   * @return Whether the bukkit in use is for MC >= 1.9
   */
  public static boolean isOneNine() {
    return Bukkit.getVersion().contains("1.9") || isOneTen() || isOneEleven() || isOneTwelve();
  }

  /**
   * @return Whether the bukkit in use is for MC >= 1.10
   */
  public static boolean isOneTen() {
    return Bukkit.getVersion().contains("1.10") || isOneEleven() || isOneTwelve();
  }

  /**
   * @return Whether the bukkit in use is for MC >= 1.11
   */
  public static boolean isOneEleven() {
    return Bukkit.getVersion().contains("1.11") || isOneTwelve();
  }

  /**
   * @return Whether the bukkit in use is for MC >= 1.12
   */
  public static boolean isOneTwelve() {
    return Bukkit.getVersion().contains("1.12");
  }

  public static Boolean isBoolean(String value) {
    return value.equalsIgnoreCase(String.valueOf(true)) || value.equalsIgnoreCase(String.valueOf(false));
  }

  public static Boolean isOnline(UUID id) {
   return IDFinder.getPlayer(id.toString()) != null;
  }

  public static Boolean isOnline(UUID id, String world) {
    if(IDFinder.getPlayer(id.toString()) != null) {
      return WorldFinder.getWorld(id, WorldVariant.BALANCE).equalsIgnoreCase(world);
    }
    return false;
  }

  public static Boolean isDouble(String value, String world) {
    try {
      TNE.debug("MISCUtils.isDouble(" + value + "," + world + ")");
      Double.valueOf(value.replace(ConfigurationManager.getString("config.yml", "Core.currency.Decimal", false, world, ""), "."));
      TNE.debug("Double confirmed");
      return true;
    } catch(Exception e) {
      TNE.debug("Double denied");
      return false;
    }
  }

  public static Boolean isInteger(String value) {
    try {
      Integer.valueOf(value);
      return true;
    } catch(Exception e) {
      return false;
    }
  }

  public static void restore(CommandSender sender) {
    File file = new File(TNE.instance().getDataFolder(), "extracted.yml");
    YamlConfiguration original = YamlConfiguration.loadConfiguration(file);
    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
    Set<String> accounts = configuration.getConfigurationSection("Accounts").getKeys(false);

    accounts.forEach((username) -> {
      UUID id = IDFinder.getID(username);
      TNEAccount account = new TNEAccount(id, username);
      Set<String> worlds = configuration.getConfigurationSection("Accounts." + username + ".Balances").getKeys(false);
      worlds.forEach((world) -> {
        Set<String> currencies = configuration.getConfigurationSection("Accounts." + username + ".Balances." + world).getKeys(false);
        currencies.forEach((currency) -> {
          String balance = original.getString("Accounts." + username + ".Balances." + world + "." + currency);
          account.setHoldings(world, currency, new BigDecimal(balance));
        });
      });
      TNE.manager().addAccount(account);
    });

    sender.sendMessage(ChatColor.WHITE + "Restored accounts from extracted.yml.");
  }

  public static boolean hasAccount(UUID id) {
    for (TNEAccount tneAccount : TNE.saveManager().getTNEManager().getTNEProvider().loadAccounts()) {
      System.out.println(id.toString() + " == " + tneAccount.identifier().toString());
      if(tneAccount.identifier().toString().equalsIgnoreCase(id.toString())) return true;
    }
    return false;
  }

  public static boolean hasAccount(String name) {
    for (TNEAccount tneAccount : TNE.saveManager().getTNEManager().getTNEProvider().loadAccounts()) {
      if(tneAccount.displayName().equalsIgnoreCase(name)) return true;
    }
    return false;
  }

  public static void idExtract(CommandSender sender) {
    LocalDateTime now = LocalDateTime.now();
    int year = now.getYear();
    int month = now.getMonthValue();
    int day = now.getDayOfMonth();

    StringBuilder content = new StringBuilder();

    content.append("---------------------- ID Portion -------------------------------" + System.getProperty("line.separator"));
    TNE.saveManager().getTNEManager().getTNEProvider().loadEconomyIDS().forEach((username, id)->{
          content.append("Username: " + username + " UUID: " + id.toString() + System.getProperty("line.separator"));
    });

    content.append("---------------------- Account Portion -------------------------------" + System.getProperty("line.separator"));
    TNE.saveManager().getTNEManager().getTNEProvider().loadAccounts().forEach((account ->{
      content.append("Username: " + account.displayName() + " UUID: " + account.identifier().toString() + System.getProperty("line.separator"));
    }));

    String name = TNE.instance().getServerName() + "-" + year + "-" + month + "-" + day + "-idextracted";
    String pasteURL = MISCUtils.pastebinUpload(name, content);

    String result = "Successfully extracted ECOIDS data." + ((pasteURL.contains("pastebin.com"))? " Uploaded backup to " + pasteURL : "");
    sender.sendMessage(result);
  }

  public static void extract(CommandSender sender) {
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
      if(account != null) {
        TNE.debug("Extracting Account: " + username);
        TNE.debug("WorldHoldings null? " + (account.getWorldHoldings()));
        account.getWorldHoldings().forEach((world, holdings) -> {
          holdings.getHoldings().forEach((currency, amount) -> {
            configuration.set("Accounts." + username + ".Balances." + world + "." + currency, amount.toPlainString());
          });
        });
      }
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
  }

  public static void idTest(Player player, CommandSender sender) {
    StringBuilder builder = new StringBuilder();

    builder.append("Getting ID by player instance." + System.getProperty("line.separator"));
    for(int i = 0; i < 40; i++) {
      builder.append(IDFinder.getID(player).toString() + System.getProperty("line.separator"));
    }

    builder.append("Getting ID by sender instance." + System.getProperty("line.separator"));
    for(int i = 0; i < 40; i++) {
      builder.append(IDFinder.getID(sender).toString() + System.getProperty("line.separator"));
    }

    builder.append("Getting ID by username." + System.getProperty("line.separator"));
    for(int i = 0; i < 40; i++) {
      builder.append(IDFinder.getID(player.getName()).toString() + System.getProperty("line.separator"));
    }
    player.sendMessage("IDFinder Test Results: " + pastebinUpload("IDFinder Test", builder));
  }

  public static void commandTest(Player player) {
    String username = player.getName();
    String[] ids = new String[7];
    ids[0] = IDFinder.getID(player).toString();
    player.performCommand("money give " + username + " 2000");
    ids[1] = IDFinder.getID(player).toString();
    player.performCommand("money take " + username + " 2000");
    ids[2] = IDFinder.getID(player).toString();
    player.performCommand("money set " + username + " 2000");
    ids[3] = IDFinder.getID(player).toString();
    player.performCommand("money set " + username + " 0");
    ids[4] = IDFinder.getID(player).toString();
    player.performCommand("baltop");
    ids[5] = IDFinder.getID(player).toString();
    player.performCommand("bal");
    ids[6] = IDFinder.getID(player).toString();

    player.sendMessage("Results of tests: " + String.join(", ", ids));
  }

  public static List<String> serverBlacklist() {
    List<String> list = new ArrayList<>();
    boolean failed = false;
    try {
      URL url = new URL("https://creatorfromhell.com/tne/blacklist.txt");
      Scanner s = new Scanner(url.openStream());

      String line;
      while(s.hasNext()) {
        line = s.nextLine();
        if(line.trim().equalsIgnoreCase("")) continue;
        list.add(line);
      }
    }
    catch(IOException ex) {
      failed = true;
    }
    if(failed) {
      List<String> blacklist = new ArrayList<>();
      return blacklist;
    }
    return list;
  }

  public static String pastebinUpload(String name, StringBuilder content) {
    final String base = "https://pastebin.com/api/api_post.php";
    final String devKey = "b73948f0d7d8cb449085dc90168a5deb";
    final String format = "txt";
    final String expiration = "N";
    final String privateCode = "1";

    String parameters = "api_option=paste&api_paste_private=" + privateCode +
        "&api_expire_date=" + expiration +
        "&api_dev_key=" + devKey;

    return sendPostRequest(base, parameters + "&api_paste_name=" + name + "&api_paste_code=" + content.toString());
  }

  private static String sendPostRequest(String URL, String parameters) {
    StringBuilder builder = new StringBuilder();
    try {
      HttpURLConnection connection = (HttpURLConnection) new URL(URL).openConnection();
      connection.setRequestMethod("POST");
      connection.setRequestProperty("User-Agent", USER_AGENT);
      connection.setDoOutput(true);
      OutputStream os = connection.getOutputStream();
      os.write(parameters.getBytes());
      os.flush();
      os.close();

      int responseCode = connection.getResponseCode();

      if(responseCode == HttpURLConnection.HTTP_OK) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response;
        while ((response = reader.readLine()) != null) {
          builder.append(response);
        }
        reader.close();
      }
    } catch (Exception e) {
      TNE.debug(e);
    }
    return builder.toString();
  }
}