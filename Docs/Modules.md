# Modules
TNE uses a module-based system to provide extra features. Certain "extras" are provided in the TNE core to give modules 
certain helper classes, such as item names, and inventory helpers.

## About
Modules are separate jars that provide extra features for TNE, and are located in the "modules" directory of the TNE folder.
The module system allows features to be added without the need to update the main TNE Plugin, and allows third-party developers to easily add addition features to TNE.

## Commands
These are the base commands associated with modules.  

| Command                                |    ShortCut                          | Permission          | Description                                                                           |
|----------------------------------------|--------------------------------------|---------------------|---------------------------------------------------------------------------------------|
| /tnemodule                             | /tnem                                | tne.module          | The base modules command.                                                             |
| /tnemodule reload                      | /\<base command\> r                  | tne.module.reload   | Reloads all modules.                                                                  |
| /tnemodule load                        | /\<base command\> l                  | tne.module.load     | Used to load a module.                                                                | 
| /tnemodule unload                      | /\<base command\> u                  | tne.module.unload   | Used to unload a module.                                                              | 
| /tnemodule info                        | /\<base command\> i                  | tne.module.info     | Shows basic information about a module, include the version and author.               |

## Advantages
There's various advantages of switching to a module-based system. Here's the major ones:
- Ability to update individual modules, without needed to mess with the core plugin itself
  - This allows bug fixes to be pushed out without everyone needing to update their version of TNE.
- Third-party developers may extend TNE without creating an entire plugin, and without messing around with Bukkit Events, or any API.
- Ability to add features to TNE without needing server owners to update TNE. This allows servers to add the latest TNE 
features without needing to restart their server, or perform the ever risky /reload command. All you have to do is simply type /tnemodule update, or /tnemodule reload.

## Official Modules
These are the Official TNE Modules that are packaged with each TNE release. Notice some of these are planned features, please check the TNE Version column for confirmation.  

Name - The name of the module  
Version - The module's current version  
TNE Version - The first TNE Version that comes packaged with this module  
Author - The module's author  
Description - some basic information about the module

| Name                  | Version          | TNE Version      | Author            | Description                                                                             |
|-----------------------|------------------|------------------|-------------------|-----------------------------------------------------------------------------------------| 
| Auctions              | 0.1              | 0.0.5.6+         | creatorfromhell   | Adds an auction system for players to auction off their unwanted items                  |
| Banks                 | 0.1              | 0.0.5.6+         | creatorfromhell   | Adds a banking system for players to store away their funds with ease-of-mind           |
| Credits               | 0.1              | 0.0.5.6+         | creatorfromhell   | Adds a credits system for charging players for command, and inventory access            |
| Loans                 | 0.1              | 0.0.6.0+         | creatorfromhell   | Adds the ability for players to give loans, or take loans from the server account       |
| Lotteries             | 0.1              | 0.0.6.0+         | creatorfromhell   | Adds a lottery system for world, or server-wide lottery support                         |
| Market                | 0.1              | 0.0.5.6+         | creatorfromhell   | Adds a global market system for players to sell & buy items from other players          |
| Mobs                  | 0.1              | 0.0.5.6+         | creatorfromhell   | Adds a mob reward system for players to earn money rewards for killing mobs             |
| Multiplier            | 0.1              | 0.0.5.6+         | creatorfromhell   | Adds the ability to apply a multiplier for certain players to every transaction.        |
| Plots                 | 0.1              | 0.0.6.0+         | KarateMan         | Adds in-game plots that may be bought & sold between players                            | 
| Raffle                | 0.1              | 0.0.6.0+         | creatorfromhell   | Adds the ability to raffle off items.                                                   |
| Rewards               | 0.1              | 0.0.5.6+         | creatorfromhell   | Grants server owners the ability to reward players for basics such as mining & crafting |
| Signs                 | 0.1              | 0.0.5.6+         | creatorfromhell   | Adds various signs that provide various features such as sign shops                     |
| Vaults                | 0.1              | 0.0.5.6+         | creatorfromhell   | Adds a storage system for players to store away their items with ease-of-mind           |  