package net.tnemc.trade;

import net.tnemc.core.TNE;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class TradeManager {

  public static ItemStack border;
  public static ItemStack decline;
  public static ItemStack accept;

  private Map<String, Trade> activeTrades = new HashMap<>();

  private List<String> requests = new ArrayList<>();

  public TradeManager() {
    border = TNE.item().build("BLACK_STAINED_GLASS_PANE");
    ItemMeta meta = border.getItemMeta();
    meta.setDisplayName("Border");
    border.setItemMeta(meta);


    decline = TNE.item().build("RED_STAINED_GLASS_PANE");
    ItemMeta declineMeta = decline.getItemMeta();
    meta.setDisplayName("Decline Trade");
    decline.setItemMeta(declineMeta);


    accept = TNE.item().build("RED_STAINED_GLASS_PANE");
    ItemMeta acceptMeta = accept.getItemMeta();
    meta.setDisplayName("Accept Trade");
    accept.setItemMeta(acceptMeta);
  }

  public void addRequest(UUID initiator, UUID target) {
    requests.add(getKey(initiator, target));
  }

  public boolean hasRequest(UUID initiator, UUID target) {
    return requests.contains(getKey(initiator, target));
  }

  public void removeRequest(UUID initiator, UUID target) {
    requests.remove(getKey(initiator, target));
  }

  public void addTrade(UUID initiator, UUID target) {
    removeRequest(initiator, target);

    final String key = getKey(initiator, target);
    final Trade trade = new Trade(initiator, target);
    activeTrades.put(key, trade);
  }

  public void initiateTrade(UUID initiator, UUID target) {

  }

  private String getKey(UUID initiator, UUID target) {
    return initiator.toString() + ":" + target.toString();
  }
}