package net.tnemc.core.common.api.hooks;

import net.milkbowl.vault.economy.Economy;
import net.tnemc.core.TNE;
import net.tnemc.core.common.api.Economy_TheNewEconomy;
import net.tnemc.core.common.api.Hook;
import org.bukkit.plugin.ServicePriority;

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
public class VaultHook implements Hook {
  @Override
  public boolean enabled() {
    return false;
  }

  @Override
  public void register(TNE plugin) {
    Economy_TheNewEconomy vaultEconomy = new Economy_TheNewEconomy(plugin);
    plugin.getServer().getServicesManager().register(Economy.class, vaultEconomy, plugin, ServicePriority.Highest);
    plugin.getLogger().info("Hooked into Vault");
  }
}
