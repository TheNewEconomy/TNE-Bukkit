package net.tnemc.core.listeners.player;

import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldManager;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.ItemCalculations;
import net.tnemc.core.common.currency.formatter.CurrencyFormatter;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.common.transaction.charge.TransactionCharge;
import net.tnemc.core.common.transaction.result.TransactionResult;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 8/8/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class PlayerChangedWorldListener implements Listener {

  TNE plugin;

  public PlayerChangedWorldListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onWorldChange(final PlayerChangedWorldEvent event) {
    if(TNE.configurations().getBoolean("Core.Multiworld")) {
      final Player player = event.getPlayer();
      final UUID id = IDFinder.getID(player);
      final TNEAccount account = TNE.manager().getAccount(id);
      final String world = WorldFinder.getWorld(player, WorldVariant.BALANCE);

      boolean noEconomy = TNE.instance().getWorldManager(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION)) == null || TNE.instance().getWorldManager(WorldFinder.getWorld(player, WorldVariant.CONFIGURATION)).isEconomyDisabled();
      if (!noEconomy && TNE.instance().api().getBoolean("Core.World.EnableChangeFee", WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), id.toString())) {
        if (!player.hasPermission("tne.bypass.world")) {
          WorldManager manager = TNE.instance().getWorldManager(world);
          TNETransaction transaction = new TNETransaction(account, account, world, TNE.transactionManager().getType("worldchange"));
          transaction.setRecipientCharge(new TransactionCharge(world, TNE.manager().currencyManager().get(world, manager.getChangeFeeCurrency()), manager.getChangeFee()));
          TransactionResult result = TNE.transactionManager().perform(transaction);
          if (!result.proceed()) {
            player.teleport(event.getFrom().getSpawnLocation());
          }
          Message message = new Message(result.recipientMessage());
          final String balanceWorld = WorldFinder.getWorld(player, WorldVariant.BALANCE);
          message.addVariable("$amount", CurrencyFormatter.format(TNE.manager().currencyManager().get(balanceWorld), balanceWorld, manager.getChangeFee(), player.getUniqueId().toString()));
          message.translate(world, player);
        }
        account.initializeHoldings(world);
      } else if (!noEconomy && !TNE.instance().api().getBoolean("Core.World.EnableChangeFee", WorldFinder.getWorld(player, WorldVariant.CONFIGURATION), id.toString())) {
        account.initializeHoldings(world);
      }

      if (!noEconomy && TNE.instance().api().getBoolean("Core.Multiworld") &&
          !TNE.instance().getWorldManager(event.getFrom().getName()).getBalanceWorld().equalsIgnoreCase(world)) {
        TNE.instance().getWorldManager(world).getItemCurrencies().forEach(value -> {
          ItemCalculations.setItems(id, TNE.manager().currencyManager().get(world, value),
              account.getHoldings(world, value, true, true), player.getInventory(), false);
        });
      }
    }
  }
}
