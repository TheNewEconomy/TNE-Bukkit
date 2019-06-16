package net.tnemc.web.rest.impl.account;

import net.tnemc.web.rest.IRequest;
import net.tnemc.web.rest.RequestType;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 6/16/2019.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class AccountExistsRequest implements IRequest {
  @Override
  public RequestType type() {
    return RequestType.GET;
  }

  @Override
  public String route() {
    return "/api-v1/accounts/:exists";
  }

  @Override
  public Route work(Request request, Response response) {
    return null;
  }
}