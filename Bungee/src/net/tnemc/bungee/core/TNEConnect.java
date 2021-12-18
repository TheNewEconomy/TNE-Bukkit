package net.tnemc.bungee.core;

import net.md_5.bungee.api.plugin.Plugin;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 12/17/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class TNEConnect extends Plugin {

  private static TNEConnect instance;

  @Override
  public void onEnable() {
    instance = this;
    getProxy().registerChannel("tne:balance");
    getProxy().getPluginManager().registerListener(this, new MessageListener());
  }

  public static TNEConnect instance() {
    return instance;
  }
}