package net.tnemc.core.commands.money;

import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.collection.paginate.Page;
import com.github.tnerevival.core.collection.paginate.Paginator;
import net.tnemc.core.TNE;
import net.tnemc.core.common.WorldVariant;
import net.tnemc.core.common.account.WorldFinder;
import net.tnemc.core.common.utils.MISCUtils;
import net.tnemc.core.common.utils.TopBalance;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 *
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by Daniel on 7/10/2017.
 */
public class MoneyTopCommand extends TNECommand {

  public MoneyTopCommand(TNE plugin) {
    super(plugin);
  }

  @Override
  public String getName() {
    return "top";
  }

  @Override
  public String[] getAliases() {
    return new String[0];
  }

  @Override
  public String getNode() {
    return "tne.money.top";
  }

  @Override
  public boolean console() {
    return true;
  }

  @Override
  public String getHelp() {
    return "Messages.Commands.Money.Top";
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Map<String, String> parsed = getArguments(arguments);

    int page = 1;
    int limit = (parsed.containsKey("limit") && MISCUtils.isInteger(parsed.get("limit")))? Integer.valueOf(parsed.get("limit")) : 10;
    String world = (parsed.containsKey("world"))? WorldFinder.getWorld(parsed.get("world"), WorldVariant.BALANCE) : WorldFinder.getWorld(sender, WorldVariant.BALANCE);
    String currency = (parsed.containsKey("currency") &&
        TNE.manager().currencyManager().contains(world, parsed.get("currency")) ||
        parsed.containsKey("currency") && parsed.get("currency").equalsIgnoreCase("overall")
    )? parsed.get("currency") : TNE.manager().currencyManager().get(world).name();

    Paginator paginator = new Paginator(Arrays.asList(TNE.manager().parseTop(currency, world, limit).toArray()), 10);

    if(arguments.length >= 1 && parsed.containsKey(String.valueOf(0)) && MISCUtils.isInteger(parsed.get(String.valueOf(0)))) {
      page = Integer.valueOf(parsed.get(String.valueOf(0)));
    }

    if(page > paginator.getMaxPages()) page = paginator.getMaxPages();
    TNE.debug("MoneyTopCommand.java(87): Max Pages - " + paginator.getMaxPages());

    Page p = paginator.getPage(page);

    Message top = new Message("Messages.Money.Top");
    top.addVariable("$page", page + "");
    top.addVariable("$page_top", paginator.getMaxPages() + "");
    top.translate(world, sender);

    for(Object o : p.getElements()) {
      TopBalance bal = (TopBalance)o;
      sender.sendMessage(bal.getUsername() + " has " + bal.getBalance());
    }
    return true;
  }
}