package net.tnemc.web.utils;

import net.tnemc.web.WebModule;
import net.tnemc.web.pages.helper.Balance;
import net.tnemc.web.pages.helper.NavLink;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 12/18/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class RenderUtils {

  public static Map<String, Object> buildHeader(Request request, Response response) throws SQLException {
    Map<String, Object> model = new HashMap<>();
    if(!testLogin(request, response)) return model;

    model.putAll(buildNavigation(request, response));
    model.put("user", request.session().attribute("user"));
    model.put("year", LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date())).getYear());

    List<Balance> balances = new LinkedList<>();
     /*((TNEDataManager)TNE.instance().getSaveManager().getDataManager()).getTNEProvider().loadAccount(request.session().attribute("uuid")).getWorldHoldings().forEach((world, worldHoldings)->{
      worldHoldings.getHoldings().forEach((currency, amount)-> balances.add(new Balance(currency, CurrencyFormatter.format(world, currency, amount))));
    });*/
    model.put("balances", balances);

    return model;
  }

  public static Map<String, Object> buildNavigation(Request request, Response response) {
    LinkedList<NavLink> navigationLinks = new LinkedList<>();

    for(Map.Entry<String, String> link : WebModule.instance().getManager().getNavLinks().entrySet()) {
      navigationLinks.add(new NavLink(link.getKey(), link.getValue(), request.pathInfo().contains(link.getKey())));
    }

    Map<String, Object> model = new HashMap<>();
    model.put("links", navigationLinks);
    return model;
  }

  public static boolean testLogin(Request request, Response response) {
    if(request.session().attribute("accessToken") == null) {
      request.session().attribute("redirect", request.pathInfo());
      response.redirect("/login");
      return false;
    }
    return true;
  }
}