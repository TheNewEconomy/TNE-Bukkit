package com.github.tnerevival.commands.money;

import com.github.tnerevival.TNE;
import com.github.tnerevival.commands.TNECommand;
import com.github.tnerevival.utils.MISCUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

    Player player = getPlayer(sender);
    int limit = (parsed.containsKey("limit") && MISCUtils.isInteger(parsed.get("limit")))? Integer.valueOf(parsed.get("limit")) : 1;
    boolean bank = (parsed.containsKey("bank") && MISCUtils.isBoolean(parsed.get("bank")))? Boolean.valueOf(parsed.get("bank")) : false;
    String world = (parsed.containsKey("world"))? getWorld(sender, parsed.get("world")) : getWorld(sender);
    String currency = (parsed.containsKey("currency") &&
                       TNE.instance.manager.currencyManager.contains(world, parsed.get("currency")) ||
                       parsed.containsKey("currency") && parsed.get("currency").equalsIgnoreCase("overall")
                      )? parsed.get("currency") : TNE.instance.manager.currencyManager.get(world).getName();

    return true;
  }

  @Override
  public String[] getHelpLines() {
    return new String[] {
        "/money top [currency:name] [bank:true/false] [world:world] [limit:#] - A list of players with the highest balances.",
        "[currency] - The name of the currency to get balances from. Defaults to world default. Use overall for all currencies.",
        "[bank] - Whether or not you want to rank players based on highest bank balance. Defaults to false.",
        "[world] - The world name you wish to filter, or all for every world. Defaults to current world. Use overall for all worlds.",
        "[limit] - Limit changes how many players are displayed. Defaults to 10."
    };
  }
}