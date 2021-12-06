package net.tnemc.core.common.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.tnemc.commands.bukkit.provider.BukkitPlayerProvider;
import net.tnemc.commands.core.CommandSearchInformation;
import net.tnemc.commands.core.CommandsHandler;
import net.tnemc.commands.core.provider.PlayerProvider;
import net.tnemc.core.TNE;
import net.tnemc.core.WorldGuardManager;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.TNECurrency;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.MetadataValue;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

  public static CommandSender getSender(PlayerProvider provider) {
    if(!provider.isPlayer()) {
      return Bukkit.getConsoleSender();
    }
    return Bukkit.getPlayer(provider.getUUID());
  }

  public static Player getPlayer(UUID id) {
    final String name = IDFinder.getUsername(id.toString());

    if(IDFinder.isNonPlayer(name)) return null;

    if(Bukkit.getServer().getPlayer(id) == null) {
      if(Bukkit.getServer().getPlayerExact(name) == null) {
        return null;
      }
      return Bukkit.getServer().getPlayerExact(name);
    }
    return Bukkit.getServer().getPlayer(id);
  }

  //Minecraft Version Utils

  public static Player getPlayer(CommandSender sender) {
    if(sender instanceof Player) {
      return (Player)sender;
    }
    return null;
  }

  public static Map<String, String> getArguments(String[] arguments) {
    Map<String, String> parsed = new HashMap<>();
    for(int i = 0; i < arguments.length; i++) {
      if(arguments[i].contains(":")) {
        String[] broken = arguments[i].split(":");
        parsed.put(broken[0], broken[1]);
        continue;
      }
      parsed.put(i + "", arguments[i]);
    }
    return parsed;
  }

  public static void help(CommandSender sender, String label, String[] arguments) {
    Optional<CommandSearchInformation> search = CommandsHandler.manager().search(label, arguments);

    final List<String> messages = search.get().getInformation().get().buildHelp(new BukkitPlayerProvider(sender));
    for(String message : messages) {
      sender.sendMessage(Message.replaceColours(message, false));
    }
  }

  public static BigDecimal percent(BigDecimal amount, BigDecimal percent) {
    return amount.multiply(percent).divide(TNE.ONE_HUNDRED, BigDecimal.ROUND_DOWN);
  }

  //Minecraft Version Utils
  /**
   * @return Whether the bukkit in use is for MC >= 1.8
   */
  public static boolean isOneSix() {
    return Bukkit.getVersion().contains("1.6");
  }

  /**
   * @return Whether the bukkit in use is for MC >= 1.8
   */
  public static boolean isOneSeven() {
    return Bukkit.getVersion().contains("1.7");
  }

  /**
   * @return Whether the bukkit in use is for MC >= 1.8
   */
  public static boolean isOneEight() {
    return Bukkit.getVersion().contains("1.8");
  }

  /**
   * @return Whether the bukkit in use is for MC >= 1.9
   */
  public static boolean isOneNine() {
    return Bukkit.getVersion().contains("1.9");
  }

  /**
   * @return Whether the bukkit in use is for MC >= 1.10
   */
  public static boolean isOneTen() {
    return Bukkit.getVersion().contains("1.10");
  }

  /**
   * @return Whether the bukkit in use is for MC >= 1.11
   */
  public static boolean isOneEleven() {
    return Bukkit.getVersion().contains("1.11");
  }

  /**
   * @return Whether the bukkit in use is for MC >= 1.12
   */
  public static boolean isOneTwelve() {
    return Bukkit.getVersion().contains("1.12");
  }

  public static boolean isOneThirteen() {
    return Bukkit.getVersion().contains("1.13") || isOneFourteen() || isOneFifteen() || isOneSixteen() || isOneSeventeen() || isOneSeventeen() || isOneEightteen() || isOneNineteen() || isOneTwenty();
  }

  public static boolean isOneFourteen() {
    return Bukkit.getVersion().contains("1.14") || isOneFifteen() || isOneSixteen() || isOneSeventeen() || isOneSeventeen() || isOneEightteen() || isOneNineteen() || isOneTwenty();
  }

  public static boolean isOneFifteen() {
    return Bukkit.getVersion().contains("1.15") || isOneSixteen() || isOneSeventeen() || isOneSeventeen() || isOneEightteen() || isOneNineteen() || isOneTwenty();
  }

  public static boolean isOneSixteen() {
    return Bukkit.getVersion().contains("1.16") || isOneSeventeen() || isOneEightteen() || isOneNineteen() || isOneTwenty();
  }

  public static boolean isOneSeventeen() {
    return Bukkit.getVersion().contains("1.17") || isOneEightteen() || isOneNineteen() || isOneTwenty();
  }

  public static boolean isOneEightteen() {
    return Bukkit.getVersion().contains("1.18") || isOneNineteen() || isOneTwenty();
  }

  public static boolean isOneNineteen() {
    return Bukkit.getVersion().contains("1.19") || isOneTwenty();
  }

  public static boolean isOneTwenty() {
    return Bukkit.getVersion().contains("1.20");
  }

  public static boolean offHand() {
    return isOneNine() || isOneTen() || isOneEleven() || isOneTwelve() || isOneThirteen();
  }

  public static Material getMainHand(Player player) {
    if(offHand()) {
      return player.getInventory().getItemInMainHand().getType();
    }
    return player.getInventory().getItemInHand().getType();
  }

  public static File[] getYAMLs(final File directory) {
    return directory.listFiles((dir, name) -> name.endsWith(".yml"));
  }

  public static Boolean isBoolean(String value) {
    return value.equalsIgnoreCase(String.valueOf(true)) || value.equalsIgnoreCase(String.valueOf(false));
  }

  public static Boolean isOnline(UUID id) {
   return IDFinder.getPlayer(id.toString()) != null;
  }

  public static Boolean isOnline(UUID id, String world) {
    final Player player = IDFinder.getPlayer(id.toString());
    if(player != null) {
      return WorldFinder.getWorld(id, WorldVariant.BALANCE).equalsIgnoreCase(world);
    }
    return false;
  }

  public static Boolean isDouble(String value, String world) {
    try {
      TNE.debug("MISCUtils.isDouble(" + value + "," + world + ")");
      Double.valueOf(value.replace(TNE.instance().api().getString("Core.currency.Decimal", world), "."));
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

  public static ItemStack getCustomTextureHead(String value) {
    ItemStack head = TNE.item().build("PLAYER_HEAD");
    SkullMeta meta = (SkullMeta) head.getItemMeta();
    GameProfile profile = new GameProfile(UUID.randomUUID(), "");
    profile.getProperties().put("textures", new Property("textures", value));
    Field profileField = null;
    try {
      profileField = meta.getClass().getDeclaredField("profile");
      profileField.setAccessible(true);
      profileField.set(meta, profile);
    } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ignore) {
    }
    head.setItemMeta(meta);
    return head;
  }
  
  public static boolean isSingularPlayer(String argument) {
    if(!argument.contains(",") && !argument.contains(TNE.instance().api().getString("Core.Server.ThirdParty.Faction")) &&
        !argument.contains(TNE.instance().api().getString("Core.Server.ThirdParty.Town")) &&
        !argument.contains(TNE.instance().api().getString("Core.Server.ThirdParty.Nation"))) {
      return MISCUtils.getPlayer(IDFinder.getID(argument)) != null;
    }
    return false;
  }

  public static TNECurrency findCurrency(String world, Location location) {
    if(!TNE.fawe && TNE.instance().getServer().getPluginManager().getPlugin("WorldGuard") != null) {
      return WorldGuardManager.findCurrency(world, location);
    }
    return TNE.manager().currencyManager().get(world);
  }

  public static String findCurrencyName(String world, Location location) {
    if(!TNE.fawe && TNE.instance().getServer().getPluginManager().getPlugin("WorldGuard") != null) {
      return WorldGuardManager.findCurrencyName(world, location);
    }
    return TNE.manager().currencyManager().get(world).name();
  }

  public static String findCurrencyName(String world, Location location, String defaultCurrency) {
    if(!TNE.fawe && TNE.instance().getServer().getPluginManager().getPlugin("WorldGuard") != null) {
      return WorldGuardManager.findCurrencyName(world, location);
    }

    if(!defaultCurrency.trim().equalsIgnoreCase("")) return defaultCurrency;
    return TNE.manager().currencyManager().get(world).name();
  }

  public static void restore(CommandSender sender) {
    File file = new File(TNE.instance().getDataFolder(), "extracted.yml");
    YamlConfiguration original = YamlConfiguration.loadConfiguration(file);
    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);


    if(configuration.contains("Accounts")) {
      Set<String> accounts = configuration.getConfigurationSection("Accounts").getKeys(false);

      final int frequency = (int)(accounts.size() * 0.10);
      int number = 1;
      for(String username : accounts) {
        String reformattedUsername = username.replaceAll("\\!", ".").replaceAll("\\@", "-").replaceAll("\\%", "_");
        if (reformattedUsername.equalsIgnoreCase("server_account")) reformattedUsername = TNE.instance().consoleName;
        UUID id = IDFinder.getID(reformattedUsername);
        TNEAccount account = new TNEAccount(id, reformattedUsername);
        Set<String> worlds = configuration.getConfigurationSection("Accounts." + username + ".Balances").getKeys(false);
        worlds.forEach((world) -> {
          Set<String> currencies = configuration.getConfigurationSection("Accounts." + username + ".Balances." + world).getKeys(false);
          currencies.forEach((currency) -> {
            String finalCurrency = (currency.equalsIgnoreCase("default")) ? TNE.manager().currencyManager().get(world).name() : currency;
            String balance = original.getString("Accounts." + username + ".Balances." + world + "." + currency);
            account.setHoldings(world, finalCurrency, new BigDecimal(balance));
          });
        });
        TNE.manager().addAccount(account);
        number++;
        try {
          final boolean message = (number % frequency == 0);

          if (message) {
            final int progress = (int) (number * 100 / accounts.size());
            TNE.logger().info("Restoration Progress: " + progress);
          }
        } catch(Exception ignore) {}
      }
    }

    sender.sendMessage(ChatColor.WHITE + "Restored accounts from extracted.yml.");
  }

  public static boolean hasAccount(UUID id) {
    try {
      for (TNEAccount tneAccount : TNE.saveManager().getTNEManager().getTNEProvider().loadAccounts()) {
        TNE.debug(id.toString() + " == " + tneAccount.identifier().toString());
        if(tneAccount.identifier().toString().equalsIgnoreCase(id.toString())) return true;
      }
    } catch (SQLException e) {
      TNE.debug(e);
    }
    return false;
  }

  public static boolean hasAccount(String name) {
    try {
      for (TNEAccount tneAccount : TNE.saveManager().getTNEManager().getTNEProvider().loadAccounts()) {
        if(tneAccount.displayName().equalsIgnoreCase(name)) return true;
      }
    } catch (SQLException e) {
      TNE.debug(e);
    }
    return false;
  }

  public static void idExtract(CommandSender sender) throws SQLException {
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
    String pasteURL = MISCUtils.pastebinUpload(name, "yaml", content);

    String result = "Successfully extracted ECOIDS data." + ((pasteURL.contains("pastebin.com"))? " Uploaded backup to " + pasteURL : "");
    sender.sendMessage(result);
  }

  public static String metadata(final Player player, final String name) {
    for(MetadataValue value : player.getMetadata(name)) {
      if(value.getOwningPlugin().getName().equalsIgnoreCase("theneweconomy")) {
        return value.asString();
      }
    }
    return null;
  }

  public static void extract(CommandSender sender) throws SQLException {
    File file = new File(TNE.instance().getDataFolder(), "extracted.yml");

    if(file.exists()) {
      final File directory = new File(TNE.instance().getDataFolder(), "extractions");
      directory.mkdir();

      final String fileName = "extracted-" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()) + ".yml";
      final File moved = new File(TNE.instance().getDataFolder(), "extractions/" + fileName);

      file.renameTo(moved);
    }

    if(!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        TNE.debug(e);
      }
    }
    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
    Map<String, UUID> ids = TNE.saveManager().getTNEManager().getTNEProvider().loadEconomyAccountIDS();
    TNE.debug("Accounts Length: " + ids.size());
    ids.forEach((username, id)->{
      TNEAccount account = TNE.manager().getAccount(id);
      if(account != null) {
        TNE.debug("Extracting Account: " + username);
        try {
          Map<String, BigDecimal> balances = TNE.saveManager().getTNEManager().getTNEProvider().loadAllBalances(id);

          balances.forEach((key, balance)->{
            String[] split = key.split("\\@");
            configuration.set("Accounts." + username.replaceAll("\\.", "!").replaceAll("\\-", "@").replaceAll("\\_", "%") + ".Balances." + split[0] + "." + split[1], balance.toPlainString());
          });
        } catch (SQLException e) {
          TNE.debug(e);
        }

      }
    });
    try {
      configuration.save(file);
    } catch (IOException e) {
      TNE.debug(e);
    }

    final LocalDateTime now = LocalDateTime.now();

    StringBuilder content = new StringBuilder();

    String name = TNE.instance().getServerName() + "-" + now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth() + "-extracted.yml";
    try (BufferedReader br = new BufferedReader(new FileReader(new File(TNE.instance().getDataFolder(),"extracted.yml")))){
      String line;
      while ((line = br.readLine()) != null) {
        content.append(line + System.getProperty("line.separator"));
      }
    } catch (Exception e) {
      TNE.debug(e);
    }
    String pasteURL = MISCUtils.pastebinUpload(name, "yaml", content);

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
    player.sendMessage("IDFinder Test Results: " + pastebinUpload("IDFinder Test", "yaml", builder));
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

      TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
        public X509Certificate[] getAcceptedIssuers(){return null;}
        public void checkClientTrusted(X509Certificate[] certs, String authType){}
        public void checkServerTrusted(X509Certificate[] certs, String authType){}
      }};

      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init(null, trustAllCerts, new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

      URL url = new URL("https://tnemc.net/files/blacklist.txt");
      HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
      Scanner s = new Scanner(connection.getInputStream());

      String line;
      while(s.hasNext()) {
        line = s.nextLine();
        if(line.trim().equalsIgnoreCase("")) continue;
        list.add(line);
      }
    }
    catch(Exception ex) {
      failed = true;
    }
    if(failed) {
      List<String> blacklist = new ArrayList<>();
      return blacklist;
    }
    return list;
  }

  public static List<String> dupers() {
    List<String> list = new ArrayList<>();
    boolean failed = false;
    try {

      TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
        public X509Certificate[] getAcceptedIssuers(){return null;}
        public void checkClientTrusted(X509Certificate[] certs, String authType){}
        public void checkServerTrusted(X509Certificate[] certs, String authType){}
      }};

      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init(null, trustAllCerts, new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

      URL url = new URL("https://tnemc.net/files/dupers.txt");
      HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
      Scanner s = new Scanner(connection.getInputStream());

      String line;
      while(s.hasNext()) {
        line = s.nextLine();
        if(line.trim().equalsIgnoreCase("")) continue;
        list.add(line.toUpperCase());
      }
    } catch(Exception ex) {
      failed = true;
    }
    if(failed) {
      return new ArrayList<>();
    }
    return list;
  }

  public static String md5(String toDigest) {
    String toReturn = "";
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(toDigest.getBytes());
      byte[] digest = md.digest();
      toReturn = DatatypeConverter.printHexBinary(digest).toUpperCase();
      md = null;
      digest = null;
    } catch (NoSuchAlgorithmException e) {
      TNE.debug(e);
    }
    return toReturn;
  }

  public static String pastebinUpload(String name, String format, StringBuilder content) {
    final String base = "https://pastebin.com/api/api_post.php";
    final String devKey = "b73948f0d7d8cb449085dc90168a5deb";
    final String expiration = "N";
    final String privateCode = "1";

    String parameters = "api_option=paste&api_paste_private=" + privateCode +
        "&api_paste_format=" + format +
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