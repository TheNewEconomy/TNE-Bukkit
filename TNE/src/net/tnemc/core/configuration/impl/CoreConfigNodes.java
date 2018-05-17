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
public enum CoreConfigNodes implements IConfigNode {

  CORE_HEADER {
    @Override
    public String getNode() {
      return "Core";
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
      };
    }
  },

  CORE_UUID {
    @Override
    public String getNode() {
      return "Core.UUID";
    }

    @Override
    public String getDefaultValue() {
      return "true";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Whether or not to enable UUID support(results may vary if turned off).",
      };
    }
  },

  CORE_MULTIWORLD {
    @Override
    public String getNode() {
      return "Core.Multiworld";
    }

    @Override
    public String getDefaultValue() {
      return "false";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Whether or not players should have different balances in different worlds.",
      };
    }
  },

  CORE_METRICS {
    @Override
    public String getNode() {
      return "Core.Metrics";
    }

    @Override
    public String getDefaultValue() {
      return "true";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Whether or not to enable plugin metrics. This allows us to know how many servers are running TNE,",
          "what TNE version, java version, etc."
      };
    }
  },

  SERVER_HEADER {
    @Override
    public String getNode() {
      return "Core.Server";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "All configurations relating to the server in general."
      };
    }
  },

  SERVER_MENUMATERIAL {
    @Override
    public String getNode() {
      return "Core.Server.MenuMaterial";
    }

    @Override
    public String getDefaultValue() {
      return "GOLD_INGOT";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The material to use to activate the TNE Action Menu."
      };
    }
  },

  SERVER_MOBDROP {
    @Override
    public String getNode() {
      return "Core.Server.MobDrop";
    }

    @Override
    public String getDefaultValue() {
      return "true";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Whether or not mob drops that are a currency item should be disabled."
      };
    }
  },

  SERVER_CONSOLIDATE {
    @Override
    public String getNode() {
      return "Core.Server.Consolidate";
    }

    @Override
    public String getDefaultValue() {
      return "false";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Whether or not to consolidate item-based currencies automatically in the least amount of items possible.",
          "Example: 4 quarters will turn into 1 dollar(USA currency)."
      };
    }
  },

  SERVER_NAME {
    @Override
    public String getNode() {
      return "Core.Server.Name";
    }

    @Override
    public String getDefaultValue() {
      return "Main Server";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The name of this server for data-related purposes. Max length is 100 characters."
      };
    }
  },

  SERVER_ACCOUNT_HEADER {
    @Override
    public String getNode() {
      return "Core.Server.Account";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "All configurations relating to the server's economy account."
      };
    }
  },

  SERVER_ACCOUNT_ENABLED {
    @Override
    public String getNode() {
      return "Core.Server.Account.Enabled";
    }

    @Override
    public String getDefaultValue() {
      return "true";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Whether or not the server account has been enabled."
      };
    }
  },

  SERVER_ACCOUNT_NAME {
    @Override
    public String getNode() {
      return "Core.Server.Account.Name";
    }

    @Override
    public String getDefaultValue() {
      return "Server_Account";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The name of the server account. Max length is 100 characters."
      };
    }
  },

  SERVER_ACCOUNT_BALANCE {
    @Override
    public String getNode() {
      return "Core.Server.Account.Balance";
    }

    @Override
    public String getDefaultValue() {
      return "500";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The starting balance for the server account."
      };
    }
  },

  SERVER_THIRDPARTY_HEADER {
    @Override
    public String getNode() {
      return "Core.Server.ThirdParty";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Configurations relating to third-party support."
      };
    }
  },

  SERVER_THIRDPARTY_TOWN {
    @Override
    public String getNode() {
      return "Core.Server.ThirdParty.Town";
    }

    @Override
    public String getDefaultValue() {
      return "town-";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The prefix used by the Towny plugin to denote town UUIDs."
      };
    }
  },

  SERVER_THIRDPARTY_NATION {
    @Override
    public String getNode() {
      return "Core.Server.ThirdParty.Nation";
    }

    @Override
    public String getDefaultValue() {
      return "nation-";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The prefix used by the Towny plugin to denote nation UUIDs."
      };
    }
  },

  SERVER_THIRDPARTY_FACTION {
    @Override
    public String getNode() {
      return "Core.Server.ThirdParty.Faction";
    }

    @Override
    public String getDefaultValue() {
      return "faction-";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The prefix used by the Factions plugin to denote faction UUIDs."
      };
    }
  },

  SERVER_THIRDPARTY_MCMMO {
    @Override
    public String getNode() {
      return "Core.Server.ThirdParty.McMMORewards";
    }

    @Override
    public String getDefaultValue() {
      return "true";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Whether or not mcmmo skill rewards that are a currency item should be disabled."
      };
    }
  },
  
  COMMANDS_HEADER {
    @Override
    public String getNode() {
      return "Core.Commands";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "All configurations relating to TNE commands."
      };
    }
  },

  COMMANDS_TRIGGERS {
    @Override
    public String getNode() {
      return "Core.Commands.Triggers";
    }

    @Override
    public String getDefaultValue() {
      return "/";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The command trigger(s) used to identify what is and isn't a command.",
          "To use multiple triggers, separate with comma, i.e. /,! will allow /command and !command."
      };
    }
  },

  COMMANDS_PAYSHORT {
    @Override
    public String getNode() {
      return "Core.Commands.PayShort";
    }

    @Override
    public String getDefaultValue() {
      return "true";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Whether or not players should be able to use /pay instead of /money pay."
      };
    }
  },

  COMMANDS_BALANCESHORT {
    @Override
    public String getNode() {
      return "Core.Commands.BalanceShort";
    }

    @Override
    public String getDefaultValue() {
      return "true";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Whether or not players should be able to use /balance instead of /money."
      };
    }
  },

  COMMANDS_TOPSHORT {
    @Override
    public String getNode() {
      return "Core.Commands.TopShort";
    }

    @Override
    public String getDefaultValue() {
      return "true";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Whether or not players should be able to use /baltop instead of /money top."
      };
    }
  },

  UPDATE_HEADER {
    @Override
    public String getNode() {
      return "Core.Update";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "All configurations relating to update checking."
      };
    }
  },

  UPDATE_CHECK {
    @Override
    public String getNode() {
      return "Core.Update.Check";
    }

    @Override
    public String getDefaultValue() {
      return "true";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Whether or not TNE should check if the server is using the latest build."
      };
    }
  },

  UPDATE_NOTIFY {
    @Override
    public String getNode() {
      return "Core.Update.Notify";
    }

    @Override
    public String getDefaultValue() {
      return "true";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Whether or not admins(anyone with perm. node tne.admin) should be notified on login if TNE is outdated."
      };
    }
  },

  TRANSACTION_HEADER {
    @Override
    public String getNode() {
      return "Core.Transactions";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "All configurations relating to the transaction system."
      };
    }
  },

  TRANSACTION_FORMAT {
    @Override
    public String getNode() {
      return "Core.Transactions.Format";
    }

    @Override
    public String getDefaultValue() {
      return "M, d y";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The time format to use when displaying transaction history data.."
      };
    }
  },

  TRANSACTION_TIMEZONE {
    @Override
    public String getNode() {
      return "Core.Transactions.Timezone";
    }

    @Override
    public String getDefaultValue() {
      return "US/Eastern";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The timezone to use for transactions."
      };
    }
  },

  AUTOSAVER_HEADER {
    @Override
    public String getNode() {
      return "Core.AutoSave";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "All configurations relating to the data auto saver."
      };
    }
  },

  AUTOSAVER_ENABLED {
    @Override
    public String getNode() {
      return "Core.AutoSave.Enabled";
    }

    @Override
    public String getDefaultValue() {
      return "true";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Whether or not the auto saver is enabled(will auto save player data)."
      };
    }
  },

  AUTOSAVER_INTERVAL {
    @Override
    public String getNode() {
      return "Core.AutoSaver.Interval";
    }

    @Override
    public String getDefaultValue() {
      return "600";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The interval at which the auto saver will save data(in seconds)."
      };
    }
  },

  CURRENCY_HEADER {
    @Override
    public String getNode() {
      return "Core.Currency";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "All configurations relating to currency."
      };
    }
  },

  CURRENCY_DEFAULT_HEADER {
    @Override
    public String getNode() {
      return "Core.Currency.Default";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "A default currency for example purposes."
      };
    }
  },

  CURRENCY_DEFAULT_NAME_HEADER {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Name";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Configurations relating to this currency's name for formatting purposes.",
          "Max length is 100 characters."
      };
    }
  },

  CURRENCY_DEFAULT_NAME_MAJOR_HEADER {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Name.Major";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Configurations relating to the major name."
      };
    }
  },

  CURRENCY_DEFAULT_NAME_MAJOR_SINGLE {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Name.Major.Single";
    }

    @Override
    public String getDefaultValue() {
      return "Dollar";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The singular form of this currency's name."
      };
    }
  },

  CURRENCY_DEFAULT_NAME_MAJOR_PLURAL {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Name.Major.Plural";
    }

    @Override
    public String getDefaultValue() {
      return "Dollars";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The plural form of this currency's name."
      };
    }
  },

  CURRENCY_DEFAULT_NAME_MINOR_HEADER {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Name.Minor";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Configurations relating to the minor name."
      };
    }
  },

  CURRENCY_DEFAULT_NAME_MINOR_SINGLE {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Name.Minor.Single";
    }

    @Override
    public String getDefaultValue() {
      return "Cent";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The singular form of this currency's name."
      };
    }
  },

  CURRENCY_DEFAULT_NAME_MINOR_PLURAL {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Name.Minor.Plural";
    }

    @Override
    public String getDefaultValue() {
      return "Cents";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The plural form of this currency's name."
      };
    }
  },

  CURRENCY_DEFAULT_DISABLED {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Disabled";
    }

    @Override
    public String getDefaultValue() {
      return "false";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Whether or not this currency is disabled."
      };
    }
  },

  CURRENCY_DEFAULT_FORMAT {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Format";
    }

    @Override
    public String getDefaultValue() {
      return "<symbol><major.amount><decimal><minor.amount>";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The format to use when outputting this currency into chat.",
          "The variables you're able to use here.",
          "<symbol> - The currency's symbol.",
          "<decimal> - The currency's currency.",
          "<major> - A combination of the TNECurrency's major amount and name.",
          "<minor> - A combination of the currency's minor amount and name.",
          "<major.name> - The currency's major name.",
          "<minor.name> - The currency's minor name.",
          "<major.amount> - The currency's major amount.",
          "<minor.amount> - The currency's minor amount.",
          "<short.amount> - The currency's shortened amount.",
          "<shorten> - Added to make the outputted value shortened.",
          "If shortened, it will ignore the value of Format.",
          "It's also possible to include all colour variables from messages.yml.",
          "Example: <major> and <minor>.",
      };
    }
  },

  CURRENCY_DEFAULT_MAXBALANCE {
    @Override
    public String getNode() {
      return "Core.Currency.Default.MaxBalance";
    }

    @Override
    public String getDefaultValue() {
      return "900000000000000000000000000000000000";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The maximum balance possible for this currency.",
          "Maximum possible value is 900 Decillion or 900000000000000000000000000000000000",
      };
    }
  },

  CURRENCY_DEFAULT_PREFIXES {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Prefixes";
    }

    @Override
    public String getDefaultValue() {
      return "kMGTPEZYXWV";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The SI Prefixes used when <shorten> is used for the TNECurrency's format.",
          "TNE's default prefixes are based off of the Internation Bureau of Weights and Measures official list.",
          "http://www.unitarium.com/si-prefixes along with a few addition shorts for Octillion, Nonillion,",
          "and Decillion.",
          "The order in which the characters are:",
          "Thousand - k",
          "Million - M",
          "Billion - G",
          "Trillion - T",
          "Quadrillion - P",
          "Quintillion - E",
          "Sextillion - Z",
          "Septillion - Y",
          "Octillion - X",
          "Nonillion - W",
          "Decillion - V",
      };
    }
  },

  CURRENCY_DEFAULT_BALANCE {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Balance";
    }

    @Override
    public String getDefaultValue() {
      return "200.0";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The initial balance for accounts.",
          "Will be used if no world-specific configurations are found if multiworld is enabled",
      };
    }
  },

  CURRENCY_DEFAULT_NOTABLE {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Notable";
    }

    @Override
    public String getDefaultValue() {
      return "false";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Whether or not this currency is able to be noted using the note command.",
      };
    }
  },

  CURRENCY_DEFAULT_DEFAULT {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Default";
    }

    @Override
    public String getDefaultValue() {
      return "true";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Whether or not this currency is the world's default.",
      };
    }
  },

  CURRENCY_DEFAULT_CONVERSION {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Conversion";
    }

    @Override
    public String getDefaultValue() {
      return "1.0";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The conversion power of this TNECurrency to other currencies.",
          "This is based on a decimal conversion system where 1.0 is 100% i.e. the \"normal\" rate.",
      };
    }
  },

  CURRENCY_DEFAULT_SYMBOL {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Symbol";
    }

    @Override
    public String getDefaultValue() {
      return "$";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The character to use as the symbol for this currency.",
      };
    }
  },

  CURRENCY_DEFAULT_DECIMAL {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Decimal";
    }

    @Override
    public String getDefaultValue() {
      return ".";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The character to use as the decimal place holder.",
      };
    }
  },

  CURRENCY_DEFAULT_DECIMALPLACES {
    @Override
    public String getNode() {
      return "Core.Currency.Default.DecimalPlaces";
    }

    @Override
    public String getDefaultValue() {
      return "2";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The amount of digits to display after the decimal character.",
          "The maximum amount of places is 4.",
      };
    }
  },

  CURRENCY_DEFAULT_ITEMCURRENCY {
    @Override
    public String getNode() {
      return "Core.Currency.Default.ItemCurrency";
    }

    @Override
    public String getDefaultValue() {
      return "false";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Would you like to use an item as the currency?",
      };
    }
  },

  CURRENCY_DEFAULT_ENDERCHEST {
    @Override
    public String getNode() {
      return "Core.Currency.Default.EnderChest";
    }

    @Override
    public String getDefaultValue() {
      return "false";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Would you like your item TNECurrency balances to also check the player's ender chest?",
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_HEADER {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Configurations relating to currency tiers.",
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_DOLLARS_HEADER {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.One";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The tier, this may be anything, but for organizational purposes it's best to use the singular name.",
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_DOLLAR_TYPE {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.One.Type";
    }

    @Override
    public String getDefaultValue() {
      return "Major";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The type of tier this is(Major, or Minor)."
      };
    }
  },


  CURRENCY_DEFAULT_TIERS_DOLLAR_WEIGHT {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.One.Weight";
    }

    @Override
    public String getDefaultValue() {
      return "1";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The weight of the tier. E.X. 20USD would equal 20."
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_DOLLAR_ITEM_HEADER {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.One.Item";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Configurations relating to if this tier is item-based.",
          "Most of these configurations are marked with [optional], which means you're able",
          "to remove them from this file without any harm if you don't need them."
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_DOLLAR_ITEM_MATERIAL {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.One.Item.Material";
    }

    @Override
    public String getDefaultValue() {
      return "PAPER";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The material used for this item."
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_DOLLAR_ITEM_DAMAGE {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.One.Item.Damage";
    }

    @Override
    public String getDefaultValue() {
      return "0";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "[Optional]The damage value used for this item. Defaults to 0."
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_DOLLAR_ITEM_NAME {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.One.Item.Name";
    }

    @Override
    public String getDefaultValue() {
      return "One";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "[Optional]The custom name this item must have in order to be considered currency.",
          "[Optional]This is the same as one that would be applied at an anvil."
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_DOLLAR_ITEM_LORE {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.One.Item.Lore";
    }

    @Override
    public String getDefaultValue() {
      return "Server Currency";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "[Optional]The lore string this item must have in order to be considered currency."
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_DOLLAR_ENCHANTMENTS_HEADER {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.One.Item.Enchantments";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "All configurations relating to enchantment identification for currency tiers.",
          "This section is optional, remove it if you don't need it."
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_DOLLAR_ENCHANTMENTS_EXAMPLE {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.One.Item.Enchantments.ExampleEnchantment";
    }

    @Override
    public String getDefaultValue() {
      return "*";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The format is Enchantment Name: Enchantment Level, or * for all levels."
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_DOLLAR_NAME_HEADER {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.One.Name";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Configurations relating to the naming of this tier.",
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_DOLLAR_NAME_SINGLE {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.One.Single";
    }

    @Override
    public String getDefaultValue() {
      return "One";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The singular name of this tier.",
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_DOLLAR_NAME_PLURAL {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.One.Plural";
    }

    @Override
    public String getDefaultValue() {
      return "Pennies";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The plural name of this tier.",
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_PENNY_HEADER {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.Penny";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The tier, this may be anything, but for organizational purposes it's best to use the singular name.",
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_PENNY_TYPE {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.Penny.Type";
    }

    @Override
    public String getDefaultValue() {
      return "Minor";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The oldtype of tier this is(Major, or Minor)."
      };
    }
  },


  CURRENCY_DEFAULT_TIERS_PENNY_WEIGHT {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.Penny.Weight";
    }

    @Override
    public String getDefaultValue() {
      return "1";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The weight of the tier. E.X. 20USD would equal 20."
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_PENNY_ITEM_HEADER {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.Penny.Item";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Configurations relating to if this tier is item-based.",
          "Most of these configurations are marked with [optional], which means you're able",
          "to remove them from this file without any harm if you don't need them."
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_PENNY_ITEM_MATERIAL {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.Penny.Item.Material";
    }

    @Override
    public String getDefaultValue() {
      return "PAPER";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The material used for this item."
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_PENNY_ITEM_DAMAGE {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.Penny.Item.Damage";
    }

    @Override
    public String getDefaultValue() {
      return "0";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "[Optional]The damage value used for this item. Defaults to 0."
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_PENNY_ITEM_NAME {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.Penny.Item.Name";
    }

    @Override
    public String getDefaultValue() {
      return "Penny";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "[Optional]The custom name this item must have in order to be considered currency."
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_PENNY_ITEM_LORE {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.Penny.Item.Lore";
    }

    @Override
    public String getDefaultValue() {
      return "Server Currency";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "[Optional]The lore string this item must have  in order to be considered currency."
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_PENNY_ENCHANTMENTS_HEADER {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.Penny.Item.Enchantments";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "[Optional]All configurations relating to enchantment identification for currency tiers.",
          "[Optional]This section is optional, remove it if you don't need it."
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_PENNY_ENCHANTMENTS_EXAMPLE {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.Penny.Item.Enchantments.ExampleEnchantment";
    }

    @Override
    public String getDefaultValue() {
      return "*";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The format is Enchantment Name: Enchantment Level, or * for all levels."
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_PENNY_NAME_HEADER {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.Penny.Name";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Configurations relating to the naming of this tier.",
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_PENNY_NAME_SINGLE {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.Penny.Single";
    }

    @Override
    public String getDefaultValue() {
      return "Penny";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The singular name of this tier.",
      };
    }
  },

  CURRENCY_DEFAULT_TIERS_PENNY_NAME_PLURAL {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Tiers.Penny.Plural";
    }

    @Override
    public String getDefaultValue() {
      return "Pennies";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The plural name of this tier.",
      };
    }
  },

  CURRENCY_DEFAULT_MAJOR_HEADER {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Major";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Configurations relating to the major tier of currency",
          "Example: Dollars",
      };
    }
  },

  CURRENCY_DEFAULT_MAJOR_SEPARATE {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Major.Separate";
    }

    @Override
    public String getDefaultValue() {
      return "true";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Whether or not the major value should be separated every three numeric places.",
      };
    }
  },

  CURRENCY_DEFAULT_MAJOR_SEPARATOR {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Major.Separator";
    }

    @Override
    public String getDefaultValue() {
      return ",";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The separator to use for numeric separation.",
      };
    }
  },

  CURRENCY_DEFAULT_MINOR_HEADER {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Minor";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Configurations relating to the minor tier of currency",
          "Example: Cents",
      };
    }
  },

  CURRENCY_DEFAULT_MINOR_WEIGHT {
    @Override
    public String getNode() {
      return "Core.Currency.Default.Minor.Weight";
    }

    @Override
    public String getDefaultValue() {
      return "100";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "This is used to determine how many of minor it takes to make one major.",
      };
    }
  },

  WORLD_HEADER {
    @Override
    public String getNode() {
      return "Core.World";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "All configurations relating to worlds"
      };
    }
  },

  WORLD_ENABLECHANGEFEE {
    @Override
    public String getNode() {
      return "Core.World.EnableChangeFee";
    }

    @Override
    public String getDefaultValue() {
      return "false";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Whether or not changing worlds costs money."
      };
    }
  },

  WORLD_CHANGEFEE {
    @Override
    public String getNode() {
      return "Core.World.ChangeFee";
    }

    @Override
    public String getDefaultValue() {
      return "5.0";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "How much it costs to change worlds if ChangeFee is enabled."
      };
    }
  },

  DATABASE_HEADER {
    @Override
    public String getNode() {
      return "Core.Database";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "ll configurations relating to the database"
      };
    }
  },
  
  DATABASE_TYPE {
    @Override
    public String getNode() {
      return "Core.Database.Type";
    }

    @Override
    public String getDefaultValue() {
      return "H2";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The database type to use. Current options: MySQL, H2"
      };
    }
  },

  DATABASE_PREFIX {
    @Override
    public String getNode() {
      return "Core.Database.Prefix";
    }

    @Override
    public String getDefaultValue() {
      return "TNE";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The prefix to use for TheNewEconomy MySQL and H2 Tables"
      };
    }
  },

  DATABASE_BACKUP {
    @Override
    public String getNode() {
      return "Core.Database.Backup";
    }

    @Override
    public String getDefaultValue() {
      return "true";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Whether or not to backup your database automatically before converting to newer versions of TNE"
      };
    }
  },
  
  DATABASE_FILE {
    @Override
    public String getNode() {
      return "Core.Database.File";
    }

    @Override
    public String getDefaultValue() {
      return "Economy";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "File name, minus extension, for any DB type which requires a file."
      };
    }
  },

  DATABASE_MYSQL_HEADER {
    @Override
    public String getNode() {
      return "Core.Database.MySQL";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "All configurations relating to the MySQL Database."
      };
    }
  },
  
  DATABASE_HOST {
    @Override
    public String getNode() {
      return "Core.Database.MySQL.Host";
    }

    @Override
    public String getDefaultValue() {
      return "localhost";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The host for the database."
      };
    }
  },
  
  DATABASE_PORT {
    @Override
    public String getNode() {
      return "Core.Database.MySQL.Port";
    }

    @Override
    public String getDefaultValue() {
      return "3306";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The port for the database."
      };
    }
  },
  
  DATABASE_DB {
    @Override
    public String getNode() {
      return "Core.Database.MySQL.Database";
    }

    @Override
    public String getDefaultValue() {
      return "TheNewEconomy";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The name of the database to use for storage"
      };
    }
  },
  
  DATABASE_USER {
    @Override
    public String getNode() {
      return "Core.Database.MySQL.User";
    }

    @Override
    public String getDefaultValue() {
      return "user";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The name of the user for the database"
      };
    }
  },
  
  DATABASE_PASSWORD {
    @Override
    public String getNode() {
      return "Core.Database.MySQL.Password";
    }

    @Override
    public String getDefaultValue() {
      return "password";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The password for the database user"
      };
    }
  }
}
