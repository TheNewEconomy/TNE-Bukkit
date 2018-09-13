Changelog
========================
### Beta 1.1.4
- Max balance is now 900 Nonvigintillion
- Player logout balance history logging.
  - This allows tracking possible dupe glitches/mysterious balance increases.


### Beta 1.1.3
- Major Bug Patch
- Added configuration to disable bStats
- Ability to add colour codes to currency item names and lore.
- Added eco alias to /tne
- We no longer export accounts with a balance of 0.
- Added /givemoney, /givebal, /setmoney, /setbal, /takemoney, and /takebal shortcut commands.
- Allow every player to access the yediot command set.
- Remove the need for parent permission nodes.(i.e. tne.money, etc)
- Placeholder API support.
  - tne_balance - returns player's balance
  - tne_world_worldname - returns player's balance for specific world.
  - tne_currency_currencyname - returns player's balance for specific currency.
  - tne_wcur_worldname_currencyname - returns player's balance for specific world and currency.