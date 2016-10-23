Alpha 4.0
===========
Auctions
- Ability to allow players to create auctions
  - Multiple auctions per world/global
  - Ability to have silent auctions
    - When an auction is silent bid and snipe time messages are not sent
    - This gives a sense of mystery to auctions, and means players may be likely to be against their self.
  - Lots of configuration options
  
Currency
- Added in a new advanced currency system
  - Ability to create a custom currency format for chat.
  - Ability to configure custom symbols for currencies
  - Ability to configure a conversion rating(not used at this time)
  
Transactions
- Ability to track transaction history.

Configurations
- We now only save configurations on start/stop when they're modified.

Commands
- Additions
  - /theneweconomy pin <username> <new pin> - Reset <username>'s pin.
  - /theneweconomy status
    - /theneweconomy status <username> <status> - Set <username>'s account status.
  - /money history
    - /money history [page:#] [world:name/all] [type:type/all] - See a detailed break down of your transaction history.

- Modifications
  - No longer registered via plugin.yml
    - This allows for dynamic registration, which allows us to properly disable features
  - We now check permission nodes before building help messages
  - Help messages are now limited based on lines rather than commands

Messages
- Now support multi-line messages via messages.yml
  - Simply place <newline> where you wish to start your next line
  
MISC
- Added the ability to make players drop items from their bank on death.
  - Items chosen are completely random
  - It's also possible to include empty bank slots in the drop.