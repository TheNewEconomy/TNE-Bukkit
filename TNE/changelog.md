1.1.13

New Command System
- New system gives control to the server owner
  - modifiable command names, shortcuts, permissions, etc
  - disable commands by modifying commands.yml, or change the permission
    node, add a new shortcut to run it.
- Adds tab completion 
- Adds the ability to do @a - online players and @r which is a random player in commands.

New Item Currency System // still needs completed
- No longer required to declare minor/major
- Ability to use irregular value amounts for tiers, such as 2.01 or even 1.99!

Fixes
- Fixed issue where item meta being null caused an NPE.
- Performance fixes related to /money other and /tne balance.
- Added the ability to do /money give/pay/take online to perform the command on all online players.
- Performance fixes related to UUID lookups
- Cache implementation for UUIDs(Thanks to PaulBGD)
- Fixes balance issues occurring with ender chest balances.
- Fixed issue where /money note did not properly work.
