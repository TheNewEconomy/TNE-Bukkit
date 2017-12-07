package net.tnemc.conversion;

import com.github.tnerevival.user.IDFinder;
import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.module.ModuleListener;
import net.tnemc.core.event.account.TNEAccountCreationEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * Created by Daniel on 11/9/2017.
 */
public class ConversionListener implements ModuleListener {

  private TNE plugin;

  public ConversionListener(TNE plugin) {
    this.plugin = plugin;
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onAccountCreation(TNEAccountCreationEvent event) {
    TNEAccount converted = ConversionModule.convertAccount(IDFinder.getUsername(event.getId().toString()));
    if(converted != null) {
      TNEAccount account = event.getAccount();
      converted.getWorldHoldings().forEach((world, holdings)->account.getWorldHoldings().put(world, holdings));
      event.setAccount(account);
    }
  }
}