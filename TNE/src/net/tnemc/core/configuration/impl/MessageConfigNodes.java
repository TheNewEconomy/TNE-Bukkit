package net.tnemc.core.configuration.impl;

import net.tnemc.core.configuration.IConfigNode;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public enum MessageConfigNodes implements IConfigNode {

  MESSAGES_HEADER {
    @Override
    public String getNode() {
      return "Messages";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The New Economy v0.1.0.0",
          "Author: creatorfromhell",
          "License: http://creativecommons.org/licenses/by-nc-nd/4.0/",
          "If you would like to contribute to the plugin",
          "you can do so via Github at https://github.com/TheNewEconomy/TNE-Bukkit",
          "To donate to the continued development of the plugin visit https://patreon.com/creatorfromhell",
          "All configurable messages in TNE",
          "Colour characters are supported, just use the colour code preceded by an ampersand character('&')",
          "An alternate colouring format is also viable, just oldtype out the name of the colour inside a less than and greater than symbol",
          "Colour chart",
          "-----------------",
          "&b or <aqua>",
          "&0 or <black>",
          "&9 or <blue>",
          "&3 or <dark_aqua>",
          "&1 or <dark_blue>",
          "&8 or <dark_gray>",
          "&2 or <dark_green>",
          "&5 or <dark_purple>",
          "&4 or <dark_red>",
          "&6 or <gold>",
          "&7 or <gray>",
          "&a or <green>",
          "&d or <purple>",
          "&c or <red>",
          "&f or <white>",
          "&e or <yellow>",
          "&k or <magic>",
          "&l or <bold>",
          "&o or <italic>",
          "&r or <reset>",
          "&m or <strike>",
          "&n or <underline>",
      };
    }
  },

  MESSAGE_COMMAND_HEADER {
    @Override
    public String getNode() {
      return "Messages.Command";
    }
  },

  MESSAGE_COMMAND_UNABLE {
    @Override
    public String getNode() {
      return "Messages.Command.Unable";
    }

    @Override
    public String getDefaultValue() {
      return "<red>I'm sorry, but you're not allowed to use that command.";
    }
  },

  MESSAGE_COMMAND_NONE {
    @Override
    public String getNode() {
      return "Messages.Command.None";
    }

    @Override
    public String getDefaultValue() {
      return "<yellow>Command $command $arguments could not be found! Try using $command help.";
    }
  },

  MESSAGE_COMMAND_INACTIVE {
    @Override
    public String getNode() {
      return "Messages.Command.InActive";
    }

    @Override
    public String getDefaultValue() {
      return "<red>Command $command $arguments has been deactivated! Try using $command help.";
    }
  },

  MESSAGE_COMMAND_CHARGE {
    @Override
    public String getNode() {
      return "Messages.Command.Charge";
    }

    @Override
    public String getDefaultValue() {
      return "<white>You have been charged <gold>$amount<white> for using $command.";
    }
  },

  MESSAGE_GENERAL_HEADER {
    @Override
    public String getNode() {
      return "Messages.General";
    }
  },

  MESSAGE_GENERAL_NOPERM {
    @Override
    public String getNode() {
      return "Messages.General.NoPerm";
    }

    @Override
    public String getDefaultValue() {
      return "<red>I'm sorry, but you do not have permission to do that.";
    }
  },

  MESSAGE_GENERAL_SAVED {
    @Override
    public String getNode() {
      return "Messages.General.Saved";
    }

    @Override
    public String getDefaultValue() {
      return "<yellow>Successfully saved all TNE Data!";
    }
  },

  MESSAGE_GENERAL_NOPLAYER {
    @Override
    public String getNode() {
      return "Messages.General.NoPlayer";
    }

    @Override
    public String getDefaultValue() {
      return "<red>Unable to locate player \"$player\"!";
    }
  },

  MESSAGE_GENERAL_DISABLED {
    @Override
    public String getNode() {
      return "Messages.General.Disabled";
    }

    @Override
    public String getDefaultValue() {
      return "<red>Economy features are currently disabled in this world!";
    }
  },

  MESSAGE_ITEM_HEADER {
    @Override
    public String getNode() {
      return "Messages.Item";
    }
  },

  MESSAGE_ITEM_INVALID {
    @Override
    public String getNode() {
      return "Messages.Item.Invalid";
    }

    @Override
    public String getDefaultValue() {
      return "<red>Invalid item name and/or durability combination entered.";
    }
  },

  MESSAGE_ITEM_INVALIDAMOUNT {
    @Override
    public String getNode() {
      return "Messages.Item.InvalidAmount";
    }

    @Override
    public String getDefaultValue() {
      return "<red>Invalid item amount entered.";
    }
  },

  MESSAGE_ADMIN_HEADER {
    @Override
    public String getNode() {
      return "Messages.Admin";
    }
  },

  MESSAGE_ADMIN_NOHOLDINGS {
    @Override
    public String getNode() {
      return "Messages.Admin.NoHoldings";
    }

    @Override
    public String getDefaultValue() {
      return "<red>$player has no holdings for the world \"$world\"!";
    }
  },

  MESSAGE_ADMIN_HOLDINGS {
    @Override
    public String getNode() {
      return "Messages.Admin.Holdings";
    }

    @Override
    public String getDefaultValue() {
      return "<white>$player currently has <gold>$amount <white>for world \"$world\"!";
    }
  },

  MESSAGE_ADMIN_NOTRANSACTIONS {
    @Override
    public String getNode() {
      return "Messages.Admin.NoTransactions";
    }

    @Override
    public String getDefaultValue() {
      return "<white>$player has no transactions to display.";
    }
  },

  MESSAGE_ADMIN_CONFIGURATION {
    @Override
    public String getNode() {
      return "Messages.Admin.Configuration";
    }

    @Override
    public String getDefaultValue() {
      return "<white>The value of $node is currently $value.";
    }
  },

  MESSAGE_ADMIN_SETCONFIGURATION {
    @Override
    public String getNode() {
      return "Messages.Admin.SetConfiguration";
    }

    @Override
    public String getDefaultValue() {
      return "<white>The value of $node has been set to $value.";
    }
  },

  MESSAGE_ADMIN_ID {
    @Override
    public String getNode() {
      return "Messages.Admin.ID";
    }

    @Override
    public String getDefaultValue() {
      return "<white>The UUID for $player is $id.";
    }
  },

  MESSAGE_ADMIN_EXISTS {
    @Override
    public String getNode() {
      return "Messages.Admin.Exists";
    }

    @Override
    public String getDefaultValue() {
      return "<red>A player with that name already exists.";
    }
  },

  MESSAGE_ADMIN_CREATED {
    @Override
    public String getNode() {
      return "Messages.Admin.Created";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Successfully created account for $player.";
    }
  },

  MESSAGE_ADMIN_DELETED {
    @Override
    public String getNode() {
      return "Messages.Admin.Deleted";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Successfully deleted account for $player.";
    }
  },

  MESSAGE_ADMIN_PURGEWORLD {
    @Override
    public String getNode() {
      return "Messages.Admin.PurgeWorld";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Successfully purged economy accounts in $world.";
    }
  },

  MESSAGE_ADMIN_STATUSCHANGE {
    @Override
    public String getNode() {
      return "Messages.Admin.StatusChange";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Status for $player has been changed to <green>$status<white>.";
    }
  },

  MESSAGE_ADMIN_STATUS {
    @Override
    public String getNode() {
      return "Messages.Admin.Status";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Status for $player is <green>$status<white>.";
    }
  },

  MESSAGE_ADMIN_RESET {
    @Override
    public String getNode() {
      return "Messages.Admin.Reset";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Performed an economy reset using these parameters - world = $world, currency = $currency, and player = $player.";
    }
  },

  MESSAGE_ACCOUNT_HEADER {
    @Override
    public String getNode() {
      return "Messages.Account";
    }
  },

  MESSAGE_ACCOUNT_LOCKED {
    @Override
    public String getNode() {
      return "Messages.Account.Locked";
    }

    @Override
    public String getDefaultValue() {
      return "<red>You can't do that with a locked account($player)!";
    }
  },

  MESSAGE_ACCOUNT_NOTRANSACTIONS {
    @Override
    public String getNode() {
      return "Messages.Account.NoTransactions";
    }

    @Override
    public String getDefaultValue() {
      return "<white>You have no transactions to display at this time.";
    }
  },

  MESSAGE_ACCOUNT_STATUSCHANGE {
    @Override
    public String getNode() {
      return "Messages.Account.StatusChange";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Your account's status has been changed to <green>$status<white>.";
    }
  },

  MESSAGE_CONFIGURATION_HEADER {
    @Override
    public String getNode() {
      return "Messages.Configuration";
    }
  },

  MESSAGE_CONFIGURATION_NOSUCH {
    @Override
    public String getNode() {
      return "Messages.Configuration.NoSuch";
    }

    @Override
    public String getDefaultValue() {
      return "The configuration node $node does not exist.";
    }
  },

  MESSAGE_CONFIGURATION_INVALID {
    @Override
    public String getNode() {
      return "Messages.Configuration.Invalid";
    }

    @Override
    public String getDefaultValue() {
      return "The value you specified for $node is invalid.";
    }
  },

  MESSAGE_CONFIGURATION_INVALIDFILE {
    @Override
    public String getNode() {
      return "Messages.Configuration.InvalidFile";
    }

    @Override
    public String getDefaultValue() {
      return "The configuration file you specified is invalid.";
    }
  },

  MESSAGE_CONFIGURATION_GET {
    @Override
    public String getNode() {
      return "Messages.Configuration.Get";
    }

    @Override
    public String getDefaultValue() {
      return "The value for node $node is $value.";
    }
  },

  MESSAGE_CONFIGURATION_SET {
    @Override
    public String getNode() {
      return "Messages.Configuration.Set";
    }

    @Override
    public String getDefaultValue() {
      return "Successfully set the value for $node to $value.";
    }
  },

  MESSAGE_CONFIGURATION_TNEGET {
    @Override
    public String getNode() {
      return "Messages.Configuration.TNEGet";
    }

    @Override
    public String getDefaultValue() {
      return "The parsed value for node $node is $value.";
    }
  },

  MESSAGE_CONFIGURATION_SAVED {
    @Override
    public String getNode() {
      return "Messages.Configuration.Saved";
    }

    @Override
    public String getDefaultValue() {
      return "Successfully saved all modified nodes for file $configuration.";
    }
  },

  MESSAGE_CONFIGURATION_SAVEDALL {
    @Override
    public String getNode() {
      return "Messages.Configuration.SavedAll";
    }

    @Override
    public String getDefaultValue() {
      return "Successfully saved all modified configurations.";
    }
  },

  MESSAGE_CONFIGURATION_UNDONE {
    @Override
    public String getNode() {
      return "Messages.Configuration.Undone";
    }

    @Override
    public String getDefaultValue() {
      return "Successfully undid all configuration modifications to $modified.";
    }
  },

  MESSAGE_CONFIGURATION_UNDONEALL {
    @Override
    public String getNode() {
      return "Messages.Configuration.UndoneAll";
    }

    @Override
    public String getDefaultValue() {
      return "Successfully undid all configuration modifications.";
    }
  },

  MESSAGE_CURRENCY_HEADER {
    @Override
    public String getNode() {
      return "Messages.Currency";
    }
  },

  MESSAGE_CURRENCY_LIST {
    @Override
    public String getNode() {
      return "Messages.Currency.List";
    }

    @Override
    public String getDefaultValue() {
      return "<white>The current currencies for $world are: $currencies.";
    }
  },

  MESSAGE_CURRENCY_TIERS {
    @Override
    public String getNode() {
      return "Messages.Currency.Tiers";
    }

    @Override
    public String getDefaultValue() {
      return "<white>The currency \"$currency\" currently has these tiers;<newline>Major: $major_tiers<newline>Minor: $minor_tiers";
    }
  },

  MESSAGE_CURRENCY_ALREADYEXISTS {
    @Override
    public String getNode() {
      return "Messages.Currency.AlreadyExists";
    }

    @Override
    public String getDefaultValue() {
      return "<white>A currency with the name $currency already exists in world $world.";
    }
  },

  MESSAGE_CURRENCY_RENAMED {
    @Override
    public String getNode() {
      return "Messages.Currency.Renamed";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Successfully renamed $currency in world $world to $new_Name.";
    }
  },

  MESSAGE_LANGUAGE_HEADER {
    @Override
    public String getNode() {
      return "Messages.Language";
    }
  },

  MESSAGE_LANGUAGE_CURRENT {
    @Override
    public String getNode() {
      return "Messages.Language.Current";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Your language is currently set as $language.";
    }
  },

  MESSAGE_LANGUAGE_LIST {
    @Override
    public String getNode() {
      return "Messages.Language.List";
    }

    @Override
    public String getDefaultValue() {
      return "<white>The available languages are $languages.";
    }
  },

  MESSAGE_LANGUAGE_NONE {
    @Override
    public String getNode() {
      return "Messages.Language.None";
    }

    @Override
    public String getDefaultValue() {
      return "<red>There is no language with the name \"$language\".";
    }
  },

  MESSAGE_LANGUAGE_RELOAD {
    @Override
    public String getNode() {
      return "Messages.Language.Reload";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Successfully reloaded all language files.";
    }
  },

  MESSAGE_LANGUAGE_SET {
    @Override
    public String getNode() {
      return "Messages.Language.Set";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Successfully set your language to $language.";
    }
  },

  MESSAGE_MODULE_HEADER {
    @Override
    public String getNode() {
      return "Messages.Module";
    }
  },

  MESSAGE_MODULE_INFO {
    @Override
    public String getNode() {
      return "Messages.Module.Info";
    }

    @Override
    public String getDefaultValue() {
      return "<white>==== Module Info for $module ====<newline>Author: $author<newline>Version: $version";
    }
  },

  MESSAGE_MODULE_INVALID {
    @Override
    public String getNode() {
      return "Messages.Module.Invalid";
    }

    @Override
    public String getDefaultValue() {
      return "<red>Unable to find a module with the name of \"$module\".";
    }
  },

  MESSAGE_MODULE_LIST {
    @Override
    public String getNode() {
      return "Messages.Module.List";
    }

    @Override
    public String getDefaultValue() {
      return "<white>This server is currently uses these TNE Modules: $modules.";
    }
  },

  MESSAGE_MODULE_LOADED {
    @Override
    public String getNode() {
      return "Messages.Module.Loaded";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Loaded module \"$module\" $version created by \"$author\".";
    }
  },

  MESSAGE_MODULE_RELOADED {
    @Override
    public String getNode() {
      return "Messages.Module.Reloaded";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Reloaded module \"$module\".";
    }
  },

  MESSAGE_MODULE_UNLOADED {
    @Override
    public String getNode() {
      return "Messages.Module.Unloaded";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Unloaded module \"$module\".";
    }
  },

  MESSAGE_MONEY_HEADER {
    @Override
    public String getNode() {
      return "Messages.Money";
    }
  },

  MESSAGE_MONEY_FAILED {
    @Override
    public String getNode() {
      return "Messages.Money.Failed";
    }

    @Override
    public String getDefaultValue() {
      return "<red>Unable to process your transaction at this time.";
    }
  },

  MESSAGE_MONEY_GIVEN {
    @Override
    public String getNode() {
      return "Messages.Money.Given";
    }

    @Override
    public String getDefaultValue() {
      return "<white>You were given <gold>$amount<white>.";
    }
  },

  MESSAGE_MONEY_RECEIVED {
    @Override
    public String getNode() {
      return "Messages.Money.Received";
    }

    @Override
    public String getDefaultValue() {
      return "<white>You were paid <gold>$amount <white> by <white> $player.";
    }
  },

  MESSAGE_MONEY_TAKEN {
    @Override
    public String getNode() {
      return "Messages.Money.Taken";
    }

    @Override
    public String getDefaultValue() {
      return "<white>$player took <gold>$amount<white> from you.";
    }
  },

  MESSAGE_MONEY_INSUFFICIENT {
    @Override
    public String getNode() {
      return "Messages.Money.Insufficient";
    }

    @Override
    public String getDefaultValue() {
      return "<red>I'm sorry, but you do not have <gold>$amount<red>.";
    }
  },

  MESSAGE_MONEY_HOLDINGSMULTI {
    @Override
    public String getNode() {
      return "Messages.Money.HoldingsMulti";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Your current balances for world \"$world\" are: ";
    }
  },

  MESSAGE_MONEY_HOLDINGSMULTISINGLE {
    @Override
    public String getNode() {
      return "Messages.Money.HoldingsMultiSingle";
    }

    @Override
    public String getDefaultValue() {
      return "<white>$currency: <gold>$amount";
    }
  },

  MESSAGE_MONEY_HOLDINGS {
    @Override
    public String getNode() {
      return "Messages.Money.Holdings";
    }

    @Override
    public String getDefaultValue() {
      return "<white>You currently have <gold>$amount<white> on you.";
    }
  },

  MESSAGE_MONEY_GAVE {
    @Override
    public String getNode() {
      return "Messages.Money.Gave";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Successfully gave $player <gold>$amount<white>.";
    }
  },

  MESSAGE_MONEY_RECIPIENTSET {
    @Override
    public String getNode() {
      return "Messages.Money.RecipientSet";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Your balance has been set to <gold>$amount<white> by $player.";
    }
  },

  MESSAGE_MONEY_SET {
    @Override
    public String getNode() {
      return "Messages.Money.Set";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Successfully set $player's balance to <gold>$amount<white>.";
    }
  },

  MESSAGE_MONEY_PAID {
    @Override
    public String getNode() {
      return "Messages.Money.Paid";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Successfully paid $player <gold>$amount<white>.";
    }
  },

  MESSAGE_MONEY_TOOK {
    @Override
    public String getNode() {
      return "Messages.Money.Took";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Successfully took <gold>$amount<white> from $player.";
    }
  },

  MESSAGE_MONEY_NEGATIVE {
    @Override
    public String getNode() {
      return "Messages.Money.Negative";
    }

    @Override
    public String getDefaultValue() {
      return "<red>Amount cannot be a negative value!";
    }
  },

  MESSAGE_MONEY_SELFPAY {
    @Override
    public String getNode() {
      return "Messages.Money.SelfPay";
    }

    @Override
    public String getDefaultValue() {
      return "<red>You can't pay yourself!";
    }
  },

  MESSAGE_MONEY_NOCURRENCY {
    @Override
    public String getNode() {
      return "Messages.Money.NoCurrency";
    }

    @Override
    public String getDefaultValue() {
      return "<red>The currency \"$currency\" could not be found in \"$world\".";
    }
  },

  MESSAGE_MONEY_NOTED {
    @Override
    public String getNode() {
      return "Messages.Money.Noted";
    }

    @Override
    public String getDefaultValue() {
      return "<white>A note has been given to you in the amount of <gold>$amount<white> for currency <green>$currency<white>.";
    }
  },

  MESSAGE_MONEY_NOTECLAIMED {
    @Override
    public String getNode() {
      return "Messages.Money.NoteClaimed";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Successfully claimed note for currency <green>$currency<white> in the amount of <gold>$amount<white>.<newline>Your new balance is <gold>$balance<white>.";
    }
  },

  MESSAGE_MONEY_NOTEFAILED {
    @Override
    public String getNode() {
      return "Messages.Money.NoteFailed";
    }

    @Override
    public String getDefaultValue() {
      return "<red>I'm sorry, but your attempt to claim that currency note failed!.";
    }
  },

  MESSAGE_MONEY_CONVERTED {
    @Override
    public String getNode() {
      return "Messages.Money.Converted";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Successfully exchanged \"<gold>$from_amount<white>\" to \"<gold>$amount<white>\".";
    }
  },

  MESSAGE_MONEY_TOP {
    @Override
    public String getNode() {
      return "Messages.Money.Top";
    }

    @Override
    public String getDefaultValue() {
      return "<white>=====[<gold>Economy Top<white>]===== $page/$page_top";
    }
  },

  MESSAGE_MONEY_INVALIDFORMAT {
    @Override
    public String getNode() {
      return "Messages.Money.InvalidFormat";
    }

    @Override
    public String getDefaultValue() {
      return "<red>I'm sorry, but the monetary value you've entered is wrong.";
    }
  },

  MESSAGE_MONEY_EXCEEDSCURRENCYMAXIMUM {
    @Override
    public String getNode() {
      return "Messages.Money.ExceedsCurrencyMaximum";
    }

    @Override
    public String getDefaultValue() {
      return "<red>I'm sorry, but the monetary value you've entered exceeds the maximum possible balance.";
    }
  },

  MESSAGE_MONEY_EXCEEDSPLAYERMAXIMUM {
    @Override
    public String getNode() {
      return "Messages.Money.ExceedsPlayerMaximum";
    }

    @Override
    public String getDefaultValue() {
      return "<red>I'm sorry, but performing this transaction will place your balance over the maximum allowed.";
    }
  },

  MESSAGE_MONEY_EXCEEDSOTHERPLAYERMAXIMUM {
    @Override
    public String getNode() {
      return "Messages.Money.ExceedsOtherPlayerMaximum";
    }

    @Override
    public String getDefaultValue() {
      return "<red>I'm sorry, but performing this transaction will place $player's balance over the maximum allowed.";
    }
  },

  MESSAGE_TRANSACTION_HEADER {
    @Override
    public String getNode() {
      return "Messages.Transaction";
    }
  },

  MESSAGE_TRANSACTION_ALREADY {
    @Override
    public String getNode() {
      return "Messages.Transaction.Already";
    }

    @Override
    public String getDefaultValue() {
      return "<red>The transaction with the ID of $transaction has already been voided.";
    }
  },

  MESSAGE_TRANSACTION_AWAY {
    @Override
    public String getNode() {
      return "Messages.Transaction.Away";
    }

    @Override
    public String getDefaultValue() {
      return "<white>=====[<green>Missed Transactions<white>]===== $page/$page_top<newline>ID | Type";
    }
  },

  MESSAGE_TRANSACTION_AWAYENTRY {
    @Override
    public String getNode() {
      return "Messages.Transaction.AwayEntry";
    }

    @Override
    public String getDefaultValue() {
      return "<green>$id <white>| <green>$type";
    }
  },

  MESSAGE_TRANSACTION_AWAYNONE {
    @Override
    public String getNode() {
      return "Messages.Transaction.AwayNone";
    }

    @Override
    public String getDefaultValue() {
      return "<red>No transactions occured while you were offline.";
    }
  },

  MESSAGE_TRANSACTION_HISTORY {
    @Override
    public String getNode() {
      return "Messages.Transaction.History";
    }

    @Override
    public String getDefaultValue() {
      return "<white>=====[<green>Transactions<white>]===== $page/$page_top<newline>ID | Type | Initiator | Recipient";
    }
  },

  MESSAGE_TRANSACTION_HISTORYENTRY {
    @Override
    public String getNode() {
      return "Messages.Transaction.HistoryEntry";
    }

    @Override
    public String getDefaultValue() {
      return "<green>$id <white>| <green>$type <white>| <green>$initiator <white>| <green>$recipient";
    }
  },

  MESSAGE_TRANSACTION_INFO {
    @Override
    public String getNode() {
      return "Messages.Transaction.Info";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Transaction Information:<newline><white>ID: <green>$id<newline><white>Type: <green>$type";
    }
  },

  MESSAGE_TRANSACTION_INVALID {
    @Override
    public String getNode() {
      return "Messages.Transaction.Invalid";
    }

    @Override
    public String getDefaultValue() {
      return "<red>There is no transaction with the ID of $transaction.";
    }
  },

  MESSAGE_TRANSACTION_UNABLE {
    @Override
    public String getNode() {
      return "Messages.Transaction.Unable";
    }

    @Override
    public String getDefaultValue() {
      return "<red>Unable to void that transaction at this time.";
    }
  },

  MESSAGE_TRANSACTION_VOIDED {
    @Override
    public String getNode() {
      return "Messages.Transaction.Voided";
    }

    @Override
    public String getDefaultValue() {
      return "<white>Successfully voided the transaction with the ID of <green>$transaction<white>.";
    }
  },

  MESSAGE_COMMANDS_HEADER {
    @Override
    public String getNode() {
      return "Messages.Commands";
    }
  },

  MESSAGE_COMMANDS_ADMIN_HEADER {
    @Override
    public String getNode() {
      return "Messages.Commands.Admin";
    }
  },

  MESSAGE_COMMANDS_ADMIN_BACKUP {
    @Override
    public String getNode() {
      return "Messages.Commands.Admin.Backup";
    }

    @Override
    public String getDefaultValue() {
      return "/tne backup - Creates a backup of all server data.";
    }
  },

  MESSAGE_COMMANDS_ADMIN_BALANCE {
    @Override
    public String getNode() {
      return "Messages.Commands.Admin.Balance";
    }

    @Override
    public String getDefaultValue() {
      return "/tne balance <player> [world] [currency] - Retrieves the balance of a player.<newline>- Player ~ The account owner.<newline>- World ~ The world to retrieve the balance from.<newline>- currency ~ The currency to retrieve the balance of.";
    }
  },

  MESSAGE_COMMANDS_ADMIN_BUILD {
    @Override
    public String getNode() {
      return "Messages.Commands.Admin.Build";
    }

    @Override
    public String getDefaultValue() {
      return "/tne build - Displays the version of TNE currently running.";
    }
  },

  MESSAGE_COMMANDS_ADMIN_CAVEATS {
    @Override
    public String getNode() {
      return "Messages.Commands.Admin.Caveats";
    }

    @Override
    public String getDefaultValue() {
      return "/tne caveats - Displays all known caveats for this version of TNE.";
    }
  },

  MESSAGE_COMMANDS_ADMIN_CREATE {
    @Override
    public String getNode() {
      return "Messages.Commands.Admin.Create";
    }

    @Override
    public String getDefaultValue() {
      return "/tne create <player> [balance] - Creates a new economy account.<newline>- Player ~ The account owner.<newline>- Balance ~ The starting balance of the account.";
    }
  },

  MESSAGE_COMMANDS_ADMIN_DELETE {
    @Override
    public String getNode() {
      return "Messages.Commands.Admin.Delete";
    }

    @Override
    public String getDefaultValue() {
      return "/tne delete <player> - Deletes a player account.<newline>- Player ~ The account owner.";
    }
  },

  MESSAGE_COMMANDS_ADMIN_EXTRACT {
    @Override
    public String getNode() {
      return "Messages.Commands.Admin.Extract";
    }

    @Override
    public String getDefaultValue() {
      return "/tne extract - Extracts all player balances with their username attached for database-related debugging.";
    }
  },

  MESSAGE_COMMANDS_ADMIN_ID {
    @Override
    public String getNode() {
      return "Messages.Commands.Admin.ID";
    }

    @Override
    public String getDefaultValue() {
      return "/tne id <player> - Retrieves a player's TNE UUID.<newline>- Player ~ The player you wish to discover the UUID of.";
    }
  },

  MESSAGE_COMMANDS_ADMIN_MENU {
    @Override
    public String getNode() {
      return "Messages.Commands.Admin.Menu";
    }

    @Override
    public String getDefaultValue() {
      return "/tne menu <player> - Opens a GUI for performing basic transactions on the specified player.<newline>-Player ~ The name/uuid of the player you wish to perform transactions with in the GUI.";
    }
  },

  MESSAGE_COMMANDS_ADMIN_PURGE {
    @Override
    public String getNode() {
      return "Messages.Commands.Admin.Purge";
    }

    @Override
    public String getDefaultValue() {
      return "/tne purge - Deletes all player accounts that have the default balance";
    }
  },

  MESSAGE_COMMANDS_ADMIN_RECREATE {
    @Override
    public String getNode() {
      return "Messages.Commands.Admin.Recreate";
    }

    @Override
    public String getDefaultValue() {
      return "/tne recreate - Attempts to recreate database tables. WARNING: This will delete all data in the database.";
    }
  },

  MESSAGE_COMMANDS_ADMIN_RELOAD {
    @Override
    public String getNode() {
      return "Messages.Commands.Admin.Reload";
    }

    @Override
    public String getDefaultValue() {
      return "/tne reload <configuration> - Saves modifications made via command, and reloads a configuration file.<newline>- Configuration ~ The identifier of the configuration to reload. Default is all.";
    }
  },

  MESSAGE_COMMANDS_ADMIN_RESET {
    @Override
    public String getNode() {
      return "Messages.Commands.Admin.Reset";
    }

    @Override
    public String getDefaultValue() {
      return "/tne reset - Deletes all economy-related data from the database.";
    }
  },

  MESSAGE_COMMANDS_ADMIN_RESTORE {
    @Override
    public String getNode() {
      return "Messages.Commands.Admin.Restore";
    }

    @Override
    public String getDefaultValue() {
      return "/tne restore - Restores all balances that are located in extracted.yml after /tne extract.";
    }
  },

  MESSAGE_COMMANDS_ADMIN_SAVE {
    @Override
    public String getNode() {
      return "Messages.Commands.Admin.Save";
    }

    @Override
    public String getDefaultValue() {
      return "/tne save - Force saves all TNE data.";
    }
  },

  MESSAGE_COMMANDS_ADMIN_STATUS {
    @Override
    public String getNode() {
      return "Messages.Commands.Admin.Status";
    }

    @Override
    public String getDefaultValue() {
      return "/tne status <player> [status] - Displays, or sets, the current account status of an account.<newline>- Player ~ The account owner.";
    }
  },

  MESSAGE_COMMANDS_ADMIN_UPLOAD {
    @Override
    public String getNode() {
      return "Messages.Commands.Admin.Upload";
    }

    @Override
    public String getDefaultValue() {
      return "/tne upload - Uploads the TNE debug & latest server log to pastebin.com, and provides a link to each.";
    }
  },

  MESSAGE_COMMANDS_ADMIN_VERSION {
    @Override
    public String getNode() {
      return "Messages.Commands.Admin.Version";
    }

    @Override
    public String getDefaultValue() {
      return "/tne version - Displays the version of TNE currently running.";
    }
  },

  MESSAGE_COMMANDS_CONFIG_HEADER {
    @Override
    public String getNode() {
      return "Messages.Commands.Config";
    }
  },

  MESSAGE_COMMANDS_CONFIG_GET {
    @Override
    public String getNode() {
      return "Messages.Commands.Config.Get";
    }

    @Override
    public String getDefaultValue() {
      return "/tnec get <node> [configuration] - Returns the value of a configuration.<newline>- Node ~ The configuration node to use.<newline>- Configuration ~ The configuration identifier to retrieve the value from. This may be retrieved automatically.";
    }
  },

  MESSAGE_COMMANDS_CONFIG_SAVE {
    @Override
    public String getNode() {
      return "Messages.Commands.Config.Save";
    }

    @Override
    public String getDefaultValue() {
      return "/tnec save [configuration] - Saves modifications made to a configuration value via command.<newline>- Configuration ~ The configuration identifier to retrieve the value from.";
    }
  },

  MESSAGE_COMMANDS_CONFIG_SET {
    @Override
    public String getNode() {
      return "Messages.Commands.Config.Set";
    }

    @Override
    public String getDefaultValue() {
      return "/tnec set <node> <value> [configuration] - Sets a configuration value. This will not save until you do /tnec save.";
    }
  },

  MESSAGE_COMMANDS_CONFIG_TNEGET {
    @Override
    public String getNode() {
      return "Messages.Commands.Config.TNEGet";
    }

    @Override
    public String getDefaultValue() {
      return "/tnec tneget <node> [world] [player] - Returns the value of a configuration after TNE takes into account player & world configurations.<newline>- Node ~ The configuration node to use.<newline>- World ~ The name of the world to use for parsing.<newline>- Player ~ The name of the world to use for parsing.";
    }
  },

  MESSAGE_COMMANDS_CONFIG_UNDO {
    @Override
    public String getNode() {
      return "Messages.Commands.Config.Undo";
    }

    @Override
    public String getDefaultValue() {
      return "/tnec undo [configuration/all] - Undoes modifications made to configurations.<newline>- Configuration ~ The configuration identifier to retrieve the value from. This may be retrieved automatically.";
    }
  },

  MESSAGE_COMMANDS_CURRENCY_HEADER {
    @Override
    public String getNode() {
      return "Messages.Commands.Currency";
    }
  },

  MESSAGE_COMMANDS_CURRENCY_RENAME {
    @Override
    public String getNode() {
      return "Messages.Commands.Currency.Rename";
    }

    @Override
    public String getDefaultValue() {
      return "/currency rename <currency> <new name> - Renames a currency to a different name.";
    }
  },

  MESSAGE_COMMANDS_CURRENCY_LIST {
    @Override
    public String getNode() {
      return "Messages.Commands.Currency.List";
    }

    @Override
    public String getDefaultValue() {
      return "/currency list [world] - Displays the currencies available for a world.<newline> - World ~ The world to use.";
    }
  },

  MESSAGE_COMMANDS_CURRENCY_TIERS {
    @Override
    public String getNode() {
      return "Messages.Commands.Currency.Tiers";
    }

    @Override
    public String getDefaultValue() {
      return "/currency tiers [currency] [world] - Displays the tiers for a currency.<newline>- currency ~ The Currency to check.<newline>- World ~ The world that the Currency belongs to.";
    }
  },

  MESSAGE_COMMANDS_LANGUAGE_HEADER {
    @Override
    public String getNode() {
      return "Messages.Commands.Language";
    }
  },

  MESSAGE_COMMANDS_LANGUAGE_CURRENT {
    @Override
    public String getNode() {
      return "Messages.Commands.Language.Current";
    }

    @Override
    public String getDefaultValue() {
      return "/language current - Displays your current language as set for your account.";
    }
  },

  MESSAGE_COMMANDS_LANGUAGE_LIST {
    @Override
    public String getNode() {
      return "Messages.Commands.Language.List";
    }

    @Override
    public String getDefaultValue() {
      return "/language list - Lists available languages on this server.";
    }
  },

  MESSAGE_COMMANDS_LANGUAGE_RELOAD {
    @Override
    public String getNode() {
      return "Messages.Commands.Language.Reload";
    }

    @Override
    public String getDefaultValue() {
      return "/language reload - Reloads all language files.";
    }
  },

  MESSAGE_COMMANDS_LANGUAGE_SET {
    @Override
    public String getNode() {
      return "Messages.Commands.Language.Set";
    }

    @Override
    public String getDefaultValue() {
      return "/language set <name> - Sets your current language to the one specified.";
    }
  },

  MESSAGE_COMMANDS_MODULE_HEADER {
    @Override
    public String getNode() {
      return "Messages.Commands.Module";
    }
  },

  MESSAGE_COMMANDS_MODULE_INFO {
    @Override
    public String getNode() {
      return "Messages.Commands.Module.Info";
    }

    @Override
    public String getDefaultValue() {
      return "/tnem info <module> - Displays some information about a module.<newline>- Module ~ The module to look up.";
    }
  },

  MESSAGE_COMMANDS_MODULE_LIST {
    @Override
    public String getNode() {
      return "Messages.Commands.Module.List";
    }

    @Override
    public String getDefaultValue() {
      return "/tnem list - Lists all loaded TNE modules.";
    }
  },

  MESSAGE_COMMANDS_MODULE_LOAD {
    @Override
    public String getNode() {
      return "Messages.Commands.Module.Load";
    }

    @Override
    public String getDefaultValue() {
      return "/tnem load <module> - Load a module from the modules directory.<newline>- Module ~ The module to load.";
    }
  },

  MESSAGE_COMMANDS_MODULE_RELOAD {
    @Override
    public String getNode() {
      return "Messages.Commands.Module.Reload";
    }

    @Override
    public String getDefaultValue() {
      return "/tnem reload <module> - Reloads a module from the modules directory.<newline>- Module ~ The module to reload.";
    }
  },

  MESSAGE_COMMANDS_MODULE_UNLOAD {
    @Override
    public String getNode() {
      return "Messages.Commands.Module.Unload";
    }

    @Override
    public String getDefaultValue() {
      return "/tnem unload <module> - Unload a module from the server.<newline>- Module ~ The module to unload.";
    }
  },

  MESSAGE_COMMANDS_MONEY_HEADER {
    @Override
    public String getNode() {
      return "Messages.Commands.Money";
    }
  },

  MESSAGE_COMMANDS_MONEY_BALANCE {
    @Override
    public String getNode() {
      return "Messages.Commands.Money.Balance";
    }

    @Override
    public String getDefaultValue() {
      return "/money balance [world] [currency] - Displays your current holdings.";
    }
  },

  MESSAGE_COMMANDS_MONEY_CONVERT {
    @Override
    public String getNode() {
      return "Messages.Commands.Money.Convert";
    }

    @Override
    public String getDefaultValue() {
      return "/money convert <amount> <to currency[:world]> [from currency[:world]] - Convert some of your holdings to another currency.";
    }
  },

  MESSAGE_COMMANDS_MONEY_GIVE {
    @Override
    public String getNode() {
      return "Messages.Commands.Money.Give";
    }

    @Override
    public String getDefaultValue() {
      return "/money give <player> <amount> [world] [currency] - Adds money into your economy, and gives it to a player.";
    }
  },

  MESSAGE_COMMANDS_MONEY_NOTE {
    @Override
    public String getNode() {
      return "Messages.Commands.Money.Note";
    }

    @Override
    public String getDefaultValue() {
      return "/money note <amount> [currency] - Makes your virtual currency physical, for storage/trading purposes.";
    }
  },

  MESSAGE_COMMANDS_MONEY_PAY {
    @Override
    public String getNode() {
      return "Messages.Commands.Money.Pay";
    }

    @Override
    public String getDefaultValue() {
      return "/money pay <player> <amount> [currency] - Use some of your holdings to pay another player.";
    }
  },

  MESSAGE_COMMANDS_MONEY_SET {
    @Override
    public String getNode() {
      return "Messages.Commands.Money.Set";
    }

    @Override
    public String getDefaultValue() {
      return "/money set <player> <amount> [world] [currency] - Set the holdings of a player.";
    }
  },

  MESSAGE_COMMANDS_MONEY_TAKE {
    @Override
    public String getNode() {
      return "Messages.Commands.Money.Take";
    }

    @Override
    public String getDefaultValue() {
      return "/money take <player> <amount> [world] [currency] - Removes money from your economy, specifically from a player's balance.";
    }
  },

  MESSAGE_COMMANDS_MONEY_TOP {
    @Override
    public String getNode() {
      return "Messages.Commands.Money.Top";
    }

    @Override
    public String getDefaultValue() {
      return "/money top [page] [limit:#] - A list of players with the highest balances.<newline>[limit] - Limit changes how many players are displayed. Defaults to 10.";
    }
  },

  MESSAGE_COMMANDS_TRANSACTION_HEADER {
    @Override
    public String getNode() {
      return "Messages.Commands.Transaction";
    }
  },

  MESSAGE_COMMANDS_TRANSACTION_AWAY {
    @Override
    public String getNode() {
      return "Messages.Commands.Transaction.Away";
    }

    @Override
    public String getDefaultValue() {
      return "/transaction away [page #] - Displays transactions that you missed since the last time you were on.";
    }
  },

  MESSAGE_COMMANDS_TRANSACTION_HISTORY {
    @Override
    public String getNode() {
      return "Messages.Commands.Transaction.History";
    }

    @Override
    public String getDefaultValue() {
      return "/transaction history [page:#] [world:name/all] - See a detailed break down of your transaction history.<newline>- Page ~ The page number you wish to view.<newline>- World ~ The world name you wish to filter, or all for every world. Defaults to current world.";
    }
  },

  MESSAGE_COMMANDS_TRANSACTION_INFO {
    @Override
    public String getNode() {
      return "Messages.Commands.Transaction.Info";
    }

    @Override
    public String getDefaultValue() {
      return "/transaction info <uuid> - Displays information about a transaction.<newline>- UUID ~ The id of the transaction.";
    }
  },

  MESSAGE_COMMANDS_TRANSACTION_VOID {
    @Override
    public String getNode() {
      return "Messages.Commands.Transaction.Void";
    }

    @Override
    public String getDefaultValue() {
      return "/transaction void <uuid> - Undoes a previously completed transaction.<newline>- UUID ~ The id of the transaction.";
    }
  },

  MESSAGE_WORLD_HEADER {
    @Override
    public String getNode() {
      return "Messages.WORLD";
    }
  },

  MESSAGE_WORLD_CHANGE {
    @Override
    public String getNode() {
      return "Messages.Change";
    }

    @Override
    public String getDefaultValue() {
      return "<red>You have been charged <gold> $amount<red> for changing worlds.";
    }
  },

  MESSAGE_WORLD_CHANGEFAILED {
    @Override
    public String getNode() {
      return "Messages.ChangeFailed";
    }

    @Override
    public String getDefaultValue() {
      return "<red>I'm sorry, but you need at least <gold>$amount<red> to change worlds.";
    }
  },
}