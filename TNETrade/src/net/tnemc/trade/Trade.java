package net.tnemc.trade;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.trade.inventory.TradeInventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 5/26/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class Trade {

  private Inventory inventory;

  private UUID initiator;
  private UUID acceptor;

  private boolean initiatorAccepted = false;
  private boolean acceptorAccepted = false;

  public Trade(UUID initiator, UUID acceptor) {
    this.initiator = initiator;
    this.acceptor = acceptor;

    inventory = buildInventory();
  }

  public Inventory buildInventory() {

    Inventory inventory = Bukkit.createInventory(new TradeInventoryHolder(initiator, acceptor), 54, "[TNE] Trading");
    inventory.setItem(4, TradeManager.border);
    inventory.setItem(13, TradeManager.border);
    inventory.setItem(22, TradeManager.border);
    inventory.setItem(31, TradeManager.border);
    inventory.setItem(40, TradeManager.border);
    inventory.setItem(49, TradeManager.border);

    //Initiator Tools
    inventory.setItem(45, TradeManager.decline);
    inventory.setItem(46, buildHead(initiator));
    inventory.setItem(47, buildStatus(initiatorAccepted));
    inventory.setItem(48, TradeManager.accept);

    //Acceptor Tools
    inventory.setItem(50, TradeManager.decline);
    inventory.setItem(51, buildHead(acceptor));
    inventory.setItem(52, buildStatus(acceptorAccepted));
    inventory.setItem(53, TradeManager.accept);

    return inventory;
  }

  private ItemStack buildHead(UUID player) {
    final String username = IDFinder.getUsername(player.toString());

    ItemStack skull = TNE.item().build("PLAYER_HEAD");
    SkullMeta meta = (SkullMeta)skull.getItemMeta();
    meta.setOwningPlayer(Bukkit.getPlayer(player));
    meta.setDisplayName(ChatColor.YELLOW + username);
    skull.setItemMeta(meta);

    return skull;
  }

  private ItemStack buildStatus(boolean accepted) {

    ItemStack stack = TNE.item().build(((accepted)? "GREEN_WOOL" : "WHITE_WOOL"));
    ItemMeta meta = stack.getItemMeta();
    meta.setDisplayName(((accepted)? "Accepted" : "No Status"));
    stack.setItemMeta(meta);

    return stack;
  }

  public UUID getInitiator() {
    return initiator;
  }

  public void setInitiator(UUID initiator) {
    this.initiator = initiator;
  }

  public UUID getAcceptor() {
    return acceptor;
  }

  public void setAcceptor(UUID acceptor) {
    this.acceptor = acceptor;
  }

  public boolean isInitiatorAccepted() {
    return initiatorAccepted;
  }

  public void setInitiatorAccepted(boolean initiatorAccepted) {
    this.initiatorAccepted = initiatorAccepted;
  }

  public boolean isAcceptorAccepted() {
    return acceptorAccepted;
  }

  public void setAcceptorAccepted(boolean acceptorAccepted) {
    this.acceptorAccepted = acceptorAccepted;
  }
}