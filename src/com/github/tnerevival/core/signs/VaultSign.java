package com.github.tnerevival.core.signs;

import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.account.Vault;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.serializable.SerializableLocation;
import com.github.tnerevival.utils.AccountUtils;
import org.bukkit.entity.Player;

import java.util.UUID;

public class VaultSign extends TNESign {

  public VaultSign(UUID owner, SerializableLocation location) {
    super(owner, location);
    setType(SignType.VAULT);
  }

  @Override
  public boolean onClick(Player player, boolean shift) {
    return false;
  }

  @Override
  public boolean onRightClick(Player player, boolean shift) {
    if(super.onRightClick(player, shift)) {
      if (player.hasPermission(SignType.VAULT.getUsePermission())) {

        if (!AccountUtils.getAccount(IDFinder.getID(player)).hasBank(IDFinder.getWorld(player))) {
          Message none = new Message("Messages.Vault.None");
          none.addVariable("$amount",  CurrencyFormatter.format(player.getWorld().getName(), Vault.cost(player.getWorld().getName(), IDFinder.getID(player).toString())));
          none.translate(IDFinder.getWorld(player), player);
          return false;
        }

        if (!Vault.sign(IDFinder.getWorld(player), IDFinder.getID(player).toString())) {
          new Message("Messages.Vault.NoSign").translate(IDFinder.getWorld(player), player);
          return false;
        }

        inventory = AccountUtils.getAccount(IDFinder.getID(player)).getVault(IDFinder.getWorld(player)).getInventory();
        if (super.onOpen(player)) {
          player.openInventory(inventory);
          return true;
        }
      }
    }
    return false;
  }
}