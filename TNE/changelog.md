1.1.13

New Command System
- New system gives control to the server owner
  - modifiable command names, shortcuts, permissions, etc
  - disable commands by modifying commands.yml, or change the permission
    node, add a new shortcut to run it.
- Adds tab completion 
- Adds the ability to do @a - online players and @r which is a random player in commands.
- Added parameter validation to /money balance.
- Added * as an alternative to all in money give/take commands
- Added percentage support to /money give and take commands.
  - Example: /money take <player> 5% - Will take 5% of their balance.


Fixes
- Fixed issue where item meta being null caused an NPE.
- Performance fixes related to /money other and /tne balance.
- Added the ability to do /money give/pay/take online to perform the command on all online players.
- Performance fixes related to UUID lookups
- Cache implementation for UUIDs(Thanks to PaulBGD)
- Fixes balance issues occurring with ender chest balances.
- Fixed issue where /money note did not properly work.
- Fixed issue where max balance wasn't being enforced.

1.1.15

General
- Revamped the conversion system for currencies to be easier to configure and understand
- Setting messages in messages.yml to "" will now not send any message.
- Allow importing currency items in player inventories when first starting with TNE instead of
wiping them completely

Minor Notes
- Updated to latest Reserve Economy API.

Fixes
- Fix issue where using item currencies with Towny threw String.length() error.
- Fix issues with command selectors as parameters(@a, @o)
- Fix issue where using an outdated module would result in a complete TNE crash

Planned for 1.2

New Item Currency System // still needs completed
- No longer required to declare minor/major
- Ability to use irregular value amounts for tiers, such as 2.01 or even 1.99!
- Removed the basic currency system, everything should now use the advanced currency system
