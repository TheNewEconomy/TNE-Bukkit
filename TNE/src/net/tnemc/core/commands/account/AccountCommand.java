package net.tnemc.core.commands.account;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/21/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class AccountCommand extends TNECommand {

  public AccountCommand(TNE plugin) {
    super(plugin);

    addSub(new AccountPinCommand(plugin));
  }

  @Override
  public String name() {
    return "account";
  }

  @Override
  public String[] aliases() {
    return new String[] {
        "acc"
    };
  }

  @Override
  public String node() {
    return "";
  }

  @Override
  public boolean console() {
    return false;
  }
}