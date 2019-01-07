package net.tnemc.core.common.debug;

import net.tnemc.core.TNE;
import net.tnemc.core.common.account.TNEAccount;
import net.tnemc.core.common.api.IDFinder;
import org.bukkit.Bukkit;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 1/5/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class SQLDebug {

  public static void testLoad(final int amount) {
    TNE.debug("Testing SQL load with " + amount + " accounts.");
    for(int i = 0; i < amount; i++) {
      final String account = "TEST_ACCOUNT_" + i;
      TNE.debug("Current account: " + account);

      Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
        TNE.manager().createAccount(IDFinder.getID(account), account);
        TNE.debug("Exiting current account.");
      });
    }
    TNE.debug("Finished SQL Load Testing.");
  }

  public static void loadAccountTest(final int amount) {
    TNE.debug("Testing SQL load with " + amount + " accounts.");
    for(int i = 0; i < amount; i++) {
      final String account = "TEST_ACCOUNT_" + i;
      TNE.debug("Current account: " + account);

      Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
        TNE.manager().getAccount(IDFinder.getID(account));
        TNE.debug("Exiting current account.");
      });
    }
    TNE.debug("Finished SQL Load Testing.");
  }

  public static void loadSaveAccountTest(final int amount) {
    TNE.debug("Testing SQL load with " + amount + " accounts.");
    for(int i = 0; i < amount; i++) {
      final String account = "TEST_ACCOUNT_" + i;
      TNE.debug("Current account: " + account);

      Bukkit.getScheduler().runTaskAsynchronously(TNE.instance(), ()->{
        TNEAccount acc = TNE.manager().getAccount(IDFinder.getID(account));
        TNE.manager().addAccount(acc);
        TNE.debug("Exiting current account.");
      });
    }
    TNE.debug("Finished SQL Load Testing.");
  }
}