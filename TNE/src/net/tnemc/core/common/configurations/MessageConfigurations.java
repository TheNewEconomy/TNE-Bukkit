package net.tnemc.core.common.configurations;

import net.tnemc.core.TNE;
import net.tnemc.core.common.api.IDFinder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class MessageConfigurations extends Configuration {

  private Map<String, Language> languages = new HashMap<>();

  @Override
  public FileConfiguration getConfiguration() {
    return TNE.instance().messageConfiguration();
  }

  @Override
  public List<String> node() {
    List<String> nodes = new ArrayList<>();
    nodes.add("Messages");
    return nodes;
  }

  @Override
  public File getFile() {
    return TNE.instance().getMessagesFile();
  }

  @Override
  public void load(FileConfiguration configurationFile) {
    configurations.put("Messages.General.NoPerm", "<red>I'm sorry, but you do not have permission to do that.");
    configurations.put("Messages.General.NoPlayer", "<red>Unable to locate player \"$player\"!");
    configurations.put("Messages.General.Saved", "<yellow>Successfully saved all TNE Data!");
    configurations.put("Messages.General.Disabled", "<red>Economy features are currently disabled in this world!");

    configurations.put("Messages.Command.Unable", "<red>I'm sorry, but you're not allowed to use that command.");
    configurations.put("Messages.Command.None", "<yellow>Command $command $arguments could not be found! Try using $command help.");
    configurations.put("Messages.Command.InActive", "<red>Command $command $arguments has been deactivated! Try using $command help.");
    configurations.put("Messages.Command.Charge", "<white>You have been charged <gold>$amount<white> for using $command.");

    configurations.put("Messages.Admin.NoHoldings", "<red>$player has no holdings for the world \"$world\"!");
    configurations.put("Messages.Admin.Holdings", "<white>$player currently has <gold>$amount <white>for world \"$world\"!");
    configurations.put("Messages.Admin.NoTransactions", "<white>$player has no transactions to display.");
    configurations.put("Messages.Admin.Configuration", "<white>The value of $node is currently $value.");
    configurations.put("Messages.Admin.SetConfiguration", "<white>The value of $node has been set to $value.");
    configurations.put("Messages.Admin.ID", "<white>The UUID for $player is $id.");
    configurations.put("Messages.Admin.Exists", "<red>A player with that name already exists.");
    configurations.put("Messages.Admin.Created", "<white>Successfully created account for $player.");
    configurations.put("Messages.Admin.Deleted", "<white>Successfully deleted account for $player.");
    configurations.put("Messages.Admin.PurgeWorld", "<white>Successfully purged economy accounts in $world.");
    configurations.put("Messages.Admin.StatusChanged", "<white>Status for $player has been changed to <green>$status<white>.");
    configurations.put("Messages.Admin.Status", "<white>Status for $player is <green>$status<white>.");
    configurations.put("Messages.Admin.Reset", "<white>Performed an economy reset using these parameters -  world = $world, currency = $currency, and player = $player.");

    configurations.put("Messages.Account.Locked", "<red>You can't do that with a locked account($player)!");
    configurations.put("Messages.Account.NoTransactions", "<white>You have no transactions to display at this time.");
    configurations.put("Messages.Account.StatusChange", "<white>Your account's status has been changed to <green>$status<white>.");

    configurations.put("Messages.Configuration.NoSuch", "The configuration node $node does not exist.");
    configurations.put("Messages.Configuration.Invalid", "The value you specified for $node is invalid.");
    configurations.put("Messages.Configuration.InvalidFile", "The configuration file you specified is invalid.");
    configurations.put("Messages.Configuration.Get", "The value for node $node is $value.");
    configurations.put("Messages.Configuration.Set", "Successfully set the value for $node to $value.");
    configurations.put("Messages.Configuration.TNEGet", "The parsed value for node $node is $value.");
    configurations.put("Messages.Configuration.Saved", "Successfully saved all modified nodes for file $configuration.");
    configurations.put("Messages.Configuration.SavedAll", "Successfully saved all modified configurations.");
    configurations.put("Messages.Configuration.Undone", "Successfully undid all configuration modifications to $modified.");
    configurations.put("Messages.Configuration.UndoneAll", "Successfully undid all configuration modifications.");

    configurations.put("Messages.Currency.List", "<white>The current currencies for $world are: $currencies.");
    configurations.put("Messages.Currency.Tiers", "<white>The currency \"$currency\" currently has these tiers;<newline>Major: $major_tiers<newline>Minor: $minor_tiers");
    configurations.put("Messages.Currency.AlreadyExists", "<white>A currency with the name $currency already exists in world $world.");
    configurations.put("Messages.Currency.Renamed", "<white>Successfully renamed $currency in world $world to $new_Name.");

    configurations.put("Messages.Language.Current", "<white>Your language is currently set as $language.");
    configurations.put("Messages.Language.List", "<white>The available languages are $languages.");
    configurations.put("Messages.Language.None", "<red>There is no language with the name \"$language\".");
    configurations.put("Messages.Language.Reload", "<white>Successfully reloaded all language files.");
    configurations.put("Messages.Language.Set", "<white>Successfully set your language to $language.");

    configurations.put("Messages.Module.Downloaded", "$module has been downloaded successfully.");
    configurations.put("Messages.Module.FailedDownload", "$module couldn't be downloaded successfully.");
    configurations.put("Messages.Module.Info", "<white>==== Module Info for $module ====<newline>Author: $author<newline>Version: $version");
    configurations.put("Messages.Module.Invalid", "<red>Unable to find a module with the name of \"$module\".");
    configurations.put("Messages.Module.List", "<white>This server is currently uses these TNE Modules: $modules.");
    configurations.put("Messages.Module.Loaded", "<white>Loaded module \"$module\" $version created by \"$author\".");
    configurations.put("Messages.Module.Reloaded", "<white>Reloaded module \"$module\".");
    configurations.put("Messages.Module.Unloaded", "<white>Unloaded module \"$module\".");

    configurations.put("Messages.Money.Failed", "<red>Unable to process your transaction at this time.");
    configurations.put("Messages.Money.Given", "<white>You were given <gold>$amount<white>.");
    configurations.put("Messages.Money.Received", "<white>You were paid <gold>$amount <white> by <white> $player.");
    configurations.put("Messages.Money.Taken", "<white>$player took <gold>$amount<white> from you.");
    configurations.put("Messages.Money.Insufficient", "<red>I'm sorry, but you do not have <gold>$amount<red>.");
    configurations.put("Messages.Money.HoldingsMulti", "<white>Your current balances for world \"$world\" are: ");
    configurations.put("Messages.Money.HoldingsMultiSingle", "<white>$currency: <gold>$amount");
    configurations.put("Messages.Money.Holdings", "<white>You currently have <gold>$amount<white> on you.");
    configurations.put("Messages.Money.Gave", "<white>Successfully gave $player <gold>$amount<white>.");
    configurations.put("Messages.Money.RecipientSet", "<white>Your balance has been set to <gold>$amount<white>.");
    configurations.put("Messages.Money.Set", "<white>Successfully set $player\'s balance to <gold>$amount<white>.");
    configurations.put("Messages.Money.Paid", "<white>Successfully paid $player <gold>$amount<white>.");
    configurations.put("Messages.Money.Took", "<white>Successfully took <gold>$amount<white> from $player.");
    configurations.put("Messages.Money.Negative", "<red>Amount cannot be a negative value!");
    configurations.put("Messages.Money.SelfPay", "<red>You can't pay yourself!");
    configurations.put("Messages.Money.NoCurrency", "<red>The currency \"$currency\" could not be found in \"$world\".");
    configurations.put("Messages.Money.Converted", "<white>Successfully exchanged \"<gold>$from_amount<white>\" to \"<gold>$amount<white>\".");
    configurations.put("Messages.Money.Noted", "<white>A note has been given to you in the amount of <gold>$amount<white> for currency <green>$currency<white>.");
    configurations.put("Messages.Money.NoteClaimed", "<white>Successfully claimed note for currency <green>$currency<white> in the amount of <gold>$amount<white>.<newline>Your new balance is <gold>$balance<white>.");
    configurations.put("Messages.Money.NoteMinimum", "<red>The minimum note amount of $amount was not met.");
    configurations.put("Messages.Money.NoteFailed", "<red>I'm sorry, but your attempt to claim that currency note failed!.");
    configurations.put("Messages.Money.Top", "<white>=========[<gold>Economy Top<white>]========= Page: $page/$page_top");
    configurations.put("Messages.Money.TopEntry", "<white>$player has $amount");
    configurations.put("Messages.Money.InvalidFormat", "<red>I'm sorry, but the monetary value you've entered is wrong.");
    configurations.put("Messages.Money.ExceedsCurrencyMaximum", "<red>I'm sorry, but the monetary value you've entered exceeds the maximum possible balance.");
    configurations.put("Messages.Money.ExceedsPlayerMaximum", "<red>I'm sorry, but performing this transaction will place your balance over the maximum allowed.");
    configurations.put("Messages.Money.ExceedsOtherPlayerMaximum", "<red>I'm sorry, but performing this transaction will place $player's balance over the maximum allowed.");

    configurations.put("Messages.Transaction.Already", "<red>The transaction with the ID of $transaction has already been voided.");
    configurations.put("Messages.Transaction.Away", "<white>=====[<green>Missed Transactions<white>]===== $page/$page_top<newline>ID | Type");
    configurations.put("Messages.Transaction.AwayEntry", "<green>$id <white>| <green>$type");
    configurations.put("Messages.Transaction.AwayNone", "<red>No transactions occured while you were offline.");
    configurations.put("Messages.Transaction.History", "<white>=====[<green>Transactions<white>]===== $page/$page_top<newline>ID | Type | Initiator | Recipient");
    configurations.put("Messages.Transaction.HistoryEntry", "<green>$id <white>| <green>$type <white>| <green>$initiator <white>| <green>$recipient");
    configurations.put("Messages.Transaction.Invalid", "<red>There is no transaction with the ID of $transaction.");
    configurations.put("Messages.Transaction.Unable", "<red>Unable to void that transaction at this time.");
    configurations.put("Messages.Transaction.Voided", "<white>Successfully voided the transaction with the ID of <green>$transaction<white>.");

    configurations.put("Messages.Commands.Admin.Backup", "/tne backup - Creates a backup of all server data.");
    configurations.put("Messages.Commands.Admin.Balance", "/tne balance <player> [world] [currency] - Retrieves the balance of a player.<newline>- Player ~ The account owner.<newline>- World ~ The world to retrieve the balance from.<newline>- currency ~ The currency to retrieve the balance of.");
    configurations.put("Messages.Commands.Admin.Build", "//tne build - Displays the version of TNE currently running.");
    configurations.put("Messages.Commands.Admin.Caveats", "/tne caveats - Displays all known caveats for this version of TNE.");
    configurations.put("Messages.Commands.Admin.Create", "/tne create <player> [balance] - Creates a new economy account.<newline>- Player ~ The account owner.<newline>- Balance ~ The starting balance of the account.");
    configurations.put("Messages.Commands.Admin.Delete", "/tne delete <player> - Deletes a player account.<newline>- Player ~ The account owner.");
    configurations.put("Messages.Commands.Admin.Extract", "/tne extract - Extracts all player balances with their username attached for database-related debugging.");
    configurations.put("Messages.Commands.Admin.ID", "/tne id <player> - Retrieves a player's TNE UUID.<newline>- Player ~ The player you wish to discover the UUID of.");
    configurations.put("Messages.Commands.Admin.Menu", "/tne menu <player> - Opens a GUI for performing basic transactions on the specified player.<newline>-Player ~ The name/uuid of the player you wish to perform transactions with in the GUI.");
    configurations.put("Messages.Commands.Admin.Purge", "/tne purge - Deletes all player accounts that have the default balance");
    configurations.put("Messages.Commands.Admin.Recreate", "/tne recreate - Attempts to recreate database tables. WARNING: This will delete all data in the database.");
    configurations.put("Messages.Commands.Admin.Reload", "/tne reload <configuration> - Saves modifications made via command, and reloads a configuration file.<newline>- Configuration ~ The identifier of the configuration to reload. Default is all.");
    configurations.put("Messages.Commands.Admin.Reset", "/tne reset - Deletes all economy-related data from the database.");
    configurations.put("Messages.Commands.Admin.Restore", "/tne restore - Restores all balances that are located in extracted.yml after /tne extract.");
    configurations.put("Messages.Commands.Admin.Save", "/tne save - Force saves all TNE data.");
    configurations.put("Messages.Commands.Admin.Status", "/tne status <player> [status] - Displays, or sets, the current account status of an account.<newline>- Player ~ The account owner.");
    configurations.put("Messages.Commands.Admin.Upload", "/tne upload - Uploads the TNE debug & latest server log to pastebin.com, and provides a link to each.");
    configurations.put("Messages.Commands.Admin.Version", "/tne version - Displays the version of TNE currently running.");

    configurations.put("Messages.Commands.Config.Get", "/tnec get <node> [configuration] - Returns the value of a configuration.<newline>- Node ~ The configuration node to use.<newline>- Configuration ~ The configuration identifier to retrieve the value from. This may be retrieved automatically.");
    configurations.put("Messages.Commands.Config.Save", "/tnec save [configuration] - Saves modifications made to a configuration value via command.<newline>- Configuration ~ The configuration identifier to retrieve the value from.");
    configurations.put("Messages.Commands.Config.Set", "/tnec set <node> <value> [configuration] - Sets a configuration value. This will not save until you do /tnec save.<newline>");
    configurations.put("Messages.Commands.Config.TNEGet", "/tnec tneget <node> [world] [player] - Returns the value of a configuration after TNE takes into account player & world configurations.<newline>- Node ~ The configuration node to use.<newline>- World ~ The name of the world to use for parsing.<newline>- Player ~ The name of the world to use for parsing.");
    configurations.put("Messages.Commands.Config.Undo", "/tnec undo [configuration/all] - Undoes modifications made to configurations.<newline>- Configuration ~ The configuration identifier to retrieve the value from. This may be retrieved automatically.");

    configurations.put("Messages.Commands.Currency.Rename", "/currency rename <currency> <new name> - Renames a currency to a different name.");
    configurations.put("Messages.Commands.Currency.List", "/currency list [world] - Displays the currencies available for a world.<newline> - World ~ The world to use.");
    configurations.put("Messages.Commands.Currency.Tiers", "/currency tiers <currency> [world] - Displays the tiers for a currency.<newline>- currency ~ The currency to check.<newline>- World ~ The world that the currency belongs to.");

    configurations.put("Messages.Commands.Language.Current", "/language current - Displays your current language as set for your account.");
    configurations.put("Messages.Commands.Language.List", "/language list - Lists available languages on this server.");
    configurations.put("Messages.Commands.Language.Reload", "/language reload - Reloads all language files.");
    configurations.put("Messages.Commands.Language.Set", "/language set <name> - Sets your current language to the one specified.");

    configurations.put("Messages.Commands.Module.Download", "/tnem download <module> - Attempts to download the specified module.");
    configurations.put("Messages.Commands.Module.Info", "/tnem info <module> - Displays some information about a module.<newline>- Module ~ The module to look up.");
    configurations.put("Messages.Commands.Module.List", "/tnem list - Lists all loaded TNE modules.");
    configurations.put("Messages.Commands.Module.Load", "/tnem load <module> - Load a module from the modules directory.<newline>- Module ~ The module to load.");
    configurations.put("Messages.Commands.Module.Reload", "/tnem reload <module> - Reloads a module from the modules directory.<newline>- Module ~ The module to reload.");
    configurations.put("Messages.Commands.Module.Unload", "/tnem unload <module> - Unload a module from the server.<newline>- Module ~ The module to unload.");

    configurations.put("Messages.Commands.Money.Balance", "/money balance [world] [currency] - Displays your current holdings.");
    configurations.put("Messages.Commands.Money.Convert", "/money convert <amount> <to currency[:world]> [from currency[:world]] - Convert some of your holdings to another currency.");
    configurations.put("Messages.Commands.Money.Give", "/money give <player> <amount> [world] [currency] - Adds money into your economy, and gives it to a player.");
    configurations.put("Messages.Commands.Money.Note", "/money note <amount> [currency] - Makes your virtual currency physical, for storage/trading purposes.");
    configurations.put("Messages.Commands.Money.Pay", "/money pay <player> <amount> [currency] - Use some of your holdings to pay another player.");
    configurations.put("Messages.Commands.Money.Set", "/money set <player> <amount> [world] [currency] - Set the holdings of a player.");
    configurations.put("Messages.Commands.Money.Take", "/money take <player> <amount> [world] [currency] - Removes money from your economy, specifically from a player's balance.");
    configurations.put("Messages.Commands.Money.Top", "/money top [page] [currency:name] [world:world] [limit:#] - A list of players with the highest balances.<newline>[page] - The page number to view. Defaults to 1.<newline>[currency] - The name of the currency to get balances from. Defaults to world default. Use overall for all currencies.<newline>[world] - The world name you wish to filter, or all for every world. Defaults to current world. Use overall for all worlds.<newline>[limit] - Limit changes how many players are displayed. Defaults to 10.");

    configurations.put("Messages.Commands.Transaction.Away", "/transaction away [page:#] - Displays transactions that you missed since the last time you were on.");
  configurations.put("Messages.Commands.Transaction.History", "/transaction history [player:name] [page:#] [world:name/all] - See a detailed break down of your transaction history.<newline>- Page ~ The page number you wish to view.<newline>- World ~ The world name you wish to filter, or all for every world. Defaults to current world.<newline>- Player ~ Then name of the player's history you wish to see.");
    configurations.put("Messages.Commands.Transaction.Info", "/transaction info <uuid> - Displays information about a transaction.<newline>- UUID ~ The id of the transaction.");
    configurations.put("Messages.Commands.Transaction.Void", "/transaction void <uuid> - Undoes a previously completed transaction.<newline>- UUID ~ The id of the transaction.");

    configurations.put("Messages.World.Change", "<white>You have been charged <gold> $amount<white> for changing worlds.");
    configurations.put("Messages.World.ChangeFailed", "<red>I'm sorry, but you need at least <gold>$amount<red> to change worlds.");

    loadLanguages();

    super.load(configurationFile);
  }

  public void loadLanguages() {
    File directory = new File(TNE.instance().getDataFolder(), "languages");
    directory.mkdir();
    File[] langFiles = directory.listFiles((dir, name) -> name.endsWith(".yml"));

    if(langFiles != null) {
      for (File langFile : langFiles) {
        String name = langFile.getName().replace(".yml", "");
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(langFile);

        Language lang = new Language(name, configuration);

        Iterator it = configurations.entrySet().iterator();

        while(it.hasNext()) {
          Map.Entry<String, Object> entry = (Map.Entry)it.next();
          if (getConfiguration().contains(entry.getKey()) && configuration.contains(entry.getKey())) {
            lang.addTranslation(entry.getKey(), configuration.getString(entry.getKey()));
          }
        }
        TNE.debug("Loaded language: " + lang);
        languages.put(name, lang);
      }
    }
  }

  public Map<String, Language> getLanguages() {
    return languages;
  }

  @Override
  public Object getValue(String node, String world, String player) {
    TNE.debug("Checking for translation in languages.");
    String language = (player.trim().equalsIgnoreCase(""))? "Default" : TNE.manager().getAccount(IDFinder.getID(player)).getLanguage();

    if(languages.containsKey(language)) {
      Language lang = languages.get(language);
      if(lang.hasTranslation(node)) {
        TNE.debug("Returning translation for language  \"" + language + "\".");
        return lang.getTranslation(node);
      }
    }
    return super.getValue(node, world, player);
  }
}