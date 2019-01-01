package net.tnemc.web.pages.login;

import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;
import spark.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by Daniel on 12/25/2018.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class LoginForm implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) throws Exception {
    if(request.session().attribute("accessToken") != null) {
      response.redirect("/overview");
    }

    Map<String, Object> attributes = new HashMap<>();

    MultiMap<String> parameters = new MultiMap<>();
    UrlEncoded.decodeTo(request.body(), parameters, "UTF-8");

    if(StringUtils.isEmpty(parameters.getString("mc-username"))) {
      attributes.put("message", "Invalid username entered.");
    } else if(StringUtils.isEmpty(parameters.getString("mc-password"))) {
      attributes.put("message", "Invalid password entered.");
    }



    return new ModelAndView(attributes, "login.ftl");
  }
}