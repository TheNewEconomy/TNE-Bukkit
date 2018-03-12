package net.tnemc.rewards;

import com.github.tnerevival.core.Message;
import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.currency.CurrencyFormatter;
import net.tnemc.core.common.module.Module;
import net.tnemc.core.common.module.ModuleInfo;
import net.tnemc.core.common.transaction.TNETransaction;
import net.tnemc.core.economy.transaction.charge.TransactionCharge;
import net.tnemc.core.economy.transaction.charge.TransactionChargeType;
import net.tnemc.core.economy.transaction.result.TransactionResult;
import net.tnemc.rewards.event.InteractionType;
import net.tnemc.rewards.event.TNEObjectInteractionEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * All rights reserved.
 **/
@ModuleInfo(
    name = "Rewards",
    author = "creatorfromhell",
    version = "0.1.0"
)
public class RewardsModule extends Module {

  File file;
  FileConfiguration fileConfiguration;
  MaterialsConfiguration configuration;

  private static RewardsModule instance;

  @Override
  public void load(TNE tne, String version) {
    tne.logger().info("Rewards Module loaded!");
    instance = this;
    listeners.add(new RewardsListener(tne));
  }

  @Override
  public void unload(TNE tne) {
    tne.logger().info("Rewards Module unloaded!");
    if(!file.exists()) {
      configuration.save(fileConfiguration);
    }
  }

  @Override
  public void initializeConfigurations() {
    super.initializeConfigurations();
    file = new File(TNE.instance().getDataFolder(), "materials.yml");
    fileConfiguration = YamlConfiguration.loadConfiguration(file);
  }

  @Override
  public void loadConfigurations() {
    super.loadConfigurations();
    fileConfiguration.options().copyDefaults(true);
    configuration = new MaterialsConfiguration();
    configurations.put(configuration, "Materials");
  }

  @Override
  public void saveConfigurations() {
    super.saveConfigurations();
    if(!file.exists()) {
      Reader mobsStream = null;
      try {
        mobsStream = new InputStreamReader(TNE.instance().getResource("materials.yml"), "UTF8");
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
      if (mobsStream != null) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(mobsStream);
        fileConfiguration.setDefaults(config);
      }
    }
    try {
      fileConfiguration.save(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static RewardsModule instance() {
    return instance;
  }

  public MaterialsConfiguration configuration() {
    return configuration;
  }

  public FileConfiguration fConfiguration() {
    return fileConfiguration;
  }

  public static void handleObjectEvent(TNEObjectInteractionEvent event) {
    if(event.isCancelled()) return;
    TNE.debug("TNEObjectInteractionEvent called");

    UUID id = IDFinder.getID(event.getPlayer());
    TNEAccount account = TNE.manager().getAccount(id);
    String world = WorldFinder.getWorld(event.getPlayer(), WorldVariant.BALANCE);
    BigDecimal cost = event.getType().getCost(event.getIdentifier(), WorldFinder.getWorld(event.getPlayer(), WorldVariant.CONFIGURATION), IDFinder.getID(event.getPlayer()).toString());
    String message = event.getType().getCharged();

    if(cost.compareTo(BigDecimal.ZERO) != 0 && !event.isCancelled()) {
      TransactionChargeType type = TransactionChargeType.LOSE;
      if(cost.compareTo(BigDecimal.ZERO) > 0) {
        type = TransactionChargeType.GAIN;
      }
      TNETransaction transaction = new TNETransaction(account, account, world, TNE.transactionManager().getType("give"));
      transaction.setRecipientCharge(new TransactionCharge(world, TNE.manager().currencyManager().get(world), cost, type));
      TransactionResult result = TNE.transactionManager().perform(transaction);

      String newName = event.getIdentifier() + ((event.getAmount() > 1 )? "\'s" : "");

      Message m = new Message(result.recipientMessage());
      m.addVariable("$amount", CurrencyFormatter.format(world, cost));
      m.addVariable("$stack_size", event.getAmount() + "");
      m.addVariable("$item", newName);
      m.translate(world, event.getPlayer());
    }

    if(event.getType().equals(InteractionType.ENCHANT) || event.getType().equals(InteractionType.SMELTING)) {
      TNE.debug("Inside work around loop");
      final Player p = event.getPlayer();
      final ItemStack stack = event.getStack();
      final String correctMat = event.getIdentifier();
      final String loreSearch = (event.getType().equals(InteractionType.ENCHANT))? "Enchanting Cost" : "Smelting Cost";
      TNE.debug("LoreSearch: " + loreSearch);
      TNE.instance().getServer().getScheduler().runTaskLater(TNE.instance(), new Runnable() {
        @Override
        public void run() {
          ItemStack[] contents = p.getInventory().getContents().clone();
          TNE.debug("Inventory Item Count: " + contents.length);
          for (int i = 0; i < contents.length; i++) {
            TNE.debug("Looping contents..." + i + "");
            if(contents[i] != null) TNE.debug("Item Type: " + contents[i].getType().name());
            if(stack != null) {
              TNE.debug("Correct Material: " + stack.getType().name());
            }
            TNE.debug("Correct Material: " + correctMat);
            if (contents[i] != null && contents[i].getType().name().equalsIgnoreCase(correctMat)) {
              ItemStack cloneStack = contents[i].clone();
              ItemMeta meta = cloneStack.getItemMeta();
              List<String> newLore = new ArrayList<>();
              if (meta.getLore() != null) {
                for (String s : meta.getLore()) {
                  TNE.debug("Contains Search: " + s.contains(loreSearch));
                  if (!s.contains(loreSearch)) {
                    newLore.add(s);
                    TNE.debug("Adding Lore: " + s);
                  }
                }
              }
              meta.setLore(newLore);
              cloneStack.setItemMeta(meta);
              contents[i] = cloneStack;
            }
          }
          p.getInventory().setContents(contents);
        }
      }, 5L);
    }
  }
}
