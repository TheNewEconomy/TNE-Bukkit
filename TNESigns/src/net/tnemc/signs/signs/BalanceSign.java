package net.tnemc.signs.signs;

import com.github.tnerevival.serializable.SerializableLocation;
import net.tnemc.core.TNE;
import net.tnemc.core.common.Message;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.core.common.currency.CurrencyFormatter;
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
      String world = WorldFinder.getWorld(player, WorldVariant.BALANCE);
      Message balance = new Message("Messages.Money.Balance");
      balance.addVariable("$amount",  CurrencyFormatter.format(world, currency, TNE.instance().api().getHoldings(IDFinder.getID(player).toString(), world)));
      balance.translate(world, player);
      if(!shift) return true;
    }
    return false;
  }
}