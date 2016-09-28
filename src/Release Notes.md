Alpha 3.0
============
Feature Set:
----------

* Add support for H2 in place of SQLite
  * Your SQLite database will be automatically converted for you.
* New internal transaction system
  * this is a giant step towards full transaction tracking, and history
  * This new system improves code readability, and will better help pin point the location of any bugs that occur during a transaction.
* Ability to reward/charge for using potions
* Ability to reward/charge for using items
* Ability to reward/charge for crafting
* Ability to reward/charge for smelting
* Ability to reward/charge for enchanting
* Ability to reward/charge for mining
* Ability to reward/charge for brewing potions
* Ability to charge for inventory use
  * Ability to charge per use(on open)
  * Ability to create packages and charge per second of use
* Ability to disable UUID support
* Ability to allow worlds to share economy data
* Ability to allow the use of bank balances for transactions
* Ability to charge for command use
* Ability to give reward for killing a player in mobs.yml
  * You may also specify a single player's username in the same format to
  give more money if someone kills a certain player.
* Redid sign-based features, and created a new base to allow easier implementation in the future.
  * We now do proper permissions checks.
  * Added the ability to charge for sign use, and placement
  * Added the ability to set a max limit for signs owned by a player for certain signs.
* Added a shop system that allows player to sell various items they own for money and/or an item.
  * Ability to enable/disable shop use
  * Ability to limit how many players can browse a single shop at once
  * Ability to enable money sharing with shops(currently trades aren't shared)
  * Ability to limit how many player can share money with one shop.
  * Ability to limit how many shops a single player can own
  * Ability to change how many items a shop can have for sale/trade
* Updated banks to allow for multi-player banks.
  * Bank owners can now use /bank add, and /bank remove to give/revoke a player's access to their bank.
* Internal optimizations to allow for better code readability, and allow updates to be done in a more straight forward, and faster way.

Possible Features:
-----------------
* Ability to import data from iConomy
* Ability to change configurations via command
* Ability to create Auctions(?)
* Ability to have server, and world-wide lotteries

Needs Tested:
-------------
* Account purging
* Inventory & Command Credits
* Shops
* Administrative commands
* Disabling UUID support
* Shared economy data for worlds
* Ability to create accounts via command


Alpha 3.1
================

* Bug fixes for Alpha 3.0