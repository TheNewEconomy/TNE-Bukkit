# About
TNE offers various built-in commands to provide servers with an experience that is not only user-friendly, but powerful for administrators.

# Commands
All commands provided within The New Economy.

## Core Commands
Commands provided in the core TNE Jar(tne.jar).

### /tne
##### About: All commands that are designated as being for administrative purposes.
##### Base Node: tne.admin


| Command                                                                       | Permission          | Description                                                                           |
|-------------------------------------------------------------------------------|---------------------|---------------------------------------------------------------------------------------|
| tne backup  | tne.admin.backup | Creates a backup of all server data. |
| tne balance \<player\> [world] [currency] | tne.admin.balance | Retrieves the balance of a player. Player ~ The account owner. World ~ The world to retrieve the balance from. currency ~ The currency to retrieve the balance of. |
| tne build | tne.admin.build | Displays the version of TNE currently running. |
| tne caveats | tne.admin.caveats | Displays all known caveats for this version of TNE. |
| tne create \<player\> [balance] | tne.admin.create | Creates a new economy account. Player ~ The account owner. Balance ~ The starting balance of the account. |
| tne delete \<player\> | tne.admin.delete | Deletes a player account. Player ~ The account owner. |
| tne extract | tne.admin.extract | Extracts all player balances with their username attached for database related debugging. |
| tne id \<player\> | tne.admin.id | Retrieves a player's TNE UUID. Player ~ The player you wish to discover the UUID of. |
| tne menu \<player\> | tne.admin.menu | Opens a GUI for performing basic transactions on the specified player. Player ~ The name/uuid of the player you wish to perform transactions with in the GUI. |
| tne purge | tne.admin.purge | Deletes all player accounts that have the default balance |
| tne recreate | tne.admin.recreate | Attempts to recreate database tables. WARNING: This will delete all data in the database. |
| tne reload <configuration> | tne.admin.reload | Saves modifications made via command, and reloads a configuration file. Configuration ~ The identifier of the configuration to reload. Default is all. |
| tne report \<report\> | tne.admin.report | File a bug report. Report ~ The link to the pastebin with the bug report, use the following format: https://github.com/TheNewEconomy/TNE-Bukkit/blob/master/Report%20Format.md. |
| tne reset | tne.admin.reset | Deletes all economy-related data from the database. |
| tne restore | tne.admin.restore | Restores all balances that are located in extracted.yml after performing the extract command. |
| tne save | tne.admin.save | Force saves all TNE data. |
| tne status \<player\> [status] | tne.admin.status | Displays, or sets, the current account status of an account. Player ~ The account owner. |
| tne upload | tne.admin.upload | Uploads the TNE debug & latest server log to pastebin.com, and provides a link to each. |
| tne version | tne.admin.version | Displays the version of TNE currently running. |

### /tneconfig
##### About: All commands that are used to interact with configuration values.
##### Shortcuts: tnec
##### Base Node: tne.config


| Command                                                                       | Permission          | Description                                                                           |
|-------------------------------------------------------------------------------|---------------------|---------------------------------------------------------------------------------------|
| tnec get \<node\> [configuration] | tne.config.get | Returns the value of a configuration. Node ~ The configuration node to use. Configuration ~ The configuration identifier to retrieve the value from. This may be retrieved automatically. |
| tnec save [configuration] | tne.config.save | Saves modifications made to a configuration value via command. Configuration ~ The configuration identifier to retrieve the value from. |
| tnec set \<node\> \<value\> [configuration] | tne.config.set | Sets a configuration value. This will not save until you do tnec save. |
| tnec tneget \<node\> [world] [player] | tne.config.tneget | Returns the value of a configuration after TNE takes into account player & world configurations. Node ~ The configuration node to use. World ~ The name of the world to use for parsing. Player ~ The name of the world to use for parsing. |
| tnec undo [configuration/all] | tne.config.undo | Undoes modifications made to configurations. Configuration ~ The configuration identifier to retrieve the value from. This may be retrieved automatically. |

### /currency
##### About: All commands that used to interact with various currency functions.
##### Shortcuts: cur
##### Base Node: tne.currency


| Command                                                                       | Permission          | Description                                                                           |
|-------------------------------------------------------------------------------|---------------------|---------------------------------------------------------------------------------------|
| currency rename \<currency\> \<new name\> | tne.currency.rename | Renames a currency to a different name. |
| currency list [world] | tne.currency.list | Displays the currencies available for a world.  World ~ The world to use. |
| currency tiers [currency] [world] | tne.currency.tiers | Displays the tiers for a currency. Currency ~ The Currency to check. World ~ The world that the Currency belongs to. |

### /language
##### About: All commands that used to interact with the language system
##### Shortcuts: lang
##### Base Node: tne.language


| Command                                                                       | Permission          | Description                                                                           |
|-------------------------------------------------------------------------------|---------------------|---------------------------------------------------------------------------------------|
| language current | tne.language.current | Displays your current language as set for your account. |
| language list | tne.language.list | Lists available languages on this server. |
| language reload | tne.language.reload | Reloads all language files. |
| language set \<name\> | tne.language.set | Sets your current language to the one specified. |

### /tnemodule
##### About: All commands that used to interact with the module system
##### Shortcuts: tnem
##### Base Node: tne.module


| Command                                                                       | Permission          | Description                                                                           |
|-------------------------------------------------------------------------------|---------------------|---------------------------------------------------------------------------------------|
| tnem info \<module\> | tne.module.info | Displays some information about a module. Module ~ The module to look up. |
| tnem list | tne.module.list | Lists all loaded TNE modules. |
| tnem load \<module\> | tne.module.load | Load a module from the modules directory. Module ~ The module to load. |
| tnem reload \<module\> | tne.module.reload | Reloads a module from the modules directory. Module ~ The module to reload. |
| tnem unload \<module\> | tne.module.unload | Unload a module from the server. Module ~ The module to unload. |

### /money
##### About: All commands that used to interact with the server's money system.
##### Shortcuts: /money balance(/bal, /balance), /money pay(/pay), /money top(/baltop)
##### Base Node: tne.money


| Command                                                                       | Permission          | Description                                                                           |
|-------------------------------------------------------------------------------|---------------------|---------------------------------------------------------------------------------------|
| money balance [world] [currency] | tne.money.balance | Displays your current holdings. |
| money convert \<amount\> \<to currency[:world]\> [from currency[:world]] | tne.money.convert | Convert some of your holdings to another currency. |
| money give \<player\> \<amount\> [world] [currency] | tne.money.give | Adds money into your economy, and gives it to a player. |
| money note \<amount\> [currency] | tne.money.note | Makes your virtual currency physical, for storage/trading purposes. |
| money pay \<player\> \<amount\> [currency] | tne.money.pay | Use some of your holdings to pay another player. |
| money set \<player\> \<amount\> [world] [currency] | tne.money.set | Set the holdings of a player. |
| money take \<player\> \<amount\> [world] [currency] | tne.money.take | Removes money from your economy, specifically from a player's balance. |
| money top [page] [currency:name] [world:world] [limit:#] | tne.money.top | A list of players with the highest balances. [page] The page number to view. Defaults to 1. [Currency] The name of the Currency to get balances from. Defaults to world default. Use overall for all currencies. [world] The world name you wish to filter, or all for every world. Defaults to current world. Use overall for all worlds. [limit] Limit changes how many players are displayed. Defaults to 10. |

### /transaction
##### About: All commands that used to interact with the transaction system.
##### Shortcuts: /trans
##### Base Node: tne.transaction


| Command                                                                       | Permission          | Description                                                                           |
|-------------------------------------------------------------------------------|---------------------|---------------------------------------------------------------------------------------|
| transaction away [page #] | tne.transaction.away | Displays transactions that you missed since the last time you were on. |
| transaction history [player:name] [page:#] [world:name/all] | tne.transaction.history | See a detailed break down of your transaction history. Page ~ The page number you wish to view. World ~ The world name you wish to filter, or all for every world. Defaults to current world. |
| transaction info \<uuid\> | tne.transaction.info | Displays information about a transaction. UUID ~ The id of the transaction. |
| transaction void \<uuid\> | tne.transaction.void | Undoes a previously completed transaction. UUID ~ The id of the transaction. |
