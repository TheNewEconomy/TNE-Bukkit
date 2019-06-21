package net.tnemc.web.rest.impl.account;

import com.google.gson.Gson;
import net.tnemc.core.common.api.IDFinder;
import net.tnemc.web.rest.IRequest;
import net.tnemc.web.rest.RequestType;
import net.tnemc.web.rest.object.IdentifierObject;
import net.tnemc.web.rest.service.AccountService;
import spark.Request;
import spark.Response;

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
public class AccountGetRequest implements IRequest {
  @Override
  public RequestType type() {
    return RequestType.GET;
  }

  @Override
  public String route() {
    return "/api-v1/accounts/find";
  }

  @Override
  public String work(Request request, Response response) {
    IdentifierObject id = new IdentifierObject(IDFinder.getID("Server_Account").toString());
    return new Gson().toJson(AccountService.getAccount(id.getIdentifier()));
  }
}