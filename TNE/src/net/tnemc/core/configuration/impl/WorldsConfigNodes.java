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
public enum WorldsConfigNodes implements IConfigNode {

  WORLDS_HEADER {
    @Override
    public String getNode() {
      return "Worlds";
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
          "All configurations that can be made world-specific.",
          "To use, please check the Example world below"
      };
    }
  },

  WORLDS_EXAMPLE {
    @Override
    public String getNode() {
      return "Worlds.Example";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "An example of what world-specific configurations are available",
          "Please note that you do NOT have to include all the following configurations as all",
          "world-specific configurations are optional",
          "Note: World name is case sensitive"
      };
    }
  },

  WORLDS_EXAMPLE_DISABLE {
    @Override
    public String getNode() {
      return "Worlds.Example.DisableEconomy";
    }

    @Override
    public String getDefaultValue() {
      return "true";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "Whether or not to completely disable economy features for this world"
      };
    }
  },

  WORLDS_EXAMPLE_CHANGEFEE_HEADER {
    @Override
    public String getNode() {
      return "Worlds.Example.ChangeFee";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "All configurations relating to World ChangeFees"
      };
    }
  },

  WORLDS_EXAMPLE_CHANGEFEE_CURRENCY {
    @Override
    public String getNode() {
      return "Worlds.Example.ChangeFee.Currency";
    }

    @Override
    public String getDefaultValue() {
      return "ExampleCurrency";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "The currency for which the ChangeFee is of."
      };
    }
  },

  WORLDS_EXAMPLE_CHANGEFEE_AMOUNT {
    @Override
    public String getNode() {
      return "Worlds.Example.ChangeFee.Amount";
    }

    @Override
    public String getDefaultValue() {
      return "10.0";
    }

    @Override
    public String[] getComments() {
      return new String[] {
          "How much it costs to enter this world if ChangeFee is enabled in the main configuration"
      };
    }
  },
}