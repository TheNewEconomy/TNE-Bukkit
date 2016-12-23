Advanced Economy
==============================
If you're reading this then you've come to expect more out of your economy plugin, which is why I released TNE for Bukkit
in the first place. Here I'll go over a few of the more complicated features of TNE, which there aren't many of.

###Account Sharing
This section we'll be taking a deeper look into TNE's account sharing feature, which allows you to force worlds to share 
economy account data between each other.

In order to share accounts between worlds you need to use two configuration settings for worlds.yml. These are `ShareAccounts` 
and `ShareWorld`. 

####In-Depth Configurations
<i>Here I'll go over the two configuration settings in-depth.</i>

Correct Format:
```
Worlds:
      World2:
          ShareAccounts: true
          ShareWorld: world1
```

#####ShareAccounts
<i>This is used to determine whether a world should share it's account data with another world. If it's set to true, you
must specify the world's data to use with `ShareWorld`.</i>

Example:    
`ShareAccounts: true`

#####ShareWorld
<i>The world's data to use for this world.</i>

Example:    
`ShareWorld: WorldName`

####Examples
<i>Here's a few examples to better explain how it works.</i>

Let's say you want to share data for World1 and World2 with World3.
```
worlds.yml
Worlds:
      World1:
          ShareAccounts: true
          ShareWorld: World3
      World2:
          ShareAccounts: true
          ShareWorld: World3
```

Now let's say you want to share data for World1 and World 2 with World3, but also share data for World with World4.
```
worlds.yml
Worlds:
      World1:
          ShareAccounts: true
          ShareWorld: World3
      World2:
          ShareAccounts: true
          ShareWorld: World3
      World:
          ShareAccounts: true
          ShareWorld: World4
```

The main thing to remember is that it doesn't matter what world you use as the main data source, as long as you only use 
one common world for the worlds you wish to share it with.
