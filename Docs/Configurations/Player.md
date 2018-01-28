Player Configurations
=======================
As of TNE Alpha 3.2 servers owners have the ability to set every configuration on a per-world basis.
File
--------------
players.yml

Configuration
----------------
```YAML
#Used for per-player configurations.
Players:

    # If you need help finding a player's UUID please have a look at
    # /TNE id <player name>.
    # The username field could be the player's username or UUID.
    player-uuid-here:
        #Now we Simply override and configurations we want for this specific player.
        #This allows us even more control over our server.
        Shops:
            Enabled: false
```