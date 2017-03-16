package com.github.tnerevival.core.signs;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.currency.CurrencyFormatter;
import com.github.tnerevival.serializable.SerializableLocation;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by creatorfromhell on 3/15/2017.
 * All rights reserved.
 **/
public class BalanceSign extends TNESign {

  public BalanceSign(UUID owner, SerializableLocation location) {
    super(owner, location);
    setType(SignType.BALANCE);
  }

  @Override
  public boolean onRightClick(Player player, boolean shift) {
    if(super.onRightClick(player, shift)) {
      String currency = TNE.instance().api().getString(type.getConfiguration() + ".Currency");
      String world = IDFinder.getWorld(player);
      Message balance = new Message("Messages.Money.Balance");
      balance.addVariable("$amount",  CurrencyFormatter.format(world, currency, TNE.instance().api().getBalanceDecimal(IDFinder.getID(player).toString(), world)));
      balance.translate(world, player);
      if(!shift) return true;
    }
    return false;
  }
}