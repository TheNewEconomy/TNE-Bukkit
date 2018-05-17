package net.tnemc.core.configuration.impl;

import net.tnemc.core.configuration.IConfigNode;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 5/17/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public enum PlayersConfigNodes implements IConfigNode {

  PLAYERS_HEADER {
    @Override
    public String getNode() {
      return "Players";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The New Economy v0.1.0.0",
          "Author: creatorfromhell",
          "License: http://creativecommons.org/licenses/by-nc-nd/4.0/",
          "If you would like to contribute to the plugin",
          "you can do so via Github at https://github.com/TheNewEconomy/TNE-Bukkit",
          "To donate to the continued development of the plugin visit https://patreon.com/creatorfromhell",
          "Used for per-player configurations."
      };
    }
  },

  PLAYERS_EXAMPLE {
    @Override
    public String getNode() {
      return "Players.player-uuid-here";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "If you need help finding a player's UUID please have a look at",
          "/TNE id <player name>.",
          "The username field could be the player's username or UUID.",
          "All configurations you wish to override go under this tag."
      };
    }
  },
}