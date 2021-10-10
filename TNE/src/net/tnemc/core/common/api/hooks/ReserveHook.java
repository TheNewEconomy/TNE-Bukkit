package net.tnemc.core.common.api.hooks;

import net.tnemc.core.Reserve;
import net.tnemc.core.TNE;
import net.tnemc.core.common.api.Hook;
import net.tnemc.core.common.api.ReserveEconomy;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 10/9/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ReserveHook implements Hook {

  @Override
  public boolean enabled() {
    return false;
  }

  @Override
  public void register(TNE plugin) {
    ReserveEconomy reserveEconomy = new ReserveEconomy(plugin);
    Reserve.instance().registerProvider(reserveEconomy);
    plugin.getLogger().info("Hooked into Reserve");
  }
}
