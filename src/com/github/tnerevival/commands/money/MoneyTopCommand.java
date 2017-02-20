package com.github.tnerevival.commands.money;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.IDFinder;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.core.Message;
import com.github.tnerevival.core.collection.paginate.Page;
import com.github.tnerevival.core.collection.paginate.Paginator;
import com.github.tnerevival.utils.MISCUtils;
import com.github.tnerevival.utils.TopBalance;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by creatorfromhell on 2/16/2017.
 * All rights reserved.
 **/
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
    return false;
  }

  @Override
  public boolean execute(CommandSender sender, String command, String[] arguments) {
    Map<String, String> parsed = getArguments(arguments);

    int page = 1;
    int limit = (parsed.containsKey("limit") && MISCUtils.isInteger(parsed.get("limit")))? Integer.valueOf(parsed.get("limit")) : 10;
    boolean bank = (parsed.containsKey("bank") && MISCUtils.isBoolean(parsed.get("bank")))? Boolean.valueOf(parsed.get("bank")) : false;
    String world = (parsed.containsKey("world"))? getWorld(sender, parsed.get("world")) : getWorld(sender);
    String currency = (parsed.containsKey("currency") &&
                       TNE.instance.manager.currencyManager.contains(world, parsed.get("currency")) ||
                       parsed.containsKey("currency") && parsed.get("currency").equalsIgnoreCase("overall")
                      )? parsed.get("currency") : TNE.instance.manager.currencyManager.get(world).getName();

    Paginator paginator = new Paginator(Arrays.asList(TNE.instance.manager.parseTop(currency, world, bank, limit).toArray()), 10);

    if(arguments.length >= 1 && parsed.containsKey(String.valueOf(0))) {
      if(MISCUtils.isInteger(parsed.get(String.valueOf(0)))) {
        page = Integer.valueOf(parsed.get(String.valueOf(0)));
      }
    }

    if(page > paginator.getMaxPages()) page = paginator.getMaxPages();
    System.out.println(paginator.getMaxPages());

    Page p = paginator.getPage(page);

    Message top = new Message("Messages.Money.Top");
    top.addVariable("$page", page + "");
    top.addVariable("$page_top", paginator.getMaxPages() + "");
    top.translate(getWorld(sender), sender);

    for(Object o : p.getElements()) {
      TopBalance bal = (TopBalance)o;
      sender.sendMessage(IDFinder.ecoToUsername(bal.getId()) + " has " + bal.getBalance());
    }
    return true;
  }

  @Override
  public String[] getHelpLines() {
    return new String[] {
        "/money top [page] [currency:name] [bank:true/false] [world:world] [limit:#] - A list of players with the highest balances.",
        "[page] - The page number to view. Defaults to 1.",
        "[currency] - The name of the currency to get balances from. Defaults to world default. Use overall for all currencies.",
        "[bank] - Whether or not you want to rank players based on highest bank balance. Defaults to false.",
        "[world] - The world name you wish to filter, or all for every world. Defaults to current world. Use overall for all worlds.",
        "[limit] - Limit changes how many players are displayed. Defaults to 10."
    };
  }
}