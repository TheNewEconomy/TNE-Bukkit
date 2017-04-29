<p align="center">
    <img src="http://i.imgur.com/ZS0xmkb.png" width="728" />
</p>

**Please Note: This project is in Alpha stage, and may therefore contain multiple bugs. Please report any bugs you find.**  

About
--------------
The New Economy is a feature-packed economy plugin that is currently still under heavy development. TNE is meant to offer an all-in-one solution for server economies, while also being completely configurable to each server's needs. Whether you want a simple single currency economy, or multiple currencies per world with multiple worlds, TNE is for you.

Build Status
--------------
[![Build Status](https://travis-ci.org/TheNewEconomy/TNE-Bukkit.svg?branch=master)](https://travis-ci.org/TheNewEconomy/TNE-Bukkit)

If you've switched from another economy plugin to TNE, please take a moment to tell us which one:
What economy plugin did you switch from?

![alt text](http://i.imgur.com/ZS0xmkb.png)

Please Note: This project is in Alpha stage, and may therefore contain multiple bugs. Please report any bugs you find.

Support
-------------
There's only two ways to support the development of TNE at this moment.

* [My Patreon](https://www.patreon.com/creatorfromhell)
* Share TNE with other server owners

Basic Information
----------
Some basic information you might want to look over.
- [Github](https://github.com/TheNewEconomy/TNE-Bukkit)
- [Bugs/Feature Requests/Suggestions](https://github.com/TheNewEconomy/TNE-Bukkit/issues)
- [Permission Nodes](https://github.com/TheNewEconomy/TNE-Bukkit/blob/master/Docs/Permissions.md)
- [Commands](https://github.com/TheNewEconomy/TNE-Bukkit/blob/master/Docs/Commands.md)
- [To view the default configuration file w/ comments](http://paste.ubuntu.com/6885962/)
- [We're on Esper! #TheNewEconomy](http://chat.mibbit.com/?server=irc.esper.net&channel=%23TheNewEconomy)

Default Configurations
----------
- [config.yml](https://github.com/TheNewEconomy/TNE-Bukkit/blob/master/src/com/github/resources/config.yml)
- [materials.yml](https://github.com/TheNewEconomy/TNE-Bukkit/blob/master/src/com/github/resources/materials.yml)
- [messages.yml](https://github.com/TheNewEconomy/TNE-Bukkit/blob/master/src/com/github/resources/messages.yml)
- [mobs.yml](https://github.com/TheNewEconomy/TNE-Bukkit/blob/master/src/com/github/resources/mobs.yml)
- [objects.yml](https://github.com/TheNewEconomy/TNE-Bukkit/blob/master/src/com/github/resources/objects.yml)
- [players.yml](https://github.com/TheNewEconomy/TNE-Bukkit/blob/master/src/com/github/resources/players.yml)
- [worlds.yml](https://github.com/TheNewEconomy/TNE-Bukkit/blob/master/src/com/github/resources/worlds.yml)

Features
----------
- [Features](https://github.com/TheNewEconomy/TNE-Bukkit/blob/master/Features.md) 
- [Planned](https://github.com/TheNewEconomy/TNE-Bukkit/blob/master/Upcoming.md) 

Requirements
----------
- Vault(optional)  

Support
----------
For support choose one of the following.

- Skype: creatorfromhell
- Email: daniel.viddy@gmail.com  

API
---------
To acces the API import the jar file(TNE.jar) then use the following code:  

TNE.instance().api();  

In the following example we'll get a player's balance using the API.  

TNEAPI ecoAPI = TNE.instance().api();  
ecoAPI.getBalance(player);

Source code for the API class can be found [here](https://github.com/TheNewEconomy/TNE-Bukkit/blob/master/src/com/github/tnerevival/core/api/TNEAPI.java).


Team
----------
- creatorfromhell  

Contribute
----------
To contribute to The New Economy you may fork our Github Repository, and make a Pull Request. The repository is located at: https://github.com/TheNewEconomy/TNE-Bukkit .