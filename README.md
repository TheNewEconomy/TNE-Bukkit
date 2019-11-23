<p align="center">
    <img src="http://i.imgur.com/eDlmaed.png" width="728" />
</p>

**Please Note: This project is in Alpha stage, and may therefore contain multiple bugs. Please report any bugs you find.**  

About
--------------
The New Economy is a feature-packed modular economy plugin that is currently under development. TNE is meant to offer an all-in-one solution for server owners, while also providing an easy-to-use modular system, which allows
server owners to simply disable features they don't wish to use, or even remove the module's jar to save disk space. TNE's modular system also allows third-party developers to easily add additional features without
the need for additional dependencies. Developers, of course, are welcome to add functionality via TNE's custom events, API, Vault, or Reserve(Recommended)..

Special Thanks
--------------
<p align="center">
    <img src="https://www.worldsrc.org/uploadimage/307c9b78_WorldSrc.com_img_EJ_Technologies_JProfiler.jpg" />
</p>


Build Status
--------------
[![Build Status](https://travis-ci.org/TheNewEconomy/TNE-Bukkit.svg?branch=master)](https://travis-ci.org/TheNewEconomy/TNE-Bukkit)

If you've switched from another economy plugin to TNE, please take a moment to tell us which one:
What economy plugin did you switch from?

Support
-------------
There's a few ways to support the development of TNE at this moment.

* [My Patreon](https://www.patreon.com/creatorfromhell)
* Share TNE with other server owners
* Suggest ways to improve TNE

Basic Information
----------
Some basic information you might want to look over.
- [Bugs/Feature Requests/Suggestions](https://github.com/TheNewEconomy/TNE-Bukkit/issues)
- [Permission Nodes](https://github.com/TheNewEconomy/TNE-Bukkit/wiki/Permissions-&-Commands)
- [Commands](https://github.com/TheNewEconomy/TNE-Bukkit/wiki/Permissions-&-Commands)

Default Configurations
----------
- [config.yml](https://github.com/TheNewEconomy/TNE-Bukkit/blob/master/TNE/src/net/tnemc/resources/config.yml)
- [items.yml](https://github.com/TheNewEconomy/TNE-Bukkit/blob/master/TNE/src/net/tnemc/resources/items.yml)
- [messages.yml](https://github.com/TheNewEconomy/TNE-Bukkit/blob/master/TNE/src/net/tnemc/resources/messages.yml)
- [players.yml](https://github.com/TheNewEconomy/TNE-Bukkit/blob/master/TNE/src/net/tnemc/resources/players.yml)
- [worlds.yml](https://github.com/TheNewEconomy/TNE-Bukkit/blob/master/TNE/src/net/tnemc/resources/worlds.yml)

Features
----------
- [Features](https://github.com/TheNewEconomy/TNE-Bukkit/blob/master/Features.md) 
- [Possible](https://github.com/TheNewEconomy/TNE-Bukkit/blob/master/Possible.md) 

Requirements
----------
- Vault (Optional) 
- Reserve (Optional)

Support
----------
For support choose one of the following.

- Discord: https://discord.gg/WNdwzpy (Recommended)
- Skype: creatorfromhell
- Email: daniel.viddy@gmail.com  

API
---------
To access the API import the jar file(TNE.jar) then use the following code:  

```java
TNE.instance().api();  
```

In the following example we'll get a player's balance using the API.  

```java
TNEAPI ecoAPI = TNE.instance().api();  
ecoAPI.getBalance(player);
```

Source code for the API class can be found [here](https://github.com/TheNewEconomy/TNE-Bukkit/blob/master/src/com/github/tnerevival/core/api/TNEAPI.java).


Team
----------
- creatorfromhell  

Contribute
----------
To contribute to The New Economy you may fork our Github Repository, and make a Pull Request. The repository is located at: https://github.com/TheNewEconomy/TNE-Bukkit .
