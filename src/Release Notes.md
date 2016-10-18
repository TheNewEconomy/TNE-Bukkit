Alpha 4.0
===========
Auctions
- Ability to allow players to create auctions
  - Multiple auctions per world/global
  - Ability to have silent auctions
    - When an auction is silent bid and snipe time messages are not sent
    - This gives a sense of mystery to auctions, and means players may be likely to be against their self.
  - Lots of configuration options
  
Configurations
- We now only save configurations on start/stop when they're modified.

Commands

- Modifications
  - No longer registered via plugin.yml
    - This allows for dynamic registration, which allows us to properly disable features
  - We now check permission nodes before building help messages
  - Help messages are now limited based on lines rather than commands

Messages
- Now support multi-line messages via messages.yml
  - Simply place <newline> where you wish to start your next line