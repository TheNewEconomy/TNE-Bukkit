package net.tnemc.core.commands.currency;

import net.tnemc.core.TNE;
import net.tnemc.core.commands.TNECommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 7/9/2021.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class CurrencyEditorCommand extends TNECommand {

  public CurrencyEditorCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String name() {
    return "editor";
  }

  @Override
  public String[] aliases() {
    return new String[] {
        "edit"
    };
  }

  @Override
  public String node() {
    return "tne.currency.editor";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String helpLine() {
    return "Messages.Commands.Currency.Editor";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    TNE.menuManager().open("currency_list", (Player)sender);
    return true;
  }
}