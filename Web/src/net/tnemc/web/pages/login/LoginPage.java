package net.tnemc.web.pages.login;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

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
public class LoginPage implements TemplateViewRoute {

  @Override
  public ModelAndView handle(Request request, Response response) throws Exception {
    if(request.session().attribute("accessToken") != null) {
      response.redirect("/overview");
    }
    response.status(200);
    response.type("text/html");
    return null;
  }
}