package net.tnemc.core.menu.impl.shared;

import net.tnemc.core.TNE;
import net.tnemc.core.menu.Menu;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/31/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class EnchantmentMenu extends Menu {

  private String dataValue;

  public EnchantmentMenu(String name, String dataValue, Integer rows) {
    super(name, "Select Enchantments", rows);

    this.dataValue = dataValue;
  }

  @Override
  public Inventory buildInventory(Player player) {

    final Optional<Object> retrieved = Optional.of(TNE.menuManager().getViewerData(player.getUniqueId(), dataValue));

    final List<String> current = (retrieved.isPresent())? (List<String>)retrieved.get() : new ArrayList<>();

    for(Enchantment enchant : Enchantment.values()) {

    }

    return super.buildInventory(player);
  }
}